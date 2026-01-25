// port-lint: source ollama/src/pull.rs
package ai.solace.coder.ollama

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.math.pow
import kotlin.time.Duration

/** Events emitted while pulling a model from Ollama. */
sealed class PullEvent {
    /** A human-readable status message (e.g., "verifying", "writing"). */
    data class Status(val status: String) : PullEvent()

    /** Byte-level progress update for a specific layer digest. */
    data class ChunkProgress(
            val digest: String,
            val total: Long? = null,
            val completed: Long? = null
    ) : PullEvent()

    /** The pull finished successfully. */
    object Success : PullEvent()

    /** Error event with a message. */
    data class Error(val message: String) : PullEvent()
}

/**
 * A simple observer for pull progress events. Implementations decide how to
 * render progress (CLI, TUI, logs, ...).
 */
interface PullProgressReporter {
    fun onEvent(event: PullEvent)
}

/** A minimal CLI reporter that writes inline progress to stderr. */
class CliProgressReporter : PullProgressReporter {
    private var printedHeader: Boolean = false
    private var lastLineLen: Int = 0
    private var lastCompletedSum: Long = 0
    private var lastInstant: Instant = Clock.System.now()
    private val totalsByDigest = mutableMapOf<String, Pair<Long, Long>>()

    override fun onEvent(event: PullEvent) {
        when (event) {
            is PullEvent.Status -> {
                val status = event.status
                // Avoid noisy manifest messages; otherwise show status inline.
                if (status.equals("pulling manifest", ignoreCase = true)) {
                    return
                }
                val pad = (lastLineLen - status.length).coerceAtLeast(0)
                val line = "\r$status${" ".repeat(pad)}"
                lastLineLen = status.length
                print(line)
                // In a real implementation we might need to flush stdout/stderr
            }
            is PullEvent.ChunkProgress -> {
                val digest = event.digest
                val current = totalsByDigest.getOrPut(digest) { 0L to 0L }
                var t = current.first
                var c = current.second

                event.total?.let { t = it }
                event.completed?.let { c = it }

                totalsByDigest[digest] = t to c

                var sumTotal = 0L
                var sumCompleted = 0L
                for (v in totalsByDigest.values) {
                    sumTotal += v.first
                    sumCompleted += v.second
                }

                if (sumTotal > 0) {
                    if (!printedHeader) {
                        val gb = sumTotal.toDouble() / (1024.0 * 1024.0 * 1024.0)
                        val header = "Downloading model: total ${gb.format(2)} GB\n"
                        print("\r\u001b[2K")
                        print(header)
                        printedHeader = true
                    }
                    val now = Clock.System.now()
                    val dt = (now - lastInstant).toDouble(kotlin.time.DurationUnit.SECONDS).coerceAtLeast(0.001)
                    val dbytes = (sumCompleted - lastCompletedSum).toDouble().coerceAtLeast(0.0)
                    val speedMbS = dbytes / (1024.0 * 1024.0) / dt
                    lastCompletedSum = sumCompleted
                    lastInstant = now

                    val doneGb = sumCompleted.toDouble() / (1024.0 * 1024.0 * 1024.0)
                    val totalGb = sumTotal.toDouble() / (1024.0 * 1024.0 * 1024.0)
                    val pct = sumCompleted.toDouble() * 100.0 / sumTotal.toDouble()
                    val text = "${doneGb.format(2)}/${totalGb.format(2)} GB (${pct.format(1)}%) ${speedMbS.format(1)} MB/s"
                    val pad = (lastLineLen - text.length).coerceAtLeast(0)
                    val line = "\r$text${" ".repeat(pad)}"
                    lastLineLen = text.length
                    print(line)
                }
            }
            is PullEvent.Error -> {
                // This will be handled by the caller, so we don't do anything
                // here or the error will be printed twice.
            }
            is PullEvent.Success -> {
                println()
            }
        }
    }

    private fun Double.format(digits: Int): String {
        // Simple manual formatting for commonMain
        val factor = 10.0.pow(digits.toDouble())
        val rounded = (this * factor).toLong().toDouble() / factor
        return rounded.toString()
    }
}

/**
 * For now the TUI reporter delegates to the CLI reporter. This keeps UI and
 * CLI behavior aligned until a dedicated TUI integration is implemented.
 */
class TuiProgressReporter(private val delegate: CliProgressReporter = CliProgressReporter()) : PullProgressReporter {
    override fun onEvent(event: PullEvent) {
        delegate.onEvent(event)
    }
}
