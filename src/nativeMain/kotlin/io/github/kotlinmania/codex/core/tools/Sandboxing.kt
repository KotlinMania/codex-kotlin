// port-lint: source core/src/tools/sandboxing.rs
package io.github.kotlinmania.codex.core.tools

import io.github.kotlinmania.codex.core.error.CodexError
import io.github.kotlinmania.codex.core.session.Session
import io.github.kotlinmania.codex.core.session.SessionServices
import io.github.kotlinmania.codex.core.session.TurnContext
import io.github.kotlinmania.codex.exec.process.SandboxType
import io.github.kotlinmania.codex.exec.sandbox.CommandSpec
import io.github.kotlinmania.codex.exec.sandbox.ExecEnv
import io.github.kotlinmania.codex.exec.sandbox.SandboxManager
import io.github.kotlinmania.codex.exec.sandbox.SandboxPreference
import io.github.kotlinmania.codex.protocol.AskForApproval
import io.github.kotlinmania.codex.protocol.ReviewDecision
import io.github.kotlinmania.codex.protocol.SandboxCommandAssessment
import io.github.kotlinmania.codex.protocol.SandboxPolicy
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// ApprovalStore is imported from io.github.kotlinmania.codex.core.session.Turn

/**
 * Helper to get cached approval or fetch a new one.
 * Simplified to work with the boolean-based ApprovalStore in Turn.kt.
 */
suspend fun <K> withCachedApproval(
        services: SessionServices,
        key: K,
        fetch: suspend () -> ReviewDecision
): ReviewDecision where K : Any {
    val store = services.toolApprovals
    val keyStr = key.toString()

    // Check if already approved
    store.isApproved(keyStr)?.let { approved ->
        return if (approved) ReviewDecision.ApprovedForSession else ReviewDecision.Denied
    }

    val decision = fetch()

    // Cache approval decision
    if (decision == ReviewDecision.ApprovedForSession || decision == ReviewDecision.Approved) {
        store.setApproval(keyStr, true)
    } else if (decision == ReviewDecision.Denied) {
        store.setApproval(keyStr, false)
    }

    return decision
}

data class ApprovalCtx(
        val session: Session,
        val turn: TurnContext,
        val callId: String,
        val retryReason: String?,
        val risk: SandboxCommandAssessment?
)

sealed class ApprovalRequirement {
    data class Skip(val bypassSandbox: Boolean) : ApprovalRequirement()
    data class NeedsApproval(val reason: String?) : ApprovalRequirement()
    data class Forbidden(val reason: String) : ApprovalRequirement()
}

fun defaultApprovalRequirement(
        policy: AskForApproval,
        sandboxPolicy: SandboxPolicy
): ApprovalRequirement {
    val needsApproval =
            when (policy) {
                AskForApproval.Never, AskForApproval.OnFailure -> false
                AskForApproval.OnRequest -> sandboxPolicy != SandboxPolicy.DangerFullAccess
                AskForApproval.UnlessTrusted -> true
            }

    return if (needsApproval) {
        ApprovalRequirement.NeedsApproval(null)
    } else {
        ApprovalRequirement.Skip(bypassSandbox = false)
    }
}

enum class SandboxOverride {
    NoOverride,
    BypassSandboxFirstAttempt
}

interface Approvable<Req> {
    // type ApprovalKey
    // fun approvalKey(req: Req): ApprovalKey

    fun sandboxModeForFirstAttempt(req: Req): SandboxOverride {
        return SandboxOverride.NoOverride
    }

    fun shouldBypassApproval(policy: AskForApproval, alreadyApproved: Boolean): Boolean {
        if (alreadyApproved) {
            return true
        }
        return policy == AskForApproval.Never
    }

    fun approvalRequirement(req: Req): ApprovalRequirement? {
        return null
    }

    fun wantsNoSandboxApproval(policy: AskForApproval): Boolean {
        return policy != AskForApproval.Never && policy != AskForApproval.OnRequest
    }

    suspend fun startApprovalAsync(req: Req, ctx: ApprovalCtx): ReviewDecision
}

enum class SandboxablePreference {
    Auto,
    Require,
    Forbid
}

interface Sandboxable {
    fun sandboxPreference(): SandboxablePreference
    fun escalateOnFailure(): Boolean {
        return true
    }
}

data class ToolCtx(
        val session: Session,
        val turn: TurnContext,
        val callId: String,
        val toolName: String
)

data class SandboxRetryData(
        val command: List<String>,
        val cwd: String // PathBuf -> String
)

interface ProvidesSandboxRetryData {
    fun sandboxRetryData(): SandboxRetryData?
}

sealed class ToolError : Exception() {
    data class Rejected(val reason: String) : ToolError() {
        override val message: String = reason
    }
    data class Codex(val error: CodexError) : ToolError() {
        override val message: String = error.toString()
    }
}

interface ToolRuntime<Req, Out> : Approvable<Req>, Sandboxable {
    suspend fun run(
            req: Req,
            attempt: SandboxAttempt,
            ctx: ToolCtx
    ): Result<Out> // Using Result<Out> which can wrap ToolError logic or throw
}

class SandboxAttempt(
        val sandbox: SandboxType,
        val policy: SandboxPolicy,
        val manager: SandboxManager,
        val sandboxCwd: String, // Path -> String
        val codexLinuxSandboxExe: String? // PathBuf -> String
) {
    fun envFor(spec: CommandSpec): Result<ExecEnv> {
        val res = manager.transform(spec, policy, sandbox, sandboxCwd, codexLinuxSandboxExe)
        return if (res.isSuccess()) {
            Result.success(res.getOrThrow())
        } else {
            // Convert CodexError to Exception
            val err = res.onFailure {}.getOrNull()
            Result.failure(Exception(err?.toString() ?: "Unknown error"))
        }
    }
}
