// port-lint: source core/src/turn_diff_tracker.rs
package io.github.kotlinmania.codex.core.session

import io.github.kotlinmania.codex.protocol.FileChange

/**
 * Thread-safe wrapper for TurnDiffTracker that can be shared across tasks.
 *
 * This is the expect declaration - actual implementation is in nativeMain.
 *
 * Ported from Rust codex-rs/core/src/tools/context.rs SharedTurnDiffTracker
 */
expect class SharedTurnDiffTracker() {
    suspend fun onPatchBegin(changes: Map<String, FileChange>)
    suspend fun computeUnifiedDiff(): String
    suspend fun getChangedFiles(): List<ChangedFile>
    suspend fun clear()
    suspend fun hasChanges(): Boolean
}

/**
 * Information about a changed file.
 */
expect class ChangedFile {
    val path: String
    val originalPath: String?
    val changeType: ChangeType
}

/**
 * Type of file change.
 */
expect enum class ChangeType {
    Added,
    Modified,
    Deleted,
    Renamed
}
