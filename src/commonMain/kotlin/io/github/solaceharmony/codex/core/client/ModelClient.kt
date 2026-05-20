// port-lint: source client.rs
package io.github.solaceharmony.codex.core.client

import io.github.solaceharmony.codex.api.AuthProvider
import io.github.solaceharmony.codex.api.endpoint.ChatClient
import io.github.solaceharmony.codex.api.endpoint.CompactClient
import io.github.solaceharmony.codex.api.endpoint.ResponsesClient
import io.github.solaceharmony.codex.api.endpoint.ResponsesOptions
import io.github.solaceharmony.codex.api.endpoint.aggregate
import io.github.solaceharmony.codex.api.endpoint.streamingMode
import io.github.solaceharmony.codex.api.common.CompactionInput
import io.github.solaceharmony.codex.api.common.Prompt as ApiPrompt
import io.github.solaceharmony.codex.api.common.Reasoning
import io.github.solaceharmony.codex.api.common.ResponseStream as ApiResponseStream
import io.github.solaceharmony.codex.protocol.ResponseEvent
import io.github.solaceharmony.codex.api.common.createTextParamForRequest
import io.github.solaceharmony.codex.api.error.ApiError
import io.github.solaceharmony.codex.api.telemetry.RequestTelemetry
import io.github.solaceharmony.codex.api.telemetry.SseTelemetry
import io.github.solaceharmony.codex.core.AuthManager
import io.github.solaceharmony.codex.core.AuthMode
import io.github.solaceharmony.codex.core.CodexAuth
import io.github.solaceharmony.codex.core.config.Config
import io.github.solaceharmony.codex.core.CodexErr
import io.github.solaceharmony.codex.core.model.ModelFamily
import io.github.solaceharmony.codex.core.model.ModelProviderInfo
import io.github.solaceharmony.codex.core.model.WireApi
import io.github.solaceharmony.codex.core.prompt.Prompt
import io.github.solaceharmony.codex.protocol.ConversationId
import io.github.solaceharmony.codex.protocol.ReasoningEffort
import io.github.solaceharmony.codex.protocol.ReasoningSummary
import io.github.solaceharmony.codex.protocol.ResponseItem
import io.github.solaceharmony.codex.protocol.SessionSource
import io.ktor.client.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.JsonElement
import kotlin.time.Duration

/**
 * Main client for streaming model interactions.
 *
 * Manages authentication, API client creation, and streaming responses
 * through the Chat Completions or Responses API endpoints.
 *
 * Mirrors the upstream ModelClient from core/src/client.rs
 */
class ModelClient(
    private val config: Config,
    private val authManager: AuthManager?,
    private val otelEventManager: OtelEventManager,
    private val provider: ModelProviderInfo,
    private val conversationId: ConversationId,
    private val effort: ReasoningEffort?,
    private val summary: ReasoningSummary,
    private val sessionSource: SessionSource,
) {

    /**
     * Get the effective model context window accounting for the configured percentage.
     */
    fun getModelContextWindow(): Long? {
        val pct = config.modelFamily.effectiveContextWindowPercent
        val window = config.modelContextWindow
            ?: getModelInfo(config.modelFamily)?.contextWindow
            ?: return null
        return (window * pct) / 100
    }

    /**
     * Get the auto-compact token limit for this model.
     */
    fun getAutoCompactTokenLimit(): Long? {
        return config.modelAutoCompactTokenLimit
            ?: getModelInfo(config.modelFamily)?.autoCompactTokenLimit
    }

    fun config(): Config = config

    fun provider(): ModelProviderInfo = provider

    /**
     * Streams a single model turn using either the Responses or Chat
     * Completions wire API, depending on the configured provider.
     *
     * For Chat providers, the underlying stream is optionally aggregated
     * based on the `showRawAgentReasoning` flag in the config.
     */
    suspend fun stream(prompt: Prompt): Result<ResponseStream> {
        return when (provider.wireApi) {
            WireApi.Responses -> streamResponsesApi(prompt)
            WireApi.Chat -> {
                val apiStream = streamChatCompletions(prompt).getOrElse {
                    return Result.failure(it)
                }

                val processedStream = if (config.showRawAgentReasoning) {
                    apiStream.streamingMode()
                } else {
                    apiStream.aggregate()
                }

                Result.success(mapResponseStream(processedStream, otelEventManager))
            }
        }
    }

    /**
     * Streams a turn via the OpenAI Chat Completions API.
     *
     * This path is only used when the provider is configured with
     * `WireApi.Chat`; it does not support `outputSchema` today.
     */
    private suspend fun streamChatCompletions(prompt: Prompt): Result<ApiResponseStream> {
        if (prompt.outputSchema != null) {
            return Result.failure(
                CodexErr.UnsupportedOperation(
                    "output_schema is not supported for Chat Completions API"
                ).toException()
            )
        }

        val instructions = prompt.getFullInstructions(config.modelFamily)
        val toolsJson = createToolsJsonForChatCompletionsApi(prompt.tools)
            ?: return Result.failure(Exception("Failed to create tools JSON"))
        val apiPrompt = buildApiPrompt(prompt, instructions, toolsJson)
        val conversationIdStr = conversationId.toString()
        val sessionSourceClone = sessionSource

        var refreshed = false
        while (true) {
            val auth = authManager?.auth()
            val apiProvider = provider.toApiProvider(auth?.mode)
            val apiAuth = authProviderFromAuth(auth, provider)
                ?: return Result.failure(Exception("Failed to create API auth"))

            val httpClient = buildHttpClient()
            val (requestTelemetry, sseTelemetry) = buildStreamingTelemetry()
            val client = ChatClient(httpClient, apiProvider, apiAuth)
                .withTelemetry(requestTelemetry, sseTelemetry)

            val streamResult = client.streamPrompt(
                model = config.model,
                prompt = apiPrompt,
                conversationId = conversationIdStr,
                sessionSource = sessionSourceClone
            )

            return when {
                streamResult.isSuccess -> streamResult
                isUnauthorizedError(streamResult) -> {
                    handleUnauthorized(
                        HttpStatusCode.Unauthorized,
                        refreshed,
                        authManager,
                        auth
                    ).getOrElse { return Result.failure(it) }
                    refreshed = true
                    continue
                }
                else -> Result.failure(
                    streamResult.exceptionOrNull() ?: Exception("Unknown error")
                )
            }
        }
    }

    /**
     * Streams a turn via the OpenAI Responses API.
     *
     * Handles SSE fixtures, reasoning summaries, verbosity, and the
     * `text` controls used for output schemas.
     */
    private suspend fun streamResponsesApi(prompt: Prompt): Result<ResponseStream> {
        // TODO: Handle SSE fixture loading if CODEX_RS_SSE_FIXTURE is set

        val instructions = prompt.getFullInstructions(config.modelFamily)
        val toolsJson = createToolsJsonForResponsesApi(prompt.tools)
            ?: return Result.failure(Exception("Failed to create tools JSON"))

        val reasoning = if (config.modelFamily.supportsReasoningSummaries) {
            Reasoning(
                effort = effort ?: config.modelFamily.defaultReasoningEffort,
                summary = summary
            )
        } else {
            null
        }

        val include = if (reasoning != null) {
            listOf("reasoning.encrypted_content")
        } else {
            emptyList()
        }

        val verbosity = if (config.modelFamily.supportVerbosity) {
            config.modelVerbosity ?: config.modelFamily.defaultVerbosity
        } else {
            if (config.modelVerbosity != null) {
                // TODO: Log warning about verbosity being ignored
            }
            null
        }

        val text = createTextParamForRequest(verbosity, prompt.outputSchema)
        val apiPrompt = buildApiPrompt(prompt, instructions, toolsJson)
        val conversationIdStr = conversationId.toString()
        val sessionSourceClone = sessionSource

        var refreshed = false
        while (true) {
            val auth = authManager?.auth()
            val apiProvider = provider.toApiProvider(auth?.mode)
            val apiAuth = authProviderFromAuth(auth, provider)
                ?: return Result.failure(Exception("Failed to create API auth"))

            val httpClient = buildHttpClient()
            val (requestTelemetry, sseTelemetry) = buildStreamingTelemetry()
            val client = ResponsesClient(httpClient, apiProvider, apiAuth)
                .withTelemetry(requestTelemetry, sseTelemetry)

            val options = ResponsesOptions(
                reasoning = reasoning,
                include = include,
                promptCacheKey = conversationIdStr,
                text = text,
                storeOverride = null,
                conversationId = conversationIdStr,
                sessionSource = sessionSourceClone
            )

            val streamResult = client.streamPrompt(
                model = config.model,
                prompt = apiPrompt,
                options = options
            )

            return when {
                streamResult.isSuccess -> {
                    val stream = streamResult.getOrThrow()
                    Result.success(mapResponseStream(stream, otelEventManager))
                }
                isUnauthorizedError(streamResult) -> {
                    handleUnauthorized(
                        HttpStatusCode.Unauthorized,
                        refreshed,
                        authManager,
                        auth
                    ).getOrElse { return Result.failure(it) }
                    refreshed = true
                    continue
                }
                else -> Result.failure(
                    streamResult.exceptionOrNull() ?: Exception("Unknown error")
                )
            }
        }
    }

    fun getProvider(): ModelProviderInfo = provider

    fun getOtelEventManager(): OtelEventManager = otelEventManager

    fun getSessionSource(): SessionSource = sessionSource

    fun getModel(): String = config.model

    fun getModelFamily(): ModelFamily = config.modelFamily

    fun getReasoningEffort(): ReasoningEffort? = effort

    fun getReasoningSummary(): ReasoningSummary = summary

    fun getAuthManager(): AuthManager? = authManager

    /**
     * Compacts the current conversation history using the Compact endpoint.
     *
     * This is a unary call (no streaming) that returns a new list of
     * `ResponseItem`s representing the compacted transcript.
     */
    suspend fun compactConversationHistory(prompt: Prompt): Result<List<ResponseItem>> {
        if (prompt.input.isEmpty()) {
            return Result.success(emptyList())
        }

        val auth = authManager?.auth()
        val apiProvider = provider.toApiProvider(auth?.mode)
        val apiAuth = authProviderFromAuth(auth, provider)
            ?: return Result.failure(Exception("Failed to create API auth"))

        val httpClient = buildHttpClient()
        val requestTelemetry = buildRequestTelemetry()
        val client = CompactClient(httpClient, apiProvider, apiAuth)
            .withTelemetry(requestTelemetry)

        val instructions = prompt.getFullInstructions(config.modelFamily)
        val payload = CompactionInput(
            model = config.model,
            input = prompt.input,
            instructions = instructions
        )

        // Build extra headers for subagent
        val configureHeaders: io.ktor.client.request.HttpRequestBuilder.() -> Unit = {
            if (sessionSource == SessionSource.SubAgent) {
                val subagent = "review"
                headers.append("x-openai-subagent", subagent)
            }
        }

        return client.compactInput(payload, configureHeaders)
    }

    /**
     * Builds request and SSE telemetry for streaming API calls (Chat/Responses).
     */
    private fun buildStreamingTelemetry(): Pair<RequestTelemetry, SseTelemetry> {
        val telemetry = ApiTelemetry(otelEventManager)
        return Pair(telemetry, telemetry)
    }

    /**
     * Builds request telemetry for unary API calls (e.g., Compact endpoint).
     */
    private fun buildRequestTelemetry(): RequestTelemetry {
        return ApiTelemetry(otelEventManager)
    }
}

/**
 * Adapts the core `Prompt` type into the `codex-api` payload shape.
 */
private fun buildApiPrompt(prompt: Prompt, instructions: String, toolsJson: List<JsonElement>): ApiPrompt {
    return ApiPrompt(
        instructions = instructions,
        input = prompt.getFormattedInput(),
        tools = toolsJson,
        parallelToolCalls = prompt.parallelToolCalls,
        outputSchema = prompt.outputSchema
    )
}

/**
 * Maps an API response stream to the core ResponseStream type.
 */
private fun mapResponseStream(
    apiStream: ApiResponseStream,
    otelEventManager: OtelEventManager
): ResponseStream {
    // TODO: Implement full stream mapping with telemetry events
    // For now, return a placeholder
    return ResponseStream(
        flow {
            // Poll the API stream and emit events
            while (true) {
                val result = apiStream.next() ?: break
                if (result.isFailure) {
                    val error = result.exceptionOrNull() ?: Exception("Unknown error")
                    otelEventManager.seeEventCompletedFailed(error)
                    emit(Result.failure(error))
                    break
                }
                val event = result.getOrNull() ?: break

                // Handle completion events for telemetry
                if (event is ResponseEvent.Completed) {
                    event.tokenUsage?.let { usage ->
                        otelEventManager.sseEventCompleted(
                            inputTokens = usage.inputTokens,
                            outputTokens = usage.outputTokens,
                            cachedInputTokens = usage.cachedInputTokens,
                            reasoningOutputTokens = usage.reasoningOutputTokens,
                            totalTokens = usage.totalTokens
                        )
                    }
                }

                emit(Result.success(event))
            }
        }
    )
}

/**
 * Handles a 401 response by optionally refreshing ChatGPT tokens once.
 */
private suspend fun handleUnauthorized(
    status: HttpStatusCode,
    refreshed: Boolean,
    authManager: AuthManager?,
    auth: CodexAuth?
): Result<Unit> {
    if (refreshed) {
        return Result.failure(mapUnauthorizedStatus(status))
    }

    if (authManager != null && auth != null && auth.mode == AuthMode.ChatGPT) {
        val refreshResult = authManager.refreshToken()
        return if (refreshResult.isSuccess) {
            Result.success(Unit)
        } else {
            val msg = refreshResult.exceptionOrNull()?.message ?: "Unknown error"
            Result.failure(CodexErr.RefreshTokenFailed(msg).toException())
        }
    }

    return Result.failure(mapUnauthorizedStatus(status))
}

private fun mapUnauthorizedStatus(status: HttpStatusCode): Exception {
    return ApiError.Transport(
        io.github.solaceharmony.codex.client.error.TransportError.Http(status = status)
    )
}

private fun isUnauthorizedError(result: Result<*>): Boolean {
    val error = result.exceptionOrNull()
    if (error !is ApiError.Transport) return false
    val inner = error.error
    return inner is io.github.solaceharmony.codex.client.error.TransportError.Http &&
        inner.status == HttpStatusCode.Unauthorized
}

/**
 * Telemetry implementation for API requests and SSE events.
 */
private class ApiTelemetry(
    private val otelEventManager: OtelEventManager
) : RequestTelemetry, SseTelemetry {

    override fun onRequest(
        attempt: Int,
        status: HttpStatusCode?,
        error: Throwable?,
        duration: Duration
    ) {
        otelEventManager.recordApiRequest(
            attempt = attempt.toLong(),
            status = status?.value,
            errorMessage = error?.message,
            duration = duration
        )
    }

    override fun onSsePoll(
        hasData: Boolean,
        duration: Duration
    ) {
        otelEventManager.logSseEvent(hasData, duration)
    }
}

/**
 * Core ResponseStream wrapper.
 * TODO: This should be defined in core, not here.
 */
data class ResponseStream(
    val events: Flow<Result<ResponseEvent>>
)

/**
 * Placeholder for OtelEventManager.
 * TODO: Port from codex-otel crate.
 */
class OtelEventManager {
    fun sseEventCompleted(
        inputTokens: Long,
        outputTokens: Long,
        cachedInputTokens: Long?,
        reasoningOutputTokens: Long?,
        totalTokens: Long
    ) {
        // TODO: Implement telemetry logging
    }

    fun seeEventCompletedFailed(error: Throwable) {
        // TODO: Implement error telemetry
    }

    fun recordApiRequest(
        attempt: Long,
        status: Int?,
        errorMessage: String?,
        duration: Duration
    ) {
        // TODO: Implement request telemetry
    }

    fun logSseEvent(hasData: Boolean, duration: Duration) {
        // SSE event telemetry placeholder
    }
}




// Placeholder functions that need to be ported from other modules

private fun getModelInfo(modelFamily: ModelFamily): ModelInfo? {
    // TODO: Port from openaiModelInfo.rs
    return null
}

private fun createToolsJsonForChatCompletionsApi(tools: List<Any>): List<JsonElement>? {
    // TODO: Port from tools/spec.rs
    return emptyList()
}

private fun createToolsJsonForResponsesApi(tools: List<Any>): List<JsonElement>? {
    // TODO: Port from tools/spec.rs
    return emptyList()
}

private fun authProviderFromAuth(auth: CodexAuth?, provider: ModelProviderInfo): AuthProvider? {
    // TODO: Port from apiBridge.rs
    return null
}

private fun buildHttpClient(): HttpClient {
    // TODO: Port from defaultClient.rs
    return HttpClient()
}

data class ModelInfo(
    val contextWindow: Long,
    val autoCompactTokenLimit: Long?
)
