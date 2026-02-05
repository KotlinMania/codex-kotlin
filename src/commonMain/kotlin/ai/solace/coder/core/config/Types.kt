// port-lint: source core/src/config/types.rs
package ai.solace.coder.core.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

const val DEFAULT_OTEL_ENVIRONMENT: String = "dev"

@Serializable
sealed class McpServerTransportConfig {
    @Serializable
    data class Stdio(
        @SerialName("command") val command: String,
        @SerialName("args") val args: List<String> = emptyList(),
        @SerialName("env") val env: Map<String, String>? = null,
        @SerialName("env_vars") val envVars: List<String>? = null,
        @SerialName("cwd") val cwd: String? = null,
        @SerialName("http_headers") val httpHeaders: Map<String, String>? = null,
        @SerialName("env_http_headers") val envHttpHeaders: Map<String, String>? = null,
    ) : McpServerTransportConfig()

    @Serializable
    data class StreamableHttp(
        @SerialName("url") val url: String,
        @SerialName("bearer_token") val bearerToken: String? = null,
        @SerialName("bearer_token_env_var") val bearerTokenEnvVar: String? = null,
    ) : McpServerTransportConfig()
}

@Serializable
data class McpServerConfig(
    @SerialName("transport") val transport: McpServerTransportConfig,
    @SerialName("enabled") val enabled: Boolean = true,
    // Note: these are normalized fields; serialization names retained for parity in raw
    @SerialName("startup_timeout_sec") val startupTimeout: Duration? = null,
    @SerialName("tool_timeout_sec") val toolTimeout: Duration? = null,
    @SerialName("enabled_tools") val enabledTools: List<String>? = null,
    @SerialName("disabled_tools") val disabledTools: List<String>? = null,
)

@Serializable
data class RawMcpServerConfig(
    // stdio
    @SerialName("command") val command: String? = null,
    @SerialName("args") val args: List<String>? = null,
    @SerialName("env") val env: Map<String, String>? = null,
    @SerialName("env_vars") val envVars: List<String>? = null,
    @SerialName("cwd") val cwd: String? = null,
    @SerialName("http_headers") val httpHeaders: Map<String, String>? = null,
    @SerialName("env_http_headers") val envHttpHeaders: Map<String, String>? = null,
    // streamable_http
    @SerialName("url") val url: String? = null,
    @SerialName("bearer_token") val bearerToken: String? = null,
    @SerialName("bearer_token_env_var") val bearerTokenEnvVar: String? = null,
    // shared
    @SerialName("startup_timeout_sec") val startupTimeoutSec: Double? = null,
    @SerialName("startup_timeout_ms") val startupTimeoutMs: Long? = null,
    @SerialName("tool_timeout_sec") val toolTimeoutSec: Double? = null,
    @SerialName("enabled") val enabled: Boolean? = null,
    @SerialName("enabled_tools") val enabledTools: List<String>? = null,
    @SerialName("disabled_tools") val disabledTools: List<String>? = null,
) {
    fun normalize(): McpServerConfig {
        val startup = secondsToDuration(startupTimeoutSec) ?: millisToDuration(startupTimeoutMs)
        val tool = secondsToDuration(toolTimeoutSec)

        val isStdio = command != null || args != null || env != null || envVars != null || cwd != null || httpHeaders != null || envHttpHeaders != null
        val isHttp = url != null || bearerToken != null || bearerTokenEnvVar != null

        val transport = when {
            isStdio && isHttp -> error("invalid MCP server config: mix of stdio and streamable_http fields")
            isHttp -> McpServerTransportConfig.StreamableHttp(
                url = url ?: error("invalid MCP server config: missing 'url' for streamable_http"),
                bearerToken = bearerToken,
                bearerTokenEnvVar = bearerTokenEnvVar,
            )
            isStdio -> McpServerTransportConfig.Stdio(
                command = command ?: error("invalid MCP server config: missing 'command' for stdio"),
                args = args ?: emptyList(),
                env = env,
                envVars = envVars,
                cwd = cwd,
                httpHeaders = httpHeaders,
                envHttpHeaders = envHttpHeaders,
            )
            else -> error("invalid MCP server config: missing transport fields (need either 'url' or 'command')")
        }

        return McpServerConfig(
            transport = transport,
            enabled = enabled ?: true,
            startupTimeout = startup,
            toolTimeout = tool,
            enabledTools = enabledTools,
            disabledTools = disabledTools,
        )
    }
}

@Serializable
data class FeaturesToml(
    val entries: Map<String, Boolean> = emptyMap()
)

// =============================================================================
// Shell Environment Policy (exec_env)
// =============================================================================

@Serializable
enum class ShellEnvironmentPolicyInherit {
    /** "Core" environment variables for the platform. */
    @SerialName("core")
    Core,

    /** Inherits the full environment from the parent process. */
    @SerialName("all")
    All,

    /** Do not inherit any environment variables from the parent process. */
    @SerialName("none")
    None
}

/**
 * Policy for building the `env` when spawning a process via either the `shell` or `local_shell` tool.
 *
 * Deserializable (TOML/JSON) form.
 */
@Serializable
data class ShellEnvironmentPolicyToml(
    val inherit: ShellEnvironmentPolicyInherit? = null,
    @SerialName("ignore_default_excludes")
    val ignoreDefaultExcludes: Boolean? = null,
    val exclude: List<String>? = null,
    @SerialName("set")
    val setVars: Map<String, String>? = null,
    @SerialName("include_only")
    val includeOnly: List<String>? = null,
    @SerialName("experimental_use_profile")
    val experimentalUseProfile: Boolean? = null,
)

/**
 * Simple wildcard matcher for environment variable names.
 *
 * Port of Rust `WildMatchPattern<'*', '?'>` usage.
 */
data class EnvironmentVariablePattern private constructor(
    private val pattern: String,
    private val caseInsensitive: Boolean,
) {
    companion object {
        fun newCaseInsensitive(pattern: String): EnvironmentVariablePattern =
            EnvironmentVariablePattern(pattern = pattern, caseInsensitive = true)
    }

    fun matches(name: String): Boolean {
        val p = if (caseInsensitive) pattern.uppercase() else pattern
        val n = if (caseInsensitive) name.uppercase() else name
        return wildcardMatch(p, n)
    }
}

/**
 * Deriving the `env` based on this policy works as follows:
 * 1. Create an initial map based on the `inherit` policy.
 * 2. If `ignore_default_excludes` is false, filter the map using the default exclude patterns.
 * 3. If `exclude` is not empty, filter the map using the provided patterns.
 * 4. Insert any entries from `setVars` into the map.
 * 5. If non-empty, filter the map using the `includeOnly` patterns.
 */
data class ShellEnvironmentPolicy(
    val inherit: ShellEnvironmentPolicyInherit = ShellEnvironmentPolicyInherit.All,
    val ignoreDefaultExcludes: Boolean = false,
    val exclude: List<EnvironmentVariablePattern> = emptyList(),
    val setVars: Map<String, String> = emptyMap(),
    val includeOnly: List<EnvironmentVariablePattern> = emptyList(),
    val useProfile: Boolean = false,
) {
    companion object {
        fun fromToml(toml: ShellEnvironmentPolicyToml): ShellEnvironmentPolicy {
            val inherit = toml.inherit ?: ShellEnvironmentPolicyInherit.All
            val ignoreDefaultExcludes = toml.ignoreDefaultExcludes ?: false
            val exclude =
                toml.exclude
                    .orEmpty()
                    .map(EnvironmentVariablePattern::newCaseInsensitive)
            val setVars = toml.setVars.orEmpty()
            val includeOnly =
                toml.includeOnly
                    .orEmpty()
                    .map(EnvironmentVariablePattern::newCaseInsensitive)
            val useProfile = toml.experimentalUseProfile ?: false

            return ShellEnvironmentPolicy(
                inherit = inherit,
                ignoreDefaultExcludes = ignoreDefaultExcludes,
                exclude = exclude,
                setVars = setVars,
                includeOnly = includeOnly,
                useProfile = useProfile,
            )
        }
    }
}

private fun wildcardMatch(pattern: String, text: String): Boolean {
    // Standard DP wildcard matching for '*' and '?'.
    val dp = Array(pattern.length + 1) { BooleanArray(text.length + 1) }
    dp[0][0] = true
    for (i in 1..pattern.length) {
        dp[i][0] = dp[i - 1][0] && pattern[i - 1] == '*'
    }
    for (i in 1..pattern.length) {
        val pc = pattern[i - 1]
        for (j in 1..text.length) {
            val tc = text[j - 1]
            dp[i][j] =
                when (pc) {
                    '*' -> dp[i - 1][j] || dp[i][j - 1]
                    '?' -> dp[i - 1][j - 1]
                    else -> dp[i - 1][j - 1] && pc == tc
                }
        }
    }
    return dp[pattern.length][text.length]
}
