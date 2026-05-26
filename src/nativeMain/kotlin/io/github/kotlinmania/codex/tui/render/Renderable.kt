// port-lint: source tui/src/render/renderable.rs
package io.github.kotlinmania.codex.tui.render

import kotlin.math.min
import kotlin.math.max

/**
 * Buffer for terminal rendering (equivalent to ratatui::buffer::Buffer).
 */
class Buffer(
    val width: Int,
    val height: Int
)

/**
 * Rectangle for layout (equivalent to ratatui::layout::Rect).
 */
data class Rect(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
) {
    fun isEmpty(): Boolean = width <= 0 || height <= 0

    fun intersection(other: Rect): Rect {
        val x1 = max(x, other.x)
        val y1 = max(y, other.y)
        val x2 = min(x + width, other.x + other.width)
        val y2 = min(y + height, other.y + other.height)
        return if (x1 < x2 && y1 < y2) {
            Rect(x1, y1, x2 - x1, y2 - y1)
        } else {
            Rect(0, 0, 0, 0)
        }
    }

    fun bottom(): Int = y + height

    companion object {
        fun new(x: Int, y: Int, width: Int, height: Int): Rect = Rect(x, y, width, height)
    }
}

/**
 * Insets for padding (equivalent to crate::render::Insets).
 */
data class Insets(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int
) {
    companion object {
        fun tlbr(top: Int, left: Int, bottom: Int, right: Int): Insets =
            Insets(left, top, right, bottom)

        fun vh(v: Int, h: Int): Insets = Insets(h, v, h, v)
    }
}

/**
 * Extension function for Rect to apply insets.
 */
fun Rect.inset(insets: Insets): Rect {
    return Rect(
        x = x + insets.left,
        y = y + insets.top,
        width = max(0, width - insets.left - insets.right),
        height = max(0, height - insets.top - insets.bottom)
    )
}

/**
 * Style for text rendering.
 */
data class Style(
    val fg: Color? = null,
    val bg: Color? = null,
    val bold: Boolean = false,
    val dim: Boolean = false
) {
    companion object {
        fun default(): Style = Style()
    }
}

/**
 * Color for styling.
 */
enum class Color {
    Black, Red, Green, Yellow, Blue, Magenta, Cyan, White, Reset
}

/**
 * Styled text span (equivalent to ratatui::text::Span).
 */
data class Span(
    val content: String,
    val style: Style = Style.default()
) {
    fun renderRef(area: Rect, buf: Buffer) {
        // Implementation deferred to ratatui-kotlin integration
    }
}

/**
 * Line of text with optional spans (equivalent to ratatui::text::Line).
 */
data class Line(val spans: List<Span>) {
    constructor(text: String) : this(listOf(Span(text)))

    fun renderRef(area: Rect, buf: Buffer) {
        // Implementation deferred to ratatui-kotlin integration
    }
}

/**
 * Paragraph widget (equivalent to ratatui::widgets::Paragraph).
 */
data class Paragraph(val lines: List<Line>) {
    constructor(text: String) : this(text.lines().map { Line(it) })

    fun renderRef(area: Rect, buf: Buffer) {
        // Implementation deferred to ratatui-kotlin integration
    }

    fun lineCount(width: Int): Int = lines.size
}

/**
 * Trait for renderable items (equivalent to Rust's Renderable trait).
 */
interface Renderable {
    fun render(area: Rect, buf: Buffer)
    fun desiredHeight(width: Int): Int
    fun cursorPos(area: Rect): Pair<Int, Int>? = null
}

/**
 * Wrapper for owned or borrowed renderables (equivalent to RenderableItem enum).
 */
sealed class RenderableItem : Renderable {
    data class Owned(val child: Renderable) : RenderableItem()
    data class Borrowed(val child: Renderable) : RenderableItem()

    override fun render(area: Rect, buf: Buffer) {
        when (this) {
            is Owned -> child.render(area, buf)
            is Borrowed -> child.render(area, buf)
        }
    }

    override fun desiredHeight(width: Int): Int = when (this) {
        is Owned -> child.desiredHeight(width)
        is Borrowed -> child.desiredHeight(width)
    }

    override fun cursorPos(area: Rect): Pair<Int, Int>? = when (this) {
        is Owned -> child.cursorPos(area)
        is Borrowed -> child.cursorPos(area)
    }

    companion object {
        fun from(renderable: Renderable): RenderableItem = Owned(renderable)
    }
}

/**
 * Unit type renderable (empty, renders nothing).
 */
data object UnitRenderable : Renderable {
    override fun render(area: Rect, buf: Buffer) {}
    override fun desiredHeight(width: Int): Int = 0
}

/**
 * String renderable implementation.
 */
class StringRenderable(private val value: String) : Renderable {
    override fun render(area: Rect, buf: Buffer) {}
    override fun desiredHeight(width: Int): Int = 1
}

/**
 * Span renderable implementation.
 */
class SpanRenderable(private val span: Span) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        span.renderRef(area, buf)
    }
    override fun desiredHeight(width: Int): Int = 1
}

/**
 * Line renderable implementation.
 */
class LineRenderable(private val line: Line) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        line.renderRef(area, buf)
    }
    override fun desiredHeight(width: Int): Int = 1
}

/**
 * Paragraph renderable implementation.
 */
class ParagraphRenderable(private val paragraph: Paragraph) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        paragraph.renderRef(area, buf)
    }
    override fun desiredHeight(width: Int): Int = paragraph.lineCount(width)
}

/**
 * Optional renderable implementation.
 */
class OptionalRenderable<R : Renderable>(private val value: R?) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        value?.render(area, buf)
    }
    override fun desiredHeight(width: Int): Int = value?.desiredHeight(width) ?: 0
}

/**
 * Lays out children vertically in a column (equivalent to ColumnRenderable).
 */
class ColumnRenderable : Renderable {
    private val children: MutableList<RenderableItem> = mutableListOf()

    override fun render(area: Rect, buf: Buffer) {
        var y = area.y
        for (child in children) {
            val childArea = Rect.new(
                area.x, y, area.width, child.desiredHeight(area.width)
            ).intersection(area)
            if (!childArea.isEmpty()) {
                child.render(childArea, buf)
            }
            y += childArea.height
        }
    }

    override fun desiredHeight(width: Int): Int =
        children.sumOf { it.desiredHeight(width) }

    override fun cursorPos(area: Rect): Pair<Int, Int>? {
        var y = area.y
        for (child in children) {
            val childArea = Rect.new(
                area.x, y, area.width, child.desiredHeight(area.width)
            ).intersection(area)
            if (!childArea.isEmpty()) {
                child.cursorPos(childArea)?.let { return it }
            }
            y += childArea.height
        }
        return null
    }

    fun push(child: Renderable) {
        children.add(RenderableItem.Owned(child))
    }

    fun pushRef(child: Renderable) {
        children.add(RenderableItem.Borrowed(child))
    }

    companion object {
        fun new(): ColumnRenderable = ColumnRenderable()

        fun <T : Renderable> with(children: Iterable<T>): ColumnRenderable {
            val column = ColumnRenderable()
            for (child in children) {
                column.push(child)
            }
            return column
        }
    }
}

/**
 * Child with flex factor for FlexRenderable.
 */
data class FlexChild(
    val flex: Int,
    val child: RenderableItem
)

/**
 * Lays out children in a column, with the ability to specify a flex factor for each child.
 */
class FlexRenderable : Renderable {
    private val children: MutableList<FlexChild> = mutableListOf()

    fun push(flex: Int, child: Renderable) {
        children.add(FlexChild(flex, RenderableItem.Owned(child)))
    }

    fun pushItem(flex: Int, child: RenderableItem) {
        children.add(FlexChild(flex, child))
    }

    private fun allocate(area: Rect): List<Rect> {
        val allocatedRects = mutableListOf<Rect>()
        val childSizes = IntArray(children.size)
        var allocatedSize = 0
        var totalFlex = 0

        val maxSize = area.height
        var lastFlexChildIdx = 0
        for ((i, flexChild) in children.withIndex()) {
            if (flexChild.flex > 0) {
                totalFlex += flexChild.flex
                lastFlexChildIdx = i
            } else {
                childSizes[i] = min(
                    flexChild.child.desiredHeight(area.width),
                    max(0, maxSize - allocatedSize)
                )
                allocatedSize += childSizes[i]
            }
        }
        val freeSpace = max(0, maxSize - allocatedSize)

        var allocatedFlexSpace = 0
        if (totalFlex > 0) {
            val spacePerFlex = freeSpace / totalFlex
            for ((i, flexChild) in children.withIndex()) {
                if (flexChild.flex > 0) {
                    val maxChildExtent = if (i == lastFlexChildIdx) {
                        freeSpace - allocatedFlexSpace
                    } else {
                        spacePerFlex * flexChild.flex
                    }
                    val childSize = min(
                        flexChild.child.desiredHeight(area.width),
                        maxChildExtent
                    )
                    childSizes[i] = childSize
                    allocatedSize += childSize
                    allocatedFlexSpace += childSize
                }
            }
        }

        var y = area.y
        for (size in childSizes) {
            allocatedRects.add(Rect.new(area.x, y, area.width, size))
            y += size
        }
        return allocatedRects
    }

    override fun render(area: Rect, buf: Buffer) {
        allocate(area).zip(children).forEach { (rect, child) ->
            child.child.render(rect, buf)
        }
    }

    override fun desiredHeight(width: Int): Int {
        return allocate(Rect.new(0, 0, width, Int.MAX_VALUE))
            .lastOrNull()?.bottom() ?: 0
    }

    override fun cursorPos(area: Rect): Pair<Int, Int>? {
        return allocate(area)
            .zip(children)
            .firstNotNullOfOrNull { (rect, child) -> child.child.cursorPos(rect) }
    }

    companion object {
        fun new(): FlexRenderable = FlexRenderable()
    }
}

/**
 * Lays out children horizontally in a row (equivalent to RowRenderable).
 */
class RowRenderable : Renderable {
    private val children: MutableList<Pair<Int, RenderableItem>> = mutableListOf()

    override fun render(area: Rect, buf: Buffer) {
        var x = area.x
        for ((width, child) in children) {
            val availableWidth = max(0, area.width - (x - area.x))
            val childArea = Rect.new(x, area.y, min(width, availableWidth), area.height)
            if (childArea.isEmpty()) break
            child.render(childArea, buf)
            x += width
        }
    }

    override fun desiredHeight(width: Int): Int {
        var maxHeight = 0
        var widthRemaining = width
        for ((childWidth, child) in children) {
            val w = min(childWidth, widthRemaining)
            if (w == 0) break
            maxHeight = max(maxHeight, child.desiredHeight(w))
            widthRemaining = max(0, widthRemaining - w)
        }
        return maxHeight
    }

    override fun cursorPos(area: Rect): Pair<Int, Int>? {
        var x = area.x
        for ((width, child) in children) {
            val availableWidth = max(0, area.width - (x - area.x))
            val childArea = Rect.new(x, area.y, min(width, availableWidth), area.height)
            if (!childArea.isEmpty()) {
                child.cursorPos(childArea)?.let { return it }
            }
            x += width
        }
        return null
    }

    fun push(width: Int, child: Renderable) {
        children.add(Pair(width, RenderableItem.Owned(child)))
    }

    fun pushRef(width: Int, child: Renderable) {
        children.add(Pair(width, RenderableItem.Borrowed(child)))
    }

    companion object {
        fun new(): RowRenderable = RowRenderable()
    }
}

/**
 * Wraps a child with insets/padding (equivalent to InsetRenderable).
 */
class InsetRenderable(
    private val child: RenderableItem,
    private val insets: Insets
) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        child.render(area.inset(insets), buf)
    }

    override fun desiredHeight(width: Int): Int {
        val innerWidth = max(0, width - insets.left - insets.right)
        return child.desiredHeight(innerWidth) + insets.top + insets.bottom
    }

    override fun cursorPos(area: Rect): Pair<Int, Int>? {
        return child.cursorPos(area.inset(insets))
    }

    companion object {
        fun new(child: Renderable, insets: Insets): InsetRenderable {
            return InsetRenderable(RenderableItem.Owned(child), insets)
        }

        fun newItem(child: RenderableItem, insets: Insets): InsetRenderable {
            return InsetRenderable(child, insets)
        }
    }
}

/**
 * Extension function to wrap a Renderable with insets.
 */
fun Renderable.withInset(insets: Insets): RenderableItem {
    return RenderableItem.Owned(InsetRenderable(RenderableItem.Owned(this), insets))
}
