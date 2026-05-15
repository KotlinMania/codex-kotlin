// port-lint: source ollama/src/pull.rs
package io.github.solaceharmony.codex.ollama

import io.github.solaceharmony.codex.utils.writeStderrInline
import kotlin.time.TimeSource

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
class CliProgressReporter private constructor(
    private var printedHeader: Boolean,
    private var lastLineLen: Int,
    private var lastCompletedSum: Long,
    private var lastInstant: TimeSource.Monotonic.ValueTimeMark,
    private val totalsByDigest: MutableMap<String, TotalAndCompleted>,
) : PullProgressReporter {
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
                writeStderrInline(line)
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

                val (sumTotal, sumCompleted) = totalsByDigest.values.fold(0L to 0L) { acc, tc ->
                    (acc.first + tc.total) to (acc.second + tc.completed)
                }

                if (sumTotal > 0) {
                    if (!printedHeader) {
                        val gb = sumTotal.toDouble() / (1024.0 * 1024.0 * 1024.0)
                        val header = "Downloading model: total ${formatDouble(gb, 2)} GB\n"
                        writeStderrInline("\r[2K$header")
                        printedHeader = true
                    }

                    val dt = (lastInstant.elapsedNow().inWholeNanoseconds.toDouble() / 1e9)
                        .coerceAtLeast(0.001)
                    val dbytes = (sumCompleted - lastCompletedSum).coerceAtLeast(0).toDouble()
                    val speedMbS = dbytes / (1024.0 * 1024.0) / dt
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
                    writeStderrInline(line)
                }
            }
            is PullEvent.Error -> {
                // This will be handled by the caller, so we do not do anything
                // here or the error will be printed twice.
            }
            PullEvent.Success -> {
                writeStderrInline("\n")
            }
        }
    }

    companion object {
        fun default(): CliProgressReporter {
            return new()
        }

        fun new(): CliProgressReporter {
            return CliProgressReporter(
                printedHeader = false,
                lastLineLen = 0,
                lastCompletedSum = 0,
                lastInstant = TimeSource.Monotonic.markNow(),
                totalsByDigest = HashMap(),
            )
        }
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
