// port-lint: source core/src/tools/handlers/test_sync.rs
package io.github.kotlinmania.codex.core.tools.handlers

import io.github.kotlinmania.codex.core.tools.ToolHandler
import io.github.kotlinmania.codex.core.tools.ToolInvocation
import io.github.kotlinmania.codex.core.tools.ToolKind
import io.github.kotlinmania.codex.core.tools.ToolOutput

class TestSyncHandler : ToolHandler {
    override val kind: ToolKind = ToolKind.Function

    override suspend fun handle(invocation: ToolInvocation): Result<ToolOutput> {
        // Implementation placeholder
        return Result.success(
                ToolOutput.Function(
                        content = "test_sync_tool not implemented",
                        contentItems = null,
                        success = false
                )
        )
    }
}
