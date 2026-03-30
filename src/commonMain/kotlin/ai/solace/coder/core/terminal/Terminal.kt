// port-lint: source core/src/terminal.rs
package ai.solace.coder.core.terminal

/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * Copyright (c) 2025 Sydney Renee, The Solace Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Terminal detection utilities.
 *
 * This module feeds terminal metadata into OpenTelemetry user-agent logging and into
 * terminal-specific configuration choices in the TUI.
 */

import ai.solace.coder.utils.Environment

/**
 * Structured terminal identification data.
 */
data class TerminalInfo(
    /** The detected terminal name category. */
    val name: TerminalName,
    /** The `TERM_PROGRAM` value when provided by the terminal. */
    val termProgram: String?,
    /** The terminal version string when available. */
    val version: String?,
    /** The `TERM` value when falling back to capability strings. */
    val term: String?,
    /** Multiplexer metadata when a terminal multiplexer is active. */
    val multiplexer: Multiplexer?,
) {
    /** Creates terminal metadata from a `TERM_PROGRAM` match. */
    companion object {
        internal fun fromTermProgram(
            name: TerminalName,
            termProgram: String,
            version: String?,
            multiplexer: Multiplexer?,
        ): TerminalInfo = TerminalInfo(
            name = name,
            termProgram = termProgram,
            version = version,
            term = null,
            multiplexer = multiplexer,
        )

        /** Creates terminal metadata from a `TERM_PROGRAM` match plus a `TERM` value. */
        internal fun fromTermProgramAndTerm(
            name: TerminalName,
            termProgram: String,
            version: String?,
            term: String?,
            multiplexer: Multiplexer?,
        ): TerminalInfo = TerminalInfo(
            name = name,
            termProgram = termProgram,
            version = version,
            term = term,
            multiplexer = multiplexer,
        )

        /** Creates terminal metadata from a known terminal name and optional version. */
        internal fun fromName(
            name: TerminalName,
            version: String?,
            multiplexer: Multiplexer?,
        ): TerminalInfo = TerminalInfo(
            name = name,
            termProgram = null,
            version = version,
            term = null,
            multiplexer = multiplexer,
        )

        /** Creates terminal metadata from a `TERM` capability value. */
        internal fun fromTerm(
            term: String,
            multiplexer: Multiplexer?,
        ): TerminalInfo = TerminalInfo(
            name = TerminalName.Unknown,
            termProgram = null,
            version = null,
            term = term,
            multiplexer = multiplexer,
        )

        /** Creates terminal metadata for unknown terminals. */
        internal fun unknown(multiplexer: Multiplexer?): TerminalInfo = TerminalInfo(
            name = TerminalName.Unknown,
            termProgram = null,
            version = null,
            term = null,
            multiplexer = multiplexer,
        )
    }

    /** Formats the terminal info as a User-Agent token. */
    fun userAgentToken(): String {
        val raw = if (termProgram != null) {
            val v = version?.takeIf { it.isNotEmpty() }
            if (v != null) "$termProgram/$v" else termProgram
        } else if (term != null && term.isNotEmpty()) {
            term
        } else {
            when (name) {
                TerminalName.AppleTerminal -> formatTerminalVersion("Apple_Terminal", version)
                TerminalName.Ghostty -> formatTerminalVersion("Ghostty", version)
                TerminalName.Iterm2 -> formatTerminalVersion("iTerm.app", version)
                TerminalName.WarpTerminal -> formatTerminalVersion("WarpTerminal", version)
                TerminalName.VsCode -> formatTerminalVersion("vscode", version)
                TerminalName.WezTerm -> formatTerminalVersion("WezTerm", version)
                TerminalName.Kitty -> "kitty"
                TerminalName.Alacritty -> "Alacritty"
                TerminalName.Konsole -> formatTerminalVersion("Konsole", version)
                TerminalName.GnomeTerminal -> "gnome-terminal"
                TerminalName.Vte -> formatTerminalVersion("VTE", version)
                TerminalName.WindowsTerminal -> "WindowsTerminal"
                TerminalName.Unknown -> "unknown"
            }
        }

        return sanitizeHeaderValue(raw)
    }
}

/**
 * Known terminal name categories derived from environment variables.
 */
enum class TerminalName {
    /** Apple Terminal (Terminal.app). */
    AppleTerminal,
    /** Ghostty terminal emulator. */
    Ghostty,
    /** iTerm2 terminal emulator. */
    Iterm2,
    /** Warp terminal emulator. */
    WarpTerminal,
    /** Visual Studio Code integrated terminal. */
    VsCode,
    /** WezTerm terminal emulator. */
    WezTerm,
    /** kitty terminal emulator. */
    Kitty,
    /** Alacritty terminal emulator. */
    Alacritty,
    /** KDE Konsole terminal emulator. */
    Konsole,
    /** GNOME Terminal emulator. */
    GnomeTerminal,
    /** VTE backend terminal. */
    Vte,
    /** Windows Terminal emulator. */
    WindowsTerminal,
    /** Unknown or missing terminal identification. */
    Unknown,
}

/**
 * Detected terminal multiplexer metadata.
 */
sealed class Multiplexer {
    /** tmux terminal multiplexer. */
    data class Tmux(
        /**
         * tmux version string when `TERM_PROGRAM=tmux` is available.
         *
         * This is derived from `TERM_PROGRAM_VERSION`.
         */
        val version: String?,
    ) : Multiplexer()

    /** zellij terminal multiplexer. */
    data object Zellij : Multiplexer()
}

/**
 * tmux client terminal identification captured via `tmux display-message`.
 *
 * [termtype] corresponds to `#{client_termtype}` and typically reflects the
 * underlying terminal program (for example, `ghostty` or `wezterm`) with an
 * optional version suffix. [termname] comes from `#{client_termname}` and
 * preserves the TERM capability string exposed by the client (for example,
 * `xterm-256color`).
 *
 * This information is only available when running under tmux and lets us
 * attribute the session to the underlying terminal rather than to tmux itself.
 */
internal data class TmuxClientInfo(
    val termtype: String? = null,
    val termname: String? = null,
)

/**
 * Environment variable access used by terminal detection.
 *
 * This interface exists to allow faking the environment in tests.
 */
internal interface TerminalEnvironment {
    /** Returns an environment variable when set. */
    fun get(name: String): String?

    /** Returns whether an environment variable is set. */
    fun has(name: String): Boolean = get(name) != null

    /** Returns a non-empty environment variable. */
    fun getNonEmpty(name: String): String? = get(name)?.let { noneIfWhitespace(it) }

    /** Returns whether an environment variable is set and non-empty. */
    fun hasNonEmpty(name: String): Boolean = getNonEmpty(name) != null

    /** Returns tmux client details when available. */
    fun tmuxClientInfo(): TmuxClientInfo
}

/**
 * Reads environment variables from the running process.
 */
internal object ProcessTerminalEnvironment : TerminalEnvironment {
    override fun get(name: String): String? = Environment.get(name)

    override fun tmuxClientInfo(): TmuxClientInfo = tmuxClientInfoFromProcess()
}

// Lazy-initialized terminal info
private val TERMINAL_INFO: TerminalInfo by lazy {
    detectTerminalInfoFromEnv(ProcessTerminalEnvironment)
}

/**
 * Returns a sanitized terminal identifier for User-Agent strings.
 */
fun userAgent(): String = terminalInfo().userAgentToken()

/**
 * Returns structured terminal metadata for the current process.
 */
fun terminalInfo(): TerminalInfo = TERMINAL_INFO

/**
 * Detects structured terminal metadata from an injectable environment.
 *
 * Detection order favors explicit identifiers before falling back to capability strings:
 * - If `TERM_PROGRAM=tmux`, the tmux client term type/name are used instead. The client term
 *   type is split on whitespace to extract a program name plus optional version (for example,
 *   `ghostty 1.2.3`), while the client term name becomes the `TERM` capability string.
 * - Otherwise, `TERM_PROGRAM` (plus `TERM_PROGRAM_VERSION`) drives the detected terminal name.
 * - Next, terminal-specific variables (WEZTERM, iTerm2, Apple Terminal, kitty, etc.) are checked.
 * - Finally, `TERM` is used as the capability fallback with [TerminalName.Unknown].
 *
 * tmux client term info is only consulted when a tmux multiplexer is detected, and it is
 * derived from `tmux display-message` to surface the underlying terminal program instead of
 * reporting tmux itself.
 */
internal fun detectTerminalInfoFromEnv(env: TerminalEnvironment): TerminalInfo {
    val multiplexer = detectMultiplexer(env)

    val termProgram = env.getNonEmpty("TERM_PROGRAM")
    if (termProgram != null) {
        if (isTmuxTermProgram(termProgram)
            && multiplexer is Multiplexer.Tmux
        ) {
            val terminal = terminalFromTmuxClientInfo(env.tmuxClientInfo(), multiplexer)
            if (terminal != null) {
                return terminal
            }
        }

        val version = env.getNonEmpty("TERM_PROGRAM_VERSION")
        val name = terminalNameFromTermProgram(termProgram) ?: TerminalName.Unknown
        return TerminalInfo.fromTermProgram(name, termProgram, version, multiplexer)
    }

    if (env.has("WEZTERM_VERSION")) {
        val version = env.getNonEmpty("WEZTERM_VERSION")
        return TerminalInfo.fromName(TerminalName.WezTerm, version, multiplexer)
    }

    if (env.has("ITERM_SESSION_ID") || env.has("ITERM_PROFILE") || env.has("ITERM_PROFILE_NAME")) {
        return TerminalInfo.fromName(TerminalName.Iterm2, null, multiplexer)
    }

    if (env.has("TERM_SESSION_ID")) {
        return TerminalInfo.fromName(TerminalName.AppleTerminal, null, multiplexer)
    }

    if (env.has("KITTY_WINDOW_ID")
        || (env.get("TERM")?.contains("kitty") == true)
    ) {
        return TerminalInfo.fromName(TerminalName.Kitty, null, multiplexer)
    }

    if (env.has("ALACRITTY_SOCKET")
        || (env.get("TERM") == "alacritty")
    ) {
        return TerminalInfo.fromName(TerminalName.Alacritty, null, multiplexer)
    }

    if (env.has("KONSOLE_VERSION")) {
        val version = env.getNonEmpty("KONSOLE_VERSION")
        return TerminalInfo.fromName(TerminalName.Konsole, version, multiplexer)
    }

    if (env.has("GNOME_TERMINAL_SCREEN")) {
        return TerminalInfo.fromName(TerminalName.GnomeTerminal, null, multiplexer)
    }

    if (env.has("VTE_VERSION")) {
        val version = env.getNonEmpty("VTE_VERSION")
        return TerminalInfo.fromName(TerminalName.Vte, version, multiplexer)
    }

    if (env.has("WT_SESSION")) {
        return TerminalInfo.fromName(TerminalName.WindowsTerminal, null, multiplexer)
    }

    val term = env.getNonEmpty("TERM")
    if (term != null) {
        return TerminalInfo.fromTerm(term, multiplexer)
    }

    return TerminalInfo.unknown(multiplexer)
}

private fun detectMultiplexer(env: TerminalEnvironment): Multiplexer? {
    if (env.hasNonEmpty("TMUX") || env.hasNonEmpty("TMUX_PANE")) {
        return Multiplexer.Tmux(
            version = tmuxVersionFromEnv(env),
        )
    }

    if (env.hasNonEmpty("ZELLIJ")
        || env.hasNonEmpty("ZELLIJ_SESSION_NAME")
        || env.hasNonEmpty("ZELLIJ_VERSION")
    ) {
        return Multiplexer.Zellij
    }

    return null
}

private fun isTmuxTermProgram(value: String): Boolean =
    value.equals("tmux", ignoreCase = true)

private fun terminalFromTmuxClientInfo(
    clientInfo: TmuxClientInfo,
    multiplexer: Multiplexer?,
): TerminalInfo? {
    val termtype = clientInfo.termtype?.let { noneIfWhitespace(it) }
    val termname = clientInfo.termname?.let { noneIfWhitespace(it) }

    if (termtype != null) {
        val (program, version) = splitTermProgramAndVersion(termtype)
        val name = terminalNameFromTermProgram(program) ?: TerminalName.Unknown
        return TerminalInfo.fromTermProgramAndTerm(
            name,
            program,
            version,
            termname,
            multiplexer,
        )
    }

    return termname?.let { TerminalInfo.fromTerm(it, multiplexer) }
}

private fun tmuxVersionFromEnv(env: TerminalEnvironment): String? {
    val termProgram = env.get("TERM_PROGRAM") ?: return null
    if (!isTmuxTermProgram(termProgram)) {
        return null
    }
    return env.getNonEmpty("TERM_PROGRAM_VERSION")
}

private fun splitTermProgramAndVersion(value: String): Pair<String, String?> {
    val parts = value.trim().split("\\s+".toRegex())
    val program = parts.firstOrNull() ?: ""
    val version = parts.getOrNull(1)
    return Pair(program, version)
}

private fun tmuxClientInfoFromProcess(): TmuxClientInfo {
    val termtype = tmuxDisplayMessage("#{client_termtype}")
    val termname = tmuxDisplayMessage("#{client_termname}")
    return TmuxClientInfo(termtype = termtype, termname = termname)
}

/**
 * Runs `tmux display-message -p <format>` and returns the trimmed output,
 * or null if tmux is not available or the command fails.
 */
internal expect fun tmuxDisplayMessage(format: String): String?

/**
 * Sanitizes a terminal token for use in User-Agent headers.
 *
 * Invalid header characters are replaced with underscores.
 */
private fun sanitizeHeaderValue(value: String): String =
    value.map { c -> if (isValidHeaderValueChar(c)) c else '_' }.joinToString("")

/** Returns whether a character is allowed in User-Agent header values. */
private fun isValidHeaderValueChar(c: Char): Boolean =
    c.isLetterOrDigit() || c == '-' || c == '_' || c == '.' || c == '/'

internal fun terminalNameFromTermProgram(value: String): TerminalName? {
    val normalized = value.trim()
        .filter { c -> c != ' ' && c != '-' && c != '_' && c != '.' }
        .lowercase()

    return when (normalized) {
        "appleterminal" -> TerminalName.AppleTerminal
        "ghostty" -> TerminalName.Ghostty
        "iterm", "iterm2", "itermapp" -> TerminalName.Iterm2
        "warp", "warpterminal" -> TerminalName.WarpTerminal
        "vscode" -> TerminalName.VsCode
        "wezterm" -> TerminalName.WezTerm
        "kitty" -> TerminalName.Kitty
        "alacritty" -> TerminalName.Alacritty
        "konsole" -> TerminalName.Konsole
        "gnometerminal" -> TerminalName.GnomeTerminal
        "vte" -> TerminalName.Vte
        "windowsterminal" -> TerminalName.WindowsTerminal
        else -> null
    }
}

private fun formatTerminalVersion(name: String, version: String?): String {
    val v = version?.takeIf { it.isNotEmpty() }
    return if (v != null) "$name/$v" else name
}

private fun noneIfWhitespace(value: String): String? =
    value.trim().takeIf { it.isNotEmpty() }
