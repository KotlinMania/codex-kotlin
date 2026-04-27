// port-lint: source tui/src/render/renderable.rs
package ai.solace.coder.tui.render

import ratatui.buffer.Buffer
import ratatui.layout.Rect
import ratatui.text.Line
import ratatui.text.Span
import ratatui.widgets.paragraph.Paragraph

interface Renderable {
    fun render(area: Rect, buf: Buffer)
    fun desiredHeight(width: Int): Int
    fun cursorPos(area: Rect): Pair<Int, Int>? = null
}

sealed class RenderableItem : Renderable {
    class Owned(val child: Renderable) : RenderableItem() {
        override fun render(area: Rect, buf: Buffer) = child.render(area, buf)
        override fun desiredHeight(width: Int): Int = child.desiredHeight(width)
        override fun cursorPos(area: Rect): Pair<Int, Int>? = child.cursorPos(area)
    }

    class Borrowed(val child: Renderable) : RenderableItem() {
        override fun render(area: Rect, buf: Buffer) = child.render(area, buf)
        override fun desiredHeight(width: Int): Int = child.desiredHeight(width)
        override fun cursorPos(area: Rect): Pair<Int, Int>? = child.cursorPos(area)
    }
}

/** A no-op renderable with zero height. */
object EmptyRenderable : Renderable {
    @Suppress("UNUSED_PARAMETER")
    override fun render(area: Rect, buf: Buffer) {}
    override fun desiredHeight(width: Int): Int = 0
}

/** A renderable that renders a single string. */
class StringRenderable(private val text: String) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        Span(text).render(area, buf)
    }

    override fun desiredHeight(width: Int): Int = 1
}

/** A renderable that wraps a [Span]. */
class SpanRenderable(private val span: Span) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        span.render(area, buf)
    }

    override fun desiredHeight(width: Int): Int = 1
}

/** A renderable that wraps a [Line]. */
class LineRenderable(private val line: Line) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        line.render(area, buf)
    }

    override fun desiredHeight(width: Int): Int = 1
}

/** A renderable that wraps a [Paragraph]. */
class ParagraphRenderable(private val paragraph: Paragraph) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        paragraph.render(area, buf)
    }

    override fun desiredHeight(width: Int): Int = paragraph.lineCount(width)
}

/** A renderable that delegates to an optional inner renderable. */
class OptionalRenderable(private val inner: Renderable?) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        inner?.render(area, buf)
    }

    override fun desiredHeight(width: Int): Int {
        return inner?.desiredHeight(width) ?: 0
    }
}

/** Lays out children in a vertical column. */
class ColumnRenderable : Renderable {
    private val children: MutableList<RenderableItem> = mutableListOf()

    override fun render(area: Rect, buf: Buffer) {
        var y = area.y
        for (child in children) {
            val childArea = Rect.new(area.x, y, area.width, child.desiredHeight(area.width))
                .intersection(area)
            if (!childArea.isEmpty()) {
                child.render(childArea, buf)
            }
            y += childArea.height
        }
    }

    override fun desiredHeight(width: Int): Int {
        return children.sumOf { it.desiredHeight(width) }
    }

    /**
     * Returns the cursor position of the first child that has a cursor position, offset by the
     * child's position in the column.
     *
     * It is generally assumed that either zero or one child will have a cursor position.
     */
    override fun cursorPos(area: Rect): Pair<Int, Int>? {
        var y = area.y
        for (child in children) {
            val childArea = Rect.new(area.x, y, area.width, child.desiredHeight(area.width))
                .intersection(area)
            if (!childArea.isEmpty()) {
                val pos = child.cursorPos(childArea)
                if (pos != null) return pos
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

        fun with(children: List<Renderable>): ColumnRenderable {
            val col = ColumnRenderable()
            for (child in children) {
                col.push(child)
            }
            return col
        }
    }
}

private class FlexChild(
    val flex: Int,
    val child: RenderableItem,
)

/**
 * Lays out children in a column, with the ability to specify a flex factor for each child.
 *
 * Children with flex factor > 0 will be allocated the remaining space after the non-flex children,
 * proportional to the flex factor.
 */
class FlexRenderable : Renderable {
    private val children: MutableList<FlexChild> = mutableListOf()

    fun push(flex: Int, child: Renderable) {
        children.add(FlexChild(flex = flex, child = RenderableItem.Owned(child)))
    }

    /**
     * Loosely inspired by Flutter's Flex widget.
     *
     * Ref https://github.com/flutter/flutter/blob/3fd81edbf1e015221e143c92b2664f4371bdc04a/packages/flutter/lib/src/rendering/flex.dart#L1205-L1209
     */
    private fun allocate(area: Rect): List<Rect> {
        val childSizes = IntArray(children.size)
        var allocatedSize = 0
        var totalFlex = 0

        // 1. Allocate space to non-flex children.
        val maxSize = area.height
        var lastFlexChildIdx = 0
        for ((i, fc) in children.withIndex()) {
            if (fc.flex > 0) {
                totalFlex += fc.flex
                lastFlexChildIdx = i
            } else {
                childSizes[i] = minOf(
                    fc.child.desiredHeight(area.width),
                    (maxSize - allocatedSize).coerceAtLeast(0)
                )
                allocatedSize += childSizes[i]
            }
        }
        val freeSpace = (maxSize - allocatedSize).coerceAtLeast(0)
        // 2. Allocate space to flex children, proportional to their flex factor.
        var allocatedFlexSpace = 0
        if (totalFlex > 0) {
            val spacePerFlex = freeSpace / totalFlex
            for ((i, fc) in children.withIndex()) {
                if (fc.flex > 0) {
                    // Last flex child gets all the remaining space, to prevent a rounding error
                    // from not allocating all the space.
                    val maxChildExtent = if (i == lastFlexChildIdx) {
                        freeSpace - allocatedFlexSpace
                    } else {
                        spacePerFlex * fc.flex
                    }
                    val childSize = minOf(fc.child.desiredHeight(area.width), maxChildExtent)
                    childSizes[i] = childSize
                    allocatedSize += childSize
                    allocatedFlexSpace += childSize
                }
            }
        }

        val allocatedRects = mutableListOf<Rect>()
        var y = area.y
        for (size in childSizes) {
            val childArea = Rect.new(area.x, y, area.width, size)
            allocatedRects.add(childArea)
            y += childArea.height
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
            .lastOrNull()
            ?.bottom()
            ?: 0
    }

    override fun cursorPos(area: Rect): Pair<Int, Int>? {
        for ((rect, child) in allocate(area).zip(children)) {
            val pos = child.child.cursorPos(rect)
            if (pos != null) return pos
        }
        return null
    }

    companion object {
        fun new(): FlexRenderable = FlexRenderable()
    }
}

/** Lays out children in a horizontal row with specified widths. */
class RowRenderable : Renderable {
    private val children: MutableList<Pair<Int, RenderableItem>> = mutableListOf()

    override fun render(area: Rect, buf: Buffer) {
        var x = area.x
        for ((childWidth, child) in children) {
            val availableWidth = (area.width - (x - area.x)).coerceAtLeast(0)
            val childArea = Rect.new(x, area.y, minOf(childWidth, availableWidth), area.height)
            if (childArea.isEmpty()) break
            child.render(childArea, buf)
            x += childWidth
        }
    }

    override fun desiredHeight(width: Int): Int {
        var maxHeight = 0
        var widthRemaining = width
        for ((childWidth, child) in children) {
            val w = minOf(childWidth, widthRemaining)
            if (w == 0) break
            val height = child.desiredHeight(w)
            if (height > maxHeight) {
                maxHeight = height
            }
            widthRemaining = (widthRemaining - w).coerceAtLeast(0)
        }
        return maxHeight
    }

    override fun cursorPos(area: Rect): Pair<Int, Int>? {
        var x = area.x
        for ((childWidth, child) in children) {
            val availableWidth = (area.width - (x - area.x)).coerceAtLeast(0)
            val childArea = Rect.new(x, area.y, minOf(childWidth, availableWidth), area.height)
            if (!childArea.isEmpty()) {
                val pos = child.cursorPos(childArea)
                if (pos != null) return pos
            }
            x += childWidth
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

/** A renderable that wraps a child with insets. */
class InsetRenderable(
    private val child: RenderableItem,
    private val insets: Insets,
) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        child.render(area.inset(insets), buf)
    }

    override fun desiredHeight(width: Int): Int {
        return child.desiredHeight(width - insets.left - insets.right) + insets.top + insets.bottom
    }

    override fun cursorPos(area: Rect): Pair<Int, Int>? {
        return child.cursorPos(area.inset(insets))
    }

    companion object {
        fun new(child: Renderable, insets: Insets): InsetRenderable {
            return InsetRenderable(
                child = RenderableItem.Owned(child),
                insets = insets,
            )
        }
    }
}

/** Extension to wrap a renderable with insets. */
fun Renderable.inset(insets: Insets): RenderableItem {
    return RenderableItem.Owned(InsetRenderable(RenderableItem.Owned(this), insets))
}
