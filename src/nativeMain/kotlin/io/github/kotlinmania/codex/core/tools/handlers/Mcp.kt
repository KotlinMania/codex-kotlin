// port-lint: source core/src/tools/handlers/mcp.rs
package io.github.kotlinmania.codex.core.tools.handlers

import io.github.kotlinmania.codex.core.function_tool.FunctionCallError
import io.github.kotlinmania.codex.core.tools.ToolHandler
import io.github.kotlinmania.codex.core.tools.ToolInvocation
import io.github.kotlinmania.codex.core.tools.ToolKind
import io.github.kotlinmania.codex.core.tools.ToolOutput
import io.github.kotlinmania.codex.core.tools.ToolPayload
import io.github.kotlinmania.codex.protocol.CallToolResult
import io.github.kotlinmania.codex.protocol.ResponseInputItem

class McpHandler : ToolHandler {
    override fun kind(): ToolKind {
        return ToolKind.Mcp
    }

    override suspend fun handle(invocation: ToolInvocation): ToolOutput {
        val payload = invocation.payload as? ToolPayload.Mcp ?: return ToolOutput.Mcp(
            io.github.kotlinmania.codex.protocol.Result(
                value = null,
                error = "Invalid payload for McpHandler"
            )
        )

        val result = invocation.session.callMcpTool(
            payload.server,
            payload.tool,
            payload.rawArguments
        )

        return ToolOutput.Mcp(result)
    }
}
