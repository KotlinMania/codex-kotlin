// port-lint: source tui/src/tui/frame_rate_limiter.rs
package io.github.solaceharmony.codex.tui

import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Instant

/** A 120 FPS minimum frame interval, approximately 8.33ms. */
internal val MIN_FRAME_INTERVAL: Duration = 8_333_334.nanoseconds

/** Remembers the most recent emitted draw so new deadlines can be clamped forward. */
internal class FrameRateLimiter {
    private var lastEmittedAt: Instant? = null

    /** Return [requested], clamped forward if it would exceed the maximum frame rate. */
    fun clampDeadline(requested: Instant): Instant {
        val last = lastEmittedAt ?: return requested
        val minAllowed = last + MIN_FRAME_INTERVAL
        return maxOf(requested, minAllowed)
    }

    /** Record that a draw notification was emitted at [emittedAt]. */
    fun markEmitted(emittedAt: Instant) {
        lastEmittedAt = emittedAt
    }
}
