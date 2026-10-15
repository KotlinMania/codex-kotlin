// port-lint: tests token_data.rs
package io.github.kotlinmania.codex.login

import io.github.kotlinmania.codex.protocol.AuthPlan
import io.github.kotlinmania.codex.protocol.KnownPlan
import io.github.kotlinmania.codex.utils.Base64Util
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Instant

class TokenDataTest {
    private fun fakeJwt(payloadJson: String): String {
        val headerJson = """{"alg":"none","typ":"JWT"}"""
        val headerB64 = Base64Util.encodeUrlSafeNoPad(headerJson.encodeToByteArray())
        val payloadB64 = Base64Util.encodeUrlSafeNoPad(payloadJson.encodeToByteArray())
        val signatureB64 = Base64Util.encodeUrlSafeNoPad("sig".encodeToByteArray())
        return "$headerB64.$payloadB64.$signatureB64"
    }

    @Test
    fun idTokenInfoParsesEmailAndPlan() {
        val fakeJwt =
            fakeJwt(
                """
                {
                    "email": "user@example.com",
                    "https://api.openai.com/auth": {
                        "chatgpt_plan_type": "pro"
                    }
                }
                """.trimIndent(),
            )

        val info = parseChatgptJwtClaims(fakeJwt).getOrThrow()
        assertEquals("user@example.com", info.email)
        assertEquals("Pro", info.getChatgptPlanType())
    }

    @Test
    fun idTokenInfoParsesGoPlan() {
        val fakeJwt =
            fakeJwt(
                """
                {
                    "email": "user@example.com",
                    "https://api.openai.com/auth": {
                        "chatgpt_plan_type": "go"
                    }
                }
                """.trimIndent(),
            )

        val info = parseChatgptJwtClaims(fakeJwt).getOrThrow()
        assertEquals("user@example.com", info.email)
        assertEquals("Go", info.getChatgptPlanType())
    }

    @Test
    fun idTokenInfoParsesHcPlanAsEnterprise() {
        val fakeJwt =
            fakeJwt(
                """
                {
                    "email": "user@example.com",
                    "https://api.openai.com/auth": {
                        "chatgpt_plan_type": "hc"
                    }
                }
                """.trimIndent(),
            )

        val info = parseChatgptJwtClaims(fakeJwt).getOrThrow()
        assertEquals("user@example.com", info.email)
        assertEquals("Enterprise", info.getChatgptPlanType())
        assertTrue(info.isWorkspaceAccount())
    }

    @Test
    fun idTokenInfoParsesUsageBasedBusinessPlans() {
        val selfServeBusinessJwt =
            fakeJwt(
                """
                {
                    "email": "user@example.com",
                    "https://api.openai.com/auth": {
                        "chatgpt_plan_type": "self_serve_business_usage_based"
                    }
                }
                """.trimIndent(),
            )
        val selfServeBusiness = parseChatgptJwtClaims(selfServeBusinessJwt).getOrThrow()
        assertEquals("Self Serve Business Usage Based", selfServeBusiness.getChatgptPlanType())
        assertEquals("self_serve_business_usage_based", selfServeBusiness.getChatgptPlanTypeRaw())
        assertTrue(selfServeBusiness.isWorkspaceAccount())

        val enterpriseCbpJwt =
            fakeJwt(
                """
                {
                    "email": "user@example.com",
                    "https://api.openai.com/auth": {
                        "chatgpt_plan_type": "enterprise_cbp_usage_based"
                    }
                }
                """.trimIndent(),
            )
        val enterpriseCbp = parseChatgptJwtClaims(enterpriseCbpJwt).getOrThrow()
        assertEquals("Enterprise CBP Usage Based", enterpriseCbp.getChatgptPlanType())
        assertEquals("enterprise_cbp_usage_based", enterpriseCbp.getChatgptPlanTypeRaw())
        assertTrue(enterpriseCbp.isWorkspaceAccount())
    }

    @Test
    fun idTokenInfoHandlesMissingFields() {
        val fakeJwt = fakeJwt("""{"sub": "123"}""")

        val info = parseChatgptJwtClaims(fakeJwt).getOrThrow()
        assertNull(info.email)
        assertNull(info.getChatgptPlanType())
        assertFalse(info.isFedrampAccount())
    }

    @Test
    fun idTokenInfoParsesFedrampAccountClaim() {
        val fakeJwt =
            fakeJwt(
                """
                {
                    "email": "user@example.com",
                    "https://api.openai.com/auth": {
                        "chatgpt_account_id": "account-fed",
                        "chatgpt_account_is_fedramp": true
                    }
                }
                """.trimIndent(),
            )

        val info = parseChatgptJwtClaims(fakeJwt).getOrThrow()
        assertEquals("account-fed", info.chatgptAccountId)
        assertTrue(info.isFedrampAccount())
    }

    @Test
    fun jwtExpirationParsesExpClaim() {
        val fakeJwt = fakeJwt("""{"exp": 1700000000}""")

        val expiresAt = parseJwtExpiration(fakeJwt).getOrThrow()
        assertEquals(Instant.fromEpochSeconds(1700000000), expiresAt)
    }

    @Test
    fun jwtExpirationHandlesMissingExp() {
        val fakeJwt = fakeJwt("""{"sub": "123"}""")

        val expiresAt = parseJwtExpiration(fakeJwt).getOrThrow()
        assertNull(expiresAt)
    }

    @Test
    fun jwtExpirationRejectsMalformedJwt() {
        val result = parseJwtExpiration("not-a-jwt")
        assertTrue(result.isFailure)
        val err = result.exceptionOrNull()
        assertEquals("invalid ID token format", err?.message)
    }

    @Test
    fun workspaceAccountDetectionMatchesWorkspacePlans() {
        val workspace =
            IdTokenInfo(
                chatgptPlanType = AuthPlan.known(KnownPlan.Business),
            )
        assertTrue(workspace.isWorkspaceAccount())

        val personalPro =
            IdTokenInfo(
                chatgptPlanType = AuthPlan.known(KnownPlan.Pro),
            )
        assertFalse(personalPro.isWorkspaceAccount())

        val personalProLite =
            IdTokenInfo(
                chatgptPlanType = AuthPlan.known(KnownPlan.ProLite),
            )
        assertFalse(personalProLite.isWorkspaceAccount())
    }
}
