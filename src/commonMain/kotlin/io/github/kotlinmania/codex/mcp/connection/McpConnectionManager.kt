// port-lint: source core/src/mcpConnectionManager.rs
package io.github.kotlinmania.codex.mcp.connection

import io.github.kotlinmania.codex.protocol.CallToolResult
import io.github.kotlinmania.codex.protocol.ElicitationAction
import io.github.kotlinmania.codex.protocol.Event
import io.github.kotlinmania.codex.protocol.McpTool
import io.github.kotlinmania.codex.utils.concurrent.CancellationToken
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.json.JsonElement

/**
 * MCP server configuration.
 *
 * Ported from Rust codex-rs/core/src/config/types.rs McpServerConfig
 */
data class McpServerConfig(
        val command: String,
        val args: List<String> = emptyList(),
        val env: Map<String, String> = emptyMap()
)

/**
 * MCP Connection Manager stub.
 *
 * Manages connections to MCP (Model Context Protocol) servers and provides tool execution
 * capabilities.
 *
 * TODO: Port full implementation from Rust codex-rs/mcp-client/src/connectionManager.rs
 */
class McpConnectionManager {
    private val tools = mutableMapOf<String, McpTool>()
    private val pendingElicitations = mutableMapOf<String, ElicitationCallback>()

    data class ElicitationCallback(
            val serverName: String,
            val requestId: String,
            val callback: (ElicitationResponse) -> Unit
    )

    data class ElicitationResponse(val action: ElicitationAction, val content: String?)

    /** Initialize the connection manager with server configurations. */
    suspend fun initialize(
            servers: Map<String, McpServerConfig>,
            eventChannel: Channel<Event>,
            cancellationToken: CancellationToken
    ) {
        // TODO: Start MCP servers and establish connections
        println("DEBUG: McpConnectionManager.initialize called with ${servers.size} servers")
    }

    /** List all available tools from connected MCP servers. */
    fun listAllTools(): Map<String, McpTool> {
        return tools.toMap()
    }

    /** Parse an MCP tool name into server and tool parts. Format: "mcpServernameToolname" */
    fun parseToolName(toolName: String): Pair<String, String>? {
        if (!toolName.startsWith("mcp__")) return null
        val parts = toolName.removePrefix("mcp__").split("__", limit = 2)
        return if (parts.size == 2) {
            Pair(parts[0], parts[1])
        } else {
            null
        }
    }

    /** Call a tool on a specific MCP server. */
    suspend fun callTool(
            server: String,
            tool: String,
            arguments: JsonElement?
    ): Result<CallToolResult> {
        // TODO: Implement actual MCP tool call
        return Result.failure(NotImplementedError("MCP tool calls not yet implemented"))
    }

    /** Resolve an elicitation request from an MCP server. */
    suspend fun resolveElicitation(
            serverName: String,
            requestId: String,
            response: ElicitationResponse
    ) {
        val callback = pendingElicitations.remove("$serverName:$requestId")
        callback?.callback?.invoke(response)
    }

    /** List all resources from all connected servers. */
    fun listAllResources(): Map<String, List<io.github.kotlinmania.codex.protocol.McpResource>> {
        return emptyMap()
    }

    /** List all resource templates from all connected servers. */
    fun listAllResourceTemplates(): Map<String, List<io.github.kotlinmania.codex.protocol.McpResourceTemplate>> {
        return emptyMap()
    }

    /** List resources from a specific server. */
    suspend fun listResources(
            server: String,
            params: io.github.kotlinmania.codex.protocol.ListResourcesRequestParams?
    ): io.github.kotlinmania.codex.protocol.ListResourcesResult {
        return io.github.kotlinmania.codex.protocol.ListResourcesResult(emptyList())
    }

    /** List resource templates from a specific server. */
    suspend fun listResourceTemplates(
            server: String,
            params: io.github.kotlinmania.codex.protocol.ListResourceTemplatesRequestParams?
    ): io.github.kotlinmania.codex.protocol.ListResourceTemplatesResult {
        return io.github.kotlinmania.codex.protocol.ListResourceTemplatesResult(emptyList())
    }

    /** Read a resource from a specific server. */
    suspend fun readResource(
            server: String,
            params: io.github.kotlinmania.codex.protocol.ReadResourceRequestParams
    ): io.github.kotlinmania.codex.protocol.ReadResourceResult {
        return io.github.kotlinmania.codex.protocol.ReadResourceResult(emptyList())
    }

    /** Shutdown all MCP connections. */
    suspend fun shutdown() {
        // TODO: Gracefully shutdown all MCP server connections
        tools.clear()
        pendingElicitations.clear()
    }
}
