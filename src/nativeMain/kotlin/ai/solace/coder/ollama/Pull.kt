@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

// port-lint: source ollama/src/pull.rs
package ai.solace.coder.ollama

import kotlin.math.pow
import kotlin.math.round
import kotlin.time.TimeSource
import platform.posix.fflush
import platform.posix.fputs
import platform.posix.stderr

/** Events emitted while pulling a model from Ollama. */
sealed class PullEvent {
    /** A human-readable status message (e.g., "verifying", "writing"). */
    data class Status(val status: String) : PullEvent()

    /** Byte-level progress update for a specific layer digest. */
    data class ChunkProgress(
        val digest: String,
        val total: Long?,
        val completed: Long?,
    ) : PullEvent()

    /** The pull finished successfully. */
    data object Success : PullEvent()

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

private data class TotalAndCompleted(
    var total: Long,
    var completed: Long,
)

/** A minimal CLI reporter that writes inline progress to stderr. */
class CliProgressReporter private constructor() : PullProgressReporter {
    private var printedHeader: Boolean = false
    private var lastLineLen: Int = 0
    private var lastCompletedSum: Long = 0
    private var lastInstant = TimeSource.Monotonic.markNow()
    private val totalsByDigest: MutableMap<String, TotalAndCompleted> = HashMap()

    override fun onEvent(event: PullEvent) {
        when (event) {
            is PullEvent.Status -> {
                val status = event.status
                // Avoid noisy manifest messages; otherwise show status inline.
                if (status.equals("pulling manifest", ignoreCase = true)) {
                    return
                }
                val pad = (lastLineLen - status.length).coerceAtLeast(0)
                val line = "\r$status" + " ".repeat(pad)
                lastLineLen = status.length
                writeStderr(line)
                flushStderr()
            }
            is PullEvent.ChunkProgress -> {
                if (event.total != null) {
                    val entry = totalsByDigest.getOrPut(event.digest) { TotalAndCompleted(0, 0) }
                    entry.total = event.total
                }
                if (event.completed != null) {
                    val entry = totalsByDigest.getOrPut(event.digest) { TotalAndCompleted(0, 0) }
                    entry.completed = event.completed
                }

                var sumTotal = 0L
                var sumCompleted = 0L
                for ((total, completed) in totalsByDigest.values) {
                    sumTotal += total
                    sumCompleted += completed
                }

                if (sumTotal > 0) {
                    if (!printedHeader) {
                        val gb = sumTotal.toDouble() / (1024.0 * 1024.0 * 1024.0)
                        val header = "Downloading model: total ${formatDouble(gb, 2)} GB\n"
                        writeStderr("\r\u001b[2K")
                        writeStderr(header)
                        printedHeader = true
                    }

                    val dtSeconds = (lastInstant.elapsedNow().inWholeNanoseconds.toDouble() / 1e9)
                        .coerceAtLeast(0.001)
                    val dbytes = (sumCompleted - lastCompletedSum).coerceAtLeast(0).toDouble()
                    val speedMbS = dbytes / (1024.0 * 1024.0) / dtSeconds
                    lastCompletedSum = sumCompleted
                    lastInstant = TimeSource.Monotonic.markNow()

                    val doneGb = sumCompleted.toDouble() / (1024.0 * 1024.0 * 1024.0)
                    val totalGb = sumTotal.toDouble() / (1024.0 * 1024.0 * 1024.0)
                    val pct = sumCompleted.toDouble() * 100.0 / sumTotal.toDouble()
                    val text =
                        "${formatDouble(doneGb, 2)}/${formatDouble(totalGb, 2)} GB (${formatDouble(pct, 1)}%) " +
                            "${formatDouble(speedMbS, 1)} MB/s"
                    val pad = (lastLineLen - text.length).coerceAtLeast(0)
                    val line = "\r$text" + " ".repeat(pad)
                    lastLineLen = text.length
                    writeStderr(line)
                    flushStderr()
                }
            }
            is PullEvent.Error -> {
                // This will be handled by the caller, so we don't do anything
                // here or the error will be printed twice.
            }
            PullEvent.Success -> {
                writeStderr("\n")
                flushStderr()
            }
        }
    }

    companion object {
        fun new(): CliProgressReporter = CliProgressReporter()
    }
}

/**
 * For now the TUI reporter delegates to the CLI reporter. This keeps UI and
 * CLI behavior aligned until a dedicated TUI integration is implemented.
 */
class TuiProgressReporter(
    private val inner: CliProgressReporter = CliProgressReporter.new(),
) : PullProgressReporter {
    override fun onEvent(event: PullEvent) {
        inner.onEvent(event)
    }
}

private fun writeStderr(text: String) {
    fputs(text, stderr)
}

private fun flushStderr() {
    fflush(stderr)
}

private fun formatDouble(value: Double, decimals: Int): String {
    if (decimals == 0) {
        return value.toLong().toString()
    }
    val multiplier = when (decimals) {
        1 -> 10.0
        2 -> 100.0
        else -> 10.0.pow(decimals.toDouble())
    }
    val rounded = round(value * multiplier) / multiplier
    val str = rounded.toString()
    val dotIndex = str.indexOf('.')
    return if (dotIndex < 0) {
        str + "." + "0".repeat(decimals)
    } else {
        val decimalPart = str.substring(dotIndex + 1)
        if (decimalPart.length >= decimals) {
            str.substring(0, dotIndex + 1 + decimals)
        } else {
            str + "0".repeat(decimals - decimalPart.length)
        }
    }
}

