// port-lint: source core/src/tools/spec.rs
package io.github.kotlinmania.codex.core.tools

import kotlinx.serialization.json.JsonObject

/**
 * Tool specification types.
 * TODO: Complete port from Rust codex-rs/core/src/tools/spec.rs
 */
sealed class ToolSpec {
    abstract val name: String

    /**
     * A function tool defined by name, description, and JSON schema for parameters.
     */
    data class Function(
        override val name: String,
        val description: String,
        val parameters: JsonObject? = null
    ) : ToolSpec()

    /**
     * A custom tool.
     */
    data class Custom(override val name: String, val description: String) : ToolSpec()

    /**
     * An MCP (Model Context Protocol) tool.
     */
    data class Mcp(override val name: String, val server: String, val tool: McpTool) : ToolSpec()
}

/**
 * MCP tool definition.
 */
data class McpTool(
    val name: String,
    val description: String?,
    val inputSchema: Map<String, Any>? = null
)

/**
 * Configured tool specification with additional metadata.
 */
data class ConfiguredToolSpec(
    val spec: ToolSpec,
    val supportsParallelToolCalls: Boolean = true
)

// Note: ToolsConfig is defined in Codex.kt (commonMain), not here to avoid duplication

/**
 * Builder for tool specs.
 */
class ToolSpecBuilder {
    private val specs = mutableListOf<ConfiguredToolSpec>()
    private val handlers = mutableMapOf<String, ToolHandler>()

    fun add(spec: ToolSpec, supportsParallel: Boolean = true) {
        specs.add(ConfiguredToolSpec(spec, supportsParallel))
    }

    fun build(): Pair<List<ConfiguredToolSpec>, ToolRegistry> {
        return Pair(specs.toList(), ToolRegistry(handlers))
    }
}

/**
 * Build tool specs from configuration.
 */
fun buildSpecs(config: io.github.kotlinmania.codex.core.session.ToolsConfig, mcpTools: Map<String, McpTool>?): ToolSpecBuilder {
    val builder = ToolSpecBuilder()
    // TODO: Add default tools based on config
    return builder
}
