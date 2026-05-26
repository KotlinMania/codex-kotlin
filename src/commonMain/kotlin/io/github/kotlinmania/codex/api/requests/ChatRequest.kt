// port-lint: source codex-rs/codex-api/src/requests/chat.rs
package io.github.kotlinmania.codex.api.requests

import io.github.kotlinmania.codex.api.provider.Provider
import io.ktor.client.request.*
import kotlinx.serialization.json.*

/** Assembled request body plus header configuration for Chat Completions streaming calls. */
data class ChatRequest(
    val body: JsonElement,
    val configureHeaders: HttpRequestBuilder.() -> Unit,
)

/** Builder for ChatRequest. */
class ChatRequestBuilder(
    private val model: String,
    private val instructions: String,
    private val input: List<io.github.kotlinmania.codex.protocol.ResponseItem>,
    private val tools: List<JsonElement>,
) {
    private var conversationId: String? = null
    private var sessionSource: io.github.kotlinmania.codex.protocol.SessionSource? = null

    fun conversationId(id: String?): ChatRequestBuilder {
        conversationId = id
        return this
    }

    fun sessionSource(source: io.github.kotlinmania.codex.protocol.SessionSource?): ChatRequestBuilder {
        sessionSource = source
        return this
    }

    fun build(provider: Provider): Result<ChatRequest> {
        return try {
            val messages = mutableListOf<JsonElement>()

            // System message
            messages.add(buildJsonObject {
                put("role", "system")
                put("content", instructions)
            })

            // TODO: Full message processing logic from Rust (reasoning anchoring, deduplication, etc.)
            // For now, basic message conversion from protocol ResponseItem
            for (item in input) {
                when (item) {
                    is io.github.kotlinmania.codex.protocol.ResponseItem.Message -> {
                        val textContent = item.content.filterIsInstance<io.github.kotlinmania.codex.protocol.ContentItem.OutputText>()
                            .joinToString("") { it.text }

                        messages.add(buildJsonObject {
                            put("role", item.role)
                            put("content", textContent)
                        })
                    }
                    else -> {
                        // TODO: Handle other ResponseItem types (Reasoning, FunctionCall, etc.)
                    }
                }
            }

            val payload = buildJsonObject {
                put("model", model)
                put("messages", JsonArray(messages))
                put("stream", true)
                put("tools", JsonArray(tools))
            }

            val configureHeaders: HttpRequestBuilder.() -> Unit = {
                buildConversationHeaders(conversationId, this)
                subagentHeader(sessionSource)?.let { subagent ->
                    insertHeader(this, "x-openai-subagent", subagent)
                }
            }

            Result.success(ChatRequest(payload, configureHeaders))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

