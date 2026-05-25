// port-lint: source core/src/tools/handlers/mcp.rs
package io.github.kotlinmania.codex.core.tools.handlers

import io.github.kotlinmania.codex.core.FunctionCallError
import io.github.kotlinmania.codex.core.tools.ToolHandler
import io.github.kotlinmania.codex.core.tools.ToolInvocation
import io.github.kotlinmania.codex.core.tools.ToolKind
import io.github.kotlinmania.codex.core.tools.ToolOutput
import io.github.kotlinmania.codex.core.tools.ToolPayload
import io.github.kotlinmania.codex.protocol.McpResult
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * Handler for MCP tool calls. Dispatches to `Session.callTool` and wraps the
 * result into a `ToolOutput.Mcp` value. Mirrors the upstream McpHandler in
 * codex-rs/core/src/tools/handlers/mcp.rs.
 */
class McpHandler : ToolHandler {
    override val kind: ToolKind = ToolKind.Mcp

    override suspend fun handle(invocation: ToolInvocation): Result<ToolOutput> {
        val payload = invocation.payload
        if (payload !is ToolPayload.Mcp) {
            return Result.failure(
                FunctionCallError.RespondToModel("mcp handler received unsupported payload")
            )
        }

        // Parse the `rawArguments` as JSON. An empty string is OK, but invalid JSON is not.
        val argumentsValue: JsonElement? = if (payload.rawArguments.trim().isEmpty()) {
            null
        } else {
            try {
                Json.parseToJsonElement(payload.rawArguments)
            } catch (e: Exception) {
                return Result.failure(
                    FunctionCallError.RespondToModel(
                        "failed to parse tool call arguments: ${e.message}"
                    )
                )
            }
        }

        val callResult = invocation.session.callTool(
            payload.server,
            payload.tool,
            argumentsValue
        )

        // Mirrors Rust: the MCP call always produces a successful ToolOutput.Mcp,
        // with the inner Result carrying the per-call success/failure.
        return Result.success(
            ToolOutput.Mcp(
                callResult.fold(
                    onSuccess = { McpResult<io.github.kotlinmania.codex.protocol.CallToolResult, String>(value = it) },
                    onFailure = { McpResult<io.github.kotlinmania.codex.protocol.CallToolResult, String>(error = it.message ?: "Unknown error") }
                )
            )
        )
    }
}
