// port-lint: source common.rs
package io.github.kotlinmania.codex.api.common

import io.github.kotlinmania.codex.protocol.ReasoningEffort
import io.github.kotlinmania.codex.protocol.ReasoningSummary
import io.github.kotlinmania.codex.protocol.ResponseEvent
import io.github.kotlinmania.codex.protocol.ResponseItem
import io.github.kotlinmania.codex.protocol.Verbosity
import kotlinx.serialization.json.JsonElement

/**
 * Canonical prompt input for Chat and Responses endpoints.
 */
internal data class Prompt(
    val instructions: String,
    val input: List<ResponseItem>,
    val tools: List<JsonElement>,
    val parallelToolCalls: Boolean,
    val outputSchema: JsonElement?,
)

/** Canonical input payload for the compaction endpoint. */
internal data class CompactionInput(
    val model: String,
    val input: List<ResponseItem>,
    val instructions: String,
)

// ResponseEvent is imported from io.github.kotlinmania.codex.protocol.ResponseEvent
// See protocol/Models.kt for the full definition

/** Reasoning config payload. */
internal data class Reasoning(
    val effort: ReasoningEffort?,
    val summary: ReasoningSummary?,
)

/** Text formatting types used by OpenAI text controls. */
internal enum class TextFormatType { JsonSchema, }

/** Controls JSON formatted output. */
internal data class TextFormat(
    val type: TextFormatType,
    val strict: Boolean,
    val schema: JsonElement,
    val name: String,
)

/** Controls the text field for Responses API. */
internal data class TextControls(
    val verbosity: OpenAiVerbosity?,
    val format: TextFormat?,
)

/** Verbosity mapping for OpenAI. */
internal enum class OpenAiVerbosity { Low, Medium, High }

internal fun openAiVerbosityConfig(v: Verbosity): OpenAiVerbosity =
    when (v) {
        Verbosity.Low -> OpenAiVerbosity.Low
        Verbosity.Medium -> OpenAiVerbosity.Medium
        Verbosity.High -> OpenAiVerbosity.High
    }

/** Responses API request payload. */
internal data class ResponsesApiRequest(
    val model: String,
    val instructions: String,
    val input: List<ResponseItem>,
    val tools: List<JsonElement>,
    val toolChoice: String,
    val parallelToolCalls: Boolean,
    val reasoning: Reasoning?,
    val store: Boolean,
    val stream: Boolean,
    val include: List<String>,
    val promptCacheKey: String?,
    val text: TextControls?,
)

/** Create text param controls from verbosity and optional output schema. */
internal fun createTextParamForRequest(
    verbosity: Verbosity?,
    outputSchema: JsonElement?,
): TextControls? {
    if (verbosity == null && outputSchema == null) return null
    val format =
        outputSchema?.let { schema ->
            TextFormat(
                type = TextFormatType.JsonSchema,
                strict = true,
                schema = schema,
                name = "codex_output_schema",
            )
        }
    return TextControls(
        verbosity = verbosity?.let { openAiVerbosityConfig(it) },
        format = format,
    )
}

/**
 * Stream of response events.
 * Uses io.github.kotlinmania.codex.protocol.ResponseEvent.
 */
internal interface ResponseStream {
    /**
     * Receive the next event, or null if stream ended.
     * Uses the ResponseEvent from protocol package.
     */
    suspend fun next(): Result<ResponseEvent>?
}
