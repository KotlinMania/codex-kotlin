// port-lint: source common/src/modelPresets.rs
package io.github.kotlinmania.codex.common

import io.github.kotlinmania.codex.core.AuthMode
import io.github.kotlinmania.codex.protocol.ReasoningEffort

const val HIDE_GPT5_1_MIGRATION_PROMPT_CONFIG: String = "hide_gpt5_1_migration_prompt"
const val HIDE_GPT_5_1_CODEX_MAX_MIGRATION_PROMPT_CONFIG: String =
    "hide_gpt-5.1-codex-max_migration_prompt"

/**
 * A reasoning effort option that can be surfaced for a model.
 */
data class ReasoningEffortPreset(
    /** Effort level that the model supports. */
    val effort: ReasoningEffort,
    /** Short human description shown next to the effort in UIs. */
    val description: String,
)

data class ModelUpgrade(
    val id: String,
    val reasoningEffortMapping: Map<ReasoningEffort, ReasoningEffort>?,
    val migrationConfigKey: String,
)

/**
 * Metadata describing a Codex-supported model.
 */
data class ModelPreset(
    /** Stable identifier for the preset. */
    val id: String,
    /** Model slug (e.g., "gpt-5"). */
    val model: String,
    /** Display name shown in UIs. */
    val displayName: String,
    /** Short human description shown in UIs. */
    val description: String,
    /** Reasoning effort applied when none is explicitly chosen. */
    val defaultReasoningEffort: ReasoningEffort,
    /** Supported reasoning effort options. */
    val supportedReasoningEfforts: List<ReasoningEffortPreset>,
    /** Whether this is the default model for new users. */
    val isDefault: Boolean,
    /** Recommended upgrade model. */
    val upgrade: ModelUpgrade?,
    /** Whether this preset should appear in the picker UI. */
    val showInPicker: Boolean,
)

private val PRESETS: List<ModelPreset> by lazy {
    listOf(
        ModelPreset(
            id = "gpt-5.1-codex-max",
            model = "gpt-5.1-codex-max",
            displayName = "gpt-5.1-codex-max",
            description = "Latest Codex-optimized flagship for deep and fast reasoning.",
            defaultReasoningEffort = ReasoningEffort.Medium,
            supportedReasoningEfforts =
                listOf(
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.Low,
                        description = "Fast responses with lighter reasoning",
                    ),
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.Medium,
                        description = "Balances speed and reasoning depth for everyday tasks",
                    ),
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.High,
                        description = "Maximizes reasoning depth for complex problems",
                    ),
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.XHigh,
                        description = "Extra high reasoning depth for complex problems",
                    ),
                ),
            isDefault = true,
            upgrade = null,
            showInPicker = true,
        ),
        ModelPreset(
            id = "gpt-5.1-codex",
            model = "gpt-5.1-codex",
            displayName = "gpt-5.1-codex",
            description = "Optimized for codex.",
            defaultReasoningEffort = ReasoningEffort.Medium,
            supportedReasoningEfforts =
                listOf(
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.Low,
                        description = "Fastest responses with limited reasoning",
                    ),
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.Medium,
                        description = "Dynamically adjusts reasoning based on the task",
                    ),
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.High,
                        description = "Maximizes reasoning depth for complex or ambiguous problems",
                    ),
                ),
            isDefault = false,
            upgrade =
                ModelUpgrade(
                    id = "gpt-5.1-codex-max",
                    reasoningEffortMapping = null,
                    migrationConfigKey = HIDE_GPT_5_1_CODEX_MAX_MIGRATION_PROMPT_CONFIG,
                ),
            showInPicker = true,
        ),
        ModelPreset(
            id = "gpt-5.1-codex-mini",
            model = "gpt-5.1-codex-mini",
            displayName = "gpt-5.1-codex-mini",
            description = "Optimized for codex. Cheaper, faster, but less capable.",
            defaultReasoningEffort = ReasoningEffort.Medium,
            supportedReasoningEfforts =
                listOf(
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.Medium,
                        description = "Dynamically adjusts reasoning based on the task",
                    ),
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.High,
                        description = "Maximizes reasoning depth for complex or ambiguous problems",
                    ),
                ),
            isDefault = false,
            upgrade =
                ModelUpgrade(
                    id = "gpt-5.1-codex-max",
                    reasoningEffortMapping = null,
                    migrationConfigKey = HIDE_GPT_5_1_CODEX_MAX_MIGRATION_PROMPT_CONFIG,
                ),
            showInPicker = true,
        ),
        ModelPreset(
            id = "gpt-5.1",
            model = "gpt-5.1",
            displayName = "gpt-5.1",
            description = "Broad world knowledge with strong general reasoning.",
            defaultReasoningEffort = ReasoningEffort.Medium,
            supportedReasoningEfforts =
                listOf(
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.Low,
                        description = "Balances speed with some reasoning; useful for straightforward queries and short explanations",
                    ),
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.Medium,
                        description = "Provides a solid balance of reasoning depth and latency for general-purpose tasks",
                    ),
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.High,
                        description = "Maximizes reasoning depth for complex or ambiguous problems",
                    ),
                ),
            isDefault = false,
            upgrade =
                ModelUpgrade(
                    id = "gpt-5.1-codex-max",
                    reasoningEffortMapping = null,
                    migrationConfigKey = HIDE_GPT_5_1_CODEX_MAX_MIGRATION_PROMPT_CONFIG,
                ),
            showInPicker = true,
        ),
        // Deprecated models.
        ModelPreset(
            id = "gpt-5-codex",
            model = "gpt-5-codex",
            displayName = "gpt-5-codex",
            description = "Optimized for codex.",
            defaultReasoningEffort = ReasoningEffort.Medium,
            supportedReasoningEfforts =
                listOf(
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.Low,
                        description = "Fastest responses with limited reasoning",
                    ),
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.Medium,
                        description = "Dynamically adjusts reasoning based on the task",
                    ),
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.High,
                        description = "Maximizes reasoning depth for complex or ambiguous problems",
                    ),
                ),
            isDefault = false,
            upgrade =
                ModelUpgrade(
                    id = "gpt-5.1-codex-max",
                    reasoningEffortMapping = null,
                    migrationConfigKey = HIDE_GPT_5_1_CODEX_MAX_MIGRATION_PROMPT_CONFIG,
                ),
            showInPicker = false,
        ),
        ModelPreset(
            id = "gpt-5-codex-mini",
            model = "gpt-5-codex-mini",
            displayName = "gpt-5-codex-mini",
            description = "Optimized for codex. Cheaper, faster, but less capable.",
            defaultReasoningEffort = ReasoningEffort.Medium,
            supportedReasoningEfforts =
                listOf(
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.Medium,
                        description = "Dynamically adjusts reasoning based on the task",
                    ),
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.High,
                        description = "Maximizes reasoning depth for complex or ambiguous problems",
                    ),
                ),
            isDefault = false,
            upgrade =
                ModelUpgrade(
                    id = "gpt-5.1-codex-mini",
                    reasoningEffortMapping = null,
                    migrationConfigKey = HIDE_GPT5_1_MIGRATION_PROMPT_CONFIG,
                ),
            showInPicker = false,
        ),
        ModelPreset(
            id = "gpt-5",
            model = "gpt-5",
            displayName = "gpt-5",
            description = "Broad world knowledge with strong general reasoning.",
            defaultReasoningEffort = ReasoningEffort.Medium,
            supportedReasoningEfforts =
                listOf(
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.Minimal,
                        description = "Fastest responses with little reasoning",
                    ),
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.Low,
                        description = "Balances speed with some reasoning; useful for straightforward queries and short explanations",
                    ),
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.Medium,
                        description = "Provides a solid balance of reasoning depth and latency for general-purpose tasks",
                    ),
                    ReasoningEffortPreset(
                        effort = ReasoningEffort.High,
                        description = "Maximizes reasoning depth for complex or ambiguous problems",
                    ),
                ),
            isDefault = false,
            upgrade =
                ModelUpgrade(
                    id = "gpt-5.1-codex-max",
                    reasoningEffortMapping = null,
                    migrationConfigKey = HIDE_GPT_5_1_CODEX_MAX_MIGRATION_PROMPT_CONFIG,
                ),
            showInPicker = false,
        ),
    )
}

internal fun builtinModelPresets(authMode: AuthMode?): List<ModelPreset> =
    PRESETS.filter { preset ->
        when (authMode) {
            AuthMode.ApiKey -> preset.showInPicker && preset.id != "gpt-5.1-codex-max"
            else -> preset.showInPicker
        }
    }

fun allModelPresets(): List<ModelPreset> = PRESETS
