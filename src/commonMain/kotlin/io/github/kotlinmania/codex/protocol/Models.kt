// port-lint: source models.rs
@file:OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)

package io.github.kotlinmania.codex.protocol

import io.github.kotlinmania.codex.utils.git.GhostCommit
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray
import kotlinx.io.buffered
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * Represents input items that can be sent to the Responses API. Tagged union with discriminator
 * field "type".
 */
@Serializable
sealed class ResponseInputItem {
        @Serializable
        @kotlinx.serialization.SerialName("message")
        data class Message(val role: String, val content: List<ContentItem>) : ResponseInputItem()

        @Serializable
        @kotlinx.serialization.SerialName("function_call_output")
        data class FunctionCallOutput(
                @kotlinx.serialization.SerialName("call_id") val callId: String,
                val output: FunctionCallOutputPayload
        ) : ResponseInputItem()

        @Serializable
        @kotlinx.serialization.SerialName("mcp_tool_call_output")
        data class McpToolCallOutput(
                @kotlinx.serialization.SerialName("call_id") val callId: String,
                val result: McpResult
        ) : ResponseInputItem()

        @Serializable
        @kotlinx.serialization.SerialName("custom_tool_call_output")
        data class CustomToolCallOutput(
                @kotlinx.serialization.SerialName("call_id") val callId: String,
                val output: String
        ) : ResponseInputItem()

        companion object {
                fun from(items: List<UserInput>): ResponseInputItem {
                        return Message(
                                role = "user",
                                content =
                                        items.map { item ->
                                                when (item) {
                                                        is UserInput.Text ->
                                                                ContentItem.InputText(text = item.text)
                                                        is UserInput.Image ->
                                                                ContentItem.InputImage(imageUrl = item.imageUrl)
                                                        is UserInput.LocalImage -> {
                                                                val path = item.path
                                                                val bytes =
                                                                        try {
                                                                            SystemFileSystem.source(Path(path)).buffered().use {
                                                                                it.readByteArray()
                                                                            }
                                                                        } catch (e: Exception) {
                                                                                return@map localImageErrorPlaceholder(
                                                                                        path = path,
                                                                                        error = e.toString(),
                                                                                )
                                                                        }

                                                                val mimeGuess =
                                                                        guessMimeType(path)
                                                                                ?: return@map localImageErrorPlaceholder(
                                                                                        path = path,
                                                                                        error = "unsupported MIME type (unknown)",
                                                                                )
                                                                if (!mimeGuess.startsWith("image/")) {
                                                                        return@map localImageErrorPlaceholder(
                                                                                path = path,
                                                                                error = "unsupported MIME type `$mimeGuess`",
                                                                        )
                                                                }

                                                                @OptIn(ExperimentalEncodingApi::class)
                                                                val encoded = Base64.encode(bytes)
                                                                ContentItem.InputImage(
                                                                        imageUrl = "data:$mimeGuess;base64,$encoded",
                                                                )
                                                        }
                                                }
                                        },
                        )
                }
        }
}

/** Content items that can appear in messages. Tagged union with discriminator field "type". */
@Serializable
sealed class ContentItem {
        @Serializable
        @kotlinx.serialization.SerialName("input_text")
        data class InputText(val text: String) : ContentItem()

        @Serializable
        @kotlinx.serialization.SerialName("input_image")
        data class InputImage(@kotlinx.serialization.SerialName("image_url") val imageUrl: String) :
                ContentItem()

        @Serializable
        @kotlinx.serialization.SerialName("output_text")
        data class OutputText(val text: String) : ContentItem()
}

/**
 * Response items that can be received from the Responses API. Tagged union with discriminator field
 * "type".
 */
@Serializable
sealed class ResponseItem {
        @Serializable
        @kotlinx.serialization.SerialName("message")
        data class Message(
                val role: String,
                val content: List<ContentItem>,
                val id: String? = null
        ) : ResponseItem()

        @Serializable
        @kotlinx.serialization.SerialName("reasoning")
        data class Reasoning(
                val id: String = "",
                val summary: List<ReasoningItemReasoningSummary>,
                val content: List<ReasoningItemContent>? = null,
                @kotlinx.serialization.SerialName("encrypted_content")
                val encryptedContent: String? = null
        ) : ResponseItem()

        @Serializable
        @kotlinx.serialization.SerialName("local_shell_call")
        data class LocalShellCall(
                val id: String? = null,
                @kotlinx.serialization.SerialName("call_id") val callId: String? = null,
                val status: LocalShellStatus,
                val action: LocalShellAction
        ) : ResponseItem()

        @Serializable
        @kotlinx.serialization.SerialName("function_call")
        data class FunctionCall(
                val id: String? = null,
                val name: String,
                val arguments: String,
                @kotlinx.serialization.SerialName("call_id") val callId: String
        ) : ResponseItem()

        @Serializable
        @kotlinx.serialization.SerialName("function_call_output")
        data class FunctionCallOutput(
                @kotlinx.serialization.SerialName("call_id") val callId: String,
                val output: FunctionCallOutputPayload
        ) : ResponseItem()

        @Serializable
        @kotlinx.serialization.SerialName("custom_tool_call")
        data class CustomToolCall(
                val id: String? = null,
                val status: String? = null,
                @kotlinx.serialization.SerialName("call_id") val callId: String,
                val name: String,
                val input: String
        ) : ResponseItem()

        @Serializable
        @kotlinx.serialization.SerialName("custom_tool_call_output")
        data class CustomToolCallOutput(
                @kotlinx.serialization.SerialName("call_id") val callId: String,
                val output: String
        ) : ResponseItem()

        @Serializable
        @kotlinx.serialization.SerialName("web_search_call")
        data class WebSearchCall(
                val id: String? = null,
                val status: String? = null,
                val action: WebSearchAction
        ) : ResponseItem()

        @Serializable
        @kotlinx.serialization.SerialName("ghost_snapshot")
        data class GhostSnapshot(
                @kotlinx.serialization.SerialName("ghost_commit") val ghostCommit: GhostCommit
        ) : ResponseItem()

        @Serializable
        @kotlinx.serialization.SerialName("compaction_summary")
        data class CompactionSummary(
                @kotlinx.serialization.SerialName("encrypted_content") val encryptedContent: String
        ) : ResponseItem()

        @Serializable @kotlinx.serialization.SerialName("other") object Other : ResponseItem()

        companion object {
                fun from(item: ResponseInputItem): ResponseItem {
                        return when (item) {
                                is ResponseInputItem.Message ->
                                        Message(role = item.role, content = item.content, id = null)
                                is ResponseInputItem.FunctionCallOutput ->
                                        FunctionCallOutput(callId = item.callId, output = item.output)
                                is ResponseInputItem.McpToolCallOutput -> {
                                        val output =
                                                item.result.value?.let { result ->
                                                        FunctionCallOutputPayload.from(result)
                                                } ?: FunctionCallOutputPayload(
                                                        content = "err: ${item.result.error ?: "missing result"}",
                                                        contentItems = null,
                                                        success = false,
                                                )
                                        FunctionCallOutput(callId = item.callId, output = output)
                                }
                                is ResponseInputItem.CustomToolCallOutput ->
                                        CustomToolCallOutput(callId = item.callId, output = item.output)
                        }
                }
        }
}

/** Status of a local shell execution. */
@Serializable
enum class LocalShellStatus {
        @kotlinx.serialization.SerialName("completed") Completed,
        @kotlinx.serialization.SerialName("in_progress") InProgress,
        @kotlinx.serialization.SerialName("incomplete") Incomplete
}

/** Action to perform in a local shell. Tagged union with discriminator field "type". */
@Serializable
sealed class LocalShellAction {
        @Serializable
        @kotlinx.serialization.SerialName("exec")
        data class Exec(
                val command: List<String>,
                @kotlinx.serialization.SerialName("timeout_ms") val timeoutMs: Long? = null,
                @kotlinx.serialization.SerialName("working_directory")
                val workingDirectory: String? = null,
                val env: Map<String, String>? = null,
                val user: String? = null
        ) : LocalShellAction()
}

/** Web search action types. Tagged union with discriminator field "type". */
@Serializable
sealed class WebSearchAction {
        @Serializable
        @kotlinx.serialization.SerialName("search")
        data class Search(val query: String? = null) : WebSearchAction()

        @Serializable
        @kotlinx.serialization.SerialName("open_page")
        data class OpenPage(val url: String? = null) : WebSearchAction()

        @Serializable
        @kotlinx.serialization.SerialName("find_in_page")
        data class FindInPage(val url: String? = null, val pattern: String? = null) :
                WebSearchAction()

        @Serializable @kotlinx.serialization.SerialName("other") object Other : WebSearchAction()
}

/** Reasoning summary item. Tagged union with discriminator field "type". */
@Serializable
sealed class ReasoningItemReasoningSummary {
        @Serializable
        @kotlinx.serialization.SerialName("summary_text")
        data class SummaryText(val text: String) : ReasoningItemReasoningSummary()
}

/** Reasoning content item. Tagged union with discriminator field "type". */
@Serializable
sealed class ReasoningItemContent {
        @Serializable
        @kotlinx.serialization.SerialName("reasoning_text")
        data class ReasoningText(val text: String) : ReasoningItemContent()

        @Serializable
        @kotlinx.serialization.SerialName("text")
        data class Text(val text: String) : ReasoningItemContent()
}

private fun shouldSerializeReasoningContent(content: List<ReasoningItemContent>?): Boolean {
        return when {
                content == null -> false
                else -> !content.any { it is ReasoningItemContent.ReasoningText }
        }
}

private fun localImageErrorPlaceholder(path: String, error: String): ContentItem {
        return ContentItem.InputText(
                text = "Codex could not read the local image at `$path`: $error",
        )
}

private fun invalidImageErrorPlaceholder(path: String, error: String): ContentItem {
        return ContentItem.InputText(
                text = "Image located at `$path` is invalid: $error",
        )
}

private fun guessMimeType(path: String): String? {
        val lower = path.lowercase()
        return when {
                lower.endsWith(".png") -> "image/png"
                lower.endsWith(".jpg") || lower.endsWith(".jpeg") -> "image/jpeg"
                lower.endsWith(".gif") -> "image/gif"
                lower.endsWith(".webp") -> "image/webp"
                lower.endsWith(".bmp") -> "image/bmp"
                lower.endsWith(".tif") || lower.endsWith(".tiff") -> "image/tiff"
                else -> null
        }
}

/**
 * Content items that can be returned by function calls. Tagged union with discriminator field
 * "type".
 */
@Serializable
sealed class FunctionCallOutputContentItem {
        @Serializable
        @kotlinx.serialization.SerialName("input_text")
        data class InputText(val text: String) : FunctionCallOutputContentItem()

        @Serializable
        @kotlinx.serialization.SerialName("input_image")
        data class InputImage(@kotlinx.serialization.SerialName("image_url") val imageUrl: String) :
                FunctionCallOutputContentItem()
}

/**
 * Payload for function call output. Custom serializer handles the dual format: plain string for
 * success, structured for failures.
 *
 * Ported from Rust codex-rs/protocol/src/models.rs FunctionCallOutputPayload
 */
@Serializable(with = FunctionCallOutputPayloadSerializer::class)
data class FunctionCallOutputPayload(
        val content: String,
        @kotlinx.serialization.SerialName("content_items")
        val contentItems: List<FunctionCallOutputContentItem>? = null,
        val success: Boolean? = null
) {
        companion object {
                private val json = Json { ignoreUnknownKeys = true }

                /**
                 * Create a FunctionCallOutputPayload from a CallToolResult.
                 *
                 * Ported from Rust codex-rs/protocol/src/models.rs implementation From<&CallToolResult> for
                 * FunctionCallOutputPayload
                 */
                fun from(callToolResult: CallToolResult): FunctionCallOutputPayload {
                        val isSuccess = callToolResult.isError != true

                        // If structuredContent is present and not null, serialize and return it
                        val structuredContent = callToolResult.structuredContent
                        if (structuredContent != null && structuredContent != JsonNull) {
                                return try {
                                        val serializedStructuredContent =
                                                json.encodeToString(
                                                        JsonElement.serializer(),
                                                        structuredContent
                                                )
                                        FunctionCallOutputPayload(
                                                content = serializedStructuredContent,
                                                success = isSuccess,
                                                contentItems = null
                                        )
                                } catch (e: Exception) {
                                        FunctionCallOutputPayload(
                                                content = e.message ?: "Serialization error",
                                                success = false,
                                                contentItems = null
                                        )
                                }
                        }

                        val content = callToolResult.content
                        val convertedItems = convertContentBlocksToItems(content)
                        if (convertedItems != null) {
                                return FunctionCallOutputPayload(
                                        content = "",
                                        contentItems = convertedItems,
                                        success = isSuccess
                                )
                        }

                        // Serialize content blocks
                        val serializedContent =
                                try {
                                        json.encodeToString(
                                                kotlinx.serialization.builtins.ListSerializer(
                                                        ContentBlock.serializer()
                                                ),
                                                content
                                        )
                                } catch (e: Exception) {
                                        return FunctionCallOutputPayload(
                                                content = e.message ?: "Serialization error",
                                                success = false,
                                                contentItems = null
                                        )
                                }

                        return FunctionCallOutputPayload(
                                content = serializedContent,
                                contentItems = convertedItems,
                                success = isSuccess
                        )
                }

                /** Convert MCP ContentBlocks to FunctionCallOutputContentItems. */
                private fun convertContentBlocksToItems(
                        blocks: List<ContentBlock>
                ): List<FunctionCallOutputContentItem>? {
                        var sawImage = false
                        val items = mutableListOf<FunctionCallOutputContentItem>()

                        for (block in blocks) {
                                when (block) {
                                        is ContentBlock.TextContent -> {
                                                items.add(
                                                        FunctionCallOutputContentItem.InputText(
                                                                text = block.text
                                                        )
                                                )
                                        }
                                        is ContentBlock.ImageContent -> {
                                                sawImage = true
                                                // Ensure data URL format
                                                val imageUrl =
                                                        if (block.data.startsWith("data:")) {
                                                                block.data
                                                        } else {
                                                                "data:${block.mimeType};base64,${block.data}"
                                                        }
                                                items.add(
                                                        FunctionCallOutputContentItem.InputImage(
                                                                imageUrl = imageUrl
                                                        )
                                                )
                                        }
                                }
                        }

                        // Only return contentItems if we saw at least one image
                        return if (sawImage) items else null
                }
        }

        fun fmt(): String = content

        fun deref(): String = content
}

/**
 * Custom serializer for FunctionCallOutputPayload. Serializes as plain string when contentItems is
 * null, otherwise as array. Deserializes from either plain string or array of content items.
 */
object FunctionCallOutputPayloadSerializer : KSerializer<FunctionCallOutputPayload> {
        override val descriptor: SerialDescriptor =
                PrimitiveSerialDescriptor("FunctionCallOutputPayload", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: FunctionCallOutputPayload) {
                val items = value.contentItems
                if (items != null) {
                        encoder.encodeSerializableValue(
                                kotlinx.serialization.builtins.ListSerializer(
                                        FunctionCallOutputContentItem.serializer()
                                ),
                                items
                        )
                        return
                }
                encoder.encodeString(value.content)
        }

        override fun deserialize(decoder: Decoder): FunctionCallOutputPayload {
                val jsonDecoder =
                        decoder as? JsonDecoder
                                ?: throw SerializationException(
                                        "FunctionCallOutputPayload deserialization requires JsonDecoder"
                                )

                return when (val element = jsonDecoder.decodeJsonElement()) {
                        is JsonPrimitive -> {
                                FunctionCallOutputPayload(
                                        content = element.content,
                                        contentItems = null,
                                        success = null
                                )
                        }
                        is JsonArray -> {
                                val items =
                                        jsonDecoder.json.decodeFromJsonElement(
                                                kotlinx.serialization.builtins.ListSerializer(
                                                        FunctionCallOutputContentItem.serializer()
                                                ),
                                                element
                                        )
                                val content = jsonDecoder.json.encodeToString(
                                        kotlinx.serialization.builtins.ListSerializer(
                                                FunctionCallOutputContentItem.serializer()
                                        ),
                                        items
                                )
                                FunctionCallOutputPayload(
                                        content = content,
                                        contentItems = items,
                                        success = null
                                )
                        }
                        else ->
                                throw SerializationException(
                                        "FunctionCallOutputPayload must be string or array"
                                )
                }
        }
}

/** Parameters for shell tool calls (container.exec or shell). */
@Serializable
data class ShellToolCallParams(
        val command: List<String>,
        val workdir: String? = null,
        @kotlinx.serialization.SerialName("timeout_ms")
        @kotlinx.serialization.json.JsonNames("timeout")
        val timeoutMs: Long? = null,
        @kotlinx.serialization.SerialName("with_escalated_permissions")
        val withEscalatedPermissions: Boolean? = null,
        val justification: String? = null
)

/** Parameters for shellCommand tool calls. */
@Serializable
data class ShellCommandToolCallParams(
        val command: String,
        val workdir: String? = null,
        @kotlinx.serialization.SerialName("timeout_ms")
        @kotlinx.serialization.json.JsonNames("timeout")
        val timeoutMs: Long? = null,
        @kotlinx.serialization.SerialName("with_escalated_permissions")
        val withEscalatedPermissions: Boolean? = null,
        val justification: String? = null
)

// GhostCommit is imported from io.github.kotlinmania.codex.utils.git.GhostCommit

/** Result type for MCP tool call outputs. Simplified version - adjust based on actual MCP types. */
@Serializable
data class CallToolResult(
        val content: List<ContentBlock>,
        @kotlinx.serialization.SerialName("structured_content")
        val structuredContent: JsonElement? = null,
        @kotlinx.serialization.SerialName("is_error") val isError: Boolean? = null
)

/** Content block for MCP tool results. */
@Serializable
sealed class ContentBlock {
        @Serializable
        @kotlinx.serialization.SerialName("text")
        data class TextContent(
                val text: String,
                val annotations: JsonElement? = null,
                @kotlinx.serialization.SerialName("type") val typeField: String = "text"
        ) : ContentBlock()

        @Serializable
        @kotlinx.serialization.SerialName("image")
        data class ImageContent(
                val data: String,
                @kotlinx.serialization.SerialName("mime_type") val mimeType: String,
                val annotations: JsonElement? = null,
                @kotlinx.serialization.SerialName("type") val typeField: String = "image"
        ) : ContentBlock()
}

/** Kotlin Result wrapper for serialization. */

/**
 * Response event from the SSE stream.
 *
 * Ported from Rust codex-rs/codex-api/src/common.rs ResponseEvent
 */
sealed class ResponseEvent {
        /** Stream created */
        object Created : ResponseEvent()

        /** Output item added (streaming started for this item) */
        data class OutputItemAdded(val item: ResponseItem) : ResponseEvent()

        /** Output item completed */
        data class OutputItemDone(val item: ResponseItem) : ResponseEvent()

        /** Text content delta (streaming text) */
        data class OutputTextDelta(val delta: String) : ResponseEvent()

        /** Reasoning summary delta */
        data class ReasoningSummaryDelta(val delta: String, val summaryIndex: Long) :
                ResponseEvent()

        /** Reasoning summary part added */
        data class ReasoningSummaryPartAdded(val summaryIndex: Long) : ResponseEvent()

        /** Reasoning content delta */
        data class ReasoningContentDelta(val delta: String, val contentIndex: Long) :
                ResponseEvent()

        /** Rate limit information */
        data class RateLimits(val snapshot: RateLimitSnapshot) : ResponseEvent()

        /** Response completed */
        data class Completed(val responseId: String?, val tokenUsage: TokenUsage?) :
                ResponseEvent()
}

@Serializable data class ReadResourceRequestParams(val uri: String)

@Serializable
data class ResourceContent(
        val uri: String,
        @kotlinx.serialization.SerialName("mime_type") val mimeType: String? = null,
        val text: String? = null,
        val blob: String? = null
)

@Serializable
data class ReadResourceResult(
        val contents: List<ResourceContent>,
        @kotlinx.serialization.SerialName("mime_type") val mimeType: String? = null
)

@Serializable
data class ListResourcesResult(
        val resources: List<McpResource>,
        @kotlinx.serialization.SerialName("nextCursor") val nextCursor: String? = null
)

@Serializable
data class ListResourceTemplatesResult(
        @kotlinx.serialization.SerialName("resourceTemplates")
        val resourceTemplates: List<McpResourceTemplate>,
        @kotlinx.serialization.SerialName("nextCursor") val nextCursor: String? = null
)

@Serializable data class ListResourcesRequestParams(val cursor: String? = null)

@Serializable data class ListResourceTemplatesRequestParams(val cursor: String? = null)
