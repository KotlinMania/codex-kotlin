// port-lint: source codex-rs/core/src/client_common.rs
package ai.solace.coder.core.prompt

import ai.solace.coder.core.model.ModelFamily
import ai.solace.coder.protocol.ResponseItem
import kotlinx.serialization.json.JsonElement

/**
 * Prompt structure for model interactions.
 *
 * Ported from Rust core/src/client_common.rs Prompt struct.
 */
data class Prompt(
    /** Conversation context input items. */
    val input: List<ResponseItem>,
    /** Tools available to the model. */
    val tools: List<Any>,
    /** Whether parallel tool calls are permitted for this prompt. */
    val parallelToolCalls: Boolean,
    /** Optional override for the built-in BASE_INSTRUCTIONS. */
    val baseInstructionsOverride: String? = null,
    /** Optional output schema for the model's response. */
    val outputSchema: JsonElement? = null
) {
    /**
     * Gets the full instructions for a model, including base instructions
     * and any overrides.
     */
    fun getFullInstructions(modelFamily: ModelFamily): String {
        // TODO: Implement proper instruction building from base_instructions.rs
        return baseInstructionsOverride ?: "Instructions placeholder"
    }

    /**
     * Gets the formatted input for API requests.
     * Returns the input items directly (they are already ResponseItem).
     */
    fun getFormattedInput(): List<ResponseItem> {
        return input
    }
}
