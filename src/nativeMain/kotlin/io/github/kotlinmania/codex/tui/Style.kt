// port-lint: source tui/src/style.rs
package io.github.kotlinmania.codex.tui

// Note: Ratatui types (Style, Color) will need to be ported separately.
// For now these stubs allow compilation and match the Rust module structure.

/** Placeholder for ratatui::style::Style. */
data class Style(val bg: Triple<Int, Int, Int>? = null) {
    companion object {
        fun default() = Style()
    }
}

fun userMessageStyle(): Style {
    return userMessageStyleFor(defaultBg())
}

/** Returns the style for a user-authored message using the provided terminal background. */
fun userMessageStyleFor(terminalBg: Triple<Int, Int, Int>?): Style {
    return when (terminalBg) {
        null -> Style.default()
        else -> Style(bg = userMessageBg(terminalBg))
    }
}

fun userMessageBg(terminalBg: Triple<Int, Int, Int>): Triple<Int, Int, Int> {
    val top = if (isLight(terminalBg)) {
        Triple(0, 0, 0)
    } else {
        Triple(255, 255, 255)
    }
    return bestColor(blend(top, terminalBg, 0.1f))
}

// Placeholder stubs for terminal_palette functions
// TODO: Port terminal_palette module (bestColor, defaultBg, etc.)
private fun bestColor(target: Triple<Int, Int, Int>): Triple<Int, Int, Int> = target
private fun defaultBg(): Triple<Int, Int, Int>? = null
