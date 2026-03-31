package ai.solace.coder.core.features

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertNull

class FeaturesTest {

    @Test
    fun testDefaultFeatures() {
        val features = Features.withDefaults()

        // Stable features with default_enabled=true
        assertTrue(features.enabled(Feature.ParallelToolCalls))
        assertTrue(features.enabled(Feature.ViewImageTool))
        assertTrue(features.enabled(Feature.ShellTool))
        assertTrue(features.enabled(Feature.ModelWarnings))

        // GhostCommit is stable but default_enabled=false
        assertFalse(features.enabled(Feature.GhostCommit))

        // Experimental features should be disabled by default (unless default_enabled)
        assertFalse(features.enabled(Feature.UnifiedExec))
        assertFalse(features.enabled(Feature.ApplyPatchFreeform))

        // Experimental with default_enabled=true
        assertTrue(features.enabled(Feature.ExecPolicy))
        assertTrue(features.enabled(Feature.RemoteCompaction))
        assertTrue(features.enabled(Feature.Skills))
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
        assertEquals("undo", Feature.GhostCommit.key())
        assertEquals("unified_exec", Feature.UnifiedExec.key())
        assertEquals("shell_tool", Feature.ShellTool.key())
        assertEquals("warnings", Feature.ModelWarnings.key())
    }

    @Test
    fun testIsKnownKey() {
        assertTrue(isKnownFeatureKey("undo"))
        assertTrue(isKnownFeatureKey("unified_exec"))
        assertTrue(isKnownFeatureKey("experimental_use_unified_exec_tool")) // legacy
        assertTrue(isKnownFeatureKey("include_apply_patch_tool")) // legacy
        assertFalse(isKnownFeatureKey("totally_fake_feature"))
    }

    @Test
    fun testApplyMap() {
        val features = Features.withDefaults()

        val map = mapOf(
            "unified_exec" to true,
            "undo" to false
        )
        features.applyMap(map)

        assertTrue(features.enabled(Feature.UnifiedExec))
        assertFalse(features.enabled(Feature.GhostCommit))
    }

    @Test
    fun testLegacyUsageTracking() {
        val features = Features.withDefaults()

        features.recordLegacyUsage("experimental_use_unified_exec_tool", Feature.UnifiedExec)

        val usages = features.legacyFeatureUsages()
        assertEquals(1, usages.size)
        assertEquals("experimental_use_unified_exec_tool", usages[0].first)
        assertEquals(Feature.UnifiedExec, usages[0].second)
    }

    @Test
    fun testLegacyUsageNotRecordedForCurrentKey() {
        val features = Features.withDefaults()

        // Using the current key shouldn't record legacy usage
        features.recordLegacyUsage("unified_exec", Feature.UnifiedExec)

        val usages = features.legacyFeatureUsages()
        assertEquals(0, usages.size)
    }

    @Test
    fun testFeatureStages() {
        assertEquals(Stage.Stable, Feature.GhostCommit.stage())
        assertTrue(Feature.UnifiedExec.stage() is Stage.Beta)
        assertEquals(Stage.Experimental, Feature.ApplyPatchFreeform.stage())
        assertEquals(Stage.Stable, Feature.ModelWarnings.stage())
    }

    @Test
    fun testBetaStageFields() {
        val stage = Feature.UnifiedExec.stage()
        assertTrue(stage is Stage.Beta)
        assertEquals("Background terminal", stage.betaMenuName())
        assertEquals(
            "Run long-running terminal commands in the background.",
            stage.betaMenuDescription()
        )
    }

    @Test
    fun testFeatureOverrides() {
        val features = Features.withDefaults()
        assertFalse(features.enabled(Feature.ApplyPatchFreeform))
        assertFalse(features.enabled(Feature.WebSearchRequest))

        val overrides = FeatureOverrides(
            includeApplyPatchTool = true,
            webSearchRequest = true
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

        // Modifying copy shouldn't affect original
        copy.disable(Feature.UnifiedExec)
        assertTrue(features.enabled(Feature.UnifiedExec))
        assertFalse(copy.enabled(Feature.UnifiedExec))
    }
}
