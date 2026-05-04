// port-lint: source core/src/tools/handlers/shell.rs
package io.github.solaceharmony.codex.core.tools.handlers

import io.github.solaceharmony.codex.core.Exec
import io.github.solaceharmony.codex.core.ExecExpiration
import io.github.solaceharmony.codex.core.ExecParams
import io.github.solaceharmony.codex.core.FunctionCallError
import io.github.solaceharmony.codex.core.command_safety.isKnownSafeCommand
import io.github.solaceharmony.codex.core.session.Session
import io.github.solaceharmony.codex.core.session.TurnContext
import io.github.solaceharmony.codex.core.tools.SharedTurnDiffTracker
import io.github.solaceharmony.codex.core.tools.ToolError
import io.github.solaceharmony.codex.core.tools.ToolHandler
import io.github.solaceharmony.codex.core.tools.ToolInvocation
import io.github.solaceharmony.codex.core.tools.ToolKind
import io.github.solaceharmony.codex.core.tools.ToolOutput
import io.github.solaceharmony.codex.core.tools.ToolPayload
import io.github.solaceharmony.codex.protocol.ShellCommandToolCallParams
import io.github.solaceharmony.codex.protocol.ShellToolCallParams
import kotlinx.serialization.json.Json

class ShellHandler : ToolHandler {
        override val kind: ToolKind = ToolKind.Function

        override fun matchesKind(payload: ToolPayload): Boolean {
                return payload is ToolPayload.Function || payload is ToolPayload.LocalShell
        }

        override fun isMutating(invocation: ToolInvocation): Boolean {
                return when (val payload = invocation.payload) {
                        is ToolPayload.Function -> {
                                try {
                                        val params =
                                                Json.decodeFromString<ShellToolCallParams>(
                                                        payload.arguments
                                                )
                                        !isKnownSafeCommand(params.command)
                                } catch (e: Exception) {
                                        true
                                }
                        }
                        is ToolPayload.LocalShell -> !isKnownSafeCommand(payload.params.command)
                        else -> true
                }
        }

        override suspend fun handle(invocation: ToolInvocation): Result<ToolOutput> {
                val payload = invocation.payload

                return when (payload) {
                        is ToolPayload.Function -> {
                                try {
                                        val params =
                                                Json.decodeFromString<ShellToolCallParams>(
                                                        payload.arguments
                                                )
                                        val execParams = toExecParams(params, invocation.turn)
                                        runExecLike(
                                                invocation.toolName,
                                                execParams,
                                                invocation.session,
                                                invocation.turn,
                                                invocation.tracker,
                                                invocation.callId,
                                                false
                                        )
                                } catch (e: Exception) {
                                        Result.failure(
                                                FunctionCallError.RespondToModel(
                                                        "failed to parse function arguments: ${e.message}"
                                                )
                                        )
                                }
                        }
                        is ToolPayload.LocalShell -> {
                                val execParams = toExecParams(payload.params, invocation.turn)
                                runExecLike(
                                        invocation.toolName,
                                        execParams,
                                        invocation.session,
                                        invocation.turn,
                                        invocation.tracker,
                                        invocation.callId,
                                        false
                                )
                        }
                        else ->
                                Result.failure(
                                        FunctionCallError.RespondToModel(
                                                "unsupported payload for shell handler: ${invocation.toolName}"
                                        )
                                )
                }
        }

        companion object {
                fun toExecParams(
                        params: ShellToolCallParams,
                        turnContext: TurnContext
                ): ExecParams {
                        return ExecParams(
                                command = params.command,
                                cwd = turnContext.resolvePath(params.workdir),
                                expiration = ExecExpiration.fromTimeoutMs(params.timeoutMs),
                                env = createEnv(turnContext.shellEnvironmentPolicy),
                                withEscalatedPermissions = params.withEscalatedPermissions,
                                justification = params.justification,
                                arg0 = null
                        )
                }

                suspend fun runExecLike(
                        toolName: String,
                        execParams: ExecParams,
                        session: Session,
                        turn: TurnContext,
                        tracker: SharedTurnDiffTracker,
                        callId: String,
                        freeform: Boolean
                ): Result<ToolOutput> {
                        val exec = Exec()
                        val runtime = io.github.solaceharmony.codex.core.tools.runtimes.ShellRuntime(exec)
                        val orchestrator = io.github.solaceharmony.codex.core.tools.ToolOrchestrator()

                        val approvalRequirement =
                                io.github.solaceharmony.codex.core.tools.defaultApprovalRequirement(
                                        turn.approvalPolicy,
                                        turn.sandboxPolicy
                                )

                        val request =
                                io.github.solaceharmony.codex.core.tools.runtimes.ShellRequest(
                                        command = execParams.command,
                                        cwd = execParams.cwd,
                                        timeoutMs =
                                                execParams.expiration.let {
                                                        if (it is ExecExpiration.Timeout)
                                                                it.duration.inWholeMilliseconds
                                                        else null
                                                },
                                        env = execParams.env,
                                        withEscalatedPermissions =
                                                execParams.withEscalatedPermissions,
                                        justification = execParams.justification,
                                        approvalRequirement = approvalRequirement
                                )

                        val toolCtx =
                                io.github.solaceharmony.codex.core.tools.ToolCtx(
                                        session = session,
                                        turn = turn,
                                        toolName = toolName,
                                        callId = callId
                                )

                        val result =
                                orchestrator.run(
                                        tool = runtime,
                                        req = request,
                                        toolCtx = toolCtx,
                                        turnCtx = turn,
                                        approvalPolicy = turn.approvalPolicy
                                )

                        return result.fold(
                                onSuccess = { output ->
                                        val aggregated = output.aggregatedOutput.text
                                        val stderr = output.stderr.text
                                        val stdout = output.stdout.text
                                        val content =
                                                if (aggregated.isNotEmpty()) {
                                                        aggregated
                                                } else if (stderr.isNotEmpty() && stdout.isNotEmpty()) {
                                                        "$stderr\n$stdout"
                                                } else if (stderr.isNotEmpty()) {
                                                        stderr
                                                } else {
                                                        stdout
                                                }
                                        Result.success(
                                                ToolOutput.Function(
                                                        content = content,
                                                        success = output.exitCode == 0
                                                )
                                        )
                                },
                                onFailure = { error ->
                                        val msg = error.message ?: "Unknown error"
                                        Result.failure(ToolError.Rejected(msg))
                                }
                        )
                }
        }
}

class ShellCommandHandler : ToolHandler {
        override val kind: ToolKind = ToolKind.Function

        override fun matchesKind(payload: ToolPayload): Boolean {
                return payload is ToolPayload.Function
        }

        override suspend fun handle(invocation: ToolInvocation): Result<ToolOutput> {
                val payload =
                        invocation.payload as? ToolPayload.Function
                                ?: return Result.failure(
                                        FunctionCallError.RespondToModel(
                                                "unsupported payload for shell_command handler: ${invocation.toolName}"
                                        )
                                )

                return try {
                        val params =
                                Json.decodeFromString<ShellCommandToolCallParams>(payload.arguments)
                        val execParams = toExecParams(params, invocation.session, invocation.turn)
                        ShellHandler.runExecLike(
                                invocation.toolName,
                                execParams,
                                invocation.session,
                                invocation.turn,
                                invocation.tracker,
                                invocation.callId,
                                true
                        )
                } catch (e: Exception) {
                        Result.failure(
                                FunctionCallError.RespondToModel(
                                        "failed to parse function arguments: ${e.message}"
                                )
                        )
                }
        }

        companion object {
                fun toExecParams(
                        params: ShellCommandToolCallParams,
                        session: Session,
                        turnContext: TurnContext
                ): ExecParams {
                        val shell = session.userShell()
                        val useLoginShell = true
                        val command = shell.deriveExecArgs(params.command, useLoginShell)

                        return ExecParams(
                                command = command,
                                cwd = turnContext.resolvePath(params.workdir),
                                expiration = ExecExpiration.fromTimeoutMs(params.timeoutMs),
                                env = createEnv(turnContext.shellEnvironmentPolicy),
                                withEscalatedPermissions = params.withEscalatedPermissions,
                                justification = params.justification,
                                arg0 = null
                        )
                }
        }
}

fun createEnv(policy: Any): Map<String, String> {
        return emptyMap()
}
