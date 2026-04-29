// port-lint: source core/src/tools/context.rs
package ai.solace.coder.core.tools

import ai.solace.coder.core.session.Session
import ai.solace.coder.core.session.TurnContext
import ai.solace.coder.protocol.CallToolResult
import ai.solace.coder.protocol.FunctionCallOutputContentItem
import ai.solace.coder.protocol.FunctionCallOutputPayload
import ai.solace.coder.protocol.McpResult
import ai.solace.coder.protocol.ResponseInputItem
import ai.solace.coder.protocol.ShellToolCallParams
import ai.solace.coder.utils.string.takeBytesAtCharBoundary

data class ToolInvocation(
    val session: Session,
    val turn: TurnContext,
    val tracker: SharedTurnDiffTracker,
    val callId: String,
    val toolName: String,
    val payload: ToolPayload,
)

sealed class ToolPayload {
    data class Function(val arguments: String) : ToolPayload()
    data class Custom(val input: String) : ToolPayload()
    data class LocalShell(val params: ShellToolCallParams) : ToolPayload()
    data class UnifiedExec(val arguments: String) : ToolPayload()
    data class Mcp(
        val server: String,
        val tool: String,
        val rawArguments: String,
    ) : ToolPayload()

    fun logPayload(): String =
        when (this) {
            is ToolPayload.Function -> arguments
            is ToolPayload.Custom -> input
            is ToolPayload.LocalShell -> params.command.joinToString(" ")
            is ToolPayload.UnifiedExec -> arguments
            is ToolPayload.Mcp -> rawArguments
        }
}

sealed class ToolOutput {
    data class Function(
        val content: String,
        val contentItems: List<FunctionCallOutputContentItem>? = null,
        val success: Boolean? = null,
    ) : ToolOutput()

    data class Mcp(val result: McpResult<CallToolResult, String>) : ToolOutput()

    fun logPreview(): String =
        when (this) {
            is ToolOutput.Function -> telemetryPreview(content)
            is ToolOutput.Mcp -> result.toString()
        }

    fun successForLogging(): Boolean =
        when (this) {
            is ToolOutput.Function -> success ?: true
            is ToolOutput.Mcp -> result.isSuccess
        }

    fun intoResponse(callId: String, payload: ToolPayload): ResponseInputItem =
        when (this) {
            is ToolOutput.Function -> {
                if (payload is ToolPayload.Custom) {
                    ResponseInputItem.CustomToolCallOutput(
                        callId = callId,
                        output = content,
                    )
                } else {
                    ResponseInputItem.FunctionCallOutput(
                        callId = callId,
                        output = FunctionCallOutputPayload(
                            content = content,
                            contentItems = contentItems,
                            success = success,
                        ),
                    )
                }
            }
            is ToolOutput.Mcp -> {
                ResponseInputItem.McpToolCallOutput(
                    callId = callId,
                    result = result,
                )
            }
        }
}

internal fun telemetryPreview(content: String): String {
    val truncatedSlice = takeBytesAtCharBoundary(content, TELEMETRY_PREVIEW_MAX_BYTES)
    val truncatedByBytes = truncatedSlice.length < content.length

    val preview = StringBuilder()
    val linesIter = truncatedSlice.lineSequence().iterator()
    for (idx in 0 until TELEMETRY_PREVIEW_MAX_LINES) {
        when {
            linesIter.hasNext() -> {
                val line = linesIter.next()
                if (idx > 0) {
                    preview.append('\n')
                }
                preview.append(line)
            }
            else -> break
        }
    }
    val truncatedByLines = linesIter.hasNext()

    if (!truncatedByBytes && !truncatedByLines) {
        return content
    }

    if (preview.length < truncatedSlice.length &&
        truncatedSlice.encodeToByteArray()
            .getOrNull(preview.toString().encodeToByteArray().size) == '\n'.code.toByte()
    ) {
        preview.append('\n')
    }

    if (preview.isNotEmpty() && preview[preview.length - 1] != '\n') {
        preview.append('\n')
    }
    preview.append(TELEMETRY_PREVIEW_TRUNCATION_NOTICE)

    return preview.toString()
}

