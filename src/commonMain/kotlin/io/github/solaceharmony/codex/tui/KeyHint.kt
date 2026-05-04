// port-lint: source tui/src/keyHint.rs
@file:OptIn(ExperimentalNativeApi::class)

package io.github.solaceharmony.codex.tui

import io.github.kotlinmania.crossterm.event.KeyCode
import io.github.kotlinmania.crossterm.event.KeyEvent
import io.github.kotlinmania.crossterm.event.KeyEventKind
import io.github.kotlinmania.crossterm.event.KeyModifiers
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.OsFamily
import kotlin.native.Platform
import ratatui.style.Modifier
import ratatui.style.Style
import ratatui.text.Span

private const val ALT_PREFIX_MACOS = "⌥ + "
private const val ALT_PREFIX_OTHER = "alt + "
private const val CTRL_PREFIX = "ctrl + "
private const val SHIFT_PREFIX = "shift + "

// Use macOS prefix by default; could be made platform-aware
private val ALT_PREFIX: String = run {
    // On macOS, import the option symbol; on other platforms, use "alt"
    if (Platform.osFamily == OsFamily.MACOSX) ALT_PREFIX_MACOS else ALT_PREFIX_OTHER
}

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
            is KeyCode.Enter -> "enter"
            is KeyCode.Up -> "↑"
            is KeyCode.Down -> "↓"
            is KeyCode.Left -> "←"
            is KeyCode.Right -> "→"
            is KeyCode.PageUp -> "pgup"
            is KeyCode.PageDown -> "pgdn"
            else -> key.toString().lowercase()
        }
        return Span("$modStr$keyStr", keyHintStyle())
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
    return Style(addModifier = ratatui.style.Modifier.DIM)
}

fun hasCtrlOrAlt(mods: KeyModifiers): Boolean {
    return (mods.contains(KeyModifiers.CONTROL) || mods.contains(KeyModifiers.ALT)) && !isAltgr(mods)
}

fun isAltgr(mods: KeyModifiers): Boolean {
    // AltGr is only relevant on Windows where it sends Alt+Ctrl together.
    // On non-Windows platforms, this always returns false.
    return kotlin.native.Platform.osFamily == OsFamily.WINDOWS &&
        mods.contains(KeyModifiers.ALT) && mods.contains(KeyModifiers.CONTROL)
}
