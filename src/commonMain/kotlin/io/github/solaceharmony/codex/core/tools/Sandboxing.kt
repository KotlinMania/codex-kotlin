// port-lint: source core/src/tools/sandboxing.rs
package io.github.solaceharmony.codex.core.tools

import io.github.solaceharmony.codex.core.CodexErr
import io.github.solaceharmony.codex.core.session.Session
import io.github.solaceharmony.codex.core.session.SessionServices
import io.github.solaceharmony.codex.core.session.TurnContext
import io.github.solaceharmony.codex.exec.process.SandboxType
import io.github.solaceharmony.codex.exec.sandbox.CommandSpec
import io.github.solaceharmony.codex.exec.sandbox.ExecEnv
import io.github.solaceharmony.codex.exec.sandbox.SandboxManager
import io.github.solaceharmony.codex.protocol.AskForApproval
import io.github.solaceharmony.codex.protocol.ReviewDecision
import io.github.solaceharmony.codex.protocol.SandboxCommandAssessment
import io.github.solaceharmony.codex.protocol.SandboxPolicy
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ApprovalStore {
    private val mutex = Mutex()
    private val map = mutableMapOf<String, ReviewDecision>()

    suspend fun get(key: Any): ReviewDecision? {
        return mutex.withLock {
            map[key.toString()]
        }
    }

    suspend fun put(key: Any, value: ReviewDecision) {
        mutex.withLock {
            map[key.toString()] = value
        }
    }
}

suspend fun withCachedApproval(
        services: SessionServices,
        key: Any,
        fetch: suspend () -> ReviewDecision
): ReviewDecision {
    val store = services.toolApprovals

    store.get(key)?.let {
        return it
    }

    val decision = fetch()

    if (decision == ReviewDecision.ApprovedForSession) {
        store.put(key, decision)
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

    @Suppress("UNUSED_PARAMETER")
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
    data class Codex(val error: CodexErr) : ToolError() {
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
            // Convert CodexErr to Exception
            val err = res.onFailure {}.getOrNull()
            Result.failure(Exception(err?.toString() ?: "Unknown error"))
        }
    }
}
