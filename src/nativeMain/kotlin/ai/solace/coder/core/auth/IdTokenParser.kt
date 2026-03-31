// port-lint: source core/src/token_data.rs
package ai.solace.coder.core.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * JWT claims structure for the id_token payload.
 */
@Serializable
private data class IdClaims(
    val email: String? = null,
    @SerialName("https://api.openai.com/auth") val auth: AuthClaims? = null,
)

@Serializable
private data class AuthClaims(
    @SerialName("chatgpt_plan_type") val chatgptPlanType: String? = null,
    @SerialName("chatgpt_account_id") val chatgptAccountId: String? = null,
)

private val lenientJson = Json { ignoreUnknownKeys = true }

/**
 * Parse a JWT id_token string to extract [IdTokenInfo] fields.
 *
 * The JWT is a standard base64url-encoded token: header.payload.signature.
 * Only the payload is decoded and parsed.
 *
 * Ported from Rust codex-rs/core/src/token_data.rs parse_id_token
 */
@OptIn(ExperimentalEncodingApi::class)
actual fun parseIdToken(jwt: String): Result<IdTokenInfo> {
    // JWT format: header.payload.signature
    val parts = jwt.split('.', limit = 3)
    if (parts.size != 3 || parts.any { it.isEmpty() }) {
        return Result.failure(IllegalArgumentException("invalid ID token format"))
    }

    val payloadB64 = parts[1]

    val payloadBytes = try {
        Base64.UrlSafe.decode(padBase64Url(payloadB64))
    } catch (t: Throwable) {
        return Result.failure(IllegalArgumentException("base64 decode error: ${t.message}", t))
    }

    val claims = try {
        lenientJson.decodeFromString(IdClaims.serializer(), payloadBytes.decodeToString())
    } catch (t: Throwable) {
        return Result.failure(IllegalArgumentException("json decode error: ${t.message}", t))
    }

    val auth = claims.auth
    return Result.success(
        IdTokenInfo(
            email = claims.email,
            rawJwt = jwt,
            chatgptPlanType = auth?.chatgptPlanType?.let { PlanType.fromString(it) },
            chatgptAccountId = auth?.chatgptAccountId,
        )
    )
}

/**
 * Pad base64url string to a multiple of 4 characters.
 */
private fun padBase64Url(input: String): String {
    val rem = input.length % 4
    if (rem == 0) return input
    return input + "=".repeat(4 - rem)
}
