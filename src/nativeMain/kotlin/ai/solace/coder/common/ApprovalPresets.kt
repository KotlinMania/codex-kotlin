// port-lint: source common/src/approvalPresets.rs
package ai.solace.coder.common

import ai.solace.coder.protocol.AskForApproval
import ai.solace.coder.protocol.SandboxPolicy

/**
 * A simple preset pairing an approval policy with a sandbox policy.
 */
data class ApprovalPreset(
    /** Stable identifier for the preset. */
    val id: String,
    /** Display label shown in UIs. */
    val label: String,
    /** Short human description shown next to the label in UIs. */
    val description: String,
    /** Approval policy to apply. */
    val approval: AskForApproval,
    /** Sandbox policy to apply. */
    val sandbox: SandboxPolicy,
)

/**
 * Built-in list of approval presets that pair approval and sandbox policy.
 *
 * Keep this UI-agnostic so it can be reused by both TUI and MCP server.
 */
fun builtinApprovalPresets(): List<ApprovalPreset> {
    return listOf(
        ApprovalPreset(
            id = "read-only",
            label = "Read Only",
            description = "Requires approval to edit files and run commands.",
            approval = AskForApproval.OnRequest,
            sandbox = SandboxPolicy.ReadOnly,
        ),
        ApprovalPreset(
            id = "auto",
            label = "Agent",
            description = "Read and edit files, and run commands.",
            approval = AskForApproval.OnRequest,
            sandbox = SandboxPolicy.newWorkspaceWritePolicy(),
        ),
        ApprovalPreset(
            id = "full-access",
            label = "Agent (full access)",
            description = "Codex can edit files outside this workspace and run commands with network access. Exercise caution when using.",
            approval = AskForApproval.Never,
            sandbox = SandboxPolicy.DangerFullAccess,
        ),
    )
}
