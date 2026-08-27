// port-lint: source tui/src/app_event.rs
package io.github.kotlinmania.codex.tui

import io.github.kotlinmania.codex.protocol.AskForApproval
import io.github.kotlinmania.codex.protocol.ConversationPathResponseEvent
import io.github.kotlinmania.codex.protocol.Event
import io.github.kotlinmania.codex.protocol.HistoryEntry
import io.github.kotlinmania.codex.protocol.Op
import io.github.kotlinmania.codex.protocol.RateLimitSnapshot
import io.github.kotlinmania.codex.protocol.ReasoningEffort
import io.github.kotlinmania.codex.protocol.SandboxPolicy
import kotlinx.io.files.Path

/**
 * Application-level events used to coordinate UI actions.
 *
 * `AppEvent` is the internal message bus between UI components and the top-level app loop.
 * Widgets emit events to request actions that must be handled at the app layer, such as opening
 * pickers, persisting configuration, or shutting down the agent.
 */
internal sealed class AppEvent {
    data class CodexEvent(val event: Event) : AppEvent()

    /** Start a new session. */
    data object NewSession : AppEvent()

    /** Clear the terminal UI and start a fresh session. */
    data object ClearUi : AppEvent()

    /** Clear the current context, start a fresh session, and submit an initial user message. */
    data class ClearUiAndSubmitUserMessage(val text: String) : AppEvent()

    /** Open the resume picker inside the running TUI session. */
    data object OpenResumePicker : AppEvent()

    /** Resume a thread by UUID or thread name inside the running TUI session. */
    data class ResumeSessionByIdOrName(val value: String) : AppEvent()

    /** Fork the current session into a new thread. */
    data object ForkCurrentSession : AppEvent()

    /** Request application exit using the requested shutdown behavior. */
    data class Exit(val mode: ExitMode) : AppEvent()

    /** Request account logout, then exit after it succeeds. */
    data object Logout : AppEvent()

    /** Request application exit due to a fatal error. */
    data class FatalExitRequest(val message: String) : AppEvent()

    /** Forward an operation to the agent without passing channels through widget layers. */
    data class CodexOp(val op: Op) : AppEvent()

    /** Kick off an asynchronous file search for the given query. */
    data class StartFileSearch(val query: String) : AppEvent()

    /** Refresh account rate limits in the background. */
    data class RefreshRateLimits(val origin: RateLimitRefreshOrigin) : AppEvent()

    /** Result of refreshing account rate limits. */
    data class RateLimitsLoaded(
        val origin: RateLimitRefreshOrigin,
        val result: Result<List<RateLimitSnapshot>>,
    ) : AppEvent()

    /** Result of refreshing a single rate-limit snapshot. */
    data class RateLimitSnapshotFetched(val snapshot: RateLimitSnapshot) : AppEvent()

    /** Result of computing a diff command. */
    data class DiffResult(val diff: String) : AppEvent()

    data object StartCommitAnimation : AppEvent()

    data object StopCommitAnimation : AppEvent()

    data object CommitTick : AppEvent()

    /** Update the current reasoning effort in the running app and widget. */
    data class UpdateReasoningEffort(val effort: ReasoningEffort?) : AppEvent()

    /** Update the current model slug in the running app and widget. */
    data class UpdateModel(val model: String) : AppEvent()

    /** Persist the selected model and reasoning effort to the appropriate config. */
    data class PersistModelSelection(
        val model: String,
        val effort: ReasoningEffort?,
    ) : AppEvent()

    /** Open the reasoning selection popup after picking a model. */
    data object OpenReasoningPopup : AppEvent()

    /** Open the confirmation prompt before enabling full access mode. */
    data object OpenFullAccessConfirmation : AppEvent()

    /** Open the Windows world-writable directories warning. */
    data class OpenWorldWritableWarningConfirmation(
        val samplePaths: List<String>,
        val extraCount: Int,
        val failedScan: Boolean,
    ) : AppEvent()

    data object OpenWindowsSandboxEnablePrompt : AppEvent()

    /** Enable the Windows sandbox feature and switch to agent mode. */
    data object EnableWindowsSandboxForAgentMode : AppEvent()

    /** Update the current approval policy in the running app and widget. */
    data class UpdateAskForApprovalPolicy(val policy: AskForApproval) : AppEvent()

    /** Update the current sandbox policy in the running app and widget. */
    data class UpdateSandboxPolicy(val policy: SandboxPolicy) : AppEvent()

    /** Update whether the full access warning prompt has been acknowledged. */
    data class UpdateFullAccessWarningAcknowledged(val acknowledged: Boolean) : AppEvent()

    /** Update whether the world-writable directories warning has been acknowledged. */
    data class UpdateWorldWritableWarningAcknowledged(val acknowledged: Boolean) : AppEvent()

    /** Update whether the rate limit switch prompt has been hidden for the session. */
    data class UpdateRateLimitSwitchPromptHidden(val hidden: Boolean) : AppEvent()

    /** Persist the acknowledgement flag for the full access warning prompt. */
    data object PersistFullAccessWarningAcknowledged : AppEvent()

    /** Persist the acknowledgement flag for the world-writable directories warning. */
    data object PersistWorldWritableWarningAcknowledged : AppEvent()

    /** Persist the acknowledgement flag for the rate limit switch prompt. */
    data object PersistRateLimitSwitchPromptHidden : AppEvent()

    /** Persist the acknowledgement flag for the model migration prompt. */
    data class PersistModelMigrationPromptAcknowledged(
        val fromModel: String,
        val toModel: String,
    ) : AppEvent()

    /** Skip the next world-writable scan after a user-confirmed continue. */
    data object SkipNextWorldWritableScan : AppEvent()

    /** Re-open the approval presets popup. */
    data object OpenApprovalsPopup : AppEvent()

    /** Forwarded conversation history snapshot from the current conversation. */
    data class ConversationHistory(val event: ConversationPathResponseEvent) : AppEvent()

    /** Open the branch picker option from the review popup. */
    data class OpenReviewBranchPicker(val path: Path) : AppEvent()

    /** Open the commit picker option from the review popup. */
    data class OpenReviewCommitPicker(val path: Path) : AppEvent()

    /** Open the custom prompt option from the review popup. */
    data object OpenReviewCustomPrompt : AppEvent()

    /** Open the approval popup. */
    data object FullScreenApprovalRequest : AppEvent()

    /** Open the feedback note entry overlay after the user selects a category. */
    data class OpenFeedbackNote(
        val category: FeedbackCategory,
        val includeLogs: Boolean,
    ) : AppEvent()

    /** Open the upload consent popup for feedback after selecting a category. */
    data class OpenFeedbackConsent(
        val category: FeedbackCategory,
    ) : AppEvent()

    /** Open the device picker for a realtime microphone or speaker. */
    data class OpenRealtimeAudioDeviceSelection(val kind: RealtimeAudioDeviceKind) : AppEvent()

    /** Persist the selected realtime microphone or speaker to top-level config. */
    data class PersistRealtimeAudioDeviceSelection(
        val kind: RealtimeAudioDeviceKind,
        val name: String?,
    ) : AppEvent()

    /** Restart the selected realtime microphone or speaker locally. */
    data class RestartRealtimeAudioDevice(val kind: RealtimeAudioDeviceKind) : AppEvent()

    /** Deliver a history lookup response to the UI layer. */
    data class HistoryLookup(val event: HistoryLookupResponse) : AppEvent()

    /** Open set/remove actions for the selected keymap action. */
    data class OpenKeymapActionMenu(
        val context: String,
        val action: String,
    ) : AppEvent()

    /** Open binding selection before replacing one binding for an action. */
    data class OpenKeymapReplaceBindingMenu(
        val context: String,
        val action: String,
    ) : AppEvent()

    /** Open key capture for the selected keymap action. */
    data class OpenKeymapCapture(
        val context: String,
        val action: String,
        val intent: KeymapEditIntent,
    ) : AppEvent()

    /** Open the keymap keypress inspector. */
    data object OpenKeymapDebug : AppEvent()

    /** Apply a captured key to the selected keymap action. */
    data class KeymapCaptured(
        val context: String,
        val action: String,
        val key: String,
        val intent: KeymapEditIntent,
    ) : AppEvent()

    /** Remove the custom root binding for the selected keymap action. */
    data class KeymapCleared(
        val context: String,
        val action: String,
    ) : AppEvent()

    /** Launch the external editor after a normal draw has completed. */
    data object LaunchExternalEditor : AppEvent()
}

internal enum class RealtimeAudioDeviceKind {
    Microphone,
    Speaker;

    fun title(): String =
        when (this) {
            Microphone -> "Microphone"
            Speaker -> "Speaker"
        }

    fun noun(): String =
        when (this) {
            Microphone -> "microphone"
            Speaker -> "speaker"
        }
}

internal enum class ThreadGoalSetMode {
    ConfirmIfExists,
    ReplaceExisting,
}

internal data class HistoryLookupResponse(
    val offset: Int,
    val logId: Long,
    val entry: HistoryEntry?,
)

internal enum class WindowsSandboxEnableMode {
    Elevated,
    Legacy,
}

internal sealed class RateLimitRefreshOrigin {
    data object StartupPrefetch : RateLimitRefreshOrigin()

    data class StatusCommand(val requestId: Long) : RateLimitRefreshOrigin()
}

internal sealed class KeymapEditIntent {
    data object ReplaceAll : KeymapEditIntent()

    data object AddAlternate : KeymapEditIntent()

    data class ReplaceOne(val oldKey: String) : KeymapEditIntent()
}

/** The exit strategy requested by the UI layer. */
internal enum class ExitMode {
    ShutdownFirst,
    Immediate,
}

/** Feedback category selected by the user before optional note and upload consent flows. */
internal enum class FeedbackCategory {
    BadResult,
    GoodResult,
    Bug,
    SafetyCheck,
    Other,
}
