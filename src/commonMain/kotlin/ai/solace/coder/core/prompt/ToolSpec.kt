package ai.solace.coder.core.prompt

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * When serialized as JSON, this produces a valid "Tool" in the OpenAI
 * Responses API.
 *
 * Ported from codex-rs/core/src/client_common.rs tools::ToolSpec
 */
@Serializable
sealed class ToolSpec {
    abstract val name: String

    @Serializable
    @SerialName("function")
    data class Function(
        val function: ResponsesApiTool
    ) : ToolSpec() {
        override val name: String get() = function.name
    }

    @Serializable
    @SerialName("local_shell")
    object LocalShell : ToolSpec() {
        override val name: String get() = "local_shell"
    }

    @Serializable
    @SerialName("web_search")
    object WebSearch : ToolSpec() {
        override val name: String get() = "web_search"
    }

    @Serializable
    @SerialName("custom")
    data class Freeform(
        val custom: FreeformTool
    ) : ToolSpec() {
        override val name: String get() = custom.name
    }
}

/**
 * Ported from codex-rs/core/src/client_common.rs tools::ResponsesApiTool
 */
@Serializable
data class ResponsesApiTool(
    val name: String,
    val description: String,
    val parameters: JsonSchema,
    val strict: Boolean
)

/**
 * Ported from codex-rs/core/src/client_common.rs tools::FreeformTool
 */
@Serializable
data class FreeformTool(
    val name: String,
    val description: String,
    val format: FreeformToolFormat
)

/**
 * Ported from codex-rs/core/src/client_common.rs tools::FreeformToolFormat
 */
@Serializable
data class FreeformToolFormat(
    val type: String,
    val syntax: String,
    val definition: String
)

/**
 * Generic JSON‑Schema subset needed for our tool definitions.
 *
 * Ported from codex-rs/core/src/tools/spec.rs JsonSchema
 */
@Serializable
sealed class JsonSchema {
    @Serializable
    @SerialName("boolean")
    data class Boolean(
        val description: String? = null
    ) : JsonSchema()

    @Serializable
    @SerialName("string")
    data class String(
        val description: String? = null
    ) : JsonSchema()

    @Serializable
    @SerialName("number")
    data class Number(
        val description: String? = null
    ) : JsonSchema()

    @Serializable
    @SerialName("array")
    data class Array(
        val items: JsonSchema,
        val description: String? = null
    ) : JsonSchema()

    @Serializable
    @SerialName("object")
    data class Object(
        val properties: Map<kotlin.String, JsonSchema>,
        val required: List<kotlin.String>? = null,
        @SerialName("additionalProperties")
        val additionalProperties: JsonElement? = null // Simplified AdditionalProperties for now
    ) : JsonSchema()
}
