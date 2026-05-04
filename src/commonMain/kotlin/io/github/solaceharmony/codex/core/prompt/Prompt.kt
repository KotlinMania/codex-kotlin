// port-lint: source client_common.rs
package io.github.solaceharmony.codex.core.prompt

import io.github.solaceharmony.codex.core.model.ModelFamily
import io.github.solaceharmony.codex.core.session.ToolSpec
import io.github.solaceharmony.codex.protocol.ResponseItem
import kotlinx.serialization.json.JsonElement

/**
 * API request payload for a single model turn.
 *
 * Ported from Rust codex-rs/core/src/clientCommon.rs `Prompt`.
 */
data class Prompt(
    /** Conversation context input items. */
    val input: List<ResponseItem> = emptyList(),

    /**
     * Tools available to the model, including additional tools sourced from
     * external MCP servers.
     */
    val tools: List<ToolSpec> = emptyList(),

    /** Whether parallel tool calls are permitted for this prompt. */
    val parallelToolCalls: Boolean = false,

    /** Optional override for the built-in BASE_INSTRUCTIONS. */
    val baseInstructionsOverride: String? = null,

    /** Optional output schema for the model response. */
    val outputSchema: JsonElement? = null
) {
    fun getFullInstructions(modelFamily: ModelFamily): String {
        val base = baseInstructionsOverride ?: modelFamily.baseInstructions
        return base
    }

    fun getFormattedInput(): List<ResponseItem> {
        return input
    }
}
