// port-lint: source tui/src/key_hint.rs
package ai.solace.coder.tui

/**
 * Key bindings and key hint rendering for the TUI.
 *
 * This module provides keyboard input handling and generates styled key hint text
 * for display in the UI (e.g., "ctrl + c", "⌥ + q").
 *
 * Ported from Rust codex-rs/tui/src/key_hint.rs
 */

// Platform-specific ALT key prefix
private val ALT_PREFIX: String = when {
    // TODO: Detect actual platform at compile time
    // For now, use macOS-style symbol
    true -> "⌥ + "  // macOS
    // else -> "alt + "  // Linux/Windows
}

private const val CTRL_PREFIX = "ctrl + "
private const val SHIFT_PREFIX = "shift + "

/**
 * Key code enumeration matching crossterm's KeyCode.
 * TODO: This is a simplified stub. Port full crossterm key handling.
 */
enum class KeyCode {
    Enter,
    Up,
    Down,
    Left,
    Right,
    PageUp,
    PageDown,
    Char,  // Placeholder for character keys
    Other; // Placeholder for other keys
    
    override fun toString(): String = name.lowercase()
}

/**
 * Key modifiers bitflags matching crossterm's KeyModifiers.
 * TODO: Implement as actual bitflags when porting crossterm.
 */
data class KeyModifiers(val bits: Int) {
    fun contains(other: KeyModifiers): Boolean = (bits and other.bits) == other.bits
    
    companion object {
        val NONE = KeyModifiers(0)
        val SHIFT = KeyModifiers(1 shl 0)
        val CONTROL = KeyModifiers(1 shl 1)
        val ALT = KeyModifiers(1 shl 2)
    }
}

/**
 * Key event kind (Press, Repeat, Release).
 * TODO: Port full crossterm KeyEventKind.
 */
enum class KeyEventKind {
    Press,
    Repeat,
    Release
}

/**
 * Key event from terminal input.
 * TODO: Port full crossterm KeyEvent.
 */
data class KeyEvent(
    val code: KeyCode,
    val modifiers: KeyModifiers,
    val kind: KeyEventKind
)

/**
 * A keyboard binding consisting of a key and modifier keys.
 */
data class KeyBinding(
    val key: KeyCode,
    val modifiers: KeyModifiers
) {
    /**
     * Check if this binding matches a key press or repeat event.
     */
    fun isPress(event: KeyEvent): Boolean {
        return this.key == event.code &&
               this.modifiers == event.modifiers &&
               (event.kind == KeyEventKind.Press || event.kind == KeyEventKind.Repeat)
    }
    
    /**
     * Convert binding to display string (e.g., "ctrl + c").
     */
    fun toDisplayString(): String {
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
        return "$modStr$keyStr"
    }
}

/**
 * Create a plain key binding (no modifiers).
 */
fun plain(key: KeyCode): KeyBinding = KeyBinding(key, KeyModifiers.NONE)

/**
 * Create an Alt+key binding.
 */
fun alt(key: KeyCode): KeyBinding = KeyBinding(key, KeyModifiers.ALT)

/**
 * Create a Shift+key binding.
 */
fun shift(key: KeyCode): KeyBinding = KeyBinding(key, KeyModifiers.SHIFT)

/**
 * Create a Ctrl+key binding.
 */
fun ctrl(key: KeyCode): KeyBinding = KeyBinding(key, KeyModifiers.CONTROL)

/**
 * Convert modifiers to prefix string (e.g., "ctrl + ", "⌥ + shift + ").
 */
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

/**
 * Check if modifiers include Ctrl or Alt (but not AltGr).
 */
fun hasCtrlOrAlt(mods: KeyModifiers): Boolean {
    return (mods.contains(KeyModifiers.CONTROL) || mods.contains(KeyModifiers.ALT)) && !isAltGr(mods)
}

/**
 * Check if modifiers represent AltGr (Alt+Ctrl on Windows).
 */
fun isAltGr(mods: KeyModifiers): Boolean {
    // TODO: Detect Windows platform at compile time
    // For now, assume non-Windows
    return false
    // On Windows: mods.contains(KeyModifiers.ALT) && mods.contains(KeyModifiers.CONTROL)
}
