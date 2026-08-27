// port-lint: source core/src/state/turn.rs
package io.github.kotlinmania.codex.core.session

import io.github.kotlinmania.codex.client.auth.AuthManager
import io.github.kotlinmania.codex.protocol.ReviewDecision
import io.github.kotlinmania.codex.utils.concurrent.CancellationToken
import io.github.kotlinmania.codex.protocol.TurnAbortReason
import io.github.kotlinmania.codex.protocol.ResponseInputItem
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
internal class ActiveTurn {
    private val tasks = linkedMapOf<String, RunningTask>()
    val turnState = TurnState()

    fun addTask(task: RunningTask) {
        val subId = task.turnContext.subId
        tasks[subId] = task
    }

    /**
     * Remove a task by subId. Returns true if no tasks remain.
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
internal data class RunningTask(
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
internal class TurnState {
    private val mutex = Mutex()
    private val pendingApprovals = linkedMapOf<String, CompletableDeferred<ReviewDecision>>()
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
 * User input items for a turn.
 *
 * Ported from Rust codex-rs/core/src/state/turn.rs
 */
sealed class UserInput {
    data class Text(val content: String) : UserInput()
    data class Image(val mimeType: String, val data: String = "") : UserInput()
    data class FileRef(val path: String) : UserInput()
}

internal class SessionTaskContext(private val session: Session) {
    fun getSession(): Session = session
}

/**
 * Async task that drives a Session turn.
 *
 * Ported from Rust codex-rs/core/src/state/turn.rs
 */
internal interface SessionTask {
    fun kind(): TaskKind
    suspend fun abort(sessionContext: SessionTaskContext, turnContext: TurnContext) {}
    suspend fun run(
        sessionContext: SessionTaskContext,
        turnContext: TurnContext,
        input: List<UserInput>,
        cancellationToken: CancellationToken
    ): String?
}
