// port-lint: source tui/src/bottom_pane/approval_overlay.rs
package ai.solace.coder.tui.bottom_pane

import ai.solace.coder.protocol.FileChange
import ai.solace.coder.protocol.SandboxCommandAssessment
import kotlinx.serialization.Serializable
import okio.Path

/**
 * Request coming from the agent that needs user approval.
 */
@Serializable
sealed class ApprovalRequest {
    @Serializable
    data class Exec(
        val id: String,
        val command: List<String>,
        val reason: String?,
        val risk: SandboxCommandAssessment?,
    ) : ApprovalRequest()

    @Serializable
    data class ApplyPatch(
        val id: String,
        val reason: String?,
        val cwd: Path,
        val changes: Map<Path, FileChange>,
    ) : ApprovalRequest()

    @Serializable
    data class McpElicitation(
        val serverName: String,
        val requestId: String,
        val message: String,
    ) : ApprovalRequest()
}

