// port-lint: source tui/src/render/mod.rs
package ai.solace.coder.tui.render

import ratatui.layout.Rect

data class Insets(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
) {
    companion object {
        fun tlbr(top: Int, left: Int, bottom: Int, right: Int): Insets {
            return Insets(top = top, left = left, bottom = bottom, right = right)
        }

        fun vh(v: Int, h: Int): Insets {
            return Insets(top = v, left = h, bottom = v, right = h)
        }
    }
}

fun Rect.inset(insets: Insets): Rect {
    val horizontal = insets.left + insets.right
    val vertical = insets.top + insets.bottom
    return Rect(
        x = x + insets.left,
        y = y + insets.top,
        width = (width - horizontal).coerceAtLeast(0),
        height = (height - vertical).coerceAtLeast(0),
    )
}
