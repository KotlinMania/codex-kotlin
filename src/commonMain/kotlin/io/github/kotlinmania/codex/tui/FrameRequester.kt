// port-lint: source tui/src/tui/frame_requester.rs
package io.github.kotlinmania.codex.tui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

/**
 * A requester for scheduling future frame draws on the TUI event loop.
 *
 * Instances of this class can be shared across coroutines to trigger frame draws from anywhere in
 * the TUI code.
 */
internal class FrameRequester private constructor(
    private val frameScheduleTx: SendChannel<Instant>,
) {
    companion object {
        /** Create a new requester and launch its associated scheduler coroutine. */
        fun new(drawTx: MutableSharedFlow<Unit>, scope: CoroutineScope): FrameRequester {
            val channel = Channel<Instant>(Channel.UNLIMITED)
            val scheduler = FrameScheduler(channel, drawTx)
            scope.launch { scheduler.run() }
            return FrameRequester(channel)
        }

        /** Create a no-op frame requester for tests. */
        fun testDummy(): FrameRequester {
            return FrameRequester(Channel(Channel.UNLIMITED))
        }
    }

    /** Schedule a frame draw as soon as possible. */
    fun scheduleFrame() {
        frameScheduleTx.trySend(Clock.System.now())
    }

    /** Schedule a frame draw to occur after the specified duration. */
    fun scheduleFrameIn(duration: Duration) {
        frameScheduleTx.trySend(Clock.System.now() + duration)
    }
}

/**
 * Coalesces frame draw requests and notifies the TUI event loop.
 *
 * Draw notifications are clamped to a maximum of 120 FPS by [FrameRateLimiter].
 */
internal class FrameScheduler(
    private val receiver: ReceiveChannel<Instant>,
    private val drawTx: MutableSharedFlow<Unit>,
    private val clock: () -> Instant = { Clock.System.now() },
) {
    private val rateLimiter = FrameRateLimiter()

    /** Run the scheduling loop until the request channel is closed. */
    suspend fun run() {
        var nextDeadline: Instant? = null

        while (true) {
            val deadline = nextDeadline
            if (deadline == null) {
                nextDeadline = rateLimiter.clampDeadline(receiver.receiveCatching().getOrNull() ?: return)
                continue
            }

            val delayDuration = deadline - clock()
            if (delayDuration <= Duration.ZERO) {
                nextDeadline = null
                rateLimiter.markEmitted(deadline)
                drawTx.tryEmit(Unit)
                continue
            }

            when (val receiveResult = receiveUntil(delayDuration)) {
                ScheduledReceive.Closed -> return
                ScheduledReceive.Timeout -> {
                    nextDeadline = null
                    rateLimiter.markEmitted(deadline)
                    drawTx.tryEmit(Unit)
                }
                is ScheduledReceive.Value -> {
                    val clamped = rateLimiter.clampDeadline(receiveResult.instant)
                    nextDeadline = minOf(deadline, clamped)
                }
            }
        }
    }

    private suspend fun receiveUntil(delayDuration: Duration): ScheduledReceive {
        return withTimeoutOrNull(delayDuration) {
            receiver.receiveCatching().getOrNull()?.let { ScheduledReceive.Value(it) }
                ?: ScheduledReceive.Closed
        } ?: ScheduledReceive.Timeout
    }
}

internal sealed interface ScheduledReceive {
    data class Value(val instant: Instant) : ScheduledReceive

    data object Timeout : ScheduledReceive

    data object Closed : ScheduledReceive
}
