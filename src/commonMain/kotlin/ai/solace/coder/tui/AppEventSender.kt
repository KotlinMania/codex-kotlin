// port-lint: source tui/src/app_event_sender.rs
package ai.solace.coder.tui

import kotlinx.coroutines.channels.SendChannel

/**
 * Helper to send app events.
 *
 * Ported from Rust codex-rs/tui/src/app_event_sender.rs
 */
internal class AppEventSender(
    val appEventTx: SendChannel<AppEvent>
) {
    /**
     * Send an event to the app event channel. If it fails, we swallow the
     * error and log it.
     */
    suspend fun send(event: AppEvent) {
        // Record inbound events for high-fidelity session replay.
        // Avoid double-logging Ops; those are logged at the point of submission.
        if (event !is AppEvent.CodexOp) {
            // TODO: session_log::log_inbound_app_event(&event);
        }
        try {
            appEventTx.send(event)
        } catch (e: Exception) {
            // println("failed to send event: $e")
        }
    }
}
