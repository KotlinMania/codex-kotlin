package io.github.solaceharmony.codex.tui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

class FrameRateLimiterTest {
    @Test
    fun defaultDoesNotClamp() {
        val t0 = Instant.fromEpochMilliseconds(1_000)
        val limiter = FrameRateLimiter()

        assertEquals(t0, limiter.clampDeadline(t0))
    }

    @Test
    fun clampsToMinimumIntervalSinceLastEmit() {
        val t0 = Instant.fromEpochMilliseconds(1_000)
        val limiter = FrameRateLimiter()

        assertEquals(t0, limiter.clampDeadline(t0))
        limiter.markEmitted(t0)

        val tooSoon = t0 + 1.milliseconds
        assertEquals(t0 + MIN_FRAME_INTERVAL, limiter.clampDeadline(tooSoon))
    }
}
