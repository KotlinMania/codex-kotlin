// port-lint: source tui/src/render/mod.rs
package io.github.kotlinmania.codex.tui.render

import ratatui.layout.Rect

internal data class Insets(
    val left: Int = 0,
    val top: Int = 0,
    val right: Int = 0,
    val bottom: Int = 0,
) {
    init {
        require(left >= 0) { "left inset must be non-negative" }
        require(top >= 0) { "top inset must be non-negative" }
        require(right >= 0) { "right inset must be non-negative" }
        require(bottom >= 0) { "bottom inset must be non-negative" }
    }

    companion object {
        fun tlbr(top: Int, left: Int, bottom: Int, right: Int): Insets {
            return Insets(top = top, left = left, bottom = bottom, right = right)
        }

        fun vh(v: Int, h: Int): Insets {
            return Insets(top = v, left = h, bottom = v, right = h)
        }
    }
}

internal fun Rect.inset(insets: Insets): Rect {
    val horizontal = saturatingAdd(insets.left, insets.right)
    val vertical = saturatingAdd(insets.top, insets.bottom)
    return Rect(
        x = saturatingAdd(x, insets.left),
        y = saturatingAdd(y, insets.top),
        width = saturatingSubtract(width, horizontal),
        height = saturatingSubtract(height, vertical),
    )
}

private fun saturatingAdd(left: Int, right: Int): Int {
    return (left.toLong() + right.toLong()).coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
}

private fun saturatingSubtract(left: Int, right: Int): Int {
    return (left.toLong() - right.toLong()).coerceAtLeast(0L).toInt()
}
