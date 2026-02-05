// port-lint: source core/src/client_common.rs
package ai.solace.coder.core.prompt

import ai.solace.coder.core.model.ModelFamily
import ai.solace.coder.protocol.ResponseItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * Prompt structure for model interactions.
 *
 * Ported from Rust core/src/client_common.rs Prompt struct.
 */
@Serializable
data class Prompt(
    /** Conversation context input items. */
    val input: List<ResponseItem>,
    /** Tools available to the model. */
    val tools: List<ToolSpec>,
    /** Whether parallel tool calls are permitted for this prompt. */
    val parallelToolCalls: Boolean,
    /** Optional override for the built-in BASE_INSTRUCTIONS. */
    val baseInstructionsOverride: String? = null,
    /** Optional output schema for the model's response. */
    val outputSchema: JsonElement? = null
) {
    companion object {
        const val APPLY_PATCH_TOOL_INSTRUCTIONS = """
# apply_patch tool

You MUST use the `apply_patch` tool to modify files.

The `apply_patch` tool takes a patch in a specific format and applies it to a file.
The patch format is a simplified version of the unified diff format.

Example:
```patch
--- path/to/file
+++ path/to/file
@@ -1,3 +1,3 @@
-old line
+new line
```
"""
    }

    /**
     * Gets the full instructions for a model, including base instructions
     * and any overrides.
     */
    fun getFullInstructions(modelFamily: ModelFamily): String {
        val base = baseInstructionsOverride ?: modelFamily.baseInstructions

        // When there are no custom instructions, add apply_patch_tool_instructions if:
        // - the model needs special instructions (4.1)
        // AND
        // - there is no apply_patch tool present
        val isApplyPatchToolPresent = tools.any { tool ->
            when (tool) {
                is ToolSpec.Function -> tool.function.name == "apply_patch"
                is ToolSpec.Freeform -> tool.custom.name == "apply_patch"
                else -> tool.name == "apply_patch"
            }
        }

        return if (baseInstructionsOverride == null &&
            modelFamily.needsSpecialApplyPatchInstructions &&
            !isApplyPatchToolPresent
        ) {
            "$base\n${Prompt.APPLY_PATCH_TOOL_INSTRUCTIONS}"
        } else {
            base
        }
    }

    /**
     * Gets the formatted input for API requests.
     */
    fun getFormattedInput(): List<ResponseItem> {
        val inputList = input.toMutableList()

        // when using the *Freeform* apply_patch tool specifically, tool outputs
        // should be structured text, not json. Do NOT reserialize when using
        // the Function tool - note that this differs from the check above for
        // instructions. We declare the result as a named variable for clarity.
        val isFreeformApplyPatchToolPresent = tools.any { tool ->
            when (tool) {
                is ToolSpec.Freeform -> tool.custom.name == "apply_patch"
                else -> false
            }
        }

        if (isFreeformApplyPatchToolPresent) {
            reserializeShellOutputs(inputList)
        }

        return inputList
    }

    private fun reserializeShellOutputs(items: MutableList<ResponseItem>) {
        val shellCallIds = mutableSetOf<String>()

        items.forEachIndexed { index, item ->
            when (item) {
                is ResponseItem.LocalShellCall -> {
                    val identifier = item.callId ?: item.id
                    if (identifier != null) {
                        shellCallIds.add(identifier)
                    }
                }
                is ResponseItem.CustomToolCall -> {
                    if (item.name == "apply_patch") {
                        shellCallIds.add(item.callId)
                    }
                }
                is ResponseItem.CustomToolCallOutput -> {
                    if (shellCallIds.remove(item.callId)) {
                        // Reserialize output if it was for a shell call
                        val structured = parseStructuredShellOutput(item.output)
                        if (structured != null) {
                            items[index] = ResponseItem.CustomToolCallOutput(
                                callId = item.callId,
                                output = structured
                            )
                        }
                    }
                }
                is ResponseItem.FunctionCall -> {
                    if (isShellToolName(item.name) || item.name == "apply_patch") {
                        shellCallIds.add(item.callId)
                    }
                }
                is ResponseItem.FunctionCallOutput -> {
                    if (shellCallIds.remove(item.callId)) {
                        val structured = parseStructuredShellOutput(item.output.content)
                        if (structured != null) {
                            items[index] = ResponseItem.FunctionCallOutput(
                                callId = item.callId,
                                output = item.output.copy(content = structured)
                            )
                        }
                    }
                }
                else -> {}
            }
        }
    }

    private fun isShellToolName(name: String): Boolean =
        name == "shell" || name == "container.exec"

    @Serializable
    private data class ExecOutputMetadataJson(
        val exit_code: Int,
        val duration_seconds: Float
    )

    @Serializable
    private data class ExecOutputJson(
        val output: String,
        val metadata: ExecOutputMetadataJson
    )

    private fun parseStructuredShellOutput(raw: String): String? {
        return try {
            val json = Json { ignoreUnknownKeys = true }
            val parsed = json.decodeFromString<ExecOutputJson>(raw)
            buildStructuredOutput(parsed)
        } catch (e: Exception) {
            null
        }
    }

    private fun buildStructuredOutput(parsed: ExecOutputJson): String {
        val sections = mutableListOf<String>()
        sections.add("Exit code: ${parsed.metadata.exit_code}")
        sections.add("Wall time: ${parsed.metadata.duration_seconds} seconds")

        var output = parsed.output
        val stripped = stripTotalOutputHeader(parsed.output)
        if (stripped != null) {
            sections.add("Total output lines: ${stripped.totalLines}")
            output = stripped.remainder
        }

        sections.add("Output:")
        sections.add(output)

        return sections.joinToString("\n")
    }

    private data class StrippedOutput(val remainder: String, val totalLines: Int)

    private fun stripTotalOutputHeader(output: String): StrippedOutput? {
        val prefix = "Total output lines: "
        if (!output.startsWith(prefix)) return null
        val afterPrefix = output.substring(prefix.length)
        val newlineIndex = afterPrefix.indexOf('\n')
        if (newlineIndex == -1) return null
        val totalSegment = afterPrefix.substring(0, newlineIndex)
        val totalLines = totalSegment.trim().toIntOrNull() ?: return null
        var remainder = afterPrefix.substring(newlineIndex + 1)
        if (remainder.startsWith('\n')) {
            remainder = remainder.substring(1)
        }
        return StrippedOutput(remainder, totalLines)
    }
}
