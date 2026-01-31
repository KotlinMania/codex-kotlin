// port-lint: source tui/src/history_cell.rs
package ai.solace.coder.tui

import ai.solace.coder.tui.render.Renderable
// import io.github.kotlinmania.ratatui.buffer.Buffer
// import io.github.kotlinmania.ratatui.layout.Rect
// import io.github.kotlinmania.ratatui.text.Line
// import io.github.kotlinmania.ratatui.text.Text
// import io.github.kotlinmania.ratatui.widgets.Paragraph
// import io.github.kotlinmania.ratatui.widgets.Wrap

/**
 * Represents an event to display in the conversation history.
 *
 * Ported from Rust codex-rs/tui/src/history_cell.rs
 */
interface HistoryCell {
    fun displayLines(width: UShort): List<Any>

    fun desiredHeight(width: UShort): UShort {
        val lines = displayLines(width)
        // val text = Text.from(lines)
        // return Paragraph(text)
        //    .wrap(Wrap(trim = false))
        //    .lineCount(width.toInt())
        //    .toUShort()
        return 0u
    }

    fun transcriptLines(width: UShort): List<Any> {
        return displayLines(width)
    }

    fun desiredTranscriptHeight(width: UShort): UShort {
        val lines = transcriptLines(width)
        // Workaround for ratatui bug: if there's only one line and it's whitespace-only, ratatui gives 2 lines.
        if (lines.size == 1) {
            // val line = lines[0]
            // if (line.spans.all { it.content.all { it.isWhitespace() } }) {
            //    return 1.toUShort()
            // }
        }

        // val text = Text.from(lines)
        // return Paragraph(text)
        //    .wrap(Wrap(trim = false))
        //    .lineCount(width.toInt())
        //    .toUShort()
        return 0u
    }

    fun isStreamContinuation(): Boolean {
        return false
    }
}

/** Implementation of Renderable for HistoryCell. */
class HistoryCellRenderable(private val cell: HistoryCell) : Renderable {
    override fun render(area: io.github.kotlinmania.ratatui.layout.Rect, buf: io.github.kotlinmania.ratatui.buffer.Buffer) {
        // val lines = cell.displayLines(area.width.toUShort())
        // val text = Text.from(lines)
        // Paragraph(text).render(area, buf)
    }

    override fun desiredHeight(width: UShort): UShort {
        return cell.desiredHeight(width)
    }
}
