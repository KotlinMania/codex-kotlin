// port-lint: source codex-rs/common/src/elapsed.rs
package ai.solace.coder.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds

class ElapsedTest {
    @Test
    fun test_format_duration_subsecond() {
        val dur = 250.milliseconds
        assertEquals("250ms", formatDuration(dur))

        val durZero = 0.milliseconds
        assertEquals("0ms", formatDuration(durZero))
    }

    @Test
    fun test_format_duration_seconds() {
        val dur = 1_500.milliseconds
        assertEquals("1.50s", formatDuration(dur))

        val dur2 = 59_999.milliseconds
        assertEquals("60.00s", formatDuration(dur2))
    }

    @Test
    fun test_format_duration_minutes() {
        val dur = 75_000.milliseconds
        assertEquals("1m 15s", formatDuration(dur))

        val durExact = 60_000.milliseconds
        assertEquals("1m 00s", formatDuration(durExact))

        val durLong = 3_601_000.milliseconds
        assertEquals("60m 01s", formatDuration(durLong))
    }

    @Test
    fun test_format_duration_one_hour_has_space() {
        val durHour = 3_600_000.milliseconds
        assertEquals("60m 00s", formatDuration(durHour))
    }
}
