// port-lint: source codex-rs/core/src/powershell.rs
package ai.solace.coder.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PowerShellTest {
    @Test
    fun extractsBasicPowershellCommand() {
        val cmd = listOf("powershell", "-Command", "Write-Host hi")
        val result = extractPowershellCommand(cmd)
        assertNotNull(result)
        assertEquals("Write-Host hi", result.second)
    }

    @Test
    fun extractsLowercaseFlags() {
        val cmd = listOf("powershell", "-nologo", "-command", "Write-Host hi")
        val result = extractPowershellCommand(cmd)
        assertNotNull(result)
        assertEquals("Write-Host hi", result.second)
    }

    @Test
    fun extractsFullPathPowershellCommand() {
        val command = "/usr/local/bin/powershell.exe"
        val cmd = listOf(command, "-Command", "Write-Host hi")
        val result = extractPowershellCommand(cmd)
        assertNotNull(result)
        assertEquals("Write-Host hi", result.second)
    }

    @Test
    fun extractsWithNoprofileAndAlias() {
        val cmd = listOf("pwsh", "-NoProfile", "-c", "Get-ChildItem | Select-String foo")
        val result = extractPowershellCommand(cmd)
        assertNotNull(result)
        assertEquals("Get-ChildItem | Select-String foo", result.second)
    }
}
