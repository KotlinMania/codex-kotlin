// port-lint: source core/src/turn_diff_tracker.rs
package ai.solace.coder.core.session

import ai.solace.coder.protocol.FileChange
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Thread-safe wrapper for TurnDiffTracker that can be shared across tasks.
 *
 * Wraps the common [TurnDiffTracker] with a mutex for coroutine-safe access.
 *
 * Ported from Rust codex-rs/core/src/tools/context.rs SharedTurnDiffTracker
 */
actual class SharedTurnDiffTracker actual constructor() {
    private val mutex = Mutex()
    private val inner = TurnDiffTracker()

    /**
     * Front-run apply patch calls to track the starting contents of any modified files.
     */
    actual suspend fun onPatchBegin(changes: Map<String, FileChange>) {
        mutex.withLock {
            inner.onPatchBegin(changes)
        }
    }

    /**
     * Recompute the aggregated unified diff.
     */
    actual suspend fun computeUnifiedDiff(): String {
        return mutex.withLock {
            inner.getUnifiedDiff() ?: ""
        }
    }

    /**
     * Get a list of changed files with their change types.
     */
    actual suspend fun getChangedFiles(): List<ChangedFile> {
        return mutex.withLock {
            inner.getChangedFiles().map { tracked ->
                ChangedFile(
                    path = tracked.path,
                    originalPath = tracked.originalPath,
                    changeType = when (tracked.changeType) {
                        TrackedChangeType.Added -> ChangeType.Added
                        TrackedChangeType.Modified -> ChangeType.Modified
                        TrackedChangeType.Deleted -> ChangeType.Deleted
                        TrackedChangeType.Renamed -> ChangeType.Renamed
                    }
                )
            }
        }
    }

    /**
     * Clear all tracked state, resetting the tracker to its initial empty state.
     */
    actual suspend fun clear() {
        mutex.withLock {
            inner.clear()
        }
    }

    /**
     * Check whether the tracker currently has any tracked baselines.
     */
    actual suspend fun hasChanges(): Boolean {
        return mutex.withLock {
            inner.hasChanges()
        }
    }
}

/**
 * Information about a changed file.
 */
actual class ChangedFile(
    actual val path: String,
    actual val originalPath: String?,
    actual val changeType: ChangeType,
)

/**
 * Type of file change.
 */
actual enum class ChangeType {
    Added,
    Modified,
    Deleted,
    Renamed,
}
