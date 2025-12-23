// port-lint: source core/src/sandboxing/assessment.rs
package ai.solace.coder.exec.sandbox

import ai.solace.coder.protocol.SandboxCommandAssessment
import ai.solace.coder.protocol.SandboxPolicy
import ai.solace.coder.protocol.SandboxRiskLevel
import ai.solace.coder.protocol.SandboxRiskLevel.*

/** Assess the risk of a command. Mirrors Rust's assess_command from sandboxing/assessment.rs */
fun assessCommand(command: List<String>, policy: SandboxPolicy): SandboxCommandAssessment {
    if (command.isEmpty()) return SandboxCommandAssessment("Empty command", SandboxRiskLevel.Low)

    val program = command.first().lowercase().substringAfterLast("/")

    // High risk commands
    val highRisk =
            setOf("rm", "dd", "mkfs", "fdisk", "mount", "umount", "chown", "chmod", "sudo", "su")

    // Medium risk commands
    val mediumRisk = setOf("mv", "cp", "ln", "kill", "pkill", "killall", "shutdown", "reboot")

    if (highRisk.contains(program)) {
        return SandboxCommandAssessment("High risk program: $program", SandboxRiskLevel.High)
    }

    if (mediumRisk.contains(program)) {
        return SandboxCommandAssessment("Medium risk program: $program", SandboxRiskLevel.Medium)
    }

    // Check for suspicious arguments in common commands
    if (program == "git" && command.contains("clean")) {
        return SandboxCommandAssessment("Suspicious git clean command", SandboxRiskLevel.Medium)
    }

    return SandboxCommandAssessment("Generic command", SandboxRiskLevel.Low)
}
