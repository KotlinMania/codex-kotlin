// port-lint: source common/src/elapsed.rs
package ai.solace.coder.common

import kotlin.time.Duration
import kotlin.time.TimeSource

/**
 * Returns a string representing the elapsed time since [startMark] like
 * "1m 15s" or "1.50s".
 */
fun formatElapsed(startMark: TimeSource.Monotonic.ValueTimeMark): String {
    return formatDuration(startMark.elapsedNow())
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
    return if (millis < 1000L) {
        "${millis}ms"
    } else if (millis < 60_000L) {
        val secs = millis.toDouble() / 1000.0
        val whole = secs.toLong()
        val frac = ((secs - whole) * 100).toLong()
        "${whole}.${frac.toString().padStart(2, '0')}s"
    } else {
        val minutes = millis / 60_000L
        val seconds = (millis % 60_000L) / 1000L
        "${minutes}m ${seconds.toString().padStart(2, '0')}s"
    }
}
