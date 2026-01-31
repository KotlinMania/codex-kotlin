// port-lint: source tui/src/render/renderable.rs
package ai.solace.coder.tui.render

import io.github.kotlinmania.ratatui.buffer.Buffer
import io.github.kotlinmania.ratatui.layout.Rect
import io.github.kotlinmania.ratatui.text.Line
import io.github.kotlinmania.ratatui.text.Span
import io.github.kotlinmania.ratatui.widgets.Paragraph
import io.github.kotlinmania.ratatui.widgets.WidgetRef

interface Renderable {
    fun render(area: Rect, buf: Buffer)
    fun desiredHeight(width: UShort): UShort
    fun cursorPos(area: Rect): Pair<UShort, UShort>? = null
}

sealed class RenderableItem : Renderable {
    data class Owned(val child: Renderable) : RenderableItem()
    data class Borrowed(val child: Renderable) : RenderableItem()

    override fun render(area: Rect, buf: Buffer) {
        when (this) {
            is Owned -> child.render(area, buf)
            is Borrowed -> child.render(area, buf)
        }
    }

    override fun desiredHeight(width: UShort): UShort {
        return when (this) {
            is Owned -> child.desiredHeight(width)
            is Borrowed -> child.desiredHeight(width)
        }
    }

    override fun cursorPos(area: Rect): Pair<UShort, UShort>? {
        return when (this) {
            is Owned -> child.cursorPos(area)
            is Borrowed -> child.cursorPos(area)
        }
    }
}

// In Kotlin we don't need From/Into as explicitly as Rust, 
// but we can provide extension functions or constructors.

object EmptyRenderable : Renderable {
    override fun render(area: Rect, buf: Buffer) {}
    override fun desiredHeight(width: UShort): UShort = 0u
}

// Extension properties/functions to make common types Renderable
// Note: In Kotlin we can't implement interface for existing types like String,
// so we use wrapper classes.

data class StringRenderable(val text: String) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        // WidgetRef.render_ref equivalent
        // Paragraph(text).render(area, buf) 
    }
    override fun desiredHeight(width: UShort): UShort = 1u
}

data class SpanRenderable(val span: Span) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        // span.render_ref(area, buf)
    }
    override fun desiredHeight(width: UShort): UShort = 1u
}

data class LineRenderable(val line: Line) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        // WidgetRef.render_ref(line, area, buf)
    }
    override fun desiredHeight(width: UShort): UShort = 1u
}

data class ParagraphRenderable(val paragraph: Paragraph) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        // paragraph.render_ref(area, buf)
    }
    override fun desiredHeight(width: UShort): UShort {
        // return paragraph.line_count(width).toUShort()
        return 0u // Placeholder
    }
}

data class OptionRenderable(val child: Renderable?) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        child?.render(area, buf)
    }
    override fun desiredHeight(width: UShort): UShort {
        return child?.desiredHeight(width) ?: 0u
    }
}

class ColumnRenderable(val children: MutableList<RenderableItem> = mutableListOf()) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        var y = area.y
        for (child in children) {
            val childArea = Rect(area.x, y, area.width, child.desiredHeight(area.width))
                .intersection(area)
            if (!childArea.isEmpty()) {
                child.render(childArea, buf)
            }
            y = (y + childArea.height).toUShort()
        }
    }

    override fun desiredHeight(width: UShort): UShort {
        return children.sumOf { it.desiredHeight(width).toInt() }.toUShort()
    }

    override fun cursorPos(area: Rect): Pair<UShort, UShort>? {
        var y = area.y
        for (child in children) {
            val childArea = Rect(area.x, y, area.width, child.desiredHeight(area.width))
                .intersection(area)
            if (!childArea.isEmpty()) {
                val pos = child.cursorPos(childArea)
                if (pos != null) return pos
            }
            y = (y + childArea.height).toUShort()
        }
        return null
    }

    fun push(child: Renderable) {
        children.add(RenderableItem.Owned(child))
    }

    companion object {
        fun with(children: Iterable<RenderableItem>): ColumnRenderable {
            return ColumnRenderable(children.toMutableList())
        }
    }
}

data class FlexChild(
    val flex: Int,
    val child: RenderableItem
)

class FlexRenderable(val children: MutableList<FlexChild> = mutableListOf()) : Renderable {
    
    fun push(flex: Int, child: RenderableItem) {
        children.add(FlexChild(flex, child))
    }

    private fun allocate(area: Rect): List<Rect> {
        val allocatedRects = mutableListOf<Rect>()
        val childSizes = IntArray(children.size)
        var allocatedSize = 0
        var totalFlex = 0

        val maxSize = area.height.toInt()
        var lastFlexChildIdx = 0
        for ((i, flexChild) in children.withIndex()) {
            if (flexChild.flex > 0) {
                totalFlex += flexChild.flex
                lastFlexChildIdx = i
            } else {
                childSizes[i] = flexChild.child.desiredHeight(area.width).toInt()
                    .coerceAtMost(maxSize - allocatedSize)
                allocatedSize += childSizes[i]
            }
        }

        val freeSpace = (maxSize - allocatedSize).coerceAtLeast(0)
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
                    val childSize = flexChild.child.desiredHeight(area.width).toInt()
                        .coerceAtMost(maxChildExtent)
                    childSizes[i] = childSize
                    allocatedSize += childSize
                    allocatedFlexSpace += childSize
                }
            }
        }

        var y = area.y.toInt()
        for (size in childSizes) {
            val childArea = Rect(area.x, y.toUShort(), area.width, size.toUShort())
            allocatedRects.add(childArea)
            y += size
        }
        return allocatedRects
    }

    override fun render(area: Rect, buf: Buffer) {
        allocate(area).zip(children).forEach { (rect, flexChild) ->
            flexChild.child.render(rect, buf)
        }
    }

    override fun desiredHeight(width: UShort): UShort {
        return allocate(Rect(0u, 0u, width, UShort.MAX_VALUE))
            .lastOrNull()?.bottom() ?: 0u
    }

    override fun cursorPos(area: Rect): Pair<UShort, UShort>? {
        return allocate(area).zip(children).firstNotNullOfOrNull { (rect, flexChild) ->
            flexChild.child.cursorPos(rect)
        }
    }
}

class RowRenderable(val children: MutableList<Pair<UShort, RenderableItem>> = mutableListOf()) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        var x = area.x
        for ((width, child) in children) {
            val availableWidth = (area.width.toInt() - (x.toInt() - area.x.toInt())).coerceAtLeast(0)
            val childArea = Rect(x, area.y, width.toInt().coerceAtMost(availableWidth).toUShort(), area.height)
            if (childArea.isEmpty()) break
            child.render(childArea, buf)
            x = (x + width).toUShort()
        }
    }

    override fun desiredHeight(width: UShort): UShort {
        var maxHeight = 0u
        var widthRemaining = width.toInt()
        for ((childWidth, child) in children) {
            val w = childWidth.toInt().coerceAtMost(widthRemaining)
            if (w == 0) break
            val height = child.desiredHeight(w.toUShort())
            if (height > maxHeight) maxHeight = height
            widthRemaining -= w
        }
        return maxHeight.toUShort()
    }

    override fun cursorPos(area: Rect): Pair<UShort, UShort>? {
        var x = area.x
        for ((width, child) in children) {
            val availableWidth = (area.width.toInt() - (x.toInt() - area.x.toInt())).coerceAtLeast(0)
            val childArea = Rect(x, area.y, width.toInt().coerceAtMost(availableWidth).toUShort(), area.height)
            if (!childArea.isEmpty()) {
                val pos = child.cursorPos(childArea)
                if (pos != null) return pos
            }
            x = (x + width).toUShort()
        }
        return null
    }

    fun push(width: UShort, child: Renderable) {
        children.add(width to RenderableItem.Owned(child))
    }
}

class InsetRenderable(
    val child: RenderableItem,
    val insets: Insets
) : Renderable {
    override fun render(area: Rect, buf: Buffer) {
        child.render(area.inset(insets), buf)
    }

    override fun desiredHeight(width: UShort): UShort {
        val childWidth = (width.toInt() - insets.left.toInt() - insets.right.toInt()).coerceAtLeast(0).toUShort()
        return (child.desiredHeight(childWidth).toInt() + insets.top.toInt() + insets.bottom.toInt()).toUShort()
    }

    override fun cursorPos(area: Rect): Pair<UShort, UShort>? {
        return child.cursorPos(area.inset(insets))
    }
}

fun Renderable.inset(insets: Insets): RenderableItem {
    val child = if (this is RenderableItem) this else RenderableItem.Owned(this)
    return RenderableItem.Owned(InsetRenderable(child, insets))
}
