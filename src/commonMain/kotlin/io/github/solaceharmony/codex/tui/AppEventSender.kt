// port-lint: source tui/src/app_event_sender.rs
package io.github.solaceharmony.codex.tui

import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.channels.SendChannel

/**
 * Convenience sender for app events.
 *
 * This wraps the raw channel so call sites can submit typed app events without duplicating
 * event-channel handling.
 */
internal class AppEventSender(
    val appEventTx: SendChannel<AppEvent>,
) {
    /** Send an event to the app event channel, ignoring closed-channel sends. */
    suspend fun send(event: AppEvent) {
        try {
            appEventTx.send(event)
        } catch (_: ClosedSendChannelException) {
        }
    }
}
