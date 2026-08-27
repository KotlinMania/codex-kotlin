// port-lint: source core/src/exec.rs
package io.github.kotlinmania.codex.core

import io.github.kotlinmania.codex.core.CodexErr
import io.github.kotlinmania.codex.core.CodexResult
import io.github.kotlinmania.codex.exec.process.SandboxType
import io.github.kotlinmania.codex.exec.sandbox.ExecEnv
import io.github.kotlinmania.codex.protocol.ExecToolCallOutput
import io.github.kotlinmania.codex.protocol.StreamOutput
import io.github.kotlinmania.codex.protocol.SandboxPolicy
import io.github.kotlinmania.codex.utils.git.platformExecuteCommand

typealias ExecToolCallOutput = io.github.kotlinmania.codex.protocol.ExecToolCallOutput
typealias StreamOutput<T> = io.github.kotlinmania.codex.protocol.StreamOutput<T>

internal data class StdoutStream(
    val subId: String,
    val callId: String,
    val txEvent: kotlinx.coroutines.channels.SendChannel<io.github.kotlinmania.codex.protocol.Event>? = null,
)

data class ExecParams(
    val command: List<String>,
    val cwd: String,
    val expiration: ExecExpiration = ExecExpiration.DefaultTimeout,
    val env: Map<String, String> = emptyMap(),
    val withEscalatedPermissions: Boolean? = null,
    val justification: String? = null,
    val arg0: String? = null,
)

open class Exec {
    open suspend fun execute(
        params: ExecParams,
        sandboxPolicy: SandboxPolicy,
        sandboxCwd: String = params.cwd,
    ): CodexResult<ExecToolCallOutput> {
        val env = ExecEnv(
            command = params.command,
            cwd = params.cwd,
            env = params.env,
            expiration = params.expiration,
            sandbox = SandboxType.None,
            withEscalatedPermissions = params.withEscalatedPermissions,
            justification = params.justification,
            arg0 = params.arg0,
        )
        return executeExecEnv(env, sandboxPolicy)
    }

    internal open suspend fun executeExecEnv(
        env: ExecEnv,
        policy: SandboxPolicy,
        stdoutStream: StdoutStream? = null,
    ): CodexResult<ExecToolCallOutput> {
        if (env.command.isEmpty()) {
            return CodexResult.failure(CodexErr.Fatal("empty command"))
        }
        val exitCode = platformExecuteCommand(env.command)
        val output = ExecToolCallOutput(
            exitCode = exitCode,
            stdout = StreamOutput.of(""),
            stderr = StreamOutput.of(""),
            aggregatedOutput = StreamOutput.of(""),
        )
        return CodexResult.success(output)
    }
}

/** Check if execution likely failed due to sandbox restrictions */
fun isLikelySandboxDenied(sandboxType: SandboxType, execOutput: ExecToolCallOutput): Boolean {
    if (sandboxType == SandboxType.None || execOutput.exitCode == 0) return false

    // Quick rejects: well-known non-sandbox shell exit codes
    val quickRejectExitCodes = setOf(2, 126, 127)
    if (quickRejectExitCodes.contains(execOutput.exitCode)) return false

    val sandboxDeniedKeywords =
        listOf(
            "operation not permitted",
            "permission denied",
            "read-only file system",
            "seccomp",
            "sandbox",
            "landlock",
            "failed to write file",
        )

    val hasSandboxKeyword =
        listOf(execOutput.stderr.text, execOutput.stdout.text, execOutput.aggregatedOutput.text)
            .any { section ->
                section.lowercase().let { lower ->
                    sandboxDeniedKeywords.any { keyword -> lower.contains(keyword) }
                }
            }

    return hasSandboxKeyword
}
