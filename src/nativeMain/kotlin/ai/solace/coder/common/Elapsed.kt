// port-lint: source codex-rs/common/src/elapsed.rs
package ai.solace.coder.common

import kotlin.math.round
import kotlin.time.Duration
import kotlin.time.TimeSource

/** Returns a string representing the elapsed time since `startTime` like "1m 15s" or "1.50s". */
fun formatElapsed(startTime: TimeSource.Monotonic.ValueTimeMark): String {
    return formatDuration(startTime.elapsedNow())
}

/**
 * Convert a [Duration] into a human-readable, compact string.
 *
 * Formatting rules:
 * * < 1 s  ->  "{milli}ms"
 * * < 60 s ->  "{sec:.2}s" (two decimal places)
 * * >= 60 s ->  "{min}m {sec:02}s"
 */
fun formatDuration(duration: Duration): String {
    val millis = duration.inWholeMilliseconds
    return formatElapsedMillis(millis)
}

private fun formatElapsedMillis(millis: Long): String {
    return if (millis < 1000) {
        "${millis}ms"
    } else if (millis < 60_000) {
        "${twoDecimal(millis.toDouble() / 1000.0)}s"
    } else {
        val minutes = millis / 60_000
        val seconds = (millis % 60_000) / 1000
        "${minutes}m ${seconds.toString().padStart(2, '0')}s"
    }
}

private fun twoDecimal(value: Double): String {
    val scaled = round(value * 100.0).toLong()
    val whole = scaled / 100
    val frac = (if (scaled < 0) -scaled else scaled) % 100
    return "$whole.${frac.toString().padStart(2, '0')}"
}
