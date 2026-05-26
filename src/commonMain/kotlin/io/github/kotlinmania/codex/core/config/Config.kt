// port-lint: source codex-rs/core/src/config/mod.rs
package io.github.kotlinmania.codex.core.config

import io.github.kotlinmania.codex.core.auth.AuthCredentialsStoreMode
import io.github.kotlinmania.codex.core.auth.ForcedLoginMethod
import io.github.kotlinmania.codex.core.features.Features
import io.github.kotlinmania.codex.core.model.ModelFamily
import io.github.kotlinmania.codex.core.model.ModelProviderInfo
import io.github.kotlinmania.codex.core.session.NotifyConfig
import io.github.kotlinmania.codex.mcp.connection.McpServerConfig
import io.github.kotlinmania.codex.protocol.AskForApproval
import io.github.kotlinmania.codex.protocol.ReasoningEffortConfig
import io.github.kotlinmania.codex.protocol.ReasoningSummaryConfig
import io.github.kotlinmania.codex.protocol.ReasoningSummary
import io.github.kotlinmania.codex.protocol.SandboxPolicy
import io.github.kotlinmania.codex.protocol.Verbosity
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
