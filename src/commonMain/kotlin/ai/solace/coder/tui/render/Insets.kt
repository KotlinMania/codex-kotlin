// port-lint: source tui/src/render/mod.rs
package ai.solace.coder.tui.render

import io.github.kotlinmania.ratatui.layout.Rect

data class Insets(
    val left: UShort = 0u,
    val top: UShort = 0u,
    val right: UShort = 0u,
    val bottom: UShort = 0u
) {
    companion object {
        fun tlbr(top: UShort, left: UShort, bottom: UShort, right: UShort): Insets {
            return Insets(top = top, left = left, bottom = bottom, right = right)
        }

        fun vh(v: UShort, h: UShort): Insets {
            return Insets(top = v, left = h, bottom = v, right = h)
        }
    }
}

fun Rect.inset(insets: Insets): Rect {
    val horizontal = (insets.left + insets.right).toUShort()
    val vertical = (insets.top + insets.bottom).toUShort()
    
    return Rect(
        x = (this.x + insets.left).toUShort(),
        y = (this.y + insets.top).toUShort(),
        width = (this.width.toInt() - horizontal.toInt()).coerceAtLeast(0).toUShort(),
        height = (this.height.toInt() - vertical.toInt()).coerceAtLeast(0).toUShort()
    )
}
