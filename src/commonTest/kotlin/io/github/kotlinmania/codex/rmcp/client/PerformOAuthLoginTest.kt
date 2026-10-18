// port-lint: tests rmcp-client/src/perform_oauth_login.rs
package io.github.kotlinmania.codex.rmcp.client

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PerformOAuthLoginTest {
    @Test
    fun parseOAuthCallbackAcceptsDefaultPath() {
        val parsed = parseOAuthCallback("/callback?code=abc&state=xyz", "/callback")
        assertTrue(parsed is CallbackOutcome.Success)
        assertEquals("abc", parsed.result.code)
        assertEquals("xyz", parsed.result.state)
    }

    @Test
    fun parseOAuthCallbackAcceptsCustomPath() {
        val parsed = parseOAuthCallback("/oauth/callback?code=abc&state=xyz", "/oauth/callback")
        assertTrue(parsed is CallbackOutcome.Success)
        assertEquals("abc", parsed.result.code)
        assertEquals("xyz", parsed.result.state)
    }

    @Test
    fun parseOAuthCallbackRejectsWrongPath() {
        val parsed = parseOAuthCallback("/callback?code=abc&state=xyz", "/oauth/callback")
        assertEquals(CallbackOutcome.Invalid, parsed)
    }

    @Test
    fun parseOAuthCallbackReturnsProviderError() {
        val parsed =
            parseOAuthCallback(
                "/callback?error=invalid_scope&error_description=scope%20rejected",
                "/callback",
            )
        assertEquals(
            CallbackOutcome.Error(
                OAuthProviderError(
                    error = "invalid_scope",
                    errorDescription = "scope rejected",
                ),
            ),
            parsed,
        )
    }

    @Test
    fun callbackPathComesFromRedirectUri() {
        val path =
            callbackPathFromRedirectUri("https://example.com/oauth/callback")
                .getOrThrow()
        assertEquals("/oauth/callback", path)
    }

    @Test
    fun appendQueryParamAddsResourceToAbsoluteUrl() {
        val url =
            appendQueryParam(
                "https://example.com/authorize?scope=read",
                "resource",
                "https://api.example.com",
            )
        assertEquals(
            "https://example.com/authorize?scope=read&resource=https%3A%2F%2Fapi.example.com",
            url,
        )
    }

    @Test
    fun appendQueryParamIgnoresEmptyValues() {
        val url =
            appendQueryParam(
                "https://example.com/authorize?scope=read",
                "resource",
                "   ",
            )
        assertEquals("https://example.com/authorize?scope=read", url)
    }

    @Test
    fun appendQueryParamHandlesUnparseableUrl() {
        val url = appendQueryParam("not a url", "resource", "api/resource")
        assertEquals("not a url?resource=api%2Fresource", url)
    }
}
