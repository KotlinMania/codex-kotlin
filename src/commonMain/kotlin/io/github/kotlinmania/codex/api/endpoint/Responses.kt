// port-lint: source responses.rs
package io.github.kotlinmania.codex.api.endpoint

import io.github.kotlinmania.codex.api.AuthProvider
import io.github.kotlinmania.codex.api.common.Prompt
import io.github.kotlinmania.codex.api.common.Reasoning
import io.github.kotlinmania.codex.api.common.ResponseStream
import io.github.kotlinmania.codex.api.common.TextControls
import io.github.kotlinmania.codex.api.provider.Provider
import io.github.kotlinmania.codex.api.requests.ResponsesRequest
import io.github.kotlinmania.codex.api.requests.ResponsesRequestBuilder
import io.github.kotlinmania.codex.api.telemetry.RequestTelemetry
import io.github.kotlinmania.codex.api.telemetry.SseTelemetry
import io.ktor.client.*

/** Options for configuring ResponsesClient. */
data class ResponsesOptions(
    val reasoning: Reasoning? = null,
    val include: List<String> = emptyList(),
    val promptCacheKey: String? = null,
    val text: TextControls? = null,
    val storeOverride: Boolean? = null,
    val conversationId: String? = null,
    val sessionSource: io.github.kotlinmania.codex.protocol.SessionSource? = null,
)

/** Client for Responses endpoint. */
internal class ResponsesClient<A : AuthProvider>(
    httpClient: HttpClient,
    provider: Provider,
    auth: A,
) {
    private val streaming: StreamingClient<A> = StreamingClient(httpClient, provider, auth)

    fun withTelemetry(
        request: RequestTelemetry?,
        sse: SseTelemetry?,
    ): ResponsesClient<A> {
        streaming.withTelemetry(request, sse)
        return this
    }

    suspend fun streamRequest(request: ResponsesRequest): Result<ResponseStream> = stream(request.body, request.configureHeaders)

    suspend fun streamPrompt(
        model: String,
        prompt: Prompt,
        options: ResponsesOptions = ResponsesOptions(),
    ): Result<ResponseStream> {
        val request =
            ResponsesRequestBuilder(model, prompt.instructions, prompt.input)
                .tools(prompt.tools)
                .parallelToolCalls(prompt.parallelToolCalls)
                .reasoning(options.reasoning)
                .include(options.include)
                .promptCacheKey(options.promptCacheKey)
                .text(options.text)
                .storeOverride(options.storeOverride)
                .conversation(options.conversationId)
                .sessionSource(options.sessionSource)
                .build(streaming.provider())
                .getOrElse { return Result.failure(it) }
        return streamRequest(request)
    }

    private suspend fun stream(
        body: kotlinx.serialization.json.JsonElement,
        configureExtraHeaders: io.ktor.client.request.HttpRequestBuilder.() -> Unit,
    ): Result<ResponseStream> =
        streaming.stream("responses", body, configureExtraHeaders) { client, requestConfig, idleTimeout, telemetry ->
            io.github.kotlinmania.codex.api.sse
                .spawnResponsesStream(client, requestConfig, idleTimeout, telemetry)
        }
}
