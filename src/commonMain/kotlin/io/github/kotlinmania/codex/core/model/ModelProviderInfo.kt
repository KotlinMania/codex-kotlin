// port-lint: source modelProviderInfo.rs
// Registry of model providers supported by Codex.
//
// Providers can be defined in two places:
//   1. Built-in defaults compiled into the binary so Codex works out-of-the-box.
//   2. User-defined entries inside `~/.codex/config.toml` under the model providers
//      key. These override or extend the defaults at runtime.
package io.github.kotlinmania.codex.core.model

import io.github.kotlinmania.codex.api.provider.Provider as ApiProvider
import io.github.kotlinmania.codex.api.provider.RetryConfig as ApiRetryConfig
import io.github.kotlinmania.codex.api.provider.WireApi as ApiWireApi
import io.github.kotlinmania.codex.core.AuthMode
import io.github.kotlinmania.codex.core.CodexErr
import io.github.kotlinmania.codex.core.EnvVarError
import io.github.kotlinmania.codex.utils.Environment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private const val DEFAULT_STREAM_IDLE_TIMEOUT_MS: Long = 300_000
private const val DEFAULT_STREAM_MAX_RETRIES: Long = 5
private const val DEFAULT_REQUEST_MAX_RETRIES: Long = 4
/** Hard cap for user-configured `streamMaxRetries`. */
private const val MAX_STREAM_MAX_RETRIES: Long = 100
/** Hard cap for user-configured `requestMaxRetries`. */
private const val MAX_REQUEST_MAX_RETRIES: Long = 100

/**
 * Wire protocol that the provider speaks. Most third-party services only
 * implement the classic OpenAI Chat Completions JSON schema, whereas OpenAI
 * itself (and a handful of others) additionally expose the more modern
 * Responses API. The two protocols use different request/response shapes
 * and cannot be auto-detected at runtime, therefore each provider entry
 * must declare which one it expects.
 */
@Serializable
enum class WireApi {
    /** The Responses API exposed by OpenAI at `/v1/responses`. */
    @SerialName("responses")
    Responses,

    /** Regular Chat Completions compatible with `/v1/chat/completions`. */
    @SerialName("chat")
    Chat;

    companion object {
        val Default: WireApi = Chat
    }
}

private fun envVar(name: String): String? = Environment.get(name)

/**
 * Serializable representation of a provider definition.
 */
@Serializable
data class ModelProviderInfo(
    /** Friendly display name. */
    val name: String,
    /** Base URL for the provider OpenAI-compatible API. */
    @SerialName("base_url")
    val baseUrl: String? = null,
    /** Environment variable that stores the user API key for this provider. */
    @SerialName("env_key")
    val envKey: String? = null,

    /** Optional instructions to help the user get a valid value for the
     *  variable and set it. */
    @SerialName("env_key_instructions")
    val envKeyInstructions: String? = null,

    /** Value to use with `Authorization: Bearer <token>` header. Use of this
     *  config is discouraged in favor of `envKey` for security reasons, but
     *  this may be necessary when using this programmatically. */
    @SerialName("experimental_bearer_token")
    val experimentalBearerToken: String? = null,

    /** Which wire protocol this provider expects. */
    @SerialName("wire_api")
    val wireApi: WireApi = WireApi.Chat,

    /** Optional query parameters to append to the base URL. */
    @SerialName("query_params")
    val queryParams: Map<String, String>? = null,

    /** Additional HTTP headers to include in requests to this provider where
     *  the (key, value) pairs are the header name and value. */
    @SerialName("http_headers")
    val httpHeaders: Map<String, String>? = null,

    /** Optional HTTP headers to include in requests to this provider where the
     *  (key, value) pairs are the header name and environment variable whose
     *  value should be used. If the environment variable is not set, or the
     *  value is empty, the header will not be included in the request. */
    @SerialName("env_http_headers")
    val envHttpHeaders: Map<String, String>? = null,

    /** Maximum number of times to retry a failed HTTP request to this provider. */
    @SerialName("request_max_retries")
    val requestMaxRetries: Long? = null,

    /** Number of times to retry reconnecting a dropped streaming response before failing. */
    @SerialName("stream_max_retries")
    val streamMaxRetries: Long? = null,

    /** Idle timeout (in milliseconds) to wait for activity on a streaming response before treating
     *  the connection as lost. */
    @SerialName("stream_idle_timeout_ms")
    val streamIdleTimeoutMs: Long? = null,

    /** Does this provider require an OpenAI API Key or ChatGPT login token? If true,
     *  user is presented with login screen on first run, and login preference and token/key
     *  are stored in auth.json. If false (which is the default), login screen is skipped,
     *  and API key (if needed) comes from the "envKey" environment variable. */
    @SerialName("requires_openai_auth")
    val requiresOpenaiAuth: Boolean = false,
) {
    private fun buildHeaderMap(): Map<String, String> {
        val headers = mutableMapOf<String, String>()
        val extra = httpHeaders
        if (extra != null) {
            for ((k, v) in extra) {
                headers[k] = v
            }
        }

        val envHeaders = envHttpHeaders
        if (envHeaders != null) {
            for ((header, envName) in envHeaders) {
                val value = envVar(envName)
                if (value != null && value.trim().isNotEmpty()) {
                    headers[header] = value
                }
            }
        }

        return headers
    }

    internal fun toApiProvider(authMode: AuthMode?): ApiProvider {
        val defaultBaseUrl = if (authMode == AuthMode.ChatGPT) {
            "https://chatgpt.com/backend-api/codex"
        } else {
            "https://api.openai.com/v1"
        }
        val resolvedBaseUrl = baseUrl ?: defaultBaseUrl

        val headers = buildHeaderMap()
        val retry = ApiRetryConfig(
            maxAttempts = requestMaxRetries(),
            baseDelay = 200.milliseconds,
            retry429 = false,
            retry5xx = true,
            retryTransport = true,
        )

        return ApiProvider(
            name = name,
            baseUrl = resolvedBaseUrl,
            queryParams = queryParams,
            wire = when (wireApi) {
                WireApi.Responses -> ApiWireApi.Responses
                WireApi.Chat -> ApiWireApi.Chat
            },
            defaultHeaders = headers,
            retry = retry,
            streamIdleTimeout = streamIdleTimeout(),
        )
    }

    /**
     * If `envKey` is Some, returns the API key for this provider if present
     * (and non-empty) in the environment. If `envKey` is required but
     * cannot be found, returns an error.
     */
    fun apiKey(): String? {
        val envKey = this.envKey ?: return null
        val envValue = envVar(envKey)
        if (envValue == null || envValue.trim().isEmpty()) {
            throw CodexErr.EnvVar(
                EnvVarError(
                    varName = envKey,
                    instructions = envKeyInstructions,
                ),
            ).toException()
        }
        return envValue
    }

    /** Effective maximum number of request retries for this provider. */
    fun requestMaxRetries(): Long =
        (requestMaxRetries ?: DEFAULT_REQUEST_MAX_RETRIES).coerceAtMost(MAX_REQUEST_MAX_RETRIES)

    /** Effective maximum number of stream reconnection attempts for this provider. */
    fun streamMaxRetries(): Long =
        (streamMaxRetries ?: DEFAULT_STREAM_MAX_RETRIES).coerceAtMost(MAX_STREAM_MAX_RETRIES)

    /** Effective idle timeout for streaming responses. */
    fun streamIdleTimeout(): Duration =
        (streamIdleTimeoutMs ?: DEFAULT_STREAM_IDLE_TIMEOUT_MS).milliseconds
}

const val DEFAULT_LMSTUDIO_PORT: Int = 1234
const val DEFAULT_OLLAMA_PORT: Int = 11434

const val LMSTUDIO_OSS_PROVIDER_ID: String = "lmstudio"
const val OLLAMA_OSS_PROVIDER_ID: String = "ollama"

/** Built-in default provider list. */
fun builtInModelProviders(): Map<String, ModelProviderInfo> {
    // We do not want to be in the business of adjucating which third-party
    // providers are bundled with Codex CLI, so we only include the OpenAI and
    // open source ("oss") providers by default. Users are encouraged to add to
    // modelProviders in config.toml to add their own providers.
    // `modelProviders` in config.toml to add their own providers.
    return listOf(
        "openai" to ModelProviderInfo(
            name = "OpenAI",
            // Allow users to override the default OpenAI endpoint by
            // exporting `OPENAI_BASE_URL`. This is useful when pointing
            // Codex at a proxy, mock server, or Azure-style deployment
            // without requiring a full TOML override for the built-in
            // OpenAI provider.
            baseUrl = envVar("OPENAI_BASE_URL")?.takeIf { it.trim().isNotEmpty() },
            envKey = null,
            envKeyInstructions = null,
            experimentalBearerToken = null,
            wireApi = WireApi.Responses,
            queryParams = null,
            httpHeaders = mapOf("version" to CARGO_PKG_VERSION),
            envHttpHeaders = mapOf(
                "OpenAI-Organization" to "OPENAI_ORGANIZATION",
                "OpenAI-Project" to "OPENAI_PROJECT",
            ),
            // Use global defaults for retry/timeout unless overridden in config.toml.
            requestMaxRetries = null,
            streamMaxRetries = null,
            streamIdleTimeoutMs = null,
            requiresOpenaiAuth = true,
        ),
        OLLAMA_OSS_PROVIDER_ID to createOssProvider(DEFAULT_OLLAMA_PORT, WireApi.Chat),
        LMSTUDIO_OSS_PROVIDER_ID to createOssProvider(DEFAULT_LMSTUDIO_PORT, WireApi.Responses),
    ).associate { (k, v) -> k to v }
}

private const val CARGO_PKG_VERSION: String = "0.0.0"

fun createOssProvider(defaultProviderPort: Int, wireApi: WireApi): ModelProviderInfo {
    // These CODEX_OSS_ environment variables are experimental: we may
    // switch to reading values from config.toml instead.
    val codexOssBaseUrl = run {
        val explicit = envVar("CODEX_OSS_BASE_URL")?.takeIf { it.trim().isNotEmpty() }
        if (explicit != null) {
            explicit
        } else {
            val port = envVar("CODEX_OSS_PORT")
                ?.takeIf { it.trim().isNotEmpty() }
                ?.toIntOrNull()
                ?: defaultProviderPort
            "http://localhost:$port/v1"
        }
    }
    return createOssProviderWithBaseUrl(codexOssBaseUrl, wireApi)
}

fun createOssProviderWithBaseUrl(baseUrl: String, wireApi: WireApi): ModelProviderInfo =
    ModelProviderInfo(
        name = "gpt-oss",
        baseUrl = baseUrl,
        envKey = null,
        envKeyInstructions = null,
        experimentalBearerToken = null,
        wireApi = wireApi,
        queryParams = null,
        httpHeaders = null,
        envHttpHeaders = null,
        requestMaxRetries = null,
        streamMaxRetries = null,
        streamIdleTimeoutMs = null,
        requiresOpenaiAuth = false,
    )
