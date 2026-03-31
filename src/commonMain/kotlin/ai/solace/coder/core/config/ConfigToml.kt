// port-lint: source core/src/config/mod.rs
package ai.solace.coder.core.config

import ai.solace.coder.core.features.FeaturesToml
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Direct deserialization target for config files (TOML/JSON). Optional fields only.
@Serializable
data class ConfigToml(
    @SerialName("profile") val profile: String? = null,
    @SerialName("profiles") val profiles: Map<String, ConfigProfile>? = null,

    @SerialName("model") val model: String? = null,
    @SerialName("model_provider") val modelProvider: String? = null,

    // MCP servers keyed by name; Raw form will be normalized later.
    @SerialName("mcp_servers") val mcpServers: Map<String, RawMcpServerConfig>? = null,

    // Nested tools section for feature toggles.
    @SerialName("tools") val tools: ToolsToml? = null,

    // Centralized feature flags (new). Prefer this over individual toggles.
    @SerialName("features") val features: FeaturesToml? = null,

    // Legacy feature toggles (kept for compatibility with existing configs).
    @SerialName("experimental_use_unified_exec_tool") val experimentalUseUnifiedExecTool: Boolean? = null,
    @SerialName("experimental_use_freeform_apply_patch") val experimentalUseFreeformApplyPatch: Boolean? = null,
)

@Serializable
data class ToolsToml(
    @SerialName("web_search") val webSearch: Boolean? = null,
    // Serde alias in Rust: accept "web_search_request" as well.
    @SerialName("web_search_request") val webSearchRequest: Boolean? = null,
    @SerialName("view_image") val viewImage: Boolean? = null,
) {
    fun webSearchEffective(): Boolean? = webSearch ?: webSearchRequest
}

// Runtime overrides such as CLI flags or env variables.
@Serializable
data class ConfigOverrides(
    @SerialName("profile") val profile: String? = null,
    @SerialName("model") val model: String? = null,
    @SerialName("model_provider") val modelProvider: String? = null,
)
