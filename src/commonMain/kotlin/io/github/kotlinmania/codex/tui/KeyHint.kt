// port-lint: source tui/src/key_hint.rs
package io.github.kotlinmania.codex.tui

import io.github.kotlinmania.crossterm.event.KeyCode
import io.github.kotlinmania.crossterm.event.KeyEvent
import io.github.kotlinmania.crossterm.event.KeyEventKind
import io.github.kotlinmania.crossterm.event.KeyModifiers
import ratatui.style.Modifier
import ratatui.style.Style
import ratatui.text.Span

private const val ALT_PREFIX = "⌥ + "
private const val CTRL_PREFIX = "ctrl + "
private const val SHIFT_PREFIX = "shift + "

data class KeyBinding(
    val key: KeyCode,
    val modifiers: KeyModifiers,
) {
    fun isPress(event: KeyEvent): Boolean {
        return key == event.code &&
            modifiers == event.modifiers &&
            (event.kind == KeyEventKind.Press || event.kind == KeyEventKind.Repeat)
    }

    fun toSpan(): Span {
        val modStr = modifiersToString(modifiers)
        val keyStr = when (key) {
            KeyCode.Enter -> "enter"
            KeyCode.Up -> "↑"
            KeyCode.Down -> "↓"
            KeyCode.Left -> "←"
            KeyCode.Right -> "→"
            KeyCode.PageUp -> "pgup"
            KeyCode.PageDown -> "pgdn"
            else -> key.toString().lowercase()
        }
        return Span.styled("$modStr$keyStr", keyHintStyle())
    }

    companion object {
        fun new(key: KeyCode, modifiers: KeyModifiers): KeyBinding {
            return KeyBinding(key = key, modifiers = modifiers)
        }
    }
}

fun plain(key: KeyCode): KeyBinding {
    return KeyBinding(key = key, modifiers = KeyModifiers.NONE)
}

fun alt(key: KeyCode): KeyBinding {
    return KeyBinding(key = key, modifiers = KeyModifiers.ALT)
}

fun shift(key: KeyCode): KeyBinding {
    return KeyBinding(key = key, modifiers = KeyModifiers.SHIFT)
}

fun ctrl(key: KeyCode): KeyBinding {
    return KeyBinding(key = key, modifiers = KeyModifiers.CONTROL)
}

private fun modifiersToString(modifiers: KeyModifiers): String {
    val result = StringBuilder()
    if (modifiers.contains(KeyModifiers.CONTROL)) {
        result.append(CTRL_PREFIX)
    }
    if (modifiers.contains(KeyModifiers.SHIFT)) {
        result.append(SHIFT_PREFIX)
    }
    if (modifiers.contains(KeyModifiers.ALT)) {
        result.append(ALT_PREFIX)
    }
    return result.toString()
}

private fun keyHintStyle(): Style {
    return Style(addModifier = Modifier.DIM)
}

fun hasCtrlOrAlt(mods: KeyModifiers): Boolean {
    return (mods.contains(KeyModifiers.CONTROL) || mods.contains(KeyModifiers.ALT)) && !isAltgr(mods)
}

fun isAltgr(mods: KeyModifiers): Boolean {
    return mods.contains(KeyModifiers.ALT) && mods.contains(KeyModifiers.CONTROL)
}
