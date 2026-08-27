package io.github.kotlinmania.codex.terminal

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TerminalDetectionTest {

    private fun fakeEnv(
        vararg vars: Pair<String, String>,
        termtype: String? = null,
        termname: String? = null,
    ): Environment =
        MapEnvironment(
            vars = vars.toMap(),
            clientInfo = TmuxClientInfo(termtype = termtype, termname = termname),
        )

    @Test
    fun detectsTermProgram() {
        val env = fakeEnv(
            "TERM_PROGRAM" to "iTerm.app",
            "TERM_PROGRAM_VERSION" to "3.5.0",
            "WEZTERM_VERSION" to "2024.2",
        )
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            TerminalInfo.fromTermProgram(
                TerminalName.Iterm2,
                "iTerm.app",
                "3.5.0",
                null,
            ),
            terminal,
        )
        assertEquals("iTerm.app/3.5.0", terminal.userAgentToken())

        val env2 = fakeEnv(
            "TERM_PROGRAM" to "iTerm.app",
            "TERM_PROGRAM_VERSION" to "",
        )
        val terminal2 = detectTerminalInfoFromEnv(env2)
        assertEquals(
            TerminalInfo.fromTermProgram(
                TerminalName.Iterm2,
                "iTerm.app",
                null,
                null,
            ),
            terminal2,
        )
        assertEquals("iTerm.app", terminal2.userAgentToken())
    }

    @Test
    fun terminalInfoReportsIsZellij() {
        val zellij = TerminalInfo.unknown(Multiplexer.Zellij)
        assertTrue(zellij.isZellij())

        val nonZellij = TerminalInfo.unknown(Multiplexer.Tmux(null))
        assertFalse(nonZellij.isZellij())
    }

    @Test
    fun detectsIterm2() {
        val env = fakeEnv("ITERM_SESSION_ID" to "w0t1p0")
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            TerminalInfo.fromName(TerminalName.Iterm2, null, null),
            terminal,
        )
        assertEquals("iTerm.app", terminal.userAgentToken())
    }

    @Test
    fun detectsAppleTerminal() {
        val env = fakeEnv("TERM_PROGRAM" to "Apple_Terminal")
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            TerminalInfo.fromTermProgram(TerminalName.AppleTerminal, "Apple_Terminal", null, null),
            terminal,
        )
        assertEquals("Apple_Terminal", terminal.userAgentToken())

        val env2 = fakeEnv("TERM_SESSION_ID" to "A1B2C3")
        val terminal2 = detectTerminalInfoFromEnv(env2)
        assertEquals(
            TerminalInfo.fromName(TerminalName.AppleTerminal, null, null),
            terminal2,
        )
        assertEquals("Apple_Terminal", terminal2.userAgentToken())
    }

    @Test
    fun detectsGhostty() {
        val env = fakeEnv("TERM_PROGRAM" to "Ghostty")
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            TerminalInfo.fromTermProgram(TerminalName.Ghostty, "Ghostty", null, null),
            terminal,
        )
        assertEquals("Ghostty", terminal.userAgentToken())
    }

    @Test
    fun detectsVsCode() {
        val env = fakeEnv(
            "TERM_PROGRAM" to "vscode",
            "TERM_PROGRAM_VERSION" to "1.86.0",
        )
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            TerminalInfo.fromTermProgram(TerminalName.VsCode, "vscode", "1.86.0", null),
            terminal,
        )
        assertEquals("vscode/1.86.0", terminal.userAgentToken())
    }

    @Test
    fun detectsWarpTerminal() {
        val env = fakeEnv(
            "TERM_PROGRAM" to "WarpTerminal",
            "TERM_PROGRAM_VERSION" to "v0.2025.12.10.08.12.stable_03",
        )
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            TerminalInfo.fromTermProgram(
                TerminalName.WarpTerminal,
                "WarpTerminal",
                "v0.2025.12.10.08.12.stable_03",
                null,
            ),
            terminal,
        )
        assertEquals("WarpTerminal/v0.2025.12.10.08.12.stable_03", terminal.userAgentToken())
    }

    @Test
    fun detectsTmuxMultiplexer() {
        val env = fakeEnv(
            "TMUX" to "/tmp/tmux-1000/default,123,0",
            "TERM_PROGRAM" to "tmux",
            termtype = "xterm-256color",
            termname = "screen-256color",
        )
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            TerminalInfo.fromTermProgramAndTerm(
                TerminalName.Unknown,
                "xterm-256color",
                null,
                "screen-256color",
                Multiplexer.Tmux(null),
            ),
            terminal,
        )
        assertEquals("xterm-256color", terminal.userAgentToken())
    }

    @Test
    fun detectsZellijMultiplexer() {
        val env = fakeEnv("ZELLIJ" to "1")
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            TerminalInfo.unknown(Multiplexer.Zellij),
            terminal,
        )
        assertTrue(terminal.isZellij())
    }

    @Test
    fun detectsWezTerm() {
        val env = fakeEnv("WEZTERM_VERSION" to "2024.2")
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            TerminalInfo.fromName(TerminalName.WezTerm, "2024.2", null),
            terminal,
        )
        assertEquals("WezTerm/2024.2", terminal.userAgentToken())
    }

    @Test
    fun detectsKitty() {
        val env = fakeEnv("KITTY_WINDOW_ID" to "1")
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            TerminalInfo.fromName(TerminalName.Kitty, null, null),
            terminal,
        )
        assertEquals("kitty", terminal.userAgentToken())
    }

    @Test
    fun detectsAlacritty() {
        val env = fakeEnv("ALACRITTY_SOCKET" to "/tmp/alacritty")
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            TerminalInfo.fromName(TerminalName.Alacritty, null, null),
            terminal,
        )
        assertEquals("Alacritty", terminal.userAgentToken())
    }

    @Test
    fun detectsKonsole() {
        val env = fakeEnv("KONSOLE_VERSION" to "230800")
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            TerminalInfo.fromName(TerminalName.Konsole, "230800", null),
            terminal,
        )
        assertEquals("Konsole/230800", terminal.userAgentToken())
    }

    @Test
    fun detectsGnomeTerminal() {
        val env = fakeEnv("GNOME_TERMINAL_SCREEN" to "1")
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            TerminalInfo.fromName(TerminalName.GnomeTerminal, null, null),
            terminal,
        )
        assertEquals("gnome-terminal", terminal.userAgentToken())
    }

    @Test
    fun detectsVte() {
        val env = fakeEnv("VTE_VERSION" to "7000")
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            TerminalInfo.fromName(TerminalName.Vte, "7000", null),
            terminal,
        )
        assertEquals("VTE/7000", terminal.userAgentToken())
    }

    @Test
    fun detectsWindowsTerminal() {
        val env = fakeEnv("WT_SESSION" to "1")
        val terminal = detectTerminalInfoFromEnv(env)
        assertEquals(
            TerminalInfo.fromName(TerminalName.WindowsTerminal, null, null),
            terminal,
        )
        assertEquals("WindowsTerminal", terminal.userAgentToken())
    }

    @Test
    fun userAgentTokenSanitization() {
        val sanitized = sanitizeHeaderValue("foo bar/1.0 (test)")
        assertEquals("foo_bar/1.0__test_", sanitized)
    }
}
