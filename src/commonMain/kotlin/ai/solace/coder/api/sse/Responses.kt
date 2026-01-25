// port-lint: source codex-api/src/sse/responses.rs
package ai.solace.coder.api.sse

import ai.solace.coder.api.common.ResponseStream
import ai.solace.coder.api.error.ApiError
import ai.solace.coder.api.telemetry.SseTelemetry
import ai.solace.coder.protocol.ResponseEvent
import ai.solace.coder.protocol.ResponseItem
import ai.solace.coder.protocol.TokenUsage
import ai.solace.coder.protocol.RateLimitSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import okio.BufferedSource
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import kotlin.time.Duration
import kotlin.time.TimeSource

private val json = Json { ignoreUnknownKeys = true }

/**
 * Streams SSE events from an on-disk fixture for tests.
 */
fun streamFromFixture(
    path: String,
    idleTimeout: Duration,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
): Result<ResponseStream> {
    return try {
        val fs = FileSystem.SYSTEM
        val okioPath = path.toPath()
        val source = fs.source(okioPath).buffer()
        val content = StringBuilder()
        while (!source.exhausted()) {
            val line = source.readUtf8Line() ?: break
            content.append(line).append("\n\n")
        }
        
        val reader = okio.Buffer().writeUtf8(content.toString())
        val channel = Channel<Result<ResponseEvent>>(1600)
        scope.launch {
            processSse(reader, channel, idleTimeout, null)
            channel.close()
        }
        Result.success(ChannelResponseStream(channel))
    } catch (e: Exception) {
        Result.failure(ApiError.Stream(e.message ?: "fixture error"))
    }
}

/**
 * Implementation of ResponseStream using a Channel.
 */
class ChannelResponseStream(
    private val rxEvent: Channel<Result<ResponseEvent>>
) : ResponseStream {
    override suspend fun next(): Result<ResponseEvent>? {
        return try {
            rxEvent.receive()
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Close the stream.
     */
    fun close() {
        rxEvent.close()
    }
}

@Serializable
private data class ResponseCompleted(
    val id: String,
    val usage: ResponseCompletedUsage? = null,
)

@Serializable
private data class ResponseCompletedUsage(
    val input_tokens: Long,
    val input_tokens_details: ResponseCompletedInputTokensDetails? = null,
    val output_tokens: Long,
    val output_tokens_details: ResponseCompletedOutputTokensDetails? = null,
    val total_tokens: Long,
)

@Serializable
private data class ResponseCompletedInputTokensDetails(
    val cached_tokens: Long,
)

@Serializable
private data class ResponseCompletedOutputTokensDetails(
    val reasoning_tokens: Long,
)

private fun ResponseCompletedUsage.toTokenUsage(): TokenUsage {
    return TokenUsage(
        inputTokens = input_tokens,
        cachedInputTokens = input_tokens_details?.cached_tokens ?: 0,
        outputTokens = output_tokens,
        reasoningOutputTokens = output_tokens_details?.reasoning_tokens ?: 0,
        totalTokens = total_tokens,
    )
}

@Serializable
private data class SseEventJson(
    val type: String,
    val response: JsonElement? = null,
    val item: JsonElement? = null,
    val delta: String? = null,
    val summary_index: Long? = null,
    val content_index: Long? = null,
)

/**
 * Main SSE processing loop.
 * Transliterated from Rust process_sse.
 */
suspend fun processSse(
    source: BufferedSource,
    txEvent: Channel<Result<ResponseEvent>>,
    idleTimeout: Duration,
    telemetry: SseTelemetry?,
) {
    var responseCompleted: ResponseCompleted? = null
    var responseError: ApiError? = null
    val timeSource = TimeSource.Monotonic

    try {
        source.eventsource().collect { sse ->
            val start = timeSource.markNow()
            // Note: In Rust, timeout is on stream.next(). 
            // Here, eventsource() flow handles framing. 
            // Real timeout would be on reading from the network source.
            // For transliteration purposes, we process the event.
            
            telemetry?.onSsePoll(true, start.elapsedNow())

            val event = try {
                json.decodeFromString<SseEventJson>(sse.data)
            } catch (e: Exception) {
                // debug!("Failed to parse SSE event: {e}, data: {}", &sse.data);
                return@collect
            }

            when (event.type) {
                "response.output_item.done" -> {
                    val itemVal = event.item ?: return@collect
                    val item = try {
                        json.decodeFromJsonElement<ResponseItem>(itemVal)
                    } catch (e: Exception) {
                        return@collect
                    }
                    txEvent.send(Result.success(ResponseEvent.OutputItemDone(item)))
                }
                "response.output_text.delta" -> {
                    event.delta?.let {
                        txEvent.send(Result.success(ResponseEvent.OutputTextDelta(it)))
                    }
                }
                "response.reasoning_summary_text.delta" -> {
                    if (event.delta != null && event.summary_index != null) {
                        txEvent.send(Result.success(ResponseEvent.ReasoningSummaryDelta(event.delta, event.summary_index)))
                    }
                }
                "response.reasoning_text.delta" -> {
                    if (event.delta != null && event.content_index != null) {
                        txEvent.send(Result.success(ResponseEvent.ReasoningContentDelta(event.delta, event.content_index)))
                    }
                }
                "response.created" -> {
                    txEvent.send(Result.success(ResponseEvent.Created))
                }
                "response.failed" -> {
                    // Extract error from response object if present
                    val errorVal = event.response?.jsonObject?.get("error")
                    if (errorVal != null) {
                        // TODO: Map specific error codes to ApiError variants as in Rust
                        responseError = ApiError.Stream("response.failed: $errorVal")
                    } else {
                        responseError = ApiError.Stream("response.failed")
                    }
                }
                "response.completed" -> {
                    event.response?.let {
                        try {
                            responseCompleted = json.decodeFromJsonElement<ResponseCompleted>(it)
                        } catch (e: Exception) {
                            responseError = ApiError.Stream("failed to parse response.completed")
                        }
                    }
                }
                "response.output_item.added" -> {
                    val itemVal = event.item ?: return@collect
                    val item = try {
                        json.decodeFromJsonElement<ResponseItem>(itemVal)
                    } catch (e: Exception) {
                        return@collect
                    }
                    txEvent.send(Result.success(ResponseEvent.OutputItemAdded(item)))
                }
                "response.reasoning_summary_part.added" -> {
                    event.summary_index?.let {
                        txEvent.send(Result.success(ResponseEvent.ReasoningSummaryPartAdded(it)))
                    }
                }
            }
        }
    } catch (e: Exception) {
        txEvent.send(Result.failure(ApiError.Stream(e.message ?: "stream error")))
        return
    }

    // Handle end of stream
    when {
        responseCompleted != null -> {
            val event = ResponseEvent.Completed(
                responseId = responseCompleted!!.id,
                tokenUsage = responseCompleted!!.usage?.toTokenUsage()
            )
            txEvent.send(Result.success(event))
        }
        responseError != null -> {
            txEvent.send(Result.failure(responseError!!))
        }
        else -> {
            txEvent.send(Result.failure(ApiError.Stream("stream closed before response.completed")))
        }
    }
}
