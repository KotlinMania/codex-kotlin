// port-lint: source core/src/tools/runtimes/unified_exec.rs
package io.github.kotlinmania.codex.core.tools.runtimes

import io.github.kotlinmania.codex.core.exec.ExecExpiration
import io.github.kotlinmania.codex.core.error.CodexError
import io.github.kotlinmania.codex.core.tools.Approvable
import io.github.kotlinmania.codex.core.tools.ApprovalCtx
import io.github.kotlinmania.codex.core.tools.ApprovalRequirement
import io.github.kotlinmania.codex.core.tools.ProvidesSandboxRetryData
import io.github.kotlinmania.codex.core.tools.SandboxAttempt
import io.github.kotlinmania.codex.core.tools.SandboxOverride
import io.github.kotlinmania.codex.core.tools.SandboxRetryData
import io.github.kotlinmania.codex.core.tools.Sandboxable
import io.github.kotlinmania.codex.core.tools.SandboxablePreference
import io.github.kotlinmania.codex.core.tools.ToolCtx
import io.github.kotlinmania.codex.core.tools.ToolError
import io.github.kotlinmania.codex.core.tools.ToolRuntime
import io.github.kotlinmania.codex.core.tools.buildCommandSpec
import io.github.kotlinmania.codex.core.tools.withCachedApproval
import io.github.kotlinmania.codex.core.unified_exec.UnifiedExecError
import io.github.kotlinmania.codex.core.unified_exec.UnifiedExecSession
import io.github.kotlinmania.codex.core.unified_exec.UnifiedExecSessionManager
import io.github.kotlinmania.codex.protocol.ReviewDecision

data class UnifiedExecRequest(
        val command: List<String>,
        val cwd: String,
        val env: Map<String, String>,
        val withEscalatedPermissions: Boolean?,
        val justification: String?,
        val approvalRequirement: ApprovalRequirement
) : ProvidesSandboxRetryData {
    override fun sandboxRetryData(): SandboxRetryData? {
        return SandboxRetryData(command = command, cwd = cwd)
    }
}

data class UnifiedExecApprovalKey(
        val command: List<String>,
        val cwd: String,
        val escalated: Boolean
)

class UnifiedExecRuntime(private val manager: UnifiedExecSessionManager) :
        ToolRuntime<UnifiedExecRequest, UnifiedExecSession>,
        Sandboxable,
        Approvable<UnifiedExecRequest> {

    override fun sandboxPreference(): SandboxablePreference {
        return SandboxablePreference.Auto
    }

    override fun escalateOnFailure(): Boolean {
        return true
    }

    fun approvalKey(req: UnifiedExecRequest): Any {
        return UnifiedExecApprovalKey(
                command = req.command,
                cwd = req.cwd,
                escalated = req.withEscalatedPermissions ?: false
        )
    }

    override suspend fun startApprovalAsync(
            req: UnifiedExecRequest,
            ctx: ApprovalCtx
    ): ReviewDecision {
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

    override fun approvalRequirement(req: UnifiedExecRequest): ApprovalRequirement? {
        return req.approvalRequirement
    }

    override fun sandboxModeForFirstAttempt(req: UnifiedExecRequest): SandboxOverride {
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
            req: UnifiedExecRequest,
            attempt: SandboxAttempt,
            ctx: ToolCtx
    ): Result<UnifiedExecSession> {
        val specResult =
                buildCommandSpec(
                        req.command,
                        req.cwd,
                        req.env,
                        ExecExpiration.DefaultTimeout,
                        req.withEscalatedPermissions,
                        req.justification
                )
        if (specResult.isFailure) {
            return Result.failure(ToolError.Rejected("missing command line for PTY"))
        }
        val spec = specResult.getOrNull()!!

        val execEnvResult = attempt.envFor(spec)
        if (execEnvResult.isFailure) {
            return Result.failure(
                    ToolError.Codex(
                            CodexError.Io(
                                    execEnvResult.exceptionOrNull()?.message ?: "Unknown error"
                            )
                    )
            )
        }
        val execEnv = execEnvResult.getOrNull()!!

        return try {
            val session =
                    manager.openSessionWithExecEnv(
                            execEnv
                    ) // This method needs to be added to SessionManager
            Result.success(session)
        } catch (e: Exception) {
            when (e) {
                is UnifiedExecError.SandboxDenied -> {
                    Result.failure(
                            ToolError.Codex(
                                    CodexError.SandboxError.Denied(e.message ?: "Sandbox denied")
                            )
                    )
                }
                is UnifiedExecError.SandboxError -> {
                    Result.failure(
                            ToolError.Codex(
                                    CodexError.SandboxError.ApplicationFailed(
                                            e.message ?: "Sandbox failed"
                                    )
                            )
                    )
                }
                else -> {
                    Result.failure(ToolError.Rejected(e.message ?: "Unknown error"))
                }
            }
        }
    }
}
