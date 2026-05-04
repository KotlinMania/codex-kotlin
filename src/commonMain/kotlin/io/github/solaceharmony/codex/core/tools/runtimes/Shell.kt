// port-lint: source core/src/tools/runtimes/shell.rs
package io.github.solaceharmony.codex.core.tools.runtimes

import io.github.solaceharmony.codex.core.Exec
import io.github.solaceharmony.codex.core.ExecExpiration
import io.github.solaceharmony.codex.core.ExecToolCallOutput
import io.github.solaceharmony.codex.core.StdoutStream
import io.github.solaceharmony.codex.core.CodexErr
import io.github.solaceharmony.codex.core.tools.Approvable
import io.github.solaceharmony.codex.core.tools.ApprovalCtx
import io.github.solaceharmony.codex.core.tools.ApprovalRequirement
import io.github.solaceharmony.codex.core.tools.ProvidesSandboxRetryData
import io.github.solaceharmony.codex.core.tools.SandboxAttempt
import io.github.solaceharmony.codex.core.tools.SandboxOverride
import io.github.solaceharmony.codex.core.tools.SandboxRetryData
import io.github.solaceharmony.codex.core.tools.Sandboxable
import io.github.solaceharmony.codex.core.tools.SandboxablePreference
import io.github.solaceharmony.codex.core.tools.ToolCtx
import io.github.solaceharmony.codex.core.tools.ToolError
import io.github.solaceharmony.codex.core.tools.ToolRuntime
import io.github.solaceharmony.codex.core.tools.buildCommandSpec
import io.github.solaceharmony.codex.core.tools.withCachedApproval
import io.github.solaceharmony.codex.exec.sandbox.ExecEnv
import io.github.solaceharmony.codex.protocol.ReviewDecision
import kotlin.time.Duration.Companion.milliseconds

data class ShellRequest(
        val command: List<String>,
        val cwd: String,
        val timeoutMs: Long?,
        val env: Map<String, String>,
        val withEscalatedPermissions: Boolean?,
        val justification: String?,
        val approvalRequirement: ApprovalRequirement
) : ProvidesSandboxRetryData {
    override fun sandboxRetryData(): SandboxRetryData? {
        return SandboxRetryData(command = command, cwd = cwd)
    }
}

data class ShellApprovalKey(val command: List<String>, val cwd: String, val escalated: Boolean)

class ShellRuntime(private val processExecutor: Exec) :
        ToolRuntime<ShellRequest, ExecToolCallOutput>, Sandboxable, Approvable<ShellRequest> {

    override fun sandboxPreference(): SandboxablePreference {
        return SandboxablePreference.Auto
    }

    override fun escalateOnFailure(): Boolean {
        return true
    }

    fun approvalKey(req: ShellRequest): Any {
        return ShellApprovalKey(
                command = req.command,
                cwd = req.cwd,
                escalated = req.withEscalatedPermissions ?: false
        )
    }

    override suspend fun startApprovalAsync(req: ShellRequest, ctx: ApprovalCtx): ReviewDecision {
        val key = approvalKey(req)
        val session = ctx.session
        val turn = ctx.turn
        val callId = ctx.callId
        val command = req.command
        val cwd = req.cwd
        val reason = ctx.retryReason ?: req.justification
        val risk = ctx.risk

        return withCachedApproval(session.services, key) {
            session.requestCommandApproval(turn, callId, command, cwd, reason, risk)
        }
    }

    override fun approvalRequirement(req: ShellRequest): ApprovalRequirement? {
        return req.approvalRequirement
    }

    override fun sandboxModeForFirstAttempt(req: ShellRequest): SandboxOverride {
        val bypass =
                req.withEscalatedPermissions == true ||
                        (req.approvalRequirement is ApprovalRequirement.Skip &&
                                req.approvalRequirement.bypassSandbox)

        return if (bypass) {
            SandboxOverride.BypassSandboxFirstAttempt
        } else {
            SandboxOverride.NoOverride
        }
    }

    override suspend fun run(
            req: ShellRequest,
            attempt: SandboxAttempt,
            ctx: ToolCtx
    ): Result<ExecToolCallOutput> {
        val specResult =
                buildCommandSpec(
                        req.command,
                        req.cwd,
                        req.env,
                        if (req.timeoutMs != null)
                                ExecExpiration.Timeout(req.timeoutMs.milliseconds)
                        else ExecExpiration.DefaultTimeout,
                        req.withEscalatedPermissions,
                        req.justification
                )
        if (specResult.isFailure) {
            return Result.failure(
                    ToolError.Rejected(
                            specResult.exceptionOrNull()?.message ?: "Invalid command spec"
                    )
            )
        }
        val spec = specResult.getOrNull()!!

        val envResult = attempt.envFor(spec)
        if (envResult.isFailure) {
            return Result.failure(
                    ToolError.Codex(
                            CodexErr.Io(envResult.exceptionOrNull()?.message ?: "Unknown error")
                    )
            )
        }
        val env = envResult.getOrNull()!!

        return try {
            executeEnv(env, attempt.policy, stdoutStream(ctx))
        } catch (e: io.github.solaceharmony.codex.core.CodexException) {
            Result.failure(ToolError.Codex(e.error))
        } catch (e: Exception) {
            Result.failure(
                    ToolError.Codex(
                            CodexErr.Io(e.message ?: "Execution failed")
                    )
            )
        }
    }

    private fun stdoutStream(ctx: ToolCtx): StdoutStream {
        return StdoutStream(
                subId = ctx.turn.subId,
                callId = ctx.callId,
                txEvent = ctx.session.getTxEvent()
        )
    }

    // Helper to execute env using Exec
    private suspend fun executeEnv(
            env: ExecEnv,
            policy: io.github.solaceharmony.codex.protocol.SandboxPolicy,
            stdoutStream: StdoutStream?
    ): Result<ExecToolCallOutput> {
        val result = processExecutor.executeExecEnv(env, policy, stdoutStream)
        return when (result) {
            is io.github.solaceharmony.codex.core.CodexResult.Success -> Result.success(result.value)
            is io.github.solaceharmony.codex.core.CodexResult.Failure ->
                    Result.failure(result.error.toException())
        }
    }
}
