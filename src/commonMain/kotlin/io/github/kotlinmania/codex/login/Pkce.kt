// port-lint: source login/src/pkce.rs
@file:OptIn(kotlin.io.encoding.ExperimentalEncodingApi::class)

package io.github.kotlinmania.codex.login

import io.github.kotlinmania.codex.core.auth.Sha256MessageDigest
import kotlin.io.encoding.Base64
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readByteArray

data class PkceCodes(
    val codeVerifier: String,
    val codeChallenge: String,
)

fun generatePkce(): PkceCodes {
    val bytes = ByteArray(64)
    secureRandomBytes(bytes)

    // Verifier: URL-safe base64 without padding (43..128 chars)
    val codeVerifier = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT).encode(bytes)

    // Challenge (S256): BASE64URL-ENCODE(SHA256(verifier)) without padding
    val digest = Sha256MessageDigest().digest(codeVerifier.encodeToByteArray())
    val codeChallenge = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT).encode(digest)

    return PkceCodes(
        codeVerifier = codeVerifier,
        codeChallenge = codeChallenge,
    )
}

private fun secureRandomBytes(out: ByteArray) {
    val source = SystemFileSystem.source(Path("/dev/urandom")).buffered()
    source.use { buffered ->
        val read = buffered.readByteArray(out.size)
        read.copyInto(out)
    }
}
