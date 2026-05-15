// port-lint: source core/src/powershell.rs
package io.github.solaceharmony.codex.core

import io.github.solaceharmony.codex.exec.shell.ShellDetector
import io.github.solaceharmony.codex.exec.shell.ShellType

private val POWERSHELL_FLAGS = listOf("-nologo", "-noprofile", "-command", "-c")

fun extractPowershellCommand(command: List<String>): Pair<String, String>? {
    if (command.size < 3) {
        return null
    }

    val shell = command[0]
    if (ShellDetector().detectShellType(shell) != ShellType.PowerShell) {
        return null
    }

    var i = 1
    while (i + 1 < command.size) {
        val flag = command[i]
        if (!POWERSHELL_FLAGS.contains(flag.lowercase())) {
            return null
        }
        if (flag.equals("-Command", ignoreCase = true) || flag.equals("-c", ignoreCase = true)) {
            val script = command[i + 1]
            return shell to script
        }
        i += 1
    }
    return null
}
