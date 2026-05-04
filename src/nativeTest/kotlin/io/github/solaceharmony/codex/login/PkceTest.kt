// port-lint: source login/src/pkce.rs
@file:OptIn(kotlin.io.encoding.ExperimentalEncodingApi::class)

package io.github.solaceharmony.codex.login

import io.github.solaceharmony.codex.core.auth.Sha256MessageDigest
import kotlin.io.encoding.Base64
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PkceTest {
    @Test
    fun verifierLengthInRange() {
        val codes = generatePkce()
        // 64 random bytes encoded URL-safe base64 (no pad) -> ceil(64/3)*4 - pad = 86 chars
        assertTrue(codes.codeVerifier.length in 43..128)
    }

    @Test
    fun challengeIsSha256OfVerifier() {
        val codes = generatePkce()
        val expected = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT).encode(
            Sha256MessageDigest().digest(codes.codeVerifier.encodeToByteArray())
        )
        assertEquals(expected, codes.codeChallenge)
    }

    @Test
    fun verifierAndChallengeContainOnlyUrlSafeBase64Chars() {
        val codes = generatePkce()
        val urlSafeAlphabet = ('A'..'Z').toSet() + ('a'..'z').toSet() + ('0'..'9').toSet() + setOf('-', '_')
        for (c in codes.codeVerifier) assertTrue(c in urlSafeAlphabet, "verifier char '$c' not URL-safe")
        for (c in codes.codeChallenge) assertTrue(c in urlSafeAlphabet, "challenge char '$c' not URL-safe")
    }

    @Test
    fun successiveCallsProduceDifferentCodes() {
        val a = generatePkce()
        val b = generatePkce()
        assertTrue(a.codeVerifier != b.codeVerifier)
    }
}
