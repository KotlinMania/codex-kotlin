// port-lint: source core/src/state/turn.rs
package ai.solace.coder.core.session

import ai.solace.coder.client.auth.AuthManager
import ai.solace.coder.exec.shell.Shell
import ai.solace.coder.exec.shell.ShellDetector
import ai.solace.coder.mcp.connection.McpConnectionManager
import ai.solace.coder.protocol.ReviewDecision
import ai.solace.coder.protocol.TurnAbortReason
import ai.solace.coder.protocol.ResponseInputItem
import ai.solace.coder.protocol.RolloutItem
import ai.solace.coder.utils.concurrent.CancellationToken
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Turn-scoped state and active turn metadata scaffolding.
 *
 * Ported from Rust codex-rs/core/src/state/turn.rs
 */

/**
 * Describes the type of work a task performs.
 */
enum class TaskKind {
    Regular,
    Review,
    Compact
}

/**
 * Metadata about the currently running turn.
 */
class ActiveTurn {
    private val tasks = linkedMapOf<String, RunningTask>()
    val turnState = TurnState()

    fun addTask(task: RunningTask) {
        val subId = task.turnContext.subId
        tasks[subId] = task
    }

    /**
     * Remove a task by sub_id. Returns true if no tasks remain.
     */
    fun removeTask(subId: String): Boolean {
        tasks.remove(subId)
        return tasks.isEmpty()
    }

    fun drainTasks(): List<RunningTask> {
        val result = tasks.values.toList()
        tasks.clear()
        return result
    }

    fun getTasks(): Map<String, RunningTask> = tasks.toMap()

    /**
     * Clear any pending approvals and input buffered for the current turn.
     */
    suspend fun clearPending() {
        turnState.clearPending()
    }
}

/**
 * A running task in the session.
 */
data class RunningTask(
    val done: CompletableDeferred<Unit>,
    val kind: TaskKind,
    val task: SessionTask,
    val cancellationToken: CancellationToken,
    val turnContext: TurnContext
)

/**
 * Mutable state for a single turn.
 *
 * Ported from Rust codex-rs/core/src/state/turn.rs
 */
class TurnState {
    private val mutex = Mutex()
    private val pendingApprovals = mutableMapOf<String, CompletableDeferred<ReviewDecision>>()
    private val pendingInput = mutableListOf<ResponseInputItem>()

    /**
     * Insert a pending approval request.
     * Returns the previous deferred if one existed for this key.
     */
    suspend fun insertPendingApproval(
        key: String,
        deferred: CompletableDeferred<ReviewDecision>
    ): CompletableDeferred<ReviewDecision>? {
        return mutex.withLock {
            pendingApprovals.put(key, deferred)
        }
    }

    /**
     * Remove and return a pending approval by key.
     */
    suspend fun removePendingApproval(key: String): CompletableDeferred<ReviewDecision>? {
        return mutex.withLock {
            pendingApprovals.remove(key)
        }
    }

    /**
     * Clear all pending approvals and input.
     */
    suspend fun clearPending() {
        mutex.withLock {
            // Complete any pending approvals with Denied
            for ((_, deferred) in pendingApprovals) {
                if (!deferred.isCompleted) {
                    deferred.complete(ReviewDecision.Denied)
                }
            }
            pendingApprovals.clear()
            pendingInput.clear()
        }
    }

    /**
     * Push pending input item for the current turn.
     */
    suspend fun pushPendingInput(input: ResponseInputItem) {
        mutex.withLock {
            pendingInput.add(input)
        }
    }

    /**
     * Take all pending input, leaving the list empty.
     */
    suspend fun takePendingInput(): List<ResponseInputItem> {
        return mutex.withLock {
            if (pendingInput.isEmpty()) {
                emptyList()
            } else {
                val result = pendingInput.toList()
                pendingInput.clear()
                result
            }
        }
    }

    /**
     * Check if there are pending approvals.
     */
    suspend fun hasPendingApprovals(): Boolean {
        return mutex.withLock {
            pendingApprovals.isNotEmpty()
        }
    }
}

/**
 * Async task that drives a Session turn.
 *
 * Implementations encapsulate a specific Codex workflow (regular chat,
 * reviews, ghost snapshots, etc.). Each task instance is owned by a
 * Session and executed on a background coroutine.
 *
 * Ported from Rust codex-rs/core/src/tasks/mod.rs
 */
interface SessionTask {
    /**
     * Describes the type of work the task performs.
     */
    fun kind(): TaskKind

    /**
     * Executes the task until completion or cancellation.
     *
     * Implementations typically stream protocol events using session and ctx,
     * returning an optional final agent message when finished. The provided
     * cancellationToken is cancelled when the session requests an abort;
     * implementers should watch for it and terminate quickly once it fires.
     *
     * @return Optional final agent message
     */
    suspend fun run(
        sessionContext: SessionTaskContext,
        turnContext: TurnContext,
        input: List<UserInput>,
        cancellationToken: CancellationToken
    ): String?

    /**
     * Gives the task a chance to perform cleanup after an abort.
     *
     * The default implementation is a no-op; override this if additional
     * teardown or notifications are required once abort_all_tasks cancels the task.
     */
    suspend fun abort(sessionContext: SessionTaskContext, turnContext: TurnContext) {
        // Default no-op
    }
}

/**
 * Thin wrapper that exposes the parts of Session task runners need.
 *
 * Ported from Rust codex-rs/core/src/tasks/mod.rs SessionTaskContext
 */
class SessionTaskContext(
    private val session: Session
) {
    fun getSession(): Session = session

    /**
     * Get the auth manager for API authentication.
     * Ported from Rust codex-rs/core/src/tasks/mod.rs SessionTaskContext::auth_manager
     */
    fun authManager(): AuthManager = session.services.authManager
}

/**
 * User input for a turn.
 *
 * Ported from Rust codex-rs/protocol/src/user_input.rs
 */
sealed class UserInput {
    /**
     * Text input from the user.
     */
    data class Text(val content: String) : UserInput()

    /**
     * Image input from the user.
     */
    data class Image(
        val data: ByteArray,
        val mimeType: String
    ) : UserInput() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Image) return false
            return data.contentEquals(other.data) && mimeType == other.mimeType
        }

        override fun hashCode(): Int {
            var result = data.contentHashCode()
            result = 31 * result + mimeType.hashCode()
            return result
        }
    }

    /**
     * File reference input.
     */
    data class FileRef(val path: String) : UserInput()
}

// TurnAbortReason is imported from ai.solace.coder.protocol

// =============================================================================
// ActiveTurnHolder - Mutex wrapper for nullable ActiveTurn
// Ported from Rust codex-rs/core/src/codex.rs active_turn: Mutex<Option<ActiveTurn>>
// =============================================================================

/**
 * Thread-safe holder for the current active turn.
 * Provides mutex-protected access to the nullable ActiveTurn.
 */
class ActiveTurnHolder {
    private val mutex = Mutex()
    private var value: ActiveTurn? = null

    /**
     * Execute a block while holding the lock, passing the current turn.
     */
    suspend fun <T> withLock(block: suspend (ActiveTurn?) -> T): T {
        return mutex.withLock { block(value) }
    }

    /**
     * Get the current active turn (requires lock).
     */
    suspend fun get(): ActiveTurn? = mutex.withLock { value }

    /**
     * Set the current active turn (requires lock).
     */
    suspend fun set(turn: ActiveTurn?) {
        mutex.withLock { value = turn }
    }
}

// =============================================================================
// SessionServices - Services available to the session
// Ported from Rust codex-rs/core/src/state/service.rs
// =============================================================================

/**
 * Services available during a session.
 *
 * Ported from Rust codex-rs/core/src/state/service.rs SessionServices
 */
data class SessionServices(
    val mcpConnectionManager: ai.solace.coder.mcp.connection.McpConnectionManager,
    val mcpStartupCancellationToken: CancellationToken,
    val unifiedExecManager: UnifiedExecSessionManager,
    val notifier: UserNotifier,
    val rollout: RolloutRecorder?,
    val userShell: ai.solace.coder.exec.shell.Shell,
    val showRawAgentReasoning: Boolean,
    val authManager: AuthManager,
    val toolApprovals: ApprovalStore
)

/**
 * Stores tool approval decisions.
 * Ported from Rust codex-rs/core/src/tools/sandboxing.rs ApprovalStore
 */
class ApprovalStore {
    private val approvals = mutableMapOf<String, Boolean>()

    fun isApproved(key: String): Boolean? = approvals[key]

    fun setApproval(key: String, approved: Boolean) {
        approvals[key] = approved
    }

    fun clear() {
        approvals.clear()
    }
}

/**
 * Manages unified exec sessions.
 * Ported from Rust codex-rs/core/src/unified_exec.rs
 */
class UnifiedExecSessionManager {
    suspend fun terminateAllSessions() {
        // TODO: Implement session termination
    }
}

/**
 * User notification service.
 * Ported from Rust codex-rs/core/src/user_notification.rs
 */
class UserNotifier(private val config: NotifyConfig?) {
    fun notify(notification: UserNotification) {
        // TODO: Implement notification
    }
}

/**
 * Notification configuration.
 */
data class NotifyConfig(
    val enabled: Boolean = true
)

/**
 * User notification types.
 */
sealed class UserNotification {
    data class AgentTurnComplete(
        val threadId: String,
        val turnId: String,
        val cwd: String,
        val inputMessages: List<String>,
        val lastAssistantMessage: String?
    ) : UserNotification()
}

/**
 * Rollout recorder for session history.
 * Ported from Rust codex-rs/core/src/rollout.rs
 */
class RolloutRecorder private constructor(
    val rolloutPath: String
) {
    suspend fun recordItems(items: List<ai.solace.coder.protocol.RolloutItem>) {
        // TODO: Implement rollout recording
    }

    suspend fun flush() {
        // TODO: Implement flush
    }

    suspend fun shutdown() {
        // TODO: Implement shutdown
    }

    companion object {
        fun new(config: Any, conversationId: String): RolloutRecorder? {
            // TODO: Implement rollout recorder creation
            return RolloutRecorder("/tmp/rollout-$conversationId.jsonl")
        }
    }
}
