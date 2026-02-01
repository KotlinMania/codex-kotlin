// port-lint: source tui/src/custom_terminal.rs
package ai.solace.coder.tui

import io.github.kotlinmania.ratatui.backend.Backend
import io.github.kotlinmania.ratatui.backend.ClearType
import io.github.kotlinmania.ratatui.buffer.Buffer
import io.github.kotlinmania.ratatui.buffer.Cell
import io.github.kotlinmania.ratatui.layout.Position
import io.github.kotlinmania.ratatui.layout.Rect
import io.github.kotlinmania.ratatui.layout.Size
import io.github.kotlinmania.ratatui.style.Color
import io.github.kotlinmania.ratatui.style.Modifier
import io.github.kotlinmania.ratatui.widgets.WidgetRef
import io.github.kotlinmania.kasuari.cursor.MoveTo
import io.github.kotlinmania.kasuari.style.Colors
import io.github.kotlinmania.kasuari.style.Print
import io.github.kotlinmania.kasuari.style.SetAttribute
import io.github.kotlinmania.kasuari.style.SetBackgroundColor
import io.github.kotlinmania.kasuari.style.SetColors
import io.github.kotlinmania.kasuari.style.SetForegroundColor
import io.github.kotlinmania.kasuari.terminal.Clear
import io.github.kotlinmania.kasuari.terminal.ClearType as KasuariClearType

// Note: In Kotlin, we use interface or abstract class for Backend.
// In ratatui-kotlin, it's likely an interface.

data class Frame(
    val viewportArea: Rect,
    val buffer: Buffer,
    var cursorPosition: Position? = null
) {
    fun area(): Rect = viewportArea

    fun renderWidgetRef(widget: WidgetRef, area: Rect) {
        widget.renderRef(area, buffer)
    }

    fun setCursorPosition(position: Position) {
        cursorPosition = position
    }

    fun bufferMut(): Buffer = buffer
}

class CustomTerminal<B : Backend>(
    var backend: B
) {
    private var buffers: Array<Buffer> = arrayOf(Buffer.empty(Rect.ZERO), Buffer.empty(Rect.ZERO))
    private var current: Int = 0
    var hiddenCursor: Boolean = false
    var viewportArea: Rect = Rect.ZERO
    var lastKnownScreenSize: Size = Size(0u, 0u)
    var lastKnownCursorPos: Position = Position(0u, 0u)

    companion object {
        fun <B : Backend> withOptions(backend: B): CustomTerminal<B> {
            val screenSize = backend.size()
            val cursorPos = backend.getCursorPosition()
            val terminal = CustomTerminal(backend)
            terminal.viewportArea = Rect(0u, cursorPos.y, 0u, 0u)
            terminal.lastKnownScreenSize = screenSize
            terminal.lastKnownCursorPos = cursorPos
            return terminal
        }
    }

    fun getFrame(): Frame {
        return Frame(
            viewportArea = viewportArea,
            buffer = currentBuffer(),
            cursorPosition = null
        )
    }

    private fun currentBuffer(): Buffer = buffers[current]
    private fun currentBufferMut(): Buffer = buffers[current]
    private fun previousBuffer(): Buffer = buffers[1 - current]
    private fun previousBufferMut(): Buffer = buffers[1 - current]

    fun backend(): B = backend
    fun backendMut(): B = backend

    fun flush() {
        val updates = diffBuffers(previousBuffer(), currentBuffer())
        val lastPutCommand = updates.filterIsInstance<DrawCommand.Put>().lastOrNull()
        if (lastPutCommand != null) {
            lastKnownCursorPos = Position(lastPutCommand.x, lastPutCommand.y)
        }
        draw(backend, updates.iterator())
    }

    fun resize(screenSize: Size) {
        lastKnownScreenSize = screenSize
    }

    fun setViewportArea(area: Rect) {
        currentBufferMut().resize(area)
        previousBufferMut().resize(area)
        viewportArea = area
    }

    fun autoresize() {
        val screenSize = size()
        if (screenSize != lastKnownScreenSize) {
            resize(screenSize)
        }
    }

    fun draw(renderCallback: (Frame) -> Unit) {
        tryDraw { frame ->
            renderCallback(frame)
            Unit
        }
    }

    fun <T> tryDraw(renderCallback: (Frame) -> T): T {
        autoresize()
        val frame = getFrame()
        val result = renderCallback(frame)
        val cursorPosition = frame.cursorPosition

        flush()

        if (cursorPosition == null) {
            hideCursor()
        } else {
            showCursor()
            setCursorPosition(cursorPosition)
        }

        swapBuffers()
        backend.flush()
        return result
    }

    fun hideCursor() {
        backend.hideCursor()
        hiddenCursor = true
    }

    fun showCursor() {
        backend.showCursor()
        hiddenCursor = false
    }

    fun getCursorPosition(): Position {
        return backend.getCursorPosition()
    }

    fun setCursorPosition(position: Position) {
        backend.setCursorPosition(position)
        lastKnownCursorPos = position
    }

    fun clear() {
        if (viewportArea.isEmpty()) return
        backend.setCursorPosition(Position(viewportArea.x, viewportArea.y))
        backend.clearRegion(ClearType.AfterCursor)
        previousBufferMut().reset()
    }

    fun swapBuffers() {
        previousBufferMut().reset()
        current = 1 - current
    }

    fun size(): Size {
        return backend.size()
    }
}

private sealed class DrawCommand {
    data class Put(val x: UShort, val y: UShort, val cell: Cell) : DrawCommand()
    data class ClearToEnd(val x: UShort, val y: UShort, val bg: Color) : DrawCommand()
}

private fun diffBuffers(a: Buffer, b: Buffer): List<DrawCommand> {
    val area = a.area
    val updates = mutableListOf<DrawCommand>()
    val lastNonblankColumns = UShortArray(area.height.toInt())

    for (y in 0u until area.height) {
        val rowStart = y.toInt() * area.width.toInt()
        val rowEnd = rowStart + area.width.toInt()
        val row = b.content.slice(rowStart until rowEnd)
        val bg = row.lastOrNull()?.bg ?: Color.Reset

        var lastNonblankColumn = 0
        var column = 0
        while (column < row.size) {
            val cell = row[column]
            val width = cell.symbol.length // Simplification for Unicode width
            if (cell.symbol != " " || cell.bg != bg || cell.modifier != Modifier.EMPTY) {
                lastNonblankColumn = column + (width - 1).coerceAtLeast(0)
            }
            column += width.coerceAtLeast(1)
        }

        if (lastNonblankColumn + 1 < row.size) {
            val x = (area.x.toInt() + lastNonblankColumn + 1).toUShort()
            val py = (area.y.toInt() + y.toInt()).toUShort()
            updates.add(DrawCommand.ClearToEnd(x, py, bg))
        }
        lastNonblankColumns[y.toInt()] = lastNonblankColumn.toUShort()
    }

    var invalidated = 0
    var toSkip = 0
    for (i in b.content.indices) {
        val current = b.content[i]
        val previous = a.content.getOrNull(i) ?: Cell.EMPTY
        
        if (!current.skip && (current != previous || invalidated > 0) && toSkip == 0) {
            val x = (i % area.width.toInt()).toUShort()
            val y = (i / area.width.toInt()).toUShort()
            if (x <= lastNonblankColumns[y.toInt()]) {
                updates.add(DrawCommand.Put(x, y, current.copy()))
            }
        }

        toSkip = (current.symbol.length - 1).coerceAtLeast(0)
        val affectedWidth = maxOf(current.symbol.length, previous.symbol.length)
        invalidated = (maxOf(affectedWidth, invalidated) - 1).coerceAtLeast(0)
    }
    return updates
}

private fun draw(backend: Backend, commands: Iterator<DrawCommand>) {
    var fg = Color.Reset
    var bg = Color.Reset
    var modifier = Modifier.EMPTY
    var lastPos: Position? = null

    // Assuming Backend provides a way to queue commands or we use kasuari directly
    // This part might need adjustment based on how ratatui-kotlin backends work.
    // For now, let's assume we can emit kasuari commands.

    for (command in commands) {
        val (x, y) = when (command) {
            is DrawCommand.Put -> command.x to command.y
            is DrawCommand.ClearToEnd -> command.x to command.y
        }

        if (lastPos == null || x.toInt() != lastPos.x.toInt() + 1 || y != lastPos.y) {
            // Move cursor
            // backend.execute(MoveTo(x, y))
        }
        lastPos = Position(x, y)

        when (command) {
            is DrawCommand.Put -> {
                val cell = command.cell
                if (cell.modifier != modifier) {
                    val diff = ModifierDiff(modifier, cell.modifier)
                    // diff.apply(backend)
                    modifier = cell.modifier
                }
                if (cell.fg != fg || cell.bg != bg) {
                    // backend.execute(SetColors(Colors(cell.fg.toKasuari(), cell.bg.toKasuari())))
                    fg = cell.fg
                    bg = cell.bg
                }
                // backend.execute(Print(cell.symbol))
            }
            is DrawCommand.ClearToEnd -> {
                // backend.execute(SetAttribute(Attribute.Reset))
                modifier = Modifier.EMPTY
                // backend.execute(SetBackgroundColor(command.bg.toKasuari()))
                bg = command.bg
                // backend.execute(Clear(KasuariClearType.UntilNewLine))
            }
        }
    }
}

private class ModifierDiff(val from: Modifier, val to: Modifier) {
    // Porting of modifier diff logic...
}
