// port-lint: source core/src/terminal.rs
package ai.solace.coder.core.terminal

import kotlin.test.Test
import kotlin.test.assertEquals

class TerminalTest {

    private class FakeEnvironment(
        private val vars: Map<String, String> = emptyMap(),
        private val tmuxClient: TmuxClientInfo = TmuxClientInfo(),
    ) : TerminalEnvironment {
        override fun get(name: String): String? = vars[name]
        override fun tmuxClientInfo(): TmuxClientInfo = tmuxClient
    }

    private fun terminalInfo(
        name: TerminalName,
        termProgram: String? = null,
        version: String? = null,
        term: String? = null,
        multiplexer: Multiplexer? = null,
    ): TerminalInfo = TerminalInfo(
        name = name,
        termProgram = termProgram,
        version = version,
        term = term,
        multiplexer = multiplexer,
    )

    @Test
    fun detectsTermProgram() {
        var env = FakeEnvironment(
            mapOf(
                "TERM_PROGRAM" to "iTerm.app",
                "TERM_PROGRAM_VERSION" to "3.5.0",
                "WEZTERM_VERSION" to "2024.2",
            )
        )
        var terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Iterm2, termProgram = "iTerm.app", version = "3.5.0"),
            terminal,
            "term_program_with_version_info",
        )
        assertEquals("iTerm.app/3.5.0", terminal.userAgentToken(), "term_program_with_version_user_agent")

        env = FakeEnvironment(
            mapOf(
                "TERM_PROGRAM" to "iTerm.app",
                "TERM_PROGRAM_VERSION" to "",
            )
        )
        terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Iterm2, termProgram = "iTerm.app"),
            terminal,
            "term_program_without_version_info",
        )
        assertEquals("iTerm.app", terminal.userAgentToken(), "term_program_without_version_user_agent")

        env = FakeEnvironment(
            mapOf(
                "TERM_PROGRAM" to "iTerm.app",
                "WEZTERM_VERSION" to "2024.2",
            )
        )
        terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Iterm2, termProgram = "iTerm.app"),
            terminal,
            "term_program_overrides_wezterm_info",
        )
        assertEquals("iTerm.app", terminal.userAgentToken(), "term_program_overrides_wezterm_user_agent")
    }

    @Test
    fun detectsIterm2() {
        val env = FakeEnvironment(mapOf("ITERM_SESSION_ID" to "w0t1p0"))
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Iterm2),
            terminal,
            "iterm_session_id_info",
        )
        assertEquals("iTerm.app", terminal.userAgentToken(), "iterm_session_id_user_agent")
    }

    @Test
    fun detectsAppleTerminal() {
        var env = FakeEnvironment(mapOf("TERM_PROGRAM" to "Apple_Terminal"))
        var terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.AppleTerminal, termProgram = "Apple_Terminal"),
            terminal,
            "apple_term_program_info",
        )
        assertEquals("Apple_Terminal", terminal.userAgentToken(), "apple_term_program_user_agent")

        env = FakeEnvironment(mapOf("TERM_SESSION_ID" to "A1B2C3"))
        terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.AppleTerminal),
            terminal,
            "apple_term_session_id_info",
        )
        assertEquals("Apple_Terminal", terminal.userAgentToken(), "apple_term_session_id_user_agent")
    }

    @Test
    fun detectsGhostty() {
        val env = FakeEnvironment(mapOf("TERM_PROGRAM" to "Ghostty"))
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Ghostty, termProgram = "Ghostty"),
            terminal,
            "ghostty_term_program_info",
        )
        assertEquals("Ghostty", terminal.userAgentToken(), "ghostty_term_program_user_agent")
    }

    @Test
    fun detectsVscode() {
        val env = FakeEnvironment(
            mapOf(
                "TERM_PROGRAM" to "vscode",
                "TERM_PROGRAM_VERSION" to "1.86.0",
            )
        )
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.VsCode, termProgram = "vscode", version = "1.86.0"),
            terminal,
            "vscode_term_program_info",
        )
        assertEquals("vscode/1.86.0", terminal.userAgentToken(), "vscode_term_program_user_agent")
    }

    @Test
    fun detectsWarpTerminal() {
        val env = FakeEnvironment(
            mapOf(
                "TERM_PROGRAM" to "WarpTerminal",
                "TERM_PROGRAM_VERSION" to "v0.2025.12.10.08.12.stable_03",
            )
        )
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(
                TerminalName.WarpTerminal,
                termProgram = "WarpTerminal",
                version = "v0.2025.12.10.08.12.stable_03",
            ),
            terminal,
            "warp_term_program_info",
        )
        assertEquals(
            "WarpTerminal/v0.2025.12.10.08.12.stable_03",
            terminal.userAgentToken(),
            "warp_term_program_user_agent",
        )
    }

    @Test
    fun detectsTmuxMultiplexer() {
        val env = FakeEnvironment(
            vars = mapOf(
                "TMUX" to "/tmp/tmux-1000/default,123,0",
                "TERM_PROGRAM" to "tmux",
            ),
            tmuxClient = TmuxClientInfo(
                termtype = "xterm-256color",
                termname = "screen-256color",
            ),
        )
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(
                TerminalName.Unknown,
                termProgram = "xterm-256color",
                term = "screen-256color",
                multiplexer = Multiplexer.Tmux(version = null),
            ),
            terminal,
            "tmux_multiplexer_info",
        )
        assertEquals("xterm-256color", terminal.userAgentToken(), "tmux_multiplexer_user_agent")
    }

    @Test
    fun detectsZellijMultiplexer() {
        val env = FakeEnvironment(mapOf("ZELLIJ" to "1"))
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            TerminalInfo(
                name = TerminalName.Unknown,
                termProgram = null,
                version = null,
                term = null,
                multiplexer = Multiplexer.Zellij,
            ),
            terminal,
            "zellij_multiplexer",
        )
    }

    @Test
    fun detectsTmuxClientTermtype() {
        val env = FakeEnvironment(
            vars = mapOf(
                "TMUX" to "/tmp/tmux-1000/default,123,0",
                "TERM_PROGRAM" to "tmux",
            ),
            tmuxClient = TmuxClientInfo(termtype = "WezTerm"),
        )
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(
                TerminalName.WezTerm,
                termProgram = "WezTerm",
                multiplexer = Multiplexer.Tmux(version = null),
            ),
            terminal,
            "tmux_client_termtype_info",
        )
        assertEquals("WezTerm", terminal.userAgentToken(), "tmux_client_termtype_user_agent")
    }

    @Test
    fun detectsTmuxClientTermname() {
        val env = FakeEnvironment(
            vars = mapOf(
                "TMUX" to "/tmp/tmux-1000/default,123,0",
                "TERM_PROGRAM" to "tmux",
            ),
            tmuxClient = TmuxClientInfo(termname = "xterm-256color"),
        )
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(
                TerminalName.Unknown,
                term = "xterm-256color",
                multiplexer = Multiplexer.Tmux(version = null),
            ),
            terminal,
            "tmux_client_termname_info",
        )
        assertEquals("xterm-256color", terminal.userAgentToken(), "tmux_client_termname_user_agent")
    }

    @Test
    fun detectsTmuxTermProgramUsesClientTermtype() {
        val env = FakeEnvironment(
            vars = mapOf(
                "TMUX" to "/tmp/tmux-1000/default,123,0",
                "TERM_PROGRAM" to "tmux",
                "TERM_PROGRAM_VERSION" to "3.6a",
            ),
            tmuxClient = TmuxClientInfo(
                termtype = "ghostty 1.2.3",
                termname = "xterm-ghostty",
            ),
        )
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(
                TerminalName.Ghostty,
                termProgram = "ghostty",
                version = "1.2.3",
                term = "xterm-ghostty",
                multiplexer = Multiplexer.Tmux(version = "3.6a"),
            ),
            terminal,
            "tmux_term_program_client_termtype_info",
        )
        assertEquals("ghostty/1.2.3", terminal.userAgentToken(), "tmux_term_program_client_termtype_user_agent")
    }

    @Test
    fun detectsWezterm() {
        var env = FakeEnvironment(mapOf("WEZTERM_VERSION" to "2024.2"))
        var terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.WezTerm, version = "2024.2"),
            terminal,
            "wezterm_version_info",
        )
        assertEquals("WezTerm/2024.2", terminal.userAgentToken(), "wezterm_version_user_agent")

        env = FakeEnvironment(
            mapOf(
                "TERM_PROGRAM" to "WezTerm",
                "TERM_PROGRAM_VERSION" to "2024.2",
            )
        )
        terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.WezTerm, termProgram = "WezTerm", version = "2024.2"),
            terminal,
            "wezterm_term_program_info",
        )
        assertEquals("WezTerm/2024.2", terminal.userAgentToken(), "wezterm_term_program_user_agent")

        env = FakeEnvironment(mapOf("WEZTERM_VERSION" to ""))
        terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.WezTerm),
            terminal,
            "wezterm_empty_info",
        )
        assertEquals("WezTerm", terminal.userAgentToken(), "wezterm_empty_user_agent")
    }

    @Test
    fun detectsKitty() {
        var env = FakeEnvironment(mapOf("KITTY_WINDOW_ID" to "1"))
        var terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Kitty),
            terminal,
            "kitty_window_id_info",
        )
        assertEquals("kitty", terminal.userAgentToken(), "kitty_window_id_user_agent")

        env = FakeEnvironment(
            mapOf(
                "TERM_PROGRAM" to "kitty",
                "TERM_PROGRAM_VERSION" to "0.30.1",
            )
        )
        terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Kitty, termProgram = "kitty", version = "0.30.1"),
            terminal,
            "kitty_term_program_info",
        )
        assertEquals("kitty/0.30.1", terminal.userAgentToken(), "kitty_term_program_user_agent")

        env = FakeEnvironment(
            mapOf(
                "TERM" to "xterm-kitty",
                "ALACRITTY_SOCKET" to "/tmp/alacritty",
            )
        )
        terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Kitty),
            terminal,
            "kitty_term_over_alacritty_info",
        )
        assertEquals("kitty", terminal.userAgentToken(), "kitty_term_over_alacritty_user_agent")
    }

    @Test
    fun detectsAlacritty() {
        var env = FakeEnvironment(mapOf("ALACRITTY_SOCKET" to "/tmp/alacritty"))
        var terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Alacritty),
            terminal,
            "alacritty_socket_info",
        )
        assertEquals("Alacritty", terminal.userAgentToken(), "alacritty_socket_user_agent")

        env = FakeEnvironment(
            mapOf(
                "TERM_PROGRAM" to "Alacritty",
                "TERM_PROGRAM_VERSION" to "0.13.2",
            )
        )
        terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Alacritty, termProgram = "Alacritty", version = "0.13.2"),
            terminal,
            "alacritty_term_program_info",
        )
        assertEquals("Alacritty/0.13.2", terminal.userAgentToken(), "alacritty_term_program_user_agent")

        env = FakeEnvironment(mapOf("TERM" to "alacritty"))
        terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Alacritty),
            terminal,
            "alacritty_term_info",
        )
        assertEquals("Alacritty", terminal.userAgentToken(), "alacritty_term_user_agent")
    }

    @Test
    fun detectsKonsole() {
        var env = FakeEnvironment(mapOf("KONSOLE_VERSION" to "230800"))
        var terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Konsole, version = "230800"),
            terminal,
            "konsole_version_info",
        )
        assertEquals("Konsole/230800", terminal.userAgentToken(), "konsole_version_user_agent")

        env = FakeEnvironment(
            mapOf(
                "TERM_PROGRAM" to "Konsole",
                "TERM_PROGRAM_VERSION" to "230800",
            )
        )
        terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Konsole, termProgram = "Konsole", version = "230800"),
            terminal,
            "konsole_term_program_info",
        )
        assertEquals("Konsole/230800", terminal.userAgentToken(), "konsole_term_program_user_agent")

        env = FakeEnvironment(mapOf("KONSOLE_VERSION" to ""))
        terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Konsole),
            terminal,
            "konsole_empty_info",
        )
        assertEquals("Konsole", terminal.userAgentToken(), "konsole_empty_user_agent")
    }

    @Test
    fun detectsGnomeTerminal() {
        var env = FakeEnvironment(mapOf("GNOME_TERMINAL_SCREEN" to "1"))
        var terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.GnomeTerminal),
            terminal,
            "gnome_terminal_screen_info",
        )
        assertEquals("gnome-terminal", terminal.userAgentToken(), "gnome_terminal_screen_user_agent")

        env = FakeEnvironment(
            mapOf(
                "TERM_PROGRAM" to "gnome-terminal",
                "TERM_PROGRAM_VERSION" to "3.50",
            )
        )
        terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.GnomeTerminal, termProgram = "gnome-terminal", version = "3.50"),
            terminal,
            "gnome_terminal_term_program_info",
        )
        assertEquals("gnome-terminal/3.50", terminal.userAgentToken(), "gnome_terminal_term_program_user_agent")
    }

    @Test
    fun detectsVte() {
        var env = FakeEnvironment(mapOf("VTE_VERSION" to "7000"))
        var terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Vte, version = "7000"),
            terminal,
            "vte_version_info",
        )
        assertEquals("VTE/7000", terminal.userAgentToken(), "vte_version_user_agent")

        env = FakeEnvironment(
            mapOf(
                "TERM_PROGRAM" to "VTE",
                "TERM_PROGRAM_VERSION" to "7000",
            )
        )
        terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Vte, termProgram = "VTE", version = "7000"),
            terminal,
            "vte_term_program_info",
        )
        assertEquals("VTE/7000", terminal.userAgentToken(), "vte_term_program_user_agent")

        env = FakeEnvironment(mapOf("VTE_VERSION" to ""))
        terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Vte),
            terminal,
            "vte_empty_info",
        )
        assertEquals("VTE", terminal.userAgentToken(), "vte_empty_user_agent")
    }

    @Test
    fun detectsWindowsTerminal() {
        var env = FakeEnvironment(mapOf("WT_SESSION" to "1"))
        var terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.WindowsTerminal),
            terminal,
            "wt_session_info",
        )
        assertEquals("WindowsTerminal", terminal.userAgentToken(), "wt_session_user_agent")

        env = FakeEnvironment(
            mapOf(
                "TERM_PROGRAM" to "WindowsTerminal",
                "TERM_PROGRAM_VERSION" to "1.21",
            )
        )
        terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.WindowsTerminal, termProgram = "WindowsTerminal", version = "1.21"),
            terminal,
            "windows_terminal_term_program_info",
        )
        assertEquals("WindowsTerminal/1.21", terminal.userAgentToken(), "windows_terminal_term_program_user_agent")
    }

    @Test
    fun detectsTermFallbacks() {
        var env = FakeEnvironment(mapOf("TERM" to "xterm-256color"))
        var terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Unknown, term = "xterm-256color"),
            terminal,
            "term_fallback_info",
        )
        assertEquals("xterm-256color", terminal.userAgentToken(), "term_fallback_user_agent")

        env = FakeEnvironment()
        terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            terminalInfo(TerminalName.Unknown),
            terminal,
            "unknown_info",
        )
        assertEquals("unknown", terminal.userAgentToken(), "unknown_user_agent")
    }
}
