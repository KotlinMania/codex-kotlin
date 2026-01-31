// port-lint: source tui/src/app_event.rs
package ai.solace.coder.tui

import ai.solace.coder.protocol.ConversationPathResponseEvent
import ai.solace.coder.protocol.Event
import ai.solace.coder.protocol.RateLimitSnapshot
import ai.solace.coder.protocol.Op
import ai.solace.coder.protocol.AskForApproval
import ai.solace.coder.protocol.SandboxPolicy
import ai.solace.coder.protocol.ReasoningEffort
// import ai.solace.coder.file_search.FileMatch
// import ai.solace.coder.common.approval_presets.ApprovalPreset
// import ai.solace.coder.common.model_presets.ModelPreset
// import ai.solace.coder.tui.bottom_pane.ApprovalRequest
// import ai.solace.coder.tui.history_cell.HistoryCell
import okio.Path

/**
 * App events for the TUI.
 *
 * Ported from Rust codex-rs/tui/src/app_event.rs
 */
sealed class AppEvent {
    data class CodexEvent(val event: Event) : AppEvent()

    /** Start a new session. */
    object NewSession : AppEvent()

    /** Request to exit the application gracefully. */
    object ExitRequest : AppEvent()

    /**
     * Forward an `Op` to the Agent. Using an `AppEvent` for this avoids
     * bubbling channels through layers of widgets.
     */
    data class CodexOp(val op: Op) : AppEvent()

    /**
     * Kick off an asynchronous file search for the given query (text after
     * the `@`). Previous searches may be cancelled by the app layer so there
     * is at most one in-flight search.
     */
    data class StartFileSearch(val query: String) : AppEvent()

    /**
     * Result of a completed asynchronous file search. The `query` echoes the
     * original search term so the UI can decide whether the results are
     * still relevant.
     */
    data class FileSearchResult(
        val query: String,
        // val matches: List<FileMatch>
    ) : AppEvent()

    /** Result of refreshing rate limits */
    data class RateLimitSnapshotFetched(val snapshot: RateLimitSnapshot) : AppEvent()

    /** Result of computing a `/diff` command. */
    data class DiffResult(val diff: String) : AppEvent()

    // data class InsertHistoryCell(val cell: HistoryCell) : AppEvent()

    object StartCommitAnimation : AppEvent()
    object StopCommitAnimation : AppEvent()
    object CommitTick : AppEvent()

    /** Update the current reasoning effort in the running app and widget. */
    data class UpdateReasoningEffort(val effort: ReasoningEffort?) : AppEvent()

    /** Update the current model slug in the running app and widget. */
    data class UpdateModel(val model: String) : AppEvent()

    /** Persist the selected model and reasoning effort to the appropriate config. */
    data class PersistModelSelection(
        val model: String,
        val effort: ReasoningEffort?
    ) : AppEvent()

    /** Open the reasoning selection popup after picking a model. */
    object OpenReasoningPopup : AppEvent()

    /** Open the confirmation prompt before enabling full access mode. */
    object OpenFullAccessConfirmation : AppEvent()

    /**
     * Open the Windows world-writable directories warning.
     * If `preset` is `Some`, the confirmation will apply the provided
     * approval/sandbox configuration on Continue; if `None`, it performs no
     * policy change and only acknowledges/dismisses the warning.
     */
    data class OpenWorldWritableWarningConfirmation(
        // val preset: ApprovalPreset?,
        /** Up to 3 sample world-writable directories to display in the warning. */
        val samplePaths: List<String>,
        /** If there are more than `sample_paths`, this carries the remaining count. */
        val extraCount: Long,
        /** True when the scan failed (e.g. ACL query error) and protections could not be verified. */
        val failedScan: Boolean
    ) : AppEvent()

    /** Prompt to enable the Windows sandbox feature before using Agent mode. */
    object OpenWindowsSandboxEnablePrompt : AppEvent()

    /** Enable the Windows sandbox feature and switch to Agent mode. */
    object EnableWindowsSandboxForAgentMode : AppEvent()

    /** Update the current approval policy in the running app and widget. */
    data class UpdateAskForApprovalPolicy(val policy: AskForApproval) : AppEvent()

    /** Update the current sandbox policy in the running app and widget. */
    data class UpdateSandboxPolicy(val policy: SandboxPolicy) : AppEvent()

    /** Update whether the full access warning prompt has been acknowledged. */
    data class UpdateFullAccessWarningAcknowledged(val acknowledged: Boolean) : AppEvent()

    /** Update whether the world-writable directories warning has been acknowledged. */
    data class UpdateWorldWritableWarningAcknowledged(val acknowledged: Boolean) : AppEvent()

    /** Update whether the rate limit switch prompt has been acknowledged for the session. */
    data class UpdateRateLimitSwitchPromptHidden(val hidden: Boolean) : AppEvent()

    /** Persist the acknowledgement flag for the full access warning prompt. */
    object PersistFullAccessWarningAcknowledged : AppEvent()

    /** Persist the acknowledgement flag for the world-writable directories warning. */
    object PersistWorldWritableWarningAcknowledged : AppEvent()

    /** Persist the acknowledgement flag for the rate limit switch prompt. */
    object PersistRateLimitSwitchPromptHidden : AppEvent()

    /** Persist the acknowledgement flag for the model migration prompt. */
    data class PersistModelMigrationPromptAcknowledged(
        val migrationConfig: String
    ) : AppEvent()

    /** Skip the next world-writable scan (one-shot) after a user-confirmed continue. */
    object SkipNextWorldWritableScan : AppEvent()

    /** Re-open the approval presets popup. */
    object OpenApprovalsPopup : AppEvent()

    /** Forwarded conversation history snapshot from the current conversation. */
    data class ConversationHistory(val event: ConversationPathResponseEvent) : AppEvent()

    /** Open the branch picker option from the review popup. */
    data class OpenReviewBranchPicker(val path: Path) : AppEvent()

    /** Open the commit picker option from the review popup. */
    data class OpenReviewCommitPicker(val path: Path) : AppEvent()

    /** Open the custom prompt option from the review popup. */
    object OpenReviewCustomPrompt : AppEvent()

    /** Open the approval popup. */
    object FullScreenApprovalRequest : AppEvent()

    /** Open the feedback note entry overlay after the user selects a category. */
    data class OpenFeedbackNote(
        val category: FeedbackCategory,
        val includeLogs: Boolean
    ) : AppEvent()

    /** Open the upload consent popup for feedback after selecting a category. */
    data class OpenFeedbackConsent(
        val category: FeedbackCategory
    ) : AppEvent()
}

enum class FeedbackCategory {
    BadResult,
    GoodResult,
    Bug,
    Other
}
