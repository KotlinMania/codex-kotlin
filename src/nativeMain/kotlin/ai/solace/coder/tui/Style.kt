// port-lint: source tui/src/style.rs
package ai.solace.coder.tui

import ratatui.style.Color
import ratatui.style.Style

fun user_message_style(): Style = user_message_style_for(defaultBg())

/**
 * Returns the style for a user-authored message using the provided terminal background.
 */
fun user_message_style_for(terminal_bg: Triple<UByte, UByte, UByte>?): Style =
    when (terminal_bg) {
        null -> Style.default()
        else -> Style.default().bg(user_message_bg(terminal_bg))
    }

fun user_message_bg(terminal_bg: Triple<UByte, UByte, UByte>): Color {
    val top = if (isLight(terminal_bg)) {
        Triple(0.toUByte(), 0.toUByte(), 0.toUByte())
    } else {
        Triple(255.toUByte(), 255.toUByte(), 255.toUByte())
    }
    return bestColor(blend(top, terminal_bg, 0.1f))
}
