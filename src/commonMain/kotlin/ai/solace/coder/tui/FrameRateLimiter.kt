// port-lint: source tui/src/tui/frame_rate_limiter.rs
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
 * Limits how frequently frame draw notifications may be emitted.
 *
 * Widgets sometimes call [FrameRequester.scheduleFrame] more frequently than a user can
 * perceive. This limiter clamps draw notifications to a maximum of 60 FPS to avoid wasted work.
 *
 * This is intentionally a small, pure helper so it can be unit-tested in isolation and used by
 * the async frame scheduler without adding complexity to the app/event loop.
 */

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

/** A 60 FPS minimum frame interval (approximately 16.67ms). */
internal val MIN_FRAME_INTERVAL: Duration = 16_666_667.nanoseconds

/**
 * Remembers the most recent emitted draw, allowing deadlines to be clamped forward.
 */
internal class FrameRateLimiter {
    private var lastEmittedAt: Instant? = null

    /**
     * Returns [requested], clamped forward if it would exceed the maximum frame rate.
     */
    fun clampDeadline(requested: Instant): Instant {
        val last = lastEmittedAt ?: return requested
        val minAllowed = last + MIN_FRAME_INTERVAL
        return maxOf(requested, minAllowed)
    }

    /**
     * Records that a draw notification was emitted at [emittedAt].
     */
    fun markEmitted(emittedAt: Instant) {
        lastEmittedAt = emittedAt
    }
}
