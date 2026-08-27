// port-lint: source core/src/tools/runtimes/apply_patch.rs
package io.github.kotlinmania.codex.core.tools.runtimes

import io.github.kotlinmania.codex.core.Exec
import io.github.kotlinmania.codex.core.ExecExpiration
import io.github.kotlinmania.codex.protocol.ExecToolCallOutput
import io.github.kotlinmania.codex.core.StdoutStream
import io.github.kotlinmania.codex.core.CodexErr
import io.github.kotlinmania.codex.core.CodexResult
import io.github.kotlinmania.codex.core.tools.Approvable
import io.github.kotlinmania.codex.core.tools.ApprovalCtx
import io.github.kotlinmania.codex.core.tools.ProvidesSandboxRetryData
import io.github.kotlinmania.codex.core.tools.SandboxAttempt
import io.github.kotlinmania.codex.core.tools.SandboxRetryData
import io.github.kotlinmania.codex.core.tools.Sandboxable
import io.github.kotlinmania.codex.core.tools.SandboxablePreference
import io.github.kotlinmania.codex.core.tools.ToolCtx
import io.github.kotlinmania.codex.core.tools.ToolError
import io.github.kotlinmania.codex.core.tools.ToolRuntime
import io.github.kotlinmania.codex.core.tools.withCachedApproval
import io.github.kotlinmania.codex.exec.sandbox.CommandSpec
import io.github.kotlinmania.codex.exec.sandbox.ExecEnv
import io.github.kotlinmania.codex.protocol.AskForApproval
import io.github.kotlinmania.codex.protocol.ReviewDecision
import kotlin.time.Duration.Companion.milliseconds

// Constants
const val CODEX_APPLY_PATCH_ARG1 = "--codex-run-as-apply-patch"

data class ApplyPatchRequest(
        val patch: String,
        val cwd: String,
        val timeoutMs: Long?,
        val userExplicitlyApproved: Boolean,
        val codexExe: String?
) : ProvidesSandboxRetryData {
        override fun sandboxRetryData(): SandboxRetryData? = null
}

data class ApprovalKey(val patch: String, val cwd: String)

class ApplyPatchRuntime(private val processExecutor: Exec) :
        ToolRuntime<ApplyPatchRequest, ExecToolCallOutput>,
        Sandboxable,
        Approvable<ApplyPatchRequest> {

        override fun sandboxPreference(): SandboxablePreference {
                return SandboxablePreference.Auto
        }

        override fun escalateOnFailure(): Boolean {
                return true
        }

        fun approvalKey(req: ApplyPatchRequest): Any {
                return ApprovalKey(req.patch, req.cwd)
        }

        override suspend fun startApprovalAsync(
                req: ApplyPatchRequest,
                ctx: ApprovalCtx
        ): ReviewDecision {
                val key = approvalKey(req)
                val session = ctx.session
                val turn = ctx.turn
                val callId = ctx.callId
                val cwd = req.cwd
                val retryReason = ctx.retryReason
                val risk = ctx.risk
                val userExplicitlyApproved = req.userExplicitlyApproved

                return withCachedApproval(session.services, key) {
                        if (retryReason != null) {
                                session.requestCommandApproval(
                                        turn,
                                        callId,
                                        listOf("apply_patch"),
                                        cwd,
                                        retryReason,
                                        risk
                                )
                        } else if (userExplicitlyApproved) {
                                ReviewDecision.ApprovedForSession
                        } else {
                                ReviewDecision.Approved
                        }
                }
        }

        override fun wantsNoSandboxApproval(policy: AskForApproval): Boolean {
                return policy != AskForApproval.Never
        }

        override suspend fun run(
                req: ApplyPatchRequest,
                attempt: SandboxAttempt,
                ctx: ToolCtx
        ): Result<ExecToolCallOutput> {
                val specResult = buildCommandSpec(req)
                if (specResult.isFailure)
                        return Result.failure(
                                specResult.exceptionOrNull() as? Throwable
                                        ?: Exception("Unknown error")
                        )
                val spec = specResult.getOrNull()!!

                val envResult = attempt.envFor(spec)
                if (envResult.isFailure)
                        return Result.failure(
                                ToolError.Codex(
                                        CodexErr.Io(
                                                envResult.exceptionOrNull()?.message
                                                        ?: "Unknown error"
                                        )
                                )
                        )
                val env = envResult.getOrNull()!!

                return executeEnv(env, attempt.policy, stdoutStream(ctx))
                        .mapCatching { it }
                        .recoverCatching {
                                throw ToolError.Codex(
                                        CodexErr.Io(it.message ?: "Execution failed")
                                )
                        }
        }

        private fun buildCommandSpec(req: ApplyPatchRequest): Result<CommandSpec> {
                val currentExeResult = getCurrentExe()
                if (currentExeResult.isFailure)
                        return Result.failure(
                                ToolError.Rejected(
                                        "failed to determine codex exe: ${currentExeResult.exceptionOrNull()?.message}"
                                )
                        )
                val exe = req.codexExe ?: currentExeResult.getOrNull()!!

                return Result.success(
                        CommandSpec(
                                program = exe,
                                args = listOf(CODEX_APPLY_PATCH_ARG1, req.patch),
                                cwd = req.cwd,
                                expiration =
                                        if (req.timeoutMs != null)
                                                ExecExpiration.Timeout(req.timeoutMs.milliseconds)
                                        else ExecExpiration.DefaultTimeout,
                                env = emptyMap(), // Minimal environment
                                withEscalatedPermissions = null,
                                justification = null
                        )
                )
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
                policy: io.github.kotlinmania.codex.protocol.SandboxPolicy,
                stdoutStream: StdoutStream?
        ): Result<ExecToolCallOutput> {
                val result = processExecutor.executeExecEnv(env, policy, stdoutStream)
                return when (result) {
                        is CodexResult.Success<ExecToolCallOutput> -> Result.success(result.value)
                        is CodexResult.Failure -> Result.failure(result.error.toException())
                }
        }

        private fun getCurrentExe(): Result<String> = Result.success("codex")
}
