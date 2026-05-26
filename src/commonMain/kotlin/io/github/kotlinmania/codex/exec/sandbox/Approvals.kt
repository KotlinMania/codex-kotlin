<<<<<<<< HEAD:src/nativeMain/kotlin/io/github/kotlinmania/codex/exec/sandbox/Approvals.kt
// port-lint: source codex-rs/core/src/tools/sandboxing.rs
========
// port-lint: source core/src/tools/sandboxing.rs
>>>>>>>> origin/main:src/commonMain/kotlin/io/github/kotlinmania/codex/exec/sandbox/Approvals.kt
package io.github.kotlinmania.codex.exec.sandbox

import io.github.kotlinmania.codex.protocol.ReviewDecision
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Store for caching approval decisions across tool calls.
 * Mirrors the upstream ApprovalStore from tools/sandboxing.rs
 */
class ApprovalStore {
    @PublishedApi
    internal val map = mutableMapOf<String, ReviewDecision>()

    /**
     * Get a cached approval decision for a key.
     * @param key The serializable key (e.g., command hash, tool call ID)
     * @return The cached decision, or null if not found
     */
    inline fun <reified K : Any> get(key: K): ReviewDecision? {
        val serialized = try {
            Json.encodeToString(key)
        } catch (e: Exception) {
            return null
        }
        return map[serialized]
    }

    /**
     * Store an approval decision for a key.
     * @param key The serializable key
     * @param value The approval decision to cache
     */
    inline fun <reified K : Any> put(key: K, value: ReviewDecision) {
        val serialized = try {
            Json.encodeToString(key)
        } catch (e: Exception) {
            return
        }
        map[serialized] = value
    }
}

/**
 * Specifies what the tool orchestrator should do with a given tool call.
 * Mirrors the upstream ApprovalRequirement enum from tools/sandboxing.rs
 */
sealed class ApprovalRequirement {
    /**
     * No approval required for this tool call.
     * @param bypassSandbox If true, the first attempt should skip sandboxing
     */
    data class Skip(val bypassSandbox: Boolean) : ApprovalRequirement()

    /**
     * Approval required for this tool call.
     * @param reason Optional explanation for why approval is needed
     */
    data class NeedsApproval(val reason: String?) : ApprovalRequirement()

    /**
     * Execution forbidden for this tool call.
     * @param reason Explanation for why execution is forbidden
     */
    data class Forbidden(val reason: String) : ApprovalRequirement()
}

/**
 * Assessment of whether a command is safe to execute in a sandbox.
 * Mirrors the upstream SandboxCommandAssessment from protocol.
 */
data class SandboxCommandAssessment(
    val safe: Boolean,
    val reason: String?
)

/**
 * Tool error types.
 * Mirrors the upstream ToolError enum from tools/sandboxing.rs
 */
sealed class ToolError {
    data class Rejected(val message: String) : ToolError()
    data class Codex(val error: Exception) : ToolError()
}

/**
 * Captures command metadata needed to re-run a tool request without sandboxing.
 * Mirrors the upstream SandboxRetryData from tools/sandboxing.rs
 */
data class SandboxRetryData(
    val command: List<String>,
    val cwd: String
)
