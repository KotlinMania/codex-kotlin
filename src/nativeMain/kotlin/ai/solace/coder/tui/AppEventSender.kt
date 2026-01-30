// port-lint: source tui/src/app_event_sender.rs
package ai.solace.coder.tui

import kotlinx.coroutines.channels.Channel

/**
 * Wrapper for sending app events to the main event channel.
 * 
 * This class wraps an unbounded channel sender and provides error handling
 * for failed sends. It also integrates with session logging for event replay.
 *
 * Ported from Rust codex-rs/tui/src/app_event_sender.rs
 */
internal data class AppEventSender(
    val appEventTx: Channel<AppEvent>
) {
    /**
     * Send an event to the app event channel. If it fails, we swallow the
     * error and log it.
     * 
     * Records inbound events for high-fidelity session replay, except for
     * CodexOp events which are logged at the point of submission.
     */
    fun send(event: AppEvent) {
        // Record inbound events for high-fidelity session replay.
        // Avoid double-logging Ops; those are logged at the point of submission.
        if (event !is AppEvent.CodexOp) {
            // TODO: Port session_log module
            // sessionLog.logInboundAppEvent(event)
        }
        
        // Try to send the event; log error if channel is closed
        val result = appEventTx.trySend(event)
        if (result.isFailure) {
            // TODO: Use proper logging framework (tracing equivalent)
            println("ERROR: failed to send event: ${result.exceptionOrNull()?.message}")
        }
    }
}
