// port-lint: source tui/src/style.rs
package ai.solace.coder.tui

import ai.solace.coder.tui.color.blend
import ai.solace.coder.tui.color.isLight
import ai.solace.coder.tui.terminal_palette.bestColor
import ai.solace.coder.tui.terminal_palette.defaultBg
import ratatui.style.Color
import ratatui.style.Style

fun userMessageStyle(): Style {
    return userMessageStyleFor(defaultBg())
}

/** Returns the style for a user-authored message using the provided terminal background. */
fun userMessageStyleFor(terminalBg: Triple<UByte, UShort, UShort>?): Style { // Using Triple as (u8, u8, u8) placeholder
    return when (terminalBg) {
        null -> Style.default()
        else -> Style.default().bg(userMessageBg(terminalBg))
    }
}

fun userMessageBg(terminalBg: Triple<UByte, UShort, UShort>): Color {
    val top = if (isLight(terminalBg)) {
        Triple(0u.toUByte(), 0u.toUShort(), 0u.toUShort())
    } else {
        Triple(255u.toUByte(), 255u.toUShort(), 255u.toUShort())
    }
    return bestColor(blend(top, terminalBg, 0.1))
}
