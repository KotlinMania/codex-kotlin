// port-lint: source chat.rs
package io.github.kotlinmania.codex.api.endpoint

import io.github.kotlinmania.codex.api.AuthProvider
import io.github.kotlinmania.codex.api.common.Prompt
import io.github.kotlinmania.codex.api.common.ResponseStream
import io.github.kotlinmania.codex.api.provider.Provider
import io.github.kotlinmania.codex.api.provider.WireApi
import io.github.kotlinmania.codex.api.requests.ChatRequest
import io.github.kotlinmania.codex.api.requests.ChatRequestBuilder
import io.github.kotlinmania.codex.api.telemetry.RequestTelemetry
import io.github.kotlinmania.codex.api.telemetry.SseTelemetry
import io.github.kotlinmania.codex.protocol.ResponseEvent
import io.ktor.client.*
import kotlinx.serialization.json.JsonElement

/** Client for Chat Completions endpoint. */
class ChatClient<A : AuthProvider>(
    httpClient: HttpClient,
    provider: Provider,
    auth: A,
) {
    private val streaming: StreamingClient<A> = StreamingClient(httpClient, provider, auth)

    fun withTelemetry(
        request: RequestTelemetry?,
        sse: SseTelemetry?,
    ): ChatClient<A> {
        streaming.withTelemetry(request, sse)
        return this
    }

    suspend fun streamRequest(request: ChatRequest): Result<ResponseStream> = stream(request.body, request.configureHeaders)

    suspend fun streamPrompt(
        model: String,
        prompt: Prompt,
        conversationId: String?,
        sessionSource: io.github.kotlinmania.codex.protocol.SessionSource?,
    ): Result<ResponseStream> {
        val request =
            ChatRequestBuilder(model, prompt.instructions, prompt.input, prompt.tools)
                .conversationId(conversationId)
                .sessionSource(sessionSource)
                .build(streaming.provider())
                .getOrElse { return Result.failure(it) }
        return streamRequest(request)
    }

    private fun path(): String =
        when (streaming.provider().wire) {
            WireApi.Chat -> "chat/completions"
            else -> "responses"
        }

    suspend fun stream(
        body: JsonElement,
        configureExtraHeaders: io.ktor.client.request.HttpRequestBuilder.() -> Unit,
    ): Result<ResponseStream> =
        streaming.stream(path(), body, configureExtraHeaders) { client, requestConfig, idleTimeout, telemetry ->
            io.github.kotlinmania.codex.api.sse
                .spawnChatStream(client, requestConfig, idleTimeout, telemetry)
        }
}

/** Aggregation mode for stream processing. */
enum class AggregateMode {
    AggregatedOnly,
    Streaming,
}

/**
 * Stream adapter that merges token deltas into a single assistant message per turn.
 * Mirrors the upstream AggregatedStream implementation Stream for AggregatedStream.
 */
class AggregatedStream private constructor(
    private val inner: ResponseStream,
    private val mode: AggregateMode,
) : ResponseStream {
    private val cumulative = StringBuilder()
    private val cumulativeReasoning = StringBuilder()
    private val pending = ArrayDeque<ResponseEvent>()

    /**
     * Poll the next event from the aggregated stream.
     * This implements the full Rust pollNext logic for event aggregation.
     */
    override suspend fun next(): Result<ResponseEvent>? {
        // Return pending events first
        pending.firstOrNull()?.let { event ->
            pending.removeFirst()
            return Result.success(event)
        }

        // Poll inner stream in a loop, aggregating as we go
        while (true) {
            val result = inner.next() ?: return null

            // Handle errors and end-of-stream
            if (result.isFailure) {
                return Result.failure(result.exceptionOrNull()!!)
            }

            val event = result.getOrNull() ?: return null

            when (event) {
                is ResponseEvent.OutputItemDone -> {
                    val item = event.item
                    val isAssistantMessage = item is io.github.kotlinmania.codex.protocol.ResponseItem.Message && item.role == "assistant"

                    if (isAssistantMessage) {
                        when (mode) {
                            AggregateMode.AggregatedOnly -> {
                                // Accumulate text from first message with OutputText content
                                if (cumulative.isEmpty()) {
                                    item.content
                                        .firstOrNull { it is io.github.kotlinmania.codex.protocol.ContentItem.OutputText }
                                        ?.let { contentItem ->
                                            if (contentItem is io.github.kotlinmania.codex.protocol.ContentItem.OutputText) {
                                                cumulative.append(contentItem.text)
                                            }
                                        }
                                }
                                continue // Don't emit, keep looping
                            }
                            AggregateMode.Streaming -> {
                                // In streaming mode, emit the item if we have not accumulated anything
                                if (cumulative.isEmpty()) {
                                    return Result.success(event)
                                } else {
                                    continue // Skip this item, we are aggregating
                                }
                            }
                        }
                    }

                    // Non-assistant messages pass through
                    return Result.success(event)
                }

                is ResponseEvent.RateLimits -> {
                    return Result.success(event)
                }

                is ResponseEvent.Completed -> {
                    var emittedAny = false

                    // Emit aggregated reasoning if we accumulated any
                    if (cumulativeReasoning.isNotEmpty()) {
                        val aggregatedReasoning =
                            io.github.kotlinmania.codex.protocol.ResponseItem.Reasoning(
                                id = "",
                                summary = emptyList(),
                                content =
                                    listOf(
                                        io.github.kotlinmania.codex.protocol.ReasoningItemContent.ReasoningText(
                                            text = cumulativeReasoning.toString(),
                                        ),
                                    ),
                                encryptedContent = null,
                            )
                        pending.add(ResponseEvent.OutputItemDone(aggregatedReasoning))
                        cumulativeReasoning.clear()
                        emittedAny = true
                    }

                    // Emit aggregated message if we accumulated any
                    if (cumulative.isNotEmpty()) {
                        val aggregatedMessage =
                            io.github.kotlinmania.codex.protocol.ResponseItem.Message(
                                role = "assistant",
                                content =
                                    listOf(
                                        io.github.kotlinmania.codex.protocol.ContentItem
                                            .OutputText(text = cumulative.toString()),
                                    ),
                                id = null,
                            )
                        pending.add(ResponseEvent.OutputItemDone(aggregatedMessage))
                        cumulative.clear()
                        emittedAny = true
                    }

                    // Add the completion event at the end
                    if (emittedAny) {
                        pending.add(event)
                        // Return the first pending event
                        return Result.success(pending.removeFirst())
                    }

                    return Result.success(event)
                }

                is ResponseEvent.Created -> {
                    continue // Skip Created events
                }

                is ResponseEvent.OutputTextDelta -> {
                    cumulative.append(event.delta)
                    if (mode == AggregateMode.Streaming) {
                        return Result.success(event)
                    } else {
                        continue // Accumulate but do not emit
                    }
                }

                is ResponseEvent.ReasoningContentDelta -> {
                    cumulativeReasoning.append(event.delta)
                    if (mode == AggregateMode.Streaming) {
                        return Result.success(event)
                    } else {
                        continue // Accumulate but do not emit
                    }
                }

                is ResponseEvent.ReasoningSummaryDelta -> {
                    continue // Skip summary deltas
                }

                is ResponseEvent.ReasoningSummaryPartAdded -> {
                    continue // Skip summary part additions
                }

                is ResponseEvent.OutputItemAdded -> {
                    return Result.success(event)
                }
            }
        }
    }

    companion object {
        fun new(inner: ResponseStream, mode: AggregateMode): AggregatedStream = AggregatedStream(inner, mode)
    }
}

/**
 * Extension functions for ResponseStream aggregation.
 * Mirrors the upstream AggregateStreamExt trait.
 */
fun ResponseStream.aggregate(): AggregatedStream = AggregatedStream.new(this, AggregateMode.AggregatedOnly)

fun ResponseStream.streamingMode(): ResponseStream = this
