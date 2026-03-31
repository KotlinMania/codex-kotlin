// port-lint: source core/src/features/legacy.rs
package ai.solace.coder.core.features

/**
 * Legacy feature toggles and alias mapping.
 *
 * Rust uses these to support older config keys while steering users toward the
 * canonical `[features]` keys.
 */
data class LegacyFeatureToggles(
    val includeApplyPatchTool: Boolean? = null,
    val experimentalUseFreeformApplyPatch: Boolean? = null,
    val experimentalUseUnifiedExecTool: Boolean? = null,
    val toolsWebSearch: Boolean? = null,
    val toolsViewImage: Boolean? = null,
) {
    fun apply(features: Features) {
        setIfSome(
            features = features,
            feature = Feature.ApplyPatchFreeform,
            maybeValue = includeApplyPatchTool,
            aliasKey = "include_apply_patch_tool",
        )
        setIfSome(
            features = features,
            feature = Feature.ApplyPatchFreeform,
            maybeValue = experimentalUseFreeformApplyPatch,
            aliasKey = "experimental_use_freeform_apply_patch",
        )
        setIfSome(
            features = features,
            feature = Feature.UnifiedExec,
            maybeValue = experimentalUseUnifiedExecTool,
            aliasKey = "experimental_use_unified_exec_tool",
        )
        setIfSome(
            features = features,
            feature = Feature.WebSearchRequest,
            maybeValue = toolsWebSearch,
            aliasKey = "tools.web_search",
        )
        setIfSome(
            features = features,
            feature = Feature.ViewImageTool,
            maybeValue = toolsViewImage,
            aliasKey = "tools.view_image",
        )
    }

    companion object {
        private data class Alias(
            val legacyKey: String,
            val feature: Feature,
        )

        private val ALIASES: List<Alias> =
            listOf(
                Alias(
                    legacyKey = "enable_experimental_windows_sandbox",
                    feature = Feature.WindowsSandbox,
                ),
                Alias(
                    legacyKey = "experimental_use_unified_exec_tool",
                    feature = Feature.UnifiedExec,
                ),
                Alias(
                    legacyKey = "experimental_use_freeform_apply_patch",
                    feature = Feature.ApplyPatchFreeform,
                ),
                Alias(
                    legacyKey = "include_apply_patch_tool",
                    feature = Feature.ApplyPatchFreeform,
                ),
                Alias(
                    legacyKey = "web_search",
                    feature = Feature.WebSearchRequest,
                ),
            )

        fun featureForKey(key: String): Feature? =
            ALIASES
                .firstOrNull { it.legacyKey == key }
                ?.feature
    }
}

private fun setIfSome(
    features: Features,
    feature: Feature,
    maybeValue: Boolean?,
    aliasKey: String,
) {
    val enabled = maybeValue ?: return
    setFeature(features = features, feature = feature, enabled = enabled)
    features.recordLegacyUsage(aliasKey, feature)
}

private fun setFeature(
    features: Features,
    feature: Feature,
    enabled: Boolean,
) {
    if (enabled) {
        features.enable(feature)
    } else {
        features.disable(feature)
    }
}

