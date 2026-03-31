// port-lint: source protocol/src/openai_models.rs
package ai.solace.coder.protocol

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A reasoning effort option that can be surfaced for a model.
 */
@Serializable
data class ReasoningEffortPreset(
    /** Effort level that the model supports. */
    val effort: ReasoningEffort,
    /** Short human description shown next to the effort in UIs. */
    val description: String,
)

@Serializable
data class ModelUpgrade(
    val id: String,
    @SerialName("reasoning_effort_mapping") val reasoningEffortMapping: Map<ReasoningEffort, ReasoningEffort>? = null,
    @SerialName("migration_config_key") val migrationConfigKey: String,
    @SerialName("model_link") val modelLink: String? = null,
    @SerialName("upgrade_copy") val upgradeCopy: String? = null,
)

/**
 * Metadata describing a Codex-supported model.
 */
@Serializable
data class ModelPreset(
    /** Stable identifier for the preset. */
    val id: String,
    /** Model slug (e.g., "gpt-5"). */
    val model: String,
    /** Display name shown in UIs. */
    @SerialName("display_name") val displayName: String,
    /** Short human description shown in UIs. */
    val description: String,
    /** Reasoning effort applied when none is explicitly chosen. */
    @SerialName("default_reasoning_effort") val defaultReasoningEffort: ReasoningEffort,
    /** Supported reasoning effort options. */
    @SerialName("supported_reasoning_efforts") val supportedReasoningEfforts: List<ReasoningEffortPreset>,
    /** Whether this is the default model for new users. */
    @SerialName("is_default") val isDefault: Boolean,
    /** Recommended upgrade model. */
    val upgrade: ModelUpgrade? = null,
    /** Whether this preset should appear in the picker UI. */
    @SerialName("show_in_picker") val showInPicker: Boolean,
    /** Whether this model is supported in the api. */
    @SerialName("supported_in_api") val supportedInApi: Boolean,
)

/** Visibility of a model in the picker or APIs. */
@Serializable
enum class ModelVisibility {
    @SerialName("list") List,
    @SerialName("hide") Hide,
    @SerialName("none") None,
}

/** Shell execution capability for a model. */
@Serializable
enum class ConfigShellToolType {
    @SerialName("default") Default,
    @SerialName("local") Local,
    @SerialName("unified_exec") UnifiedExec,
    @SerialName("disabled") Disabled,
    @SerialName("shell_command") ShellCommand,
}

@Serializable
enum class ApplyPatchToolType {
    @SerialName("freeform") Freeform,
    @SerialName("function") Function,
}

/** Server-provided truncation policy metadata for a model. */
@Serializable
enum class TruncationMode {
    @SerialName("bytes") Bytes,
    @SerialName("tokens") Tokens,
}

@Serializable
data class TruncationPolicyConfig(
    val mode: TruncationMode,
    val limit: Long,
) {
    companion object {
        fun bytes(limit: Long): TruncationPolicyConfig =
            TruncationPolicyConfig(mode = TruncationMode.Bytes, limit = limit)

        fun tokens(limit: Long): TruncationPolicyConfig =
            TruncationPolicyConfig(mode = TruncationMode.Tokens, limit = limit)
    }
}

/** Semantic version triple encoded as an array in JSON (e.g. [0, 62, 0]). */
@Serializable
data class ClientVersion(
    val major: Int,
    val minor: Int,
    val patch: Int,
)

/** Model metadata returned by the Codex backend `/models` endpoint. */
@Serializable
data class OpenAiModelInfo(
    val slug: String,
    @SerialName("display_name") val displayName: String,
    val description: String? = null,
    @SerialName("default_reasoning_level") val defaultReasoningLevel: ReasoningEffort,
    @SerialName("supported_reasoning_levels") val supportedReasoningLevels: List<ReasoningEffortPreset>,
    @SerialName("shell_type") val shellType: ConfigShellToolType,
    val visibility: ModelVisibility,
    @SerialName("supported_in_api") val supportedInApi: Boolean,
    val priority: Int,
    val upgrade: String? = null,
    @SerialName("base_instructions") val baseInstructions: String? = null,
    @SerialName("supports_reasoning_summaries") val supportsReasoningSummaries: Boolean,
    @SerialName("support_verbosity") val supportVerbosity: Boolean,
    @SerialName("default_verbosity") val defaultVerbosity: Verbosity? = null,
    @SerialName("apply_patch_tool_type") val applyPatchToolType: ApplyPatchToolType? = null,
    @SerialName("truncation_policy") val truncationPolicy: TruncationPolicyConfig,
    @SerialName("supports_parallel_tool_calls") val supportsParallelToolCalls: Boolean,
    @SerialName("context_window") val contextWindow: Long? = null,
    @SerialName("experimental_supported_tools") val experimentalSupportedTools: List<String>,
) {
    fun toModelPreset(): ModelPreset = ModelPreset(
        id = slug,
        model = slug,
        displayName = displayName,
        description = description ?: "",
        defaultReasoningEffort = defaultReasoningLevel,
        supportedReasoningEfforts = supportedReasoningLevels,
        isDefault = false, // default is the highest priority available model
        upgrade = upgrade?.let { upgradeSlug ->
            ModelUpgrade(
                id = upgradeSlug,
                reasoningEffortMapping = reasoningEffortMappingFromPresets(supportedReasoningLevels),
                migrationConfigKey = slug,
                // todo(aibrahim): add the model link here.
                modelLink = null,
                upgradeCopy = null,
            )
        },
        showInPicker = visibility == ModelVisibility.List,
        supportedInApi = supportedInApi,
    )
}

/** Response wrapper for `/models`. */
@Serializable
data class ModelsResponse(
    val models: List<OpenAiModelInfo> = emptyList(),
)

private fun reasoningEffortMappingFromPresets(
    presets: List<ReasoningEffortPreset>,
): Map<ReasoningEffort, ReasoningEffort>? {
    if (presets.isEmpty()) {
        return null
    }

    // Map every canonical effort to the closest supported effort for the new model.
    val supported = presets.map { it.effort }
    val map = mutableMapOf<ReasoningEffort, ReasoningEffort>()
    for (effort in ReasoningEffort.entries) {
        map[effort] = nearestEffort(effort, supported)
    }
    return map
}

private fun effortRank(effort: ReasoningEffort): Int = when (effort) {
    ReasoningEffort.None -> 0
    ReasoningEffort.Minimal -> 1
    ReasoningEffort.Low -> 2
    ReasoningEffort.Medium -> 3
    ReasoningEffort.High -> 4
    ReasoningEffort.XHigh -> 5
}

private fun nearestEffort(target: ReasoningEffort, supported: List<ReasoningEffort>): ReasoningEffort {
    val targetRank = effortRank(target)
    return supported.minByOrNull { kotlin.math.abs(effortRank(it) - targetRank) } ?: target
}
