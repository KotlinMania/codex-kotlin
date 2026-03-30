// port-lint: source tui/src/tui/frame_requester.rs
package ai.solace.coder.tui

/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * Copyright (c) 2025 Sydney Renee, The Solace Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Frame draw scheduling utilities for the TUI.
 *
 * This module exposes [FrameRequester], a lightweight handle that widgets and
 * background tasks can clone to request future redraws of the TUI.
 *
 * Internally it spawns a [FrameScheduler] task that coalesces many requests
 * into a single notification on a shared flow used by the main TUI event
 * loop. This keeps animations and status updates smooth without redrawing more
 * often than necessary.
 *
 * This follows the actor-style design from
 * ["Actors with Tokio"](https://ryhl.io/blog/actors-with-tokio/), with a
 * dedicated scheduler task and lightweight request handles.
 */

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

/**
 * A requester for scheduling future frame draws on the TUI event loop.
 *
 * This is the handler side of an actor/handler pair with [FrameScheduler], which coalesces
 * multiple frame requests into a single draw operation.
 *
 * Instances of this class can be freely shared across coroutines to make it possible to trigger
 * frame draws from anywhere in the TUI code.
 */
class FrameRequester(
    private val frameScheduleTx: Channel<Instant>,
) {
    companion object {
        /**
         * Create a new [FrameRequester] and spawn its associated [FrameScheduler] task.
         *
         * The provided [drawTx] is used to notify the TUI event loop of scheduled draws.
         */
        fun new(drawTx: MutableSharedFlow<Unit>, scope: CoroutineScope): FrameRequester {
            val channel = Channel<Instant>(Channel.UNLIMITED)
            val scheduler = FrameScheduler(channel, drawTx)
            scope.launch { scheduler.run() }
            return FrameRequester(channel)
        }

        /**
         * Create a no-op frame requester for tests.
         */
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
 * A scheduler for coalescing frame draw requests and notifying the TUI event loop.
 *
 * This type is internal to [FrameRequester] and is spawned as a coroutine to handle
 * scheduling logic.
 *
 * To avoid wasted redraw work, draw notifications are clamped to a maximum of 60 FPS
 * (see [FrameRateLimiter]).
 */
internal class FrameScheduler(
    private val receiver: Channel<Instant>,
    private val drawTx: MutableSharedFlow<Unit>,
) {
    private val rateLimiter = FrameRateLimiter()

    /**
     * Run the scheduling loop, coalescing frame requests and notifying the TUI event loop.
     *
     * This method runs indefinitely until all senders are dropped. A single draw notification
     * is sent for multiple requests scheduled before the next draw deadline.
     */
    suspend fun run() {
        var nextDeadline: Instant? = null

        while (true) {
            val now = Clock.System.now()
            val target = nextDeadline ?: (now + 365.days)
            val delayDuration = target - now

            if (delayDuration.isPositive()) {
                val result = select<Boolean> {
                    receiver.onReceive { drawAt ->
                        val clamped = rateLimiter.clampDeadline(drawAt)
                        nextDeadline = nextDeadline?.let { minOf(it, clamped) } ?: clamped
                        // Do not send a draw immediately here. By continuing the loop,
                        // we recompute the sleep target so the draw fires once via the
                        // timeout branch, coalescing multiple requests into a single draw.
                        true // continue
                    }
                    onTimeout(delayDuration.inWholeMilliseconds.coerceAtLeast(1)) {
                        if (nextDeadline != null) {
                            nextDeadline = null
                            rateLimiter.markEmitted(target)
                            drawTx.tryEmit(Unit)
                        }
                        true // continue
                    }
                }
                if (!result) break
            } else {
                // Deadline already passed
                if (nextDeadline != null) {
                    nextDeadline = null
                    rateLimiter.markEmitted(target)
                    drawTx.tryEmit(Unit)
                }
            }
        }
    }
}
