// port-lint: source token_data.rs
package io.github.kotlinmania.codex.login

import io.github.kotlinmania.codex.protocol.AuthPlan
import io.github.kotlinmania.codex.utils.Base64Util
import kotlin.time.Instant

/**
 * Token data stored in auth credentials.
 *
 * Ported from Rust codex-rs/login/src/token_data.rs
 */
data class TokenData(
    val idToken: IdTokenInfo,
    val accessToken: String,
    val refreshToken: String,
    val accountId: String? = null,
)

/**
 * Flat subset of useful claims in id_token from auth credentials.
 */
data class IdTokenInfo(
    val email: String? = null,
    val chatgptPlanType: AuthPlan? = null,
    val chatgptUserId: String? = null,
    val chatgptAccountId: String? = null,
    val chatgptAccountIsFedramp: Boolean = false,
    val rawJwt: String = "",
) {
    fun getChatgptPlanType(): String? = chatgptPlanType?.displayName

    fun getChatgptPlanTypeRaw(): String? = chatgptPlanType?.raw

    fun isWorkspaceAccount(): Boolean = chatgptPlanType?.isWorkspaceAccount() ?: false

    fun isFedrampAccount(): Boolean = chatgptAccountIsFedramp
}

sealed class IdTokenInfoException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    class InvalidFormat : IdTokenInfoException("invalid ID token format")

    class Base64(
        cause: Throwable,
    ) : IdTokenInfoException("invalid base64 payload", cause)

    class Json(
        cause: Throwable,
    ) : IdTokenInfoException("invalid JSON payload", cause)
}

private fun decodeJwtPayload(jwt: String): Result<String> {
    val parts = jwt.split('.')
    if (parts.size < 3 || parts[0].isEmpty() || parts[1].isEmpty() || parts[2].isEmpty()) {
        return Result.failure(IdTokenInfoException.InvalidFormat())
    }

    val payloadBytes =
        try {
            Base64Util.decodeUrlSafeNoPad(parts[1])
        } catch (e: Exception) {
            return Result.failure(IdTokenInfoException.Base64(e))
        }

    return Result.success(payloadBytes.decodeToString())
}

fun parseJwtExpiration(jwt: String): Result<Instant?> =
    decodeJwtPayload(jwt).map { payload ->
        val expMatch = Regex(""""exp"\s*:\s*(\d+)""").find(payload)
        expMatch?.groupValues?.get(1)?.toLongOrNull()?.let { exp ->
            Instant.fromEpochSeconds(exp)
        }
    }

fun parseChatgptJwtClaims(jwt: String): Result<IdTokenInfo> =
    decodeJwtPayload(jwt).map { payload ->
        val email = Regex(""""email"\s*:\s*"([^"]+)"""").find(payload)?.groupValues?.get(1)
        val planTypeRaw = Regex(""""chatgpt_plan_type"\s*:\s*"([^"]+)"""").find(payload)?.groupValues?.get(1)
        val planType = planTypeRaw?.let { AuthPlan.fromRaw(it) }

        val chatgptUserId =
            Regex(""""chatgpt_user_id"\s*:\s*"([^"]+)"""").find(payload)?.groupValues?.get(1)
                ?: Regex(""""user_id"\s*:\s*"([^"]+)"""").find(payload)?.groupValues?.get(1)

        val chatgptAccountId = Regex(""""chatgpt_account_id"\s*:\s*"([^"]+)"""").find(payload)?.groupValues?.get(1)

        val isFedramp =
            Regex(""""chatgpt_account_is_fedramp"\s*:\s*(true|false)""")
                .find(payload)
                ?.groupValues
                ?.get(1)
                ?.toBoolean() ?: false

        IdTokenInfo(
            email = email,
            chatgptPlanType = planType,
            chatgptUserId = chatgptUserId,
            chatgptAccountId = chatgptAccountId,
            chatgptAccountIsFedramp = isFedramp,
            rawJwt = jwt,
        )
    }
