// port-lint: source core/src/tools/context.rs
package ai.solace.coder.core.tools

import ai.solace.coder.core.session.Session
import ai.solace.coder.core.session.TurnContext
import ai.solace.coder.core.session.TurnDiffTracker
import ai.solace.coder.protocol.CallToolResult
import ai.solace.coder.protocol.FunctionCallOutputContentItem
import ai.solace.coder.protocol.FunctionCallOutputPayload
import ai.solace.coder.protocol.ResponseInputItem
import ai.solace.coder.protocol.ShellToolCallParams
import kotlinx.coroutines.sync.Mutex

typealias SharedTurnDiffTracker = Mutex // In Kotlin we often use Mutex directly or a wrapper

data class ToolInvocation(
    val session: Session,
    val turn: TurnContext,
    val tracker: TurnDiffTracker, // Shared via some mechanism, placeholder
    val callId: String,
    val toolName: String,
    val payload: ToolPayload
)

sealed class ToolPayload {
    data class Function(val arguments: String) : ToolPayload()
    data class Custom(val input: String) : ToolPayload()
    data class LocalShell(val params: ShellToolCallParams) : ToolPayload()
    data class UnifiedExec(val arguments: String) : ToolPayload()
    data class Mcp(
        val server: String,
        val tool: String,
        val rawArguments: String
    ) : ToolPayload()

    fun logPayload(): String {
        return when (this) {
            is Function -> arguments
            is Custom -> input
            is LocalShell -> params.command.joinToString(" ")
            is UnifiedExec -> arguments
            is Mcp -> rawArguments
        }
    }
}

sealed class ToolOutput {
    data class Function(
        val content: String,
        val contentItems: List<FunctionCallOutputContentItem>? = null,
        val success: Boolean? = null
    ) : ToolOutput()

    data class Mcp(
        val result: Result<CallToolResult>
    ) : ToolOutput()

    fun logPreview(): String {
        return when (this) {
            is Function -> telemetryPreview(content)
            is Mcp -> result.toString()
        }
    }

    fun successForLogging(): Boolean {
        return when (this) {
            is Function -> success ?: true
            is Mcp -> result.isSuccess
        }
    }

    fun intoResponse(callId: String, payload: ToolPayload): ResponseInputItem {
        return when (this) {
            is Function -> {
                if (payload is ToolPayload.Custom) {
                    ResponseInputItem.CustomToolCallOutput(
                        callId = callId,
                        output = content
                    )
                } else {
                    ResponseInputItem.FunctionCallOutput(
                        callId = callId,
                        output = FunctionCallOutputPayload(
                            content = content,
                            contentItems = contentItems,
                            success = success
                        )
                    )
                }
            }
            is Mcp -> {
                // In Rust this was Result<CallToolResult, String>, 
                // Kotlin's Result<T> wraps Throwable.
                val mappedResult = result.fold(
                    onSuccess = { it },
                    onFailure = { null } // Logic needs to match protocol exactly
                )
                ResponseInputItem.McpToolCallOutput(
                    callId = callId,
                    // Protocol might expect a specific error representation
                    result = ai.solace.coder.protocol.McpResult(value = mappedResult)
                )
            }
        }
    }
}

private const val TELEMETRY_PREVIEW_MAX_BYTES = 1024
private const val TELEMETRY_PREVIEW_MAX_LINES = 20
private const val TELEMETRY_PREVIEW_TRUNCATION_NOTICE = "... (output truncated)"

private fun telemetryPreview(content: String): String {
    // Basic implementation, needs char boundary handling like Rust
    val truncated = if (content.length > TELEMETRY_PREVIEW_MAX_BYTES) {
        content.substring(0, TELEMETRY_PREVIEW_MAX_BYTES)
    } else {
        content
    }
    
    val lines = truncated.lines()
    if (lines.size > TELEMETRY_PREVIEW_MAX_LINES) {
        return lines.take(TELEMETRY_PREVIEW_MAX_LINES).joinToString("\n") + "\n" + TELEMETRY_PREVIEW_TRUNCATION_NOTICE
    }
    
    if (content.length > TELEMETRY_PREVIEW_MAX_BYTES) {
        return truncated + "\n" + TELEMETRY_PREVIEW_TRUNCATION_NOTICE
    }
    
    return content
}
