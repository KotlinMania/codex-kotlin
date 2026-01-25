// port-lint: source codex-api/src/sse/chat.rs
package ai.solace.coder.api.sse

import ai.solace.coder.api.common.ResponseStream
import ai.solace.coder.api.error.ApiError
import ai.solace.coder.api.telemetry.SseTelemetry
import ai.solace.coder.protocol.ContentItem
import ai.solace.coder.protocol.ReasoningItemContent
import ai.solace.coder.protocol.ResponseEvent
import ai.solace.coder.protocol.ResponseItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.serialization.json.*
import okio.BufferedSource
import kotlin.time.Duration
import kotlin.time.TimeSource

private val json = Json { ignoreUnknownKeys = true }

/**
 * Process Chat SSE stream.
 * Transliterated from Rust process_chat_sse.
 */
suspend fun processChatSse(
    source: BufferedSource,
    txEvent: Channel<Result<ResponseEvent>>,
    idleTimeout: Duration,
    telemetry: SseTelemetry?,
) {
    data class ToolCallState(
        var name: String? = null,
        var arguments: StringBuilder = StringBuilder()
    )

    val toolCalls = mutableMapOf<String, ToolCallState>()
    val toolCallOrder = mutableListOf<String>()
    var assistantItem: ResponseItem.Message? = null
    var reasoningItem: ResponseItem.Reasoning? = null
    var completedSent = false
    val timeSource = TimeSource.Monotonic

    try {
        source.eventsource().collect { sse ->
            val start = timeSource.markNow()
            
            telemetry?.onSsePoll(true, start.elapsedNow())

            if (sse.data.trim().isEmpty()) return@collect

            val value = try {
                json.parseToJsonElement(sse.data).jsonObject
            } catch (e: Exception) {
                // debug!("Failed to parse ChatCompletions SSE event: {err}, data: {}", &sse.data);
                return@collect
            }

            val choices = value["choices"]?.jsonArray ?: return@collect

            for (choice in choices) {
                val choiceObj = choice.jsonObject
                
                // 1) Delta
                val delta = choiceObj["delta"]?.jsonObject
                if (delta != null) {
                    // Reasoning
                    val reasoning = delta["reasoning"]
                    if (reasoning != null) {
                        val text = when {
                            reasoning is JsonPrimitive && reasoning.isString -> reasoning.content
                            reasoning is JsonObject -> reasoning["text"]?.jsonPrimitive?.contentOrNull
                                ?: reasoning["content"]?.jsonPrimitive?.contentOrNull
                            else -> null
                        }
                        text?.let {
                            val (updated, event) = appendReasoningText(txEvent, reasoningItem, it)
                            reasoningItem = updated
                            event?.let { e -> txEvent.send(Result.success(e)) }
                        }
                    }

                    // Content
                    val content = delta["content"]
                    if (content != null) {
                        if (content is JsonArray) {
                            for (item in content) {
                                val text = item.jsonObject["text"]?.jsonPrimitive?.contentOrNull
                                text?.let {
                                    val (updated, event) = appendAssistantText(txEvent, assistantItem, it)
                                    assistantItem = updated
                                    event?.let { e -> txEvent.send(Result.success(e)) }
                                }
                            }
                        } else if (content is JsonPrimitive && content.isString) {
                            val (updated, event) = appendAssistantText(txEvent, assistantItem, content.content)
                            assistantItem = updated
                            event?.let { e -> txEvent.send(Result.success(e)) }
                        }
                    }

                    // Tool calls
                    val toolCallsArray = delta["tool_calls"]?.jsonArray
                    if (toolCallsArray != null) {
                        for (toolCall in toolCallsArray) {
                            val tcObj = toolCall.jsonObject
                            val id = tcObj["id"]?.jsonPrimitive?.contentOrNull 
                                ?: "tool-call-${toolCallOrder.size}"
                            
                            val callState = toolCalls.getOrPut(id) { ToolCallState() }
                            if (id !in toolCallOrder) {
                                toolCallOrder.add(id)
                            }

                            val func = tcObj["function"]?.jsonObject
                            if (func != null) {
                                func["name"]?.jsonPrimitive?.contentOrNull?.let { callState.name = it }
                                func["arguments"]?.jsonPrimitive?.contentOrNull?.let { callState.arguments.append(it) }
                            }
                        }
                    }
                }

                // 2) Message (non-delta)
                val message = choiceObj["message"]?.jsonObject
                if (message != null) {
                    val reasoning = message["reasoning"]
                    if (reasoning != null) {
                        val text = when {
                            reasoning is JsonPrimitive && reasoning.isString -> reasoning.content
                            reasoning is JsonObject -> reasoning["text"]?.jsonPrimitive?.contentOrNull
                                ?: reasoning["content"]?.jsonPrimitive?.contentOrNull
                            else -> null
                        }
                        text?.let {
                            val (updated, event) = appendReasoningText(txEvent, reasoningItem, it)
                            reasoningItem = updated
                            event?.let { e -> txEvent.send(Result.success(e)) }
                        }
                    }
                }

                // 3) Finish reason
                val finishReason = choiceObj["finish_reason"]?.jsonPrimitive?.contentOrNull
                if (finishReason == "stop") {
                    reasoningItem?.let {
                        txEvent.send(Result.success(ResponseEvent.OutputItemDone(it)))
                        reasoningItem = null
                    }
                    assistantItem?.let {
                        txEvent.send(Result.success(ResponseEvent.OutputItemDone(it)))
                        assistantItem = null
                    }
                    if (!completedSent) {
                        txEvent.send(Result.success(ResponseEvent.Completed(responseId = "", tokenUsage = null)))
                        completedSent = true
                    }
                    continue
                }

                if (finishReason == "length") {
                    txEvent.send(Result.failure(ApiError.ContextWindowExceeded()))
                    return@collect
                }

                if (finishReason == "tool_calls") {
                    reasoningItem?.let {
                        txEvent.send(Result.success(ResponseEvent.OutputItemDone(it)))
                        reasoningItem = null
                    }
                    for (callId in toolCallOrder) {
                        val state = toolCalls.remove(callId) ?: ToolCallState()
                        val item = ResponseItem.FunctionCall(
                            id = null,
                            name = state.name ?: "",
                            arguments = state.arguments.toString(),
                            callId = callId
                        )
                        txEvent.send(Result.success(ResponseEvent.OutputItemDone(item)))
                    }
                    toolCallOrder.clear()
                }
            }
        }
    } catch (e: Exception) {
        txEvent.send(Result.failure(ApiError.Stream(e.message ?: "stream error")))
        return
    }

    // Handle end of stream
    reasoningItem?.let {
        txEvent.send(Result.success(ResponseEvent.OutputItemDone(it)))
    }
    assistantItem?.let {
        txEvent.send(Result.success(ResponseEvent.OutputItemDone(it)))
    }
    if (!completedSent) {
        txEvent.send(Result.success(ResponseEvent.Completed(responseId = "", tokenUsage = null)))
    }
}

private suspend fun appendAssistantText(
    txEvent: Channel<Result<ResponseEvent>>,
    current: ResponseItem.Message?,
    text: String,
): Pair<ResponseItem.Message, ResponseEvent?> {
    var item = current
    var addedEvent: ResponseEvent? = null
    if (item == null) {
        item = ResponseItem.Message(
            id = null,
            role = "assistant",
            content = mutableListOf()
        )
        txEvent.send(Result.success(ResponseEvent.OutputItemAdded(item)))
    }
    
    val content = item.content as MutableList<ContentItem>
    content.add(ContentItem.OutputText(text))
    return item to ResponseEvent.OutputTextDelta(text)
}

private suspend fun appendReasoningText(
    txEvent: Channel<Result<ResponseEvent>>,
    current: ResponseItem.Reasoning?,
    text: String,
): Pair<ResponseItem.Reasoning, ResponseEvent?> {
    var item = current
    if (item == null) {
        item = ResponseItem.Reasoning(
            id = "",
            summary = emptyList(),
            content = mutableListOf(),
            encryptedContent = null
        )
        txEvent.send(Result.success(ResponseEvent.OutputItemAdded(item)))
    }

    val content = item.content as MutableList<ReasoningItemContent>
    val contentIndex = content.size.toLong()
    content.add(ReasoningItemContent.ReasoningText(text))
    
    return item to ResponseEvent.ReasoningContentDelta(text, contentIndex)
}
