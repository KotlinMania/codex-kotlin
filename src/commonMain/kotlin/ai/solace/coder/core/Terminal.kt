// port-lint: source core/src/terminal.rs
package ai.solace.coder.core

import ai.solace.coder.utils.Environment
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private val terminalMutex = Mutex()
private var terminalValue: String? = null

/**
 * Returns the detected terminal name and version for use in User-Agent strings.
 *
 * Ported from Rust codex-rs/core/src/terminal.rs
 */
suspend fun userAgent(): String {
    terminalMutex.withLock {
        if (terminalValue == null) {
            terminalValue = detectTerminal()
        }
        return terminalValue!!
    }
}

/**
 * Sanitize a header value to be used in a User-Agent string.
 *
 * This function replaces any characters that are not allowed in a User-Agent string with an underscore.
 */
private fun isValidHeaderValueChar(c: Char): Boolean {
    return c.isLetterOrDigit() || c == '-' || c == '_' || c == '.' || c == '/'
}

private fun sanitizeHeaderValue(value: String): String {
    return value.map { if (isValidHeaderValueChar(it)) it else '_' }.joinToString("")
}

private fun detectTerminal(): String {
    val termProgram = Environment.get("TERM_PROGRAM")
    val detected = if (!termProgram.isNullOrBlank()) {
        val ver = Environment.get("TERM_PROGRAM_VERSION")
        if (!ver.isNullOrBlank()) {
            "$termProgram/$ver"
        } else {
            termProgram
        }
    } else {
        val weztermVersion = Environment.get("WEZTERM_VERSION")
        if (!weztermVersion.isNullOrBlank()) {
            "WezTerm/$weztermVersion"
        } else if (Environment.get("KITTY_WINDOW_ID") != null ||
            Environment.get("TERM")?.contains("kitty") == true
        ) {
            "kitty"
        } else if (Environment.get("ALACRITTY_SOCKET") != null ||
            Environment.get("TERM") == "alacritty"
        ) {
            "Alacritty"
        } else {
            val konsoleVersion = Environment.get("KONSOLE_VERSION")
            if (!konsoleVersion.isNullOrBlank()) {
                "Konsole/$konsoleVersion"
            } else if (Environment.get("GNOME_TERMINAL_SCREEN") != null) {
                "gnome-terminal"
            } else {
                val vteVersion = Environment.get("VTE_VERSION")
                if (!vteVersion.isNullOrBlank()) {
                    "VTE/$vteVersion"
                } else if (Environment.get("WT_SESSION") != null) {
                    "WindowsTerminal"
                } else {
                    Environment.get("TERM") ?: "unknown"
                }
            }
        }
    }
    return sanitizeHeaderValue(detected)
}
