// port-lint: source tui/src/updateAction.rs
package ai.solace.coder.tui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UpdateActionTest {
    @Test
    fun detectsUpdateActionWithoutEnvMutation() {
        assertNull(
            detectUpdateAction(
                isMacos = false,
                currentExe = "/any/path",
                managedByNpm = false,
                managedByBun = false,
            )
        )
        assertEquals(
            UpdateAction.NpmGlobalLatest,
            detectUpdateAction(
                isMacos = false,
                currentExe = "/any/path",
                managedByNpm = true,
                managedByBun = false,
            )
        )
        assertEquals(
            UpdateAction.BunGlobalLatest,
            detectUpdateAction(
                isMacos = false,
                currentExe = "/any/path",
                managedByNpm = false,
                managedByBun = true,
            )
        )
        assertEquals(
            UpdateAction.BrewUpgrade,
            detectUpdateAction(
                isMacos = true,
                currentExe = "/opt/homebrew/bin/codex",
                managedByNpm = false,
                managedByBun = false,
            )
        )
        assertEquals(
            UpdateAction.BrewUpgrade,
            detectUpdateAction(
                isMacos = true,
                currentExe = "/usr/local/bin/codex",
                managedByNpm = false,
                managedByBun = false,
            )
        )
    }

    @Test
    fun commandArgsReturnsExpectedPairs() {
        assertEquals("npm" to listOf("install", "-g", "@openai/codex"), UpdateAction.NpmGlobalLatest.commandArgs())
        assertEquals("bun" to listOf("install", "-g", "@openai/codex"), UpdateAction.BunGlobalLatest.commandArgs())
        assertEquals("brew" to listOf("upgrade", "codex"), UpdateAction.BrewUpgrade.commandArgs())
    }

    @Test
    fun commandStrReturnsShellJoinedString() {
        assertEquals("npm install -g @openai/codex", UpdateAction.NpmGlobalLatest.commandStr())
        assertEquals("bun install -g @openai/codex", UpdateAction.BunGlobalLatest.commandStr())
        assertEquals("brew upgrade codex", UpdateAction.BrewUpgrade.commandStr())
    }
}
