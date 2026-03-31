// port-lint: source tui/src/key_hint.rs
package ai.solace.coder.tui

import io.github.kotlinmania.crossterm.event.KeyCode
import io.github.kotlinmania.crossterm.event.KeyEvent
import io.github.kotlinmania.crossterm.event.KeyEventKind
import io.github.kotlinmania.crossterm.event.KeyModifiers
import kotlin.native.OsFamily
import kotlin.native.Platform

private val ALT_PREFIX: String =
    if (Platform.osFamily == OsFamily.MACOSX) "\u2325 + " else "alt + "
private const val CTRL_PREFIX: String = "ctrl + "
private const val SHIFT_PREFIX: String = "shift + "

/**
 * A key binding that pairs a [KeyCode] with zero or more [KeyModifiers].
 *
 * Ported from Rust codex-rs/tui/src/key_hint.rs
 */
data class KeyBinding(
    val key: KeyCode,
    val modifiers: KeyModifiers,
) {
    /**
     * Returns `true` when [event] matches this binding and the event kind is
     * [KeyEventKind.Press] or [KeyEventKind.Repeat].
     */
    fun isPress(event: KeyEvent): Boolean {
        return key == event.code
            && modifiers == event.modifiers
            && (event.kind == KeyEventKind.Press || event.kind == KeyEventKind.Repeat)
    }

    /**
     * Returns a human-readable display string for this key binding.
     * Equivalent of the Rust `From<&KeyBinding> for Span` conversion.
     */
    fun toDisplayString(): String {
        val mods = modifiersToString(modifiers)
        val keyStr = when (key) {
            is KeyCode.Enter -> "enter"
            is KeyCode.Char -> if (key.char == ' ') "space" else key.char.lowercaseChar().toString()
            is KeyCode.Up -> "\u2191"
            is KeyCode.Down -> "\u2193"
            is KeyCode.Left -> "\u2190"
            is KeyCode.Right -> "\u2192"
            is KeyCode.PageUp -> "pgup"
            is KeyCode.PageDown -> "pgdn"
            else -> key.toString().lowercase()
        }
        return "$mods$keyStr"
    }
}

/** A binding with no modifiers. */
fun plain(key: KeyCode): KeyBinding = KeyBinding(key, KeyModifiers.NONE)

/** A binding with the Alt modifier. */
fun alt(key: KeyCode): KeyBinding = KeyBinding(key, KeyModifiers.ALT)

/** A binding with the Shift modifier. */
fun shift(key: KeyCode): KeyBinding = KeyBinding(key, KeyModifiers.SHIFT)

/** A binding with the Control modifier. */
fun ctrl(key: KeyCode): KeyBinding = KeyBinding(key, KeyModifiers.CONTROL)

/** A binding with Control + Alt modifiers. */
fun ctrlAlt(key: KeyCode): KeyBinding = KeyBinding(key, KeyModifiers.CONTROL + KeyModifiers.ALT)

/**
 * Builds the human-readable prefix string for the given [modifiers].
 * Order matches the Rust implementation: Ctrl, Shift, Alt.
 */
private fun modifiersToString(modifiers: KeyModifiers): String {
    val result = StringBuilder()
    if (KeyModifiers.CONTROL in modifiers) {
        result.append(CTRL_PREFIX)
    }
    if (KeyModifiers.SHIFT in modifiers) {
        result.append(SHIFT_PREFIX)
    }
    if (KeyModifiers.ALT in modifiers) {
        result.append(ALT_PREFIX)
    }
    return result.toString()
}

/**
 * Returns `true` when [mods] include Control or Alt, but not AltGr.
 */
fun hasCtrlOrAlt(mods: KeyModifiers): Boolean {
    return (KeyModifiers.CONTROL in mods || KeyModifiers.ALT in mods) && !isAltGr(mods)
}

/**
 * Returns `true` when [mods] represent the AltGr key combination.
 * On Windows, AltGr sends Ctrl+Alt; on other platforms this always returns `false`.
 */
fun isAltGr(@Suppress("UNUSED_PARAMETER") mods: KeyModifiers): Boolean {
    // On non-Windows platforms, AltGr is not emulated via Ctrl+Alt.
    return false
}
