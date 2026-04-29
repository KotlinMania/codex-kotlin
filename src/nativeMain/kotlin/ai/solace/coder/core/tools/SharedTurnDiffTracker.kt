// port-lint: source core/src/tools/context.rs
package ai.solace.coder.core.tools

import ai.solace.coder.core.session.TurnDiffTracker
import ai.solace.coder.core.session.ChangedFile
import ai.solace.coder.protocol.FileChange

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

