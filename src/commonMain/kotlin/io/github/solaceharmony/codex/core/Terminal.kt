// port-lint: source core/src/terminal.rs
package io.github.solaceharmony.codex.core

import io.github.solaceharmony.codex.utils.Environment

private var cachedTerminal: String? = null

fun userAgent(): String {
    val cached = cachedTerminal
    if (cached != null) return cached
    val detected = detectTerminal()
    cachedTerminal = detected
    return detected
}

/**
 * Sanitize a header value to be used in a User-Agent string.
 *
 * This function replaces any characters that are not allowed in a User-Agent string with an underscore.
 *
 * @param c The character to check.
 */
private fun isValidHeaderValueChar(c: Char): Boolean {
    return c.isLetterOrDigit() || c == '-' || c == '_' || c == '.' || c == '/'
}

private fun sanitizeHeaderValue(value: String): String {
    return value.map { if (isValidHeaderValueChar(it)) it else '_' }.joinToString("")
}

private fun envVar(name: String): String? = Environment.get(name)?.takeIf { it.isNotBlank() }

private fun envVarExists(name: String): Boolean = Environment.isSet(name)

private fun detectTerminal(): String {
    val raw = run {
        val tp = envVar("TERM_PROGRAM")
        if (tp != null) {
            val ver = envVar("TERM_PROGRAM_VERSION")
            return@run if (ver != null) "$tp/$ver" else tp
        }

        val wezVer = envVar("WEZTERM_VERSION")
        if (wezVer != null) {
            return@run "WezTerm/$wezVer"
        }
        if (envVarExists("WEZTERM_VERSION")) {
            return@run "WezTerm"
        }

        if (envVarExists("KITTY_WINDOW_ID") ||
            (Environment.get("TERM")?.contains("kitty") == true)
        ) {
            return@run "kitty"
        }

        if (envVarExists("ALACRITTY_SOCKET") ||
            (Environment.get("TERM") == "alacritty")
        ) {
            return@run "Alacritty"
        }

        val konsoleVer = envVar("KONSOLE_VERSION")
        if (konsoleVer != null) {
            return@run "Konsole/$konsoleVer"
        }
        if (envVarExists("KONSOLE_VERSION")) {
            return@run "Konsole"
        }

        if (envVarExists("GNOME_TERMINAL_SCREEN")) {
            return@run "gnome-terminal"
        }

        val vteVer = envVar("VTE_VERSION")
        if (vteVer != null) {
            return@run "VTE/$vteVer"
        }
        if (envVarExists("VTE_VERSION")) {
            return@run "VTE"
        }

        if (envVarExists("WT_SESSION")) {
            return@run "WindowsTerminal"
        }

        Environment.get("TERM") ?: "unknown"
    }

    return sanitizeHeaderValue(raw)
}
