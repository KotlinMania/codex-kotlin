// port-lint: source core/src/tools/orchestrator.rs
package io.github.kotlinmania.codex.core.tools

import io.github.kotlinmania.codex.core.ExecToolCallOutput
import io.github.kotlinmania.codex.core.CodexErr
import io.github.kotlinmania.codex.core.getErrorMessageUi
import io.github.kotlinmania.codex.exec.process.SandboxType
import io.github.kotlinmania.codex.exec.sandbox.SandboxManager
import io.github.kotlinmania.codex.protocol.AskForApproval
import io.github.kotlinmania.codex.protocol.ReviewDecision
import io.github.kotlinmania.codex.protocol.SandboxPolicy
import io.github.kotlinmania.codex.core.session.TurnContext

class ToolOrchestrator {
    private val sandbox = SandboxManager()

    suspend fun <Req, Out, T> run(
        tool: T,
        req: Req,
        toolCtx: ToolCtx,
        turnCtx: TurnContext,
        approvalPolicy: AskForApproval
    ): Result<Out> where T : ToolRuntime<Req, Out>, Req : ProvidesSandboxRetryData {
        val otel = turnCtx.client?.getOtelEventManager()
        val otelTn = toolCtx.toolName
        val otelCi = toolCtx.callId
        // val otelUser = ToolDecisionSource.User
        // val otelCfg = ToolDecisionSource.Config

        // 1) Approval
        var alreadyApproved = false

        val requirement = tool.approvalRequirement(req) ?: defaultApprovalRequirement(approvalPolicy, turnCtx.sandboxPolicy)
        
        when (requirement) {
            is ApprovalRequirement.Skip -> {
                // otel.toolDecision(otelTn, otelCi, ReviewDecision.Approved, otelCfg)
            }
            is ApprovalRequirement.Forbidden -> {
                return Result.failure(ToolErrorException(ToolError.Rejected(requirement.reason)))
            }
            is ApprovalRequirement.NeedsApproval -> {
                var risk: io.github.kotlinmania.codex.protocol.SandboxCommandAssessment? = null

                req.sandboxRetryData()?.let { metadata ->
                    risk = toolCtx.session.assessSandboxCommand(
                        turnCtx,
                        toolCtx.callId,
                        metadata.command,
                        null
                    )
                }

                val approvalCtx = ApprovalCtx(
                    session = toolCtx.session,
                    turn = turnCtx,
                    callId = toolCtx.callId,
                    retryReason = requirement.reason,
                    risk = risk
                )
                val decision = tool.startApprovalAsync(req, approvalCtx)

                // otel.toolDecision(otelTn, otelCi, decision, otelUser)

                when (decision) {
                    ReviewDecision.Denied, ReviewDecision.Abort -> {
                        return Result.failure(ToolErrorException(ToolError.Rejected("rejected by user")))
                    }
                    ReviewDecision.Approved, ReviewDecision.ApprovedForSession -> {}
                }
                alreadyApproved = true
            }
        }

        // 2) First attempt under the selected sandbox.
        val initialSandbox = when (tool.sandboxModeForFirstAttempt(req)) {
            SandboxOverride.BypassSandboxFirstAttempt -> SandboxType.None
            SandboxOverride.NoOverride -> {
                val pref = when (tool.sandboxPreference()) {
                    SandboxablePreference.Auto -> io.github.kotlinmania.codex.exec.sandbox.SandboxPreference.Auto
                    SandboxablePreference.Require -> io.github.kotlinmania.codex.exec.sandbox.SandboxPreference.Require
                    SandboxablePreference.Forbid -> io.github.kotlinmania.codex.exec.sandbox.SandboxPreference.Forbid
                }
                sandbox.selectInitialSandbox(turnCtx.sandboxPolicy, pref)
            }
        }

        val initialAttempt = SandboxAttempt(
            sandbox = initialSandbox,
            policy = turnCtx.sandboxPolicy,
            manager = sandbox,
            sandboxCwd = turnCtx.cwd,
            codexLinuxSandboxExe = turnCtx.codexLinuxSandboxExe
        )

        val result = tool.run(req, initialAttempt, toolCtx)
        
        return result.fold(
            onSuccess = { Result.success(it) },
            onFailure = { err ->
                // Check if it is a Sandbox Denied error
                // In Kotlin we might need to check exception type or wrap it
                if (err is ToolErrorException && err.error is ToolError.Codex) {
                    val codexError = err.error.error
                    if (codexError !is CodexErr.Sandbox) {
                        return Result.failure(err)
                    }
                    val sandboxErr = codexError.error
                    if (sandboxErr !is io.github.kotlinmania.codex.core.SandboxErr.Denied) {
                        return Result.failure(err)
                    }
                    val output = sandboxErr.output

                    if (!tool.escalateOnFailure()) {
                        return Result.failure(err)
                    }
                    
                    if (!tool.wantsNoSandboxApproval(approvalPolicy)) {
                        return Result.failure(err)
                    }

                    if (!tool.shouldBypassApproval(approvalPolicy, alreadyApproved)) {
                        var risk: io.github.kotlinmania.codex.protocol.SandboxCommandAssessment? = null

                        req.sandboxRetryData()?.let { metadata ->
                            val friendly = getErrorMessageUi(codexError)
                            val failureSummary = "failed in sandbox: $friendly"

                            risk = toolCtx.session.assessSandboxCommand(
                                turnCtx,
                                toolCtx.callId,
                                metadata.command,
                                failureSummary
                            )
                        }

                        val reasonMsg = buildDenialReasonFromOutput(output)
                        val approvalCtx = ApprovalCtx(
                            session = toolCtx.session,
                            turn = turnCtx,
                            callId = toolCtx.callId,
                            retryReason = reasonMsg,
                            risk = risk
                        )

                        val decision = tool.startApprovalAsync(req, approvalCtx)
                        // otel.toolDecision(...)

                        when (decision) {
                            ReviewDecision.Denied, ReviewDecision.Abort -> {
                                return Result.failure(ToolErrorException(ToolError.Rejected("rejected by user")))
                            }
                            ReviewDecision.Approved, ReviewDecision.ApprovedForSession -> {}
                        }
                    }

                    val escalatedAttempt = SandboxAttempt(
                        sandbox = SandboxType.None,
                        policy = turnCtx.sandboxPolicy,
                        manager = sandbox,
                        sandboxCwd = turnCtx.cwd,
                        codexLinuxSandboxExe = null
                    )

                    tool.run(req, escalatedAttempt, toolCtx)
                } else {
                    Result.failure(err)
                }
            }
        )
    }
}

fun buildDenialReasonFromOutput(_output: ExecToolCallOutput): String {
    return "command failed; retry without sandbox?"
}

// Helper exception to wrap ToolError for Result<T> compatibility if needed
class ToolErrorException(val error: ToolError) : Exception(error.toString())
