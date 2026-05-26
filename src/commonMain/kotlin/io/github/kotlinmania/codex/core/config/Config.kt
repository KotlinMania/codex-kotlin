// port-lint: source config_api.rs
package io.github.kotlinmania.codex.core.config

import io.github.kotlinmania.codex.core.auth.AuthCredentialsStoreMode
import io.github.kotlinmania.codex.core.ForcedLoginMethod
import io.github.kotlinmania.codex.core.model.ModelFamily
import io.github.kotlinmania.codex.protocol.Verbosity
import kotlinx.io.files.Path
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
