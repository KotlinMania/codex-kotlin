// port-lint: source core/src/tools/context.rs
package io.github.kotlinmania.codex.core.tools

import io.github.kotlinmania.codex.core.session.TurnDiffTracker
import io.github.kotlinmania.codex.core.session.ChangedFile
import io.github.kotlinmania.codex.protocol.FileChange

/**
 * Thread-safe wrapper for TurnDiffTracker that can be shared across tasks.
 */
class SharedTurnDiffTracker {
    private val tracker = TurnDiffTracker()

    suspend fun onPatchBegin(changes: Map<String, FileChange>) {
        tracker.onPatchBegin(changes)
    }

    suspend fun computeUnifiedDiff(): String {
        return tracker.computeUnifiedDiff()
    }

    suspend fun getChangedFiles(): List<ChangedFile> {
        return tracker.getChangedFiles()
    }

    suspend fun clear() {
        tracker.clear()
    }

    suspend fun hasChanges(): Boolean {
        return tracker.hasChanges()
    }
}

