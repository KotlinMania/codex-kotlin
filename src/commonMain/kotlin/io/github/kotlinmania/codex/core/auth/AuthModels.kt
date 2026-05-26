package io.github.kotlinmania.codex.core.auth

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Authentication mode for API access.
 */
enum class AuthMode {
    ApiKey,
    ChatGPT,
    None
}

/**
 * Determine where Codex should store CLI auth credentials.
 */
enum class AuthCredentialsStoreMode {
    /** Persist credentials in CODEX_HOME/auth.json */
    File,

    /** Persist credentials in the keyring. Fail if unavailable. */
    Keychain,

    /** Use keyring when available; otherwise, fall back to a file in CODEX_HOME */
    Auto
}

/**
 * Known plan types.
 */
enum class KnownPlan {
    Free,
    Plus,
    Pro,
    Team,
    Business,
    Enterprise,
    Edu
}

/**
 * Plan type - known or unknown.
 */
@Serializable
sealed class PlanType {
    @Serializable
    data class Known(val plan: KnownPlan) : PlanType()
    
    @Serializable
    data class Unknown(val value: String) : PlanType()

    companion object {
        fun fromString(value: String): PlanType {
            return when (value.lowercase()) {
                "free" -> Known(KnownPlan.Free)
                "plus" -> Known(KnownPlan.Plus)
                "pro" -> Known(KnownPlan.Pro)
                "team" -> Known(KnownPlan.Team)
                "business" -> Known(KnownPlan.Business)
                "enterprise" -> Known(KnownPlan.Enterprise)
                "edu" -> Known(KnownPlan.Edu)
                else -> Unknown(value)
            }
        }
    }
}

/**
 * ID Token information extracted from JWT.
 */
@Serializable
data class IdTokenInfo(
    val email: String? = null,
    val chatgptPlanType: PlanType? = null,
    val chatgptAccountId: String? = null,
    val rawJwt: String = ""
)

/**
 * Token data containing access and refresh tokens.
 */
@Serializable
data class TokenData(
    var idToken: IdTokenInfo = IdTokenInfo(),
    var accessToken: String = "",
    var refreshToken: String = "",
    val accountId: String? = null
)

/**
 * Structure of auth.json file.
 */
@Serializable
data class AuthDotJson(
    @SerialName("OPENAI_API_KEY") val openaiApiKey: String? = null,
    val tokens: TokenData? = null,
    @SerialName("last_refresh") val lastRefresh: Long? = null
)

/**
 * Forced login method configuration.
 */
enum class ForcedLoginMethod {
    Api,
    Chatgpt
}

/**
 * Account plan type.
 */
enum class AccountPlanType {
    Free,
    Plus,
    Pro,
    Team,
    Business,
    Enterprise,
    Edu,
    Unknown
}

/**
 * Auth configuration.
 */
data class AuthConfig(
    val codexHome: okio.Path,
    val cliAuthCredentialsStoreMode: AuthCredentialsStoreMode,
    val forcedLoginMethod: ForcedLoginMethod?,
    val forcedChatgptWorkspaceId: String?
)

/**
 * Refresh token error types.
 */
sealed class RefreshTokenError(message: String) : Exception(message) {
    class Permanent(val reason: io.github.kotlinmania.codex.core.error.RefreshTokenFailedReason, message: String) : RefreshTokenError(message)
    class Transient(message: String) : RefreshTokenError(message)

    fun failedReason(): io.github.kotlinmania.codex.core.error.RefreshTokenFailedReason? {
        return (this as? Permanent)?.reason
    }
}
