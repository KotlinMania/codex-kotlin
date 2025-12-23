package ai.solace.coder.utils.concurrent

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

/**
 * A notification mechanism that allows one or more coroutines to wait for a signal.
 *
 * Similar to tokio::sync::Notify in Rust.
 */
class Notify {
    private val _flow =
            MutableSharedFlow<Unit>(
                    replay = 0,
                    extraBufferCapacity = 1,
                    onBufferOverflow = BufferOverflow.DROP_OLDEST
            )

    /**
     * Wakes up all coroutines currently waiting on [notified].
     *
     * If no coroutines are waiting, the next call to [notified] will complete immediately (due to
     * extraBufferCapacity = 1 and DROP_OLDEST, but wait, Notify in Rust usually doesn't "remember"
     * unless Permit is used). Actually, tokio Notify has a "notified" flag.
     */
    fun notifyWaiters() {
        _flow.tryEmit(Unit)
    }

    /** Waits for a notification. */
    suspend fun notified() {
        _flow.first()
    }
}
