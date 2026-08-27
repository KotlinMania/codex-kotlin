package io.github.kotlinmania.codex.core.features

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FeaturesTest {
    @Test
    fun testDefaultFeatures() {
        val features = Features.withDefaults()

        // Stable features with defaultEnabled=true
        assertTrue(features.enabled(Feature.ViewImageTool))
        assertTrue(features.enabled(Feature.ShellTool))
        assertTrue(features.enabled(Feature.GhostCommit))

        // Features with defaultEnabled=false
        assertFalse(features.enabled(Feature.UnifiedExec))
        assertFalse(features.enabled(Feature.ApplyPatchFreeform))
        assertFalse(features.enabled(Feature.ParallelToolCalls))
        assertFalse(features.enabled(Feature.WebSearchRequest))

        // Experimental with defaultEnabled=true
        assertTrue(features.enabled(Feature.ExecPolicy))
        assertTrue(features.enabled(Feature.RemoteCompaction))
    }

    @Test
    fun testEnableDisableFeature() {
        val features = Features.withDefaults()

        assertFalse(features.enabled(Feature.UnifiedExec))
        features.enable(Feature.UnifiedExec)
        assertTrue(features.enabled(Feature.UnifiedExec))

        features.disable(Feature.UnifiedExec)
        assertFalse(features.enabled(Feature.UnifiedExec))
    }

    @Test
    fun testFeatureKeyLookup() {
        assertEquals("undo", Feature.GhostCommit.key)
        assertEquals("unified_exec", Feature.UnifiedExec.key)
        assertEquals("shell_tool", Feature.ShellTool.key)
    }

    @Test
    fun testIsKnownKey() {
        assertTrue(Feature.isKnownKey("undo"))
        assertTrue(Feature.isKnownKey("unified_exec"))
        assertTrue(Feature.isKnownKey("experimental_use_unified_exec_tool")) // legacy
        assertTrue(Feature.isKnownKey("include_apply_patch_tool")) // legacy
        assertFalse(Feature.isKnownKey("totally_fake_feature"))
    }

    @Test
    fun testApplyMap() {
        val features = Features.withDefaults()

        val map =
            mapOf(
                "unified_exec" to true,
                "undo" to false,
            )
        features.applyMap(map)

        assertTrue(features.enabled(Feature.UnifiedExec))
        assertFalse(features.enabled(Feature.GhostCommit))
    }

    @Test
    fun testLegacyUsageTracking() {
        val features = Features.withDefaults()

        features.recordLegacyUsage("experimental_use_unified_exec_tool", Feature.UnifiedExec)

        val usages = features.legacyFeatureUsages().toList()
        assertEquals(1, usages.size)
        assertEquals("experimental_use_unified_exec_tool", usages[0].first)
        assertEquals(Feature.UnifiedExec, usages[0].second)
    }

    @Test
    fun testLegacyUsageNotRecordedForCurrentKey() {
        val features = Features.withDefaults()

        // Using the current key should not record legacy usage
        features.recordLegacyUsage("unified_exec", Feature.UnifiedExec)

        val usages = features.legacyFeatureUsages().toList()
        assertEquals(0, usages.size)
    }

    @Test
    fun testFeatureStages() {
        assertEquals(Stage.Stable, Feature.GhostCommit.stage)
        assertEquals(Stage.Experimental, Feature.UnifiedExec.stage)
        assertEquals(Stage.Beta, Feature.ApplyPatchFreeform.stage)
        assertEquals(Stage.Stable, Feature.WebSearchRequest.stage)
    }

    @Test
    fun testFeatureOverrides() {
        val features = Features.withDefaults()
        assertFalse(features.enabled(Feature.ApplyPatchFreeform))
        assertFalse(features.enabled(Feature.WebSearchRequest))

        val overrides =
            FeatureOverrides(
                includeApplyPatchTool = true,
                webSearchRequest = true,
            )
        overrides.apply(features)

        assertTrue(features.enabled(Feature.ApplyPatchFreeform))
        assertTrue(features.enabled(Feature.WebSearchRequest))
    }

    @Test
    fun testCopy() {
        val features = Features.withDefaults()
        features.enable(Feature.UnifiedExec)

        val copy = features.copy()
        assertTrue(copy.enabled(Feature.UnifiedExec))

        // Modifying copy should not affect original
        copy.disable(Feature.UnifiedExec)
        assertTrue(features.enabled(Feature.UnifiedExec))
        assertFalse(copy.enabled(Feature.UnifiedExec))
    }
}
