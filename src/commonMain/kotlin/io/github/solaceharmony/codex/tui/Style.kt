// port-lint: source tui/src/style.rs
package io.github.solaceharmony.codex.tui

import ratatui.style.Color
import ratatui.style.Style

fun userMessageStyle(): Style = userMessageStyleFor(defaultBg())

/**
 * Returns the style for a user-authored message using the provided terminal background.
 */
fun userMessageStyleFor(terminalBg: Triple<UByte, UByte, UByte>?): Style =
    when (terminalBg) {
        null -> Style.default()
        else -> Style.default().bg(userMessageBg(terminalBg))
    }

fun userMessageBg(terminalBg: Triple<UByte, UByte, UByte>): Color {
    val top = if (isLight(terminalBg)) {
        Triple(0.toUByte(), 0.toUByte(), 0.toUByte())
    } else {
        Triple(255.toUByte(), 255.toUByte(), 255.toUByte())
    }
    return bestColor(blend(top, terminalBg, 0.1f))
}
