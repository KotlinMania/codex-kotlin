// port-lint: source core/src/tools/spec.rs
package ai.solace.coder.core.tools

import ai.solace.coder.protocol.ResponsesApiTool

/**
 * Tool specification types.
 * TODO: Complete port from Rust codex-rs/core/src/tools/spec.rs
 */
sealed class ToolSpec {
    abstract val name: String

    data class Function(val tool: ResponsesApiTool) : ToolSpec() {
        override val name: String get() = tool.name
    }

    data class Custom(override val name: String, val description: String) : ToolSpec()

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

/**
 * Tool configuration.
 */
data class ToolsConfig(
    val shellType: ShellType = ShellType.Default,
    val applyPatchToolType: ApplyPatchToolType = ApplyPatchToolType.Default,
    val experimentalSupportedTools: Set<String> = emptySet()
)

/**
 * Shell type configuration.
 */
enum class ShellType {
    Default,
    Bash,
    Zsh,
    PowerShell
}

/**
 * Apply patch tool type.
 */
enum class ApplyPatchToolType {
    Default,
    Freeform,
    Structured,
    None
}

/**
 * Builder for tool specs.
 */
class ToolSpecBuilder(private val config: ToolsConfig) {
    private val specs = mutableListOf<ConfiguredToolSpec>()
    private val registry = ToolRegistry()

    fun add(spec: ToolSpec, supportsParallel: Boolean = true) {
        specs.add(ConfiguredToolSpec(spec, supportsParallel))
    }

    fun build(): Pair<List<ConfiguredToolSpec>, ToolRegistry> {
        return Pair(specs.toList(), registry)
    }
}

/**
 * Build tool specs from configuration.
 */
fun buildSpecs(config: ToolsConfig, mcpTools: Map<String, McpTool>?): ToolSpecBuilder {
    val builder = ToolSpecBuilder(config)
    // TODO: Add default tools based on config
    return builder
}
