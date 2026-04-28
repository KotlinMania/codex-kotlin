// port-lint: source context.rs
package ai.solace.coder.core.tools

import ai.solace.coder.core.session.Session
import ai.solace.coder.core.session.SharedTurnDiffTracker
import ai.solace.coder.core.session.TurnContext
import ai.solace.coder.protocol.CallToolResult
import ai.solace.coder.protocol.FunctionCallOutputContentItem
import ai.solace.coder.protocol.FunctionCallOutputPayload
import ai.solace.coder.protocol.McpResult
import ai.solace.coder.protocol.ResponseInputItem
import ai.solace.coder.protocol.ShellToolCallParams

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
            is Function -> arguments
            is Custom -> input
            is LocalShell -> params.command.joinToString(" ")
            is UnifiedExec -> arguments
            is Mcp -> rawArguments
        }
}

sealed class ToolOutput {
    data class Function(
        val content: String,
        val contentItems: List<FunctionCallOutputContentItem>? = null,
        val success: Boolean? = null,
    ) : ToolOutput()

    data class Mcp(val result: Result<CallToolResult>) : ToolOutput()

    // Kotlin-only helpers used by the exec-style handlers; the Rust source
    // wraps these into ToolOutput.Function before returning.
    data class Exec(val output: ai.solace.coder.core.ExecToolCallOutput) : ToolOutput()
    data class ImageAttachment(val path: String, val message: String) : ToolOutput()

    fun logPreview(): String =
        when (this) {
            is Function -> telemetryPreview(content)
            is Mcp -> result.toString()
            is Exec -> output.toString()
            is ImageAttachment -> message
        }

    fun successForLogging(): Boolean =
        when (this) {
            is Function -> success ?: true
            is Mcp -> result.isSuccess
            is Exec -> output.exitCode == 0
            is ImageAttachment -> true
        }

    fun intoResponse(callId: String, payload: ToolPayload): ResponseInputItem =
        when (this) {
            is Function -> {
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
            is Mcp -> {
                val protoResult: McpResult<CallToolResult, String> = result.fold(
                    onSuccess = { McpResult<CallToolResult, String>(value = it) },
                    onFailure = {
                        McpResult<CallToolResult, String>(
                            error = it.message ?: "Unknown error",
                        )
                    },
                )
                ResponseInputItem.McpToolCallOutput(
                    callId = callId,
                    result = protoResult,
                )
            }
            is Exec -> ResponseInputItem.FunctionCallOutput(
                callId = callId,
                output = FunctionCallOutputPayload(
                    content = output.aggregatedOutput.text ?: output.stdout.text ?: "",
                    success = output.exitCode == 0,
                ),
            )
            is ImageAttachment -> ResponseInputItem.FunctionCallOutput(
                callId = callId,
                output = FunctionCallOutputPayload(
                    content = message,
                    success = true,
                ),
            )
        }
}

internal fun telemetryPreview(content: String): String {
    val truncatedSlice = takeBytesAtCharBoundary(content, TELEMETRY_PREVIEW_MAX_BYTES)
    val truncatedByBytes = truncatedSlice.length < content.length

    val preview = StringBuilder()
    val linesIter = truncatedSlice.split('\n').iterator()
    var truncatedByLines = false
    var idx = 0
    while (idx < TELEMETRY_PREVIEW_MAX_LINES) {
        if (!linesIter.hasNext()) {
            break
        }
        val line = linesIter.next()
        if (idx > 0) {
            preview.append('\n')
        }
        preview.append(line)
        idx += 1
    }
    if (linesIter.hasNext()) {
        truncatedByLines = true
    }

    if (!truncatedByBytes && !truncatedByLines) {
        return content
    }

    if (preview.length < truncatedSlice.length &&
        truncatedSlice.encodeToByteArray().getOrNull(preview.encodeToByteArray().size) == '\n'.code.toByte()
    ) {
        preview.append('\n')
    }

    if (preview.isNotEmpty() && preview[preview.length - 1] != '\n') {
        preview.append('\n')
    }
    preview.append(TELEMETRY_PREVIEW_TRUNCATION_NOTICE)

    return preview.toString()
}

internal fun takeBytesAtCharBoundary(content: String, maxBytes: Int): String {
    val bytes = content.encodeToByteArray()
    if (bytes.size <= maxBytes) return content
    var end = maxBytes
    while (end > 0 && (bytes[end].toInt() and 0xC0) == 0x80) {
        end -= 1
    }
    return bytes.copyOfRange(0, end).decodeToString()
}
