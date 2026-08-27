// port-lint: source terminal-detection/src/lib.rs
package io.github.kotlinmania.codex.terminal

/**
 * Known terminal name categories derived from environment variables.
 */
public enum class TerminalName {
    AppleTerminal,
    Ghostty,
    Iterm2,
    WarpTerminal,
    VsCode,
    WezTerm,
    Kitty,
    Alacritty,
    Konsole,
    GnomeTerminal,
    Vte,
    WindowsTerminal,
    Dumb,
    Unknown,
}

/**
 * Detected terminal multiplexer metadata.
 */
public sealed interface Multiplexer {
    /**
     * tmux terminal multiplexer.
     */
    public data class Tmux(
        val version: String? = null,
    ) : Multiplexer

    /**
     * zellij terminal multiplexer.
     */
    public data object Zellij : Multiplexer
}

/**
 * tmux client terminal identification captured via tmux display-message.
 */
public data class TmuxClientInfo(
    val termtype: String? = null,
    val termname: String? = null,
)

/**
 * Structured terminal identification data.
 */
public data class TerminalInfo(
    val name: TerminalName,
    val termProgram: String? = null,
    val version: String? = null,
    val term: String? = null,
    val multiplexer: Multiplexer? = null,
) {
    public fun isZellij(): Boolean = multiplexer is Multiplexer.Zellij

    public fun userAgentToken(): String {
        val raw = if (!termProgram.isNullOrEmpty()) {
            if (!version.isNullOrEmpty()) {
                "$termProgram/$version"
            } else {
                termProgram
            }
        } else if (!term.isNullOrEmpty()) {
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
                TerminalName.Dumb -> "dumb"
                TerminalName.Unknown -> "unknown"
            }
        }
        return sanitizeHeaderValue(raw)
    }

    public companion object {
        public fun fromTermProgram(
            name: TerminalName,
            termProgram: String,
            version: String? = null,
            multiplexer: Multiplexer? = null,
        ): TerminalInfo =
            TerminalInfo(
                name = name,
                termProgram = termProgram,
                version = version,
                term = null,
                multiplexer = multiplexer,
            )

        public fun fromTermProgramAndTerm(
            name: TerminalName,
            termProgram: String,
            version: String? = null,
            term: String? = null,
            multiplexer: Multiplexer? = null,
        ): TerminalInfo =
            TerminalInfo(
                name = name,
                termProgram = termProgram,
                version = version,
                term = term,
                multiplexer = multiplexer,
            )

        public fun fromName(
            name: TerminalName,
            version: String? = null,
            multiplexer: Multiplexer? = null,
        ): TerminalInfo =
            TerminalInfo(
                name = name,
                termProgram = null,
                version = version,
                term = null,
                multiplexer = multiplexer,
            )

        public fun fromTerm(
            term: String,
            multiplexer: Multiplexer? = null,
        ): TerminalInfo {
            val name = when (term) {
                "dumb" -> TerminalName.Dumb
                "wezterm", "wezterm-mux" -> TerminalName.WezTerm
                else -> TerminalName.Unknown
            }
            return TerminalInfo(
                name = name,
                termProgram = null,
                version = null,
                term = term,
                multiplexer = multiplexer,
            )
        }

        public fun unknown(multiplexer: Multiplexer? = null): TerminalInfo =
            TerminalInfo(
                name = TerminalName.Unknown,
                termProgram = null,
                version = null,
                term = null,
                multiplexer = multiplexer,
            )
    }
}

/**
 * Environment variable access used by terminal detection.
 */
public interface Environment {
    public fun get(name: String): String?
    public fun has(name: String): Boolean = get(name) != null
    public fun getNonEmpty(name: String): String? = get(name)?.takeIf { it.isNotBlank() }
    public fun hasNonEmpty(name: String): Boolean = getNonEmpty(name) != null
    public fun tmuxClientInfo(): TmuxClientInfo = TmuxClientInfo()
}

public class MapEnvironment(
    private val vars: Map<String, String>,
    private val clientInfo: TmuxClientInfo = TmuxClientInfo(),
) : Environment {
    override fun get(name: String): String? = vars[name]
    override fun tmuxClientInfo(): TmuxClientInfo = clientInfo
}

public fun detectTerminalInfoFromEnv(env: Environment): TerminalInfo {
    val multiplexer = detectMultiplexer(env)

    val termProgram = env.getNonEmpty("TERM_PROGRAM")
    if (termProgram != null) {
        if (isTmuxTermProgram(termProgram) &&
            multiplexer is Multiplexer.Tmux
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

    val termVal = env.get("TERM")
    if (env.has("KITTY_WINDOW_ID") || (termVal != null && termVal.contains("kitty"))) {
        return TerminalInfo.fromName(TerminalName.Kitty, null, multiplexer)
    }

    if (env.has("ALACRITTY_SOCKET") || termVal == "alacritty") {
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

    val termNonEmpty = env.getNonEmpty("TERM")
    if (termNonEmpty != null) {
        return TerminalInfo.fromTerm(termNonEmpty, multiplexer)
    }

    return TerminalInfo.unknown(multiplexer)
}

private fun detectMultiplexer(env: Environment): Multiplexer? {
    if (env.hasNonEmpty("TMUX") || env.hasNonEmpty("TMUX_PANE")) {
        return Multiplexer.Tmux(
            version = tmuxVersionFromEnv(env),
        )
    }

    if (env.hasNonEmpty("ZELLIJ") ||
        env.hasNonEmpty("ZELLIJ_SESSION_NAME") ||
        env.hasNonEmpty("ZELLIJ_VERSION")
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
    val termtype = clientInfo.termtype?.takeIf { it.isNotBlank() }
    val termname = clientInfo.termname?.takeIf { it.isNotBlank() }

    if (termtype != null) {
        val (program, version) = splitTermProgramAndVersion(termtype)
        val name = terminalNameFromTermProgram(program) ?: TerminalName.Unknown
        return TerminalInfo.fromTermProgramAndTerm(
            name = name,
            termProgram = program,
            version = version,
            term = termname,
            multiplexer = multiplexer,
        )
    }

    return termname?.let { TerminalInfo.fromTerm(it, multiplexer) }
}

private fun tmuxVersionFromEnv(env: Environment): String? {
    val termProgram = env.get("TERM_PROGRAM") ?: return null
    if (!isTmuxTermProgram(termProgram)) {
        return null
    }
    return env.getNonEmpty("TERM_PROGRAM_VERSION")
}

private fun splitTermProgramAndVersion(value: String): Pair<String, String?> {
    val parts = value.trim().split(Regex("\\s+"))
    val program = parts.firstOrNull() ?: ""
    val version = parts.getOrNull(1)
    return Pair(program, version)
}

public fun sanitizeHeaderValue(value: String): String {
    val sb = StringBuilder(value.length)
    for (c in value) {
        if (isValidHeaderValueChar(c)) {
            sb.append(c)
        } else {
            sb.append('_')
        }
    }
    return sb.toString()
}

private fun isValidHeaderValueChar(c: Char): Boolean =
    c.isLetterOrDigit() || c == '-' || c == '_' || c == '.' || c == '/'

public fun terminalNameFromTermProgram(value: String): TerminalName? {
    val normalized = value.trim()
        .filter { it != ' ' && it != '-' && it != '_' && it != '.' }
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
        "dumb" -> TerminalName.Dumb
        else -> null
    }
}

private fun formatTerminalVersion(name: String, version: String?): String =
    if (!version.isNullOrEmpty()) "$name/$version" else name
