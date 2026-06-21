// port-lint: source types.rs
package io.github.kotlinmania.codex.core.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

// / Types used to define the fields of [Config].
// /
// / Note this file should generally be restricted to simple struct/enum
// / definitions that do not contain business logic.

const val DEFAULT_OTEL_ENVIRONMENT: String = "dev"

@Serializable
sealed class McpServerTransportConfig {
    // / https://modelcontextprotocol.io/specification/2025-06-18/basic/transports#stdio
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

    // / https://modelcontextprotocol.io/specification/2025-06-18/basic/transports#streamable-http
    @Serializable
    data class StreamableHttp(
        @SerialName("url") val url: String,
        // / Name of the environment variable to read for an HTTP bearer token.
        // / When set, requests will include the token via `Authorization: Bearer <token>`.
        // / The actual secret value must be provided via the environment.
        @SerialName("bearer_token") val bearerToken: String? = null,
        @SerialName("bearer_token_env_var") val bearerTokenEnvVar: String? = null,
    ) : McpServerTransportConfig()
}

@Serializable
data class McpServerConfig(
    @SerialName("transport") val transport: McpServerTransportConfig,
    // / When `false`, Codex skips initializing this MCP server.
    @SerialName("enabled") val enabled: Boolean = true,
    // / Startup timeout in seconds for initializing MCP server & initially listing tools.
    @SerialName("startup_timeout_sec") val startupTimeout: Duration? = null,
    // / Default timeout for MCP tool calls initiated via this server.
    @SerialName("tool_timeout_sec") val toolTimeout: Duration? = null,
    // / Explicit allow-list of tools exposed from this server. When set, only these tools will be registered.
    @SerialName("enabled_tools") val enabledTools: List<String>? = null,
    // / Explicit deny-list of tools. These tools will be removed after applying `enabledTools`.
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
    // streamableHttp
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

        val transport =
            when {
                isStdio && isHttp -> error("invalid MCP server config: mix of stdio and streamable_http fields")
                isHttp ->
                    McpServerTransportConfig.StreamableHttp(
                        url = url ?: error("invalid MCP server config: missing 'url' for streamable_http"),
                        bearerToken = bearerToken,
                        bearerTokenEnvVar = bearerTokenEnvVar,
                    )
                isStdio ->
                    McpServerTransportConfig.Stdio(
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
    val entries: Map<String, Boolean> = emptyMap(),
)

@Serializable
enum class UriBasedFileOpener {
    @SerialName("vscode")
    VsCode,

    @SerialName("vscode-insiders")
    VsCodeInsiders,

    @SerialName("windsurf")
    Windsurf,

    @SerialName("cursor")
    Cursor,

    // / Option to disable the URI-based file opener.
    @SerialName("none")
    None,

    ;

    fun getScheme(): String? =
        when (this) {
            VsCode -> "vscode"
            VsCodeInsiders -> "vscode-insiders"
            Windsurf -> "windsurf"
            Cursor -> "cursor"
            None -> null
        }
}

// / Settings that govern if and what will be written to `~/.codex/history.jsonl`.
@Serializable
data class History(
    // / If true, history entries will not be written to disk.
    val persistence: HistoryPersistence = HistoryPersistence.SaveAll,
    // / If set, the maximum size of the history file in bytes.
    val maxBytes: Long? = null,
)

@Serializable
enum class HistoryPersistence {
    // / Save all history entries to disk.
    @SerialName("save-all")
    SaveAll,

    // / Do not write history to disk.
    @SerialName("none")
    None,
}

// ===== OTEL configuration =====

@Serializable
enum class OtelHttpProtocol {
    // / Binary payload
    @SerialName("binary")
    Binary,

    // / JSON payload
    @SerialName("json")
    Json,
}

@Serializable
data class OtelTlsConfig(
    @SerialName("ca_certificate") val caCertificate: String? = null,
    @SerialName("client_certificate") val clientCertificate: String? = null,
    @SerialName("client_private_key") val clientPrivateKey: String? = null,
)

// / Which OTEL exporter to use.
@Serializable
sealed class OtelExporterKind {
    @Serializable
    @SerialName("none")
    data object None : OtelExporterKind()

    @Serializable
    @SerialName("otlp-http")
    data class OtlpHttp(
        val endpoint: String,
        val headers: Map<String, String> = emptyMap(),
        val protocol: OtelHttpProtocol,
        val tls: OtelTlsConfig? = null,
    ) : OtelExporterKind()

    @Serializable
    @SerialName("otlp-grpc")
    data class OtlpGrpc(
        val endpoint: String,
        val headers: Map<String, String> = emptyMap(),
        val tls: OtelTlsConfig? = null,
    ) : OtelExporterKind()
}

// / OTEL settings loaded from config.toml. Fields are optional so we can apply defaults.
@Serializable
data class OtelConfigToml(
    // / Log user prompt in traces
    @SerialName("log_user_prompt") val logUserPrompt: Boolean? = null,
    // / Mark traces with environment (dev, staging, prod, test). Defaults to dev.
    val environment: String? = null,
    // / Exporter to use. Defaults to `otlp-file`.
    val exporter: OtelExporterKind? = null,
)

// / Effective OTEL settings after defaults are applied.
data class OtelConfig(
    val logUserPrompt: Boolean = false,
    val environment: String = DEFAULT_OTEL_ENVIRONMENT,
    val exporter: OtelExporterKind = OtelExporterKind.None,
)

@Serializable
sealed class Notifications {
    @Serializable
    data class Enabled(
        val enabled: Boolean,
    ) : Notifications()

    @Serializable
    data class Custom(
        val events: List<String>,
    ) : Notifications()

    companion object {
        fun default(): Notifications = Enabled(true)
    }
}

// / Collection of settings that are specific to the TUI.
@Serializable
data class Tui(
    // / Enable desktop notifications from the TUI when the terminal is unfocused.
    // / Defaults to `true`.
    val notifications: Notifications = Notifications.default(),
    // / Enable animations (welcome screen, shimmer effects, spinners).
    // / Defaults to `true`.
    val animations: Boolean = true,
)

// / Settings for notices we display to users via the tui and app-server clients
// / (primarily the Codex IDE extension). NOTE: these are different from
// / notifications - notices are warnings, NUX screens, acknowledgements, etc.
@Serializable
data class Notice(
    // / Tracks whether the user has acknowledged the full access warning prompt.
    @SerialName("hide_full_access_warning") val hideFullAccessWarning: Boolean? = null,
    // / Tracks whether the user has acknowledged the Windows world-writable directories warning.
    @SerialName("hide_world_writable_warning") val hideWorldWritableWarning: Boolean? = null,
    // / Tracks whether the user opted out of the rate limit model switch reminder.
    @SerialName("hide_rate_limit_model_nudge") val hideRateLimitModelNudge: Boolean? = null,
    // / Tracks whether the user has seen the model migration prompt
    @SerialName("hide_gpt5_1_migration_prompt") val hideGpt5_1MigrationPrompt: Boolean? = null,
    // / Tracks whether the user has seen the gpt-5.1-codex-max migration prompt
    @SerialName("hide_gpt-5.1-codex-max_migration_prompt") val hideGpt5_1CodexMaxMigrationPrompt: Boolean? = null,
) {
    companion object {
        // / referenced by configEdit helpers when writing notice flags
        internal const val TABLE_KEY: String = "notice"
    }
}

@Serializable
data class SandboxWorkspaceWrite(
    @SerialName("writable_roots") val writableRoots: List<String> = emptyList(),
    @SerialName("network_access") val networkAccess: Boolean = false,
    @SerialName("exclude_tmpdir_env_var") val excludeTmpdirEnvVar: Boolean = false,
    @SerialName("exclude_slash_tmp") val excludeSlashTmp: Boolean = false,
)

@Serializable
enum class ShellEnvironmentPolicyInherit {
    // / "Core" environment variables for the platform. On UNIX, this would
    // / include HOME, LOGNAME, PATH, SHELL, and USER, among others.
    @SerialName("core")
    Core,

    // / Inherits the full environment from the parent process.
    @SerialName("all")
    All,

    // / Do not inherit any environment variables from the parent process.
    @SerialName("none")
    None,
}

// / Policy for building the `env` when spawning a process via either the
// / `shell` or `localShell` tool.
@Serializable
data class ShellEnvironmentPolicyToml(
    val inherit: ShellEnvironmentPolicyInherit? = null,
    @SerialName("ignore_default_excludes") val ignoreDefaultExcludes: Boolean? = null,
    // / List of regular expressions.
    val exclude: List<String>? = null,
    val set: Map<String, String>? = null,
    // / List of regular expressions.
    @SerialName("include_only") val includeOnly: List<String>? = null,
    @SerialName("experimental_use_profile") val experimentalUseProfile: Boolean? = null,
)

// / Wildcard pattern for matching environment variable names, equivalent to
// / the upstream `WildMatchPattern<'*', '?'>`. Uses `*` for any sequence and `?` for
// / any single character. Always created in case-insensitive mode here, mirroring
// / `EnvironmentVariablePattern::newCaseInsensitive`.
data class EnvironmentVariablePattern(
    val pattern: String,
) {
    private val regex: Regex = compile(pattern)

    fun matches(name: String): Boolean = regex.matches(name)

    companion object {
        fun newCaseInsensitive(pattern: String): EnvironmentVariablePattern =
            EnvironmentVariablePattern(pattern)

        private fun compile(pattern: String): Regex {
            val sb = StringBuilder()
            sb.append('^')
            for (c in pattern) {
                when (c) {
                    '*' -> sb.append(".*")
                    '?' -> sb.append('.')
                    '.', '+', '(', ')', '|', '[', ']', '{', '}', '^', '$', '\\' -> {
                        sb.append('\\').append(c)
                    }
                    else -> sb.append(c)
                }
            }
            sb.append('$')
            return Regex(sb.toString(), RegexOption.IGNORE_CASE)
        }
    }
}

// / Deriving the `env` based on this policy works as follows:
// / 1. Create an initial map based on the `inherit` policy.
// / 2. If `ignoreDefaultExcludes` is false, filter the map using the default
// /    exclude pattern(s), which are: `"*KEY*"` and `"*TOKEN*"`.
// / 3. If `exclude` is not empty, filter the map using the provided patterns.
// / 4. Insert any entries from `r#set` into the map.
// / 5. If non-empty, filter the map using the `includeOnly` patterns.
data class ShellEnvironmentPolicy(
    // / Starting point when building the environment.
    val inherit: ShellEnvironmentPolicyInherit = ShellEnvironmentPolicyInherit.All,
    // / True to skip the check to exclude default environment variables that
    // / contain "KEY" or "TOKEN" in their name.
    val ignoreDefaultExcludes: Boolean = false,
    // / Environment variable names to exclude from the environment.
    val exclude: List<EnvironmentVariablePattern> = emptyList(),
    // / (key, value) pairs to insert in the environment.
    val set: MutableMap<String, String> = mutableMapOf(),
    // / Environment variable names to retain in the environment.
    val includeOnly: List<EnvironmentVariablePattern> = emptyList(),
    // / If true, the shell profile will be used to run the command.
    val useProfile: Boolean = false,
) {
    companion object {
        fun fromToml(toml: ShellEnvironmentPolicyToml): ShellEnvironmentPolicy {
            // Default to inheriting the full environment when not specified.
            val inherit = toml.inherit ?: ShellEnvironmentPolicyInherit.All
            val ignoreDefaultExcludes = toml.ignoreDefaultExcludes ?: false
            val exclude =
                (toml.exclude ?: emptyList())
                    .map { EnvironmentVariablePattern.newCaseInsensitive(it) }
            val set = (toml.set ?: emptyMap()).toMutableMap()
            val includeOnly =
                (toml.includeOnly ?: emptyList())
                    .map { EnvironmentVariablePattern.newCaseInsensitive(it) }
            val useProfile = toml.experimentalUseProfile ?: false

            return ShellEnvironmentPolicy(
                inherit = inherit,
                ignoreDefaultExcludes = ignoreDefaultExcludes,
                exclude = exclude,
                set = set,
                includeOnly = includeOnly,
                useProfile = useProfile,
            )
        }
    }
}

@Serializable
enum class ReasoningSummaryFormat {
    @SerialName("none")
    None,

    @SerialName("experimental")
    Experimental,
}
