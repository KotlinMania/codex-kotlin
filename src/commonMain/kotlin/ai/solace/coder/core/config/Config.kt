// port-lint: source codex-rs/app-server/src/config_api.rs
package ai.solace.coder.core.config

import ai.solace.coder.core.auth.AuthCredentialsStoreMode
import ai.solace.coder.core.auth.ForcedLoginMethod
import ai.solace.coder.core.model.ModelFamily
import ai.solace.coder.protocol.Verbosity
import okio.Path
import kotlinx.serialization.json.JsonElement

data class Config(
    val codexHome: Path,
    val cliAuthCredentialsStoreMode: AuthCredentialsStoreMode,
    val forcedLoginMethod: ForcedLoginMethod? = null,
    val forcedChatgptWorkspaceId: String? = null,
    val model: String,
    val modelFamily: ModelFamily,
    val modelContextWindow: Long? = null,
    val modelAutoCompactTokenLimit: Long? = null,
    val modelVerbosity: Verbosity? = null,
    val showRawAgentReasoning: Boolean = false,
    val outputSchema: JsonElement? = null,
    val tools: List<Any> = emptyList()
)
