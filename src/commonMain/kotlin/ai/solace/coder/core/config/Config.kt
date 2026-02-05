// port-lint: source core/src/config/mod.rs
package ai.solace.coder.core.config

import ai.solace.coder.core.auth.AuthCredentialsStoreMode
import ai.solace.coder.core.auth.ForcedLoginMethod
import ai.solace.coder.core.features.Features
import ai.solace.coder.core.model.ModelFamily
import ai.solace.coder.core.model.ModelProviderInfo
import ai.solace.coder.core.session.NotifyConfig
import ai.solace.coder.mcp.connection.McpServerConfig
import ai.solace.coder.protocol.AskForApproval
import ai.solace.coder.protocol.ReasoningEffortConfig
import ai.solace.coder.protocol.ReasoningSummaryConfig
import ai.solace.coder.protocol.ReasoningSummary
import ai.solace.coder.protocol.SandboxPolicy
import ai.solace.coder.protocol.Verbosity
import okio.Path
import kotlinx.serialization.json.JsonElement

/**
 * Resolved runtime configuration for a Codex session.
 *
 * Ported from Rust codex-rs/core/src/config/mod.rs Config
 */
data class Config(
    // Core paths
    val codexHome: Path,

    // Authentication
    val cliAuthCredentialsStoreMode: AuthCredentialsStoreMode,
    val forcedLoginMethod: ForcedLoginMethod? = null,
    val forcedChatgptWorkspaceId: String? = null,

    // Model configuration
    val model: String,
    val modelFamily: ModelFamily,
    val modelContextWindow: Long? = null,
    val modelAutoCompactTokenLimit: Long? = null,
    val modelVerbosity: Verbosity? = null,
    val modelProviderId: String? = null,
    val modelProvider: ModelProviderInfo = ModelProviderInfo.default(),
    val modelReasoningEffort: ReasoningEffortConfig? = null,
    val modelReasoningSummary: ReasoningSummaryConfig = ReasoningSummary.Auto,

    // Session configuration
    val cwd: String = ".",
    val approvalPolicy: AskForApproval = AskForApproval.OnRequest,
    val sandboxPolicy: SandboxPolicy = SandboxPolicy.DangerFullAccess,

    // Instructions
    val developerInstructions: String? = null,
    val userInstructions: String? = null,
    val baseInstructions: String? = null,
    val compactPrompt: String? = null,

    // Features
    val features: Features = Features.withDefaults(),
    val showRawAgentReasoning: Boolean = false,

    // MCP servers
    val mcpServers: Map<String, McpServerConfig> = emptyMap(),

    // Notification
    val notify: NotifyConfig? = null,

    // Stream retry configuration
    val streamMaxRetries: Int = 3,

    // Output schema
    val outputSchema: JsonElement? = null,
    val tools: List<Any> = emptyList()
)
