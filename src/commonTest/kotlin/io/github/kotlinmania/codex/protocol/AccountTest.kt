// port-lint: tests account.rs
package io.github.kotlinmania.codex.protocol

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AccountTest {
    @Test
    fun usageBasedPlanTypesUseExpectedWireNames() {
        assertEquals("self_serve_business_usage_based", PlanType.SelfServeBusinessUsageBased.raw)
        assertEquals("enterprise_cbp_usage_based", PlanType.EnterpriseCbpUsageBased.raw)
        assertEquals("prolite", PlanType.ProLite.raw)

        assertEquals(PlanType.SelfServeBusinessUsageBased, PlanType.fromRaw("self_serve_business_usage_based"))
        assertEquals(PlanType.ProLite, PlanType.fromRaw("prolite"))
        assertEquals(PlanType.EnterpriseCbpUsageBased, PlanType.fromRaw("enterprise_cbp_usage_based"))
    }

    @Test
    fun planFamilyHelpersGroupUsageBasedVariantsWithExistingPlans() {
        assertTrue(PlanType.Team.isTeamLike())
        assertTrue(PlanType.SelfServeBusinessUsageBased.isTeamLike())
        assertFalse(PlanType.Business.isTeamLike())

        assertTrue(PlanType.Business.isBusinessLike())
        assertTrue(PlanType.EnterpriseCbpUsageBased.isBusinessLike())
        assertFalse(PlanType.Team.isBusinessLike())
    }

    @Test
    fun workspaceAccountHelperIncludesUsageBasedWorkspacePlans() {
        assertTrue(PlanType.Team.isWorkspaceAccount())
        assertTrue(PlanType.SelfServeBusinessUsageBased.isWorkspaceAccount())
        assertTrue(PlanType.Business.isWorkspaceAccount())
        assertTrue(PlanType.EnterpriseCbpUsageBased.isWorkspaceAccount())
        assertTrue(PlanType.Enterprise.isWorkspaceAccount())
        assertTrue(PlanType.Edu.isWorkspaceAccount())
        assertFalse(PlanType.Pro.isWorkspaceAccount())
    }

    @Test
    fun authPlanTypeConvertsToAccountPlanType() {
        assertEquals(
            PlanType.EnterpriseCbpUsageBased,
            PlanType.from(AuthPlan.known(KnownPlan.EnterpriseCbpUsageBased)),
        )
        assertEquals(
            PlanType.Enterprise,
            PlanType.from(AuthPlan.known(KnownPlan.Enterprise)),
        )
        assertEquals(
            PlanType.Unknown,
            PlanType.from(AuthPlan.unknown("mystery-tier")),
        )
    }
}
