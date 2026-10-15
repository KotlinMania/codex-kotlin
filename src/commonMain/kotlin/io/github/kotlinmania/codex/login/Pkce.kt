// port-lint: source login/src/pkce.rs
package io.github.kotlinmania.codex.login

import io.github.kotlinmania.codex.core.auth.Sha256MessageDigest
import io.github.kotlinmania.codex.utils.Base64Util
import io.github.kotlinmania.io.buffered
import io.github.kotlinmania.io.files.Path
import io.github.kotlinmania.io.files.SystemFileSystem
import io.github.kotlinmania.io.readByteArray

data class PkceCodes(
    val codeVerifier: String,
    val codeChallenge: String,
)

fun generatePkce(): PkceCodes {
    val bytes = ByteArray(64)
    secureRandomBytes(bytes)

    // Verifier: URL-safe base64 without padding (43..128 chars)
    val codeVerifier = Base64Util.encodeUrlSafeNoPad(bytes)

    // Challenge (S256): BASE64URL-ENCODE(SHA256(verifier)) without padding
    val digest = Sha256MessageDigest().digest(codeVerifier.encodeToByteArray())
    val codeChallenge = Base64Util.encodeUrlSafeNoPad(digest)

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
