// port-lint: source core/src/terminal.rs
package ai.solace.coder.core.terminal

/**
 * Terminal detection and User-Agent generation.
 *
 * This module detects the terminal emulator being used and generates
 * an appropriate User-Agent string for HTTP requests.
 *
 * Ported from Rust codex-rs/core/src/terminal.rs
 */

// Lazy-initialized terminal detection
private val TERMINAL: String by lazy { detectTerminal() }

/**
 * Returns a User-Agent string identifying the terminal emulator.
 *
 * The string is detected once on first call and cached for subsequent calls.
 *
 * @return Terminal identifier string suitable for use in User-Agent headers
 */
fun userAgent(): String = TERMINAL

/**
 * Sanitize a header value to be used in a User-Agent string.
 *
 * This function replaces any characters that are not allowed in a User-Agent string with an underscore.
 *
 * @param value The value to sanitize
 * @return Sanitized value safe for use in HTTP headers
 */
private fun isValidHeaderValueChar(c: Char): Boolean {
    return c.isLetterOrDigit() || c == '-' || c == '_' || c == '.' || c == '/'
}

private fun sanitizeHeaderValue(value: String): String {
    return value.map { c ->
        if (isValidHeaderValueChar(c)) c else '_'
    }.joinToString("")
}

/**
 * Detect the terminal emulator from environment variables.
 *
 * Checks various terminal-specific environment variables in priority order:
 * 1. TERM_PROGRAM / TERM_PROGRAM_VERSION
 * 2. WEZTERM_VERSION
 * 3. KITTY_WINDOW_ID / TERM=*kitty*
 * 4. ALACRITTY_SOCKET / TERM=alacritty
 * 5. KONSOLE_VERSION
 * 6. GNOME_TERMINAL_SCREEN
 * 7. VTE_VERSION
 * 8. WT_SESSION (Windows Terminal)
 * 9. Falls back to TERM environment variable or "unknown"
 *
 * @return Detected terminal identifier
 */
private fun detectTerminal(): String {
    return sanitizeHeaderValue(
        when {
            // TERM_PROGRAM (iTerm2, VSCode, etc.)
            getEnvNonEmpty("TERM_PROGRAM") != null -> {
                val tp = getEnvNonEmpty("TERM_PROGRAM")!!
                val ver = getEnvNonEmpty("TERM_PROGRAM_VERSION")
                if (ver != null) "$tp/$ver" else tp
            }
            
            // WezTerm
            getEnvNonEmpty("WEZTERM_VERSION") != null -> {
                val v = getEnvNonEmpty("WEZTERM_VERSION")!!
                "WezTerm/$v"
            }
            
            // kitty
            getEnv("KITTY_WINDOW_ID") != null || 
            getEnv("TERM")?.contains("kitty") == true -> {
                "kitty"
            }
            
            // Alacritty
            getEnv("ALACRITTY_SOCKET") != null ||
            getEnv("TERM") == "alacritty" -> {
                "Alacritty"
            }
            
            // Konsole
            getEnvNonEmpty("KONSOLE_VERSION") != null -> {
                val v = getEnvNonEmpty("KONSOLE_VERSION")!!
                "Konsole/$v"
            }
            
            // GNOME Terminal
            getEnv("GNOME_TERMINAL_SCREEN") != null -> {
                "gnome-terminal"
            }
            
            // VTE-based terminals
            getEnvNonEmpty("VTE_VERSION") != null -> {
                val v = getEnvNonEmpty("VTE_VERSION")!!
                "VTE/$v"
            }
            
            // Windows Terminal
            getEnv("WT_SESSION") != null -> {
                "WindowsTerminal"
            }
            
            // Fallback to TERM
            else -> getEnv("TERM") ?: "unknown"
        }
    )
}

/**
 * Get environment variable value, or null if not set.
 * 
 * Note: This is a commonMain stub. Platform-specific implementations
 * should be provided in nativeMain/jvmMain as needed.
 */
private expect fun getEnv(name: String): String?

/**
 * Get environment variable value if non-empty, or null if not set or empty/whitespace.
 */
private fun getEnvNonEmpty(name: String): String? {
    return getEnv(name)?.trim()?.takeIf { it.isNotEmpty() }
}
