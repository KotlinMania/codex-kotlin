// port-lint: source elapsed.rs
package io.github.kotlinmania.codex.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds

class ElapsedTest {
    @Test
    fun testFormatDurationSubsecond() {
        val dur = 250.milliseconds
        assertEquals("250ms", formatDuration(dur))

        val durZero = 0.milliseconds
        assertEquals("0ms", formatDuration(durZero))
    }

    @Test
    fun testFormatDurationSeconds() {
        val dur = 1_500.milliseconds
        assertEquals("1.50s", formatDuration(dur))

        val dur2 = 59_999.milliseconds
        assertEquals("60.00s", formatDuration(dur2))
    }

    @Test
    fun testFormatDurationMinutes() {
        val dur = 75_000.milliseconds
        assertEquals("1m 15s", formatDuration(dur))

        val durExact = 60_000.milliseconds
        assertEquals("1m 00s", formatDuration(durExact))

        val durLong = 3_601_000.milliseconds
        assertEquals("60m 01s", formatDuration(durLong))
    }

    @Test
    fun testFormatDurationOneHourHasSpace() {
        val durHour = 3_600_000.milliseconds
        assertEquals("60m 00s", formatDuration(durHour))
    }
}
