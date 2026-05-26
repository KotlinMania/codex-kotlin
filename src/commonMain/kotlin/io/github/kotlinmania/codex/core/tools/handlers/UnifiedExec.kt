// port-lint: source core/src/tools/handlers/unified_exec.rs
package io.github.kotlinmania.codex.core.tools.handlers

<<<<<<<< HEAD:src/nativeMain/kotlin/io/github/kotlinmania/codex/core/tools/handlers/UnifiedExec.kt
import io.github.kotlinmania.codex.core.error.CodexError
========
import io.github.kotlinmania.codex.core.CodexErr
>>>>>>>> origin/main:src/commonMain/kotlin/io/github/kotlinmania/codex/core/tools/handlers/UnifiedExec.kt
import io.github.kotlinmania.codex.core.tools.ToolError
import io.github.kotlinmania.codex.core.tools.ToolHandler
import io.github.kotlinmania.codex.core.tools.ToolInvocation
import io.github.kotlinmania.codex.core.tools.ToolKind
import io.github.kotlinmania.codex.core.tools.ToolOutput
import io.github.kotlinmania.codex.core.tools.ToolPayload
<<<<<<<< HEAD:src/nativeMain/kotlin/io/github/kotlinmania/codex/core/tools/handlers/UnifiedExec.kt
import io.github.kotlinmania.codex.core.unified_exec.ExecCommandRequest
import io.github.kotlinmania.codex.core.unified_exec.UnifiedExecContext
import io.github.kotlinmania.codex.core.unified_exec.UnifiedExecSessionManager
import io.github.kotlinmania.codex.core.unified_exec.WriteStdinRequest
========
import io.github.kotlinmania.codex.core.unifiedexec.ExecCommandRequest
import io.github.kotlinmania.codex.core.unifiedexec.UnifiedExecContext
import io.github.kotlinmania.codex.core.unifiedexec.UnifiedExecSessionManager
import io.github.kotlinmania.codex.core.unifiedexec.WriteStdinRequest
>>>>>>>> origin/main:src/commonMain/kotlin/io/github/kotlinmania/codex/core/tools/handlers/UnifiedExec.kt
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/** Handler for the unifiedExec tool. */
class UnifiedExecHandler : ToolHandler {
        override val kind: ToolKind = ToolKind.Function

        private val sessionManager = UnifiedExecSessionManager()

        override fun matchesKind(payload: ToolPayload): Boolean {
                return payload is ToolPayload.Function || payload is ToolPayload.UnifiedExec
        }

        override fun isMutating(invocation: ToolInvocation): Boolean {
                val arguments =
                        when (val payload = invocation.payload) {
                                is ToolPayload.Function -> payload.arguments
                                is ToolPayload.UnifiedExec -> payload.arguments
                                else -> return true
                        }

                return try {
                        val args =
                                Json { ignoreUnknownKeys = true }
                                        .decodeFromString<ExecCommandArgs>(arguments)
                        val command = getCommand(args)
                        !isKnownSafeCommand(command)
                } catch (e: Exception) {
                        true
                }
        }

        override suspend fun handle(invocation: ToolInvocation): Result<ToolOutput> {
                val arguments =
                        when (val payload = invocation.payload) {
                                is ToolPayload.Function -> payload.arguments
                                is ToolPayload.UnifiedExec -> payload.arguments
                                else ->
                                        return Result.failure(
                                                ToolError.Codex(
                                                        CodexErr.Fatal(
                                                                "unified_exec handler received unsupported payload"
                                                        )
                                                )
                                        )
                        }

                val context =
                        UnifiedExecContext(
                                session = invocation.session,
                                turn = invocation.turn,
                                callId = invocation.callId
                        )

                return try {
                        when (invocation.toolName) {
                                "exec_command" -> {
                                        val args =
                                                try {
                                                        Json { ignoreUnknownKeys = true }
                                                                .decodeFromString<ExecCommandArgs>(
                                                                        arguments
                                                                )
                                                } catch (e: Exception) {
                                                        return Result.failure(
                                                                ToolError.Codex(
                                                                        CodexErr.Fatal(
                                                                                "failed to parse function arguments: ${e.message}"
                                                                        )
                                                                )
                                                        )
                                                }

                                        val processId = sessionManager.allocateProcessId()
                                        val commandList =
                                                listOf(
                                                        args.shell,
                                                        if (args.login) "-lc" else "-c",
                                                        args.cmd
                                                )

                                        val request =
                                                ExecCommandRequest(
                                                        command = commandList,
                                                        processId = processId,
                                                        yieldTimeMs = args.yieldTimeMs,
                                                        maxOutputTokens = args.maxOutputTokens,
                                                        workdir = args.workdir,
                                                        withEscalatedPermissions =
                                                                args.withEscalatedPermissions,
                                                        justification = args.justification
                                                )

                                        val response = sessionManager.execCommand(request, context)

                                        Result.success(
<<<<<<<< HEAD:src/nativeMain/kotlin/io/github/kotlinmania/codex/core/tools/handlers/UnifiedExec.kt
                                                ToolOutput.Exec(
                                                        io.github.kotlinmania.codex.core.exec.ExecToolCallOutput(
                                                                exitCode = response.exitCode ?: -1,
                                                                stdout =
                                                                        io.github.kotlinmania.codex.core.exec
                                                                                .StreamOutput(
                                                                                        response.output
                                                                                ),
                                                                stderr =
                                                                        io.github.kotlinmania.codex.core.exec
                                                                                .StreamOutput(""),
                                                                aggregatedOutput =
                                                                        io.github.kotlinmania.codex.core.exec
                                                                                .StreamOutput(
                                                                                        response.output
                                                                                ),
                                                                duration = response.wallTime,
                                                                timedOut = false
                                                        )
========
                                                ToolOutput.Function(
                                                        content = response.output,
                                                        success = response.exitCode == 0
>>>>>>>> origin/main:src/commonMain/kotlin/io/github/kotlinmania/codex/core/tools/handlers/UnifiedExec.kt
                                                )
                                        )
                                }
                                "write_stdin" -> {
                                        val args =
                                                try {
                                                        Json { ignoreUnknownKeys = true }
                                                                .decodeFromString<
                                                                        WriteStdinRequest>(
                                                                        arguments
                                                                )
                                                } catch (e: Exception) {
                                                        return Result.failure(
                                                                ToolError.Codex(
                                                                        CodexErr.Fatal(
                                                                                "failed to parse function arguments: ${e.message}"
                                                                        )
                                                                )
                                                        )
                                                }

                                        sessionManager.writeStdin(args.processId, args.input)

                                        Result.success(
                                                ToolOutput.Function(
                                                        "Input written to process ${args.processId}",
                                                        success = true
                                                )
                                        )
                                }
                                else ->
                                        return Result.failure(
                                                ToolError.Codex(
                                                        CodexErr.Fatal(
                                                                "unified_exec handler received unsupported tool: ${invocation.toolName}"
                                                        )
                                                )
                                        )
                        }
                } catch (e: Exception) {
                        Result.failure(
                                ToolError.Codex(
                                        CodexErr.Fatal("UnifiedExec failed: ${e.message}")
                                )
                        )
                }
        }

        private fun getCommand(args: ExecCommandArgs): List<String> {
                return listOf(args.shell, "-c", args.cmd)
        }

        private fun isKnownSafeCommand(command: List<String>): Boolean {
                // Implement allowlist check if needed
                return false
        }
}

@Serializable
data class ExecCommandArgs(
        val cmd: String,
        val workdir: String? = null,
        val shell: String = "/bin/bash",
        val login: Boolean = true,
        val yieldTimeMs: Long = 10000,
        val maxOutputTokens: Int? = null,
        val withEscalatedPermissions: Boolean? = null,
        val justification: String? = null
)
