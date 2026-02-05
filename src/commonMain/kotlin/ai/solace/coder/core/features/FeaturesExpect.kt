// port-lint: source core/src/features.rs
package ai.solace.coder.core.features

import ai.solace.coder.core.config.ConfigProfile
import ai.solace.coder.core.config.ConfigToml
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * High-level lifecycle stage for a feature.
 */
enum class Stage {
    Experimental,
    Beta,
    Stable,
    Deprecated,
    Removed,
}

/**
 * Unique features toggled via configuration.
 */
enum class Feature {
    /** Create a ghost commit at each turn. */
    GhostCommit,
    /** Use the single unified PTY-backed exec tool. */
    UnifiedExec,
    /** Enable experimental RMCP features such as OAuth login. */
    RmcpClient,
    /** Include the freeform apply_patch tool. */
    ApplyPatchFreeform,
    /** Include the view_image tool. */
    ViewImageTool,
    /** Allow the model to request web searches. */
    WebSearchRequest,
    /** Gate the execpolicy enforcement for shell/unified exec. */
    ExecPolicy,
    /** Enable the model-based risk assessments for sandboxed commands. */
    SandboxCommandAssessment,
    /** Enable Windows sandbox (restricted token) on Windows. */
    WindowsSandbox,
    /** Remote compaction enabled (only for ChatGPT auth). */
    RemoteCompaction,
    /** Enable the default shell tool. */
    ShellTool,
    /** Allow model to call multiple tools in parallel (only for models supporting it). */
    ParallelToolCalls;

    fun key(): String = info().key

    fun stage(): Stage = info().stage

    fun defaultEnabled(): Boolean = info().defaultEnabled

    private fun info(): FeatureSpec =
        FEATURE_SPECS_BY_ID[this] ?: error("Missing FeatureSpec for $this")
}

@Serializable
data class LegacyFeatureUsage(
    val alias: String,
    val feature: Feature,
)

/**
 * Holds the effective set of enabled features.
 */
class Features private constructor(
    private val enabled: MutableSet<Feature>,
    private val legacyUsages: MutableSet<LegacyFeatureUsage>,
) {
    companion object {
        /** Starts with built-in defaults. */
        fun withDefaults(): Features {
            val enabled = mutableSetOf<Feature>()
            for (spec in FEATURES) {
                if (spec.defaultEnabled) {
                    enabled.add(spec.id)
                }
            }
            return Features(enabled = enabled, legacyUsages = mutableSetOf())
        }

        fun fromConfig(
            cfg: ConfigToml,
            configProfile: ConfigProfile,
            overrides: FeatureOverrides,
        ): Features {
            val features = withDefaults()

            val baseLegacy = LegacyFeatureToggles(
                experimentalSandboxCommandAssessment = cfg.experimentalSandboxCommandAssessment,
                experimentalUseFreeformApplyPatch = cfg.experimentalUseFreeformApplyPatch,
                experimentalUseUnifiedExecTool = cfg.experimentalUseUnifiedExecTool,
                experimentalUseRmcpClient = cfg.experimentalUseRmcpClient,
                toolsWebSearch = cfg.tools?.webSearchEffective(),
                toolsViewImage = cfg.tools?.viewImage,
            )
            baseLegacy.apply(features)

            cfg.features?.let { featuresToml ->
                features.applyMap(featuresToml.entries)
            }

            val profileLegacy = LegacyFeatureToggles(
                includeApplyPatchTool = configProfile.includeApplyPatchTool,
                experimentalSandboxCommandAssessment = configProfile.experimentalSandboxCommandAssessment,
                experimentalUseFreeformApplyPatch = configProfile.experimentalUseFreeformApplyPatch,
                experimentalUseUnifiedExecTool = configProfile.experimentalUseUnifiedExecTool,
                experimentalUseRmcpClient = configProfile.experimentalUseRmcpClient,
                toolsWebSearch = configProfile.toolsWebSearch,
                toolsViewImage = configProfile.toolsViewImage,
            )
            profileLegacy.apply(features)

            configProfile.features?.let { featuresToml ->
                features.applyMap(featuresToml.entries)
            }

            overrides.apply(features)

            return features
        }
    }

    fun enabled(feature: Feature): Boolean = enabled.contains(feature)

    fun enable(feature: Feature): Features {
        enabled.add(feature)
        return this
    }

    fun disable(feature: Feature): Features {
        enabled.remove(feature)
        return this
    }

    fun enabledFeatures(): List<Feature> =
        enabled.sortedBy(Feature::ordinal)

    fun copy(): Features =
        Features(
            enabled = enabled.toMutableSet(),
            legacyUsages = legacyUsages.toMutableSet(),
        )

    fun recordLegacyUsageForce(alias: String, feature: Feature) {
        legacyUsages.add(LegacyFeatureUsage(alias = alias, feature = feature))
    }

    fun recordLegacyUsage(alias: String, feature: Feature) {
        if (alias == feature.key()) {
            return
        }
        recordLegacyUsageForce(alias, feature)
    }

    fun legacyFeatureUsages(): List<Pair<String, Feature>> =
        legacyUsages
            .map { it.alias to it.feature }
            .sortedWith(compareBy<Pair<String, Feature>>({ it.first }, { it.second.ordinal }))

    /**
     * Apply a table of key -> bool toggles (e.g. from TOML/JSON).
     */
    fun applyMap(entries: Map<String, Boolean>) {
        for ((key, value) in entries) {
            val feat = featureForKey(key)
            if (feat == null) {
                // Keep behavior non-fatal; the Rust implementation logs a warning.
                println("unknown feature key in config: $key")
                continue
            }

            if (key != feat.key()) {
                recordLegacyUsage(key, feat)
            }

            if (value) {
                enable(feat)
            } else {
                disable(feat)
            }
        }
    }
}

data class FeatureOverrides(
    val includeApplyPatchTool: Boolean? = null,
    val webSearchRequest: Boolean? = null,
    val experimentalSandboxCommandAssessment: Boolean? = null,
) {
    internal fun apply(features: Features) {
        LegacyFeatureToggles(
            includeApplyPatchTool = includeApplyPatchTool,
            toolsWebSearch = webSearchRequest,
            experimentalSandboxCommandAssessment = experimentalSandboxCommandAssessment,
        ).apply(features)
    }
}

/**
 * Deserializable features table for config formats.
 *
 * This is a "flattened map" in the Rust version; in Kotlin we encode/decode it
 * as a JSON object where keys are feature names and values are booleans.
 */
@Serializable(with = FeaturesTomlSerializer::class)
data class FeaturesToml(
    val entries: Map<String, Boolean> = emptyMap(),
)

private object FeaturesTomlSerializer : KSerializer<FeaturesToml> {
    private val mapSerializer = MapSerializer(String.serializer(), Boolean.serializer())

    override val descriptor: SerialDescriptor = mapSerializer.descriptor

    override fun deserialize(decoder: Decoder): FeaturesToml {
        val jsonDecoder = decoder as? JsonDecoder
        if (jsonDecoder == null) {
            return FeaturesToml(mapSerializer.deserialize(decoder))
        }

        val element = jsonDecoder.decodeJsonElement()
        val obj = element as? JsonObject ?: return FeaturesToml()
        val entries =
            obj.entries
                .mapNotNull { (key, value) ->
                    val bool = (value as? JsonPrimitive)?.booleanOrNull ?: return@mapNotNull null
                    key to bool
                }
                .toMap()
        return FeaturesToml(entries)
    }

    override fun serialize(encoder: Encoder, value: FeaturesToml) {
        val jsonEncoder = encoder as? JsonEncoder
        if (jsonEncoder == null) {
            mapSerializer.serialize(encoder, value.entries)
            return
        }

        val obj: JsonElement = JsonObject(value.entries.mapValues { (_, v) -> JsonPrimitive(v) })
        jsonEncoder.encodeJsonElement(obj)
    }
}

/**
 * Single, easy-to-read registry of all feature definitions.
 */
data class FeatureSpec(
    val id: Feature,
    val key: String,
    val stage: Stage,
    val defaultEnabled: Boolean,
)

val FEATURES: List<FeatureSpec> =
    listOf(
        // Stable features.
        FeatureSpec(
            id = Feature.GhostCommit,
            key = "undo",
            stage = Stage.Stable,
            defaultEnabled = true,
        ),
        FeatureSpec(
            id = Feature.ViewImageTool,
            key = "view_image_tool",
            stage = Stage.Stable,
            defaultEnabled = true,
        ),
        // Unstable features.
        FeatureSpec(
            id = Feature.UnifiedExec,
            key = "unified_exec",
            stage = Stage.Experimental,
            defaultEnabled = false,
        ),
        FeatureSpec(
            id = Feature.RmcpClient,
            key = "rmcp_client",
            stage = Stage.Experimental,
            defaultEnabled = false,
        ),
        FeatureSpec(
            id = Feature.ApplyPatchFreeform,
            key = "apply_patch_freeform",
            stage = Stage.Beta,
            defaultEnabled = false,
        ),
        FeatureSpec(
            id = Feature.WebSearchRequest,
            key = "web_search_request",
            stage = Stage.Stable,
            defaultEnabled = false,
        ),
        FeatureSpec(
            id = Feature.ExecPolicy,
            key = "exec_policy",
            stage = Stage.Experimental,
            defaultEnabled = true,
        ),
        FeatureSpec(
            id = Feature.SandboxCommandAssessment,
            key = "experimental_sandbox_command_assessment",
            stage = Stage.Experimental,
            defaultEnabled = false,
        ),
        FeatureSpec(
            id = Feature.WindowsSandbox,
            key = "enable_experimental_windows_sandbox",
            stage = Stage.Experimental,
            defaultEnabled = false,
        ),
        FeatureSpec(
            id = Feature.RemoteCompaction,
            key = "remote_compaction",
            stage = Stage.Experimental,
            defaultEnabled = true,
        ),
        FeatureSpec(
            id = Feature.ParallelToolCalls,
            key = "parallel",
            stage = Stage.Experimental,
            defaultEnabled = false,
        ),
        FeatureSpec(
            id = Feature.ShellTool,
            key = "shell_tool",
            stage = Stage.Stable,
            defaultEnabled = true,
        ),
    )

private val FEATURE_SPECS_BY_ID: Map<Feature, FeatureSpec> =
    FEATURES.associateBy(FeatureSpec::id)

private fun featureForKey(key: String): Feature? {
    for (spec in FEATURES) {
        if (spec.key == key) {
            return spec.id
        }
    }
    return LegacyFeatureToggles.featureForKey(key)
}

fun isKnownFeatureKey(key: String): Boolean =
    featureForKey(key) != null
