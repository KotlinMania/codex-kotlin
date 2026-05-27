// port-lint: ignore
// transliterated from upstream module root
package io.github.kotlinmania.codex.core.tools

import io.github.kotlinmania.codex.core.ExecExpiration
import io.github.kotlinmania.codex.exec.sandbox.CommandSpec

// Module: runtimes
// Concrete ToolRuntime implementations for specific tools. Each runtime stays
// small and focused and reuses the orchestrator for approvals + sandbox + retry.

// Shared helper to construct a CommandSpec from a tokenized command line.
// Validates that at least a program is present.
fun buildCommandSpec(
    command: List<String>,
    cwd: String, // Path -> String
    env: Map<String, String>,
    expiration: ExecExpiration,
    withEscalatedPermissions: Boolean?,
    justification: String?
): Result<CommandSpec> {
    if (command.isEmpty()) {
        return Result.failure(ToolErrorException(ToolError.Rejected("command args are empty")))
    }
    
    val program = command.first()
    val args = command.drop(1)
    
    return Result.success(CommandSpec(
        program = program,
        args = args,
        cwd = cwd,
        env = env,
        expiration = expiration,
        withEscalatedPermissions = withEscalatedPermissions,
        justification = justification
    ))
}
