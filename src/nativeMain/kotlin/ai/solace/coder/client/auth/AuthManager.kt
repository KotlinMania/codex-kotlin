// port-lint: sources codex-rs/codex-api/src/auth.rs, codex-rs/core/src/auth.rs
// NOTE: Kotlin package ai.solace.coder.client.auth maps conceptually to Rust crates codex-api (auth provider/helpers)
// and codex-core (AuthManager, storage). The Kotlin file consolidates pieces from both for KMP usage.
package ai.solace.coder.client.auth

import ai.solace.coder.core.CodexErr
import ai.solace.coder.core.CodexResult
import ai.solace.coder.utils.Environment
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Central manager providing a single source of truth for authentication data.
 * Loads once (or on preference change) and then hands out cloned CodexAuth values
 * so the rest of the program has a consistent snapshot.
 *
 * Port origins (Rust):
 * - codex-rs/core/src/auth.rs (AuthManager, storage modes)
 * - codex-rs/codex-api/src/auth.rs (AuthProvider trait, add_auth_headers)
 *
 * Implemented features:
 * - [x] Environment variable detection for API keys (OPENAI_API_KEY, CODEX_API_KEY)
 * - [x] Multiple provider support
 * - [x] Token data structure with account info
 * - [x] Refresh token support (structure in place)
 * - [x] Account plan types
 *
 * TODO: Port remaining features:
 * - [ ] Storage backends: disk (~/.codex/auth.json), keychain
 * - [ ] OAuth flow support for ChatGPT authentication
 * - [ ] Token expiry tracking and automatic refresh
 * - [ ] Secure credential storage with platform keychain integration
 * - [ ] Login/logout flow with browser callback
 */
class AuthManager(
    private val codexHome: String? = null,
    private val enableCodexApiKeyEnv: Boolean = true,
    private val authCredentialsStoreMode: AuthCredentialsStoreMode = AuthCredentialsStoreMode.File,
    initialAuth: CodexAuth? = null,
) {
    private val mutex = Mutex()
    private var cachedAuth: CodexAuth? = null

    init {
        // If an initialAuth was provided (e.g. for testing), use it directly;
        // otherwise load auth from environment / disk on construction.
        cachedAuth = initialAuth ?: loadAuth()
    }

    /**
     * Current cached auth (clone). May be null if not logged in or load failed.
     */
    suspend fun auth(): CodexAuth? {
        return mutex.withLock {
            cachedAuth
        }
    }

    /**
     * Force a reload of the auth information. Returns whether the auth value changed.
     */
    suspend fun reload(): Boolean {
        return mutex.withLock {
            val newAuth = loadAuth()
            val changed = cachedAuth != newAuth
            cachedAuth = newAuth
            changed
        }
    }

    /**
     * Refresh the bearer token (for ChatGPT auth mode).
     *
     * The on-disk auth.json refresh path lives in [ai.solace.coder.core.AuthManager].
     * This client-side manager only refreshes API-key-mode auth (which is a no-op
     * since the key never expires) and surfaces a [CodexErr.RefreshTokenFailed] for
     * ChatGPT-mode auth so callers fall back to the core manager.
     */
    suspend fun refreshToken(): CodexResult<String?> {
        val auth = mutex.withLock { cachedAuth } ?: return CodexResult.success(null)

        return when (auth.mode) {
            AuthMode.ApiKey -> CodexResult.success(auth.apiKey)
            AuthMode.ChatGPT -> CodexResult.failure(
                CodexErr.RefreshTokenFailed(
                    "ChatGPT token refresh requires the core AuthManager (with on-disk storage)",
                ),
            )
            AuthMode.None -> CodexResult.success(null)
        }
    }

    /**
     * Get authorization header value for API requests.
     */
    suspend fun getAuthorizationHeader(): String? {
        return mutex.withLock {
            cachedAuth?.let { auth ->
                when (auth.mode) {
                    AuthMode.ApiKey -> auth.apiKey?.let { "Bearer $it" }
                    AuthMode.ChatGPT -> auth.tokenData?.accessToken?.let { "Bearer $it" }
                    AuthMode.None -> null
                }
            }
        }
    }

    /**
     * Get the current token for API requests.
     */
    suspend fun getToken(): String? {
        return mutex.withLock {
            cachedAuth?.let { auth ->
                when (auth.mode) {
                    AuthMode.ApiKey -> auth.apiKey
                    AuthMode.ChatGPT -> auth.tokenData?.accessToken
                    AuthMode.None -> null
                }
            }
        }
    }

    /**
     * Get account ID if available.
     */
    suspend fun getAccountId(): String? {
        return mutex.withLock {
            cachedAuth?.tokenData?.accountId
        }
    }

    /**
     * Get account email if available.
     */
    suspend fun getAccountEmail(): String? {
        return mutex.withLock {
            cachedAuth?.tokenData?.idToken?.email
        }
    }

    /**
     * Get the account plan type.
     */
    suspend fun getAccountPlanType(): AccountPlanType? {
        return mutex.withLock {
            cachedAuth?.tokenData?.idToken?.planType?.toAccountPlanType()
        }
    }

    /**
     * Log out by clearing the in-memory cached auth.
     *
     * On-disk auth.json deletion is the responsibility of the core
     * [ai.solace.coder.core.AuthManager], which owns the storage backend.
     */
    suspend fun logout(): Boolean {
        return mutex.withLock {
            val hadAuth = cachedAuth != null
            cachedAuth = null
            hadAuth
        }
    }

    /**
     * Load auth from environment variables only.
     *
     * On-disk auth.json loading is owned by the core [ai.solace.coder.core.AuthManager],
     * which has the storage backends (file, keychain) wired up. This client-side
     * manager intentionally only handles env-var-based credentials so it can
     * stay free of platform-specific storage dependencies.
     */
    private fun loadAuth(): CodexAuth? {
        // First, check environment variables
        if (enableCodexApiKeyEnv) {
            val codexApiKey = Environment.get(CODEX_API_KEY_ENV_VAR)?.trim()
            if (!codexApiKey.isNullOrEmpty()) {
                return CodexAuth.fromApiKey(codexApiKey)
            }
        }

        val openaiApiKey = Environment.get(OPENAI_API_KEY_ENV_VAR)?.trim()
        if (!openaiApiKey.isNullOrEmpty()) {
            return CodexAuth.fromApiKey(openaiApiKey)
        }

        return null
    }

    companion object {
        const val OPENAI_API_KEY_ENV_VAR = "OPENAI_API_KEY"
        const val CODEX_API_KEY_ENV_VAR = "CODEX_API_KEY"
        const val ANTHROPIC_API_KEY_ENV_VAR = "ANTHROPIC_API_KEY"

        /**
         * Create a shared AuthManager instance.
         */
        fun shared(
            codexHome: String? = null,
            enableCodexApiKeyEnv: Boolean = true,
            authCredentialsStoreMode: AuthCredentialsStoreMode = AuthCredentialsStoreMode.File
        ): AuthManager {
            return AuthManager(codexHome, enableCodexApiKeyEnv, authCredentialsStoreMode)
        }

        /**
         * Create an AuthManager with a specific API key (for testing).
         */
        fun fromApiKey(apiKey: String): AuthManager {
            return AuthManager(
                enableCodexApiKeyEnv = false,
                initialAuth = CodexAuth.fromApiKey(apiKey),
            )
        }

        /**
         * Create an AuthManager with a specific [CodexAuth], for testing only.
         *
         * Mirrors Rust `AuthManager::from_auth_for_testing`.
         */
        fun fromAuthForTesting(auth: CodexAuth): AuthManager {
            return AuthManager(
                codexHome = null,
                enableCodexApiKeyEnv = false,
                authCredentialsStoreMode = AuthCredentialsStoreMode.File,
                initialAuth = auth,
            )
        }

        /** Rust-style spelling, mirroring `AuthManager::from_auth_for_testing`. */
        fun from_auth_for_testing(auth: CodexAuth): AuthManager = fromAuthForTesting(auth)
    }
}

/**
 * Authentication information for API requests.
 *
 * Ported from Rust codex-rs/core/src/auth.rs CodexAuth
 */
data class CodexAuth(
    val mode: AuthMode,
    val apiKey: String?,
    val tokenData: TokenData?
) {
    companion object {
        fun fromApiKey(apiKey: String): CodexAuth {
            return CodexAuth(
                mode = AuthMode.ApiKey,
                apiKey = apiKey,
                tokenData = null
            )
        }

        fun fromChatGpt(tokenData: TokenData): CodexAuth {
            return CodexAuth(
                mode = AuthMode.ChatGPT,
                apiKey = null,
                tokenData = tokenData
            )
        }
    }
}

/**
 * Token data for OAuth-based authentication.
 *
 * Ported from Rust codex-rs/core/src/token_data.rs TokenData
 */
data class TokenData(
    val idToken: IdTokenInfo,
    val accessToken: String,
    val refreshToken: String,
    val accountId: String?
)

/**
 * Information extracted from the ID token.
 *
 * Ported from Rust codex-rs/core/src/token_data.rs IdTokenInfo
 */
data class IdTokenInfo(
    val email: String?,
    val planType: PlanType?,
    val chatgptAccountId: String?,
    val rawJwt: String?
)

/**
 * Plan type from the ID token.
 */
sealed class PlanType {
    data class Known(val plan: KnownPlan) : PlanType()
    data class Unknown(val raw: String) : PlanType()

    fun toAccountPlanType(): AccountPlanType {
        return when (this) {
            is Known -> when (plan) {
                KnownPlan.Free -> AccountPlanType.Free
                KnownPlan.Plus -> AccountPlanType.Plus
                KnownPlan.Pro -> AccountPlanType.Pro
                KnownPlan.Team -> AccountPlanType.Team
                KnownPlan.Business -> AccountPlanType.Business
                KnownPlan.Enterprise -> AccountPlanType.Enterprise
                KnownPlan.Edu -> AccountPlanType.Edu
            }
            is Unknown -> AccountPlanType.Unknown
        }
    }
}

/**
 * Known subscription plan types.
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
 * Account-facing plan classification.
 *
 * Ported from Rust codex-rs/protocol/src/account.rs PlanType
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
 * Authentication modes supported by Codex.
 */
enum class AuthMode {
    /** API key authentication */
    ApiKey,

    /** ChatGPT OAuth bearer token */
    ChatGPT,

    /** No authentication */
    None
}

/**
 * Provides bearer and account identity information for API requests.
 *
 * Implementations should be cheap and non-blocking; any asynchronous
 * refresh or I/O should be handled by higher layers before requests
 * reach this interface.
 *
 * Ported from Rust codex-api/src/auth.rs AuthProvider trait.
 */
interface AuthProvider {
    /**
     * Get the bearer token for API requests.
     * Returns null if no token is available.
     */
    fun bearerToken(): String?

    /**
     * Get the account ID for the request, if available.
     * Default implementation returns null.
     */
    fun accountId(): String? = null
}

/**
 * Add authentication headers to an HTTP request.
 *
 * Adds:
 * - Authorization: Bearer <token> (if bearer token available)
 * - ChatGPT-Account-ID: <account_id> (if account ID available)
 *
 * Ported from Rust codex-api/src/auth.rs add_auth_headers function.
 *
 * @param auth The auth provider to get credentials from
 * @param headers Mutable map of headers to add to
 * @return The headers map with auth headers added
 */
fun <T : AuthProvider> addAuthHeaders(auth: T, headers: MutableMap<String, String>): MutableMap<String, String> {
    auth.bearerToken()?.let { token ->
        headers["Authorization"] = "Bearer $token"
    }
    auth.accountId()?.let { accountId ->
        headers["ChatGPT-Account-ID"] = accountId
    }
    return headers
}

/**
 * Storage mode for auth credentials.
 *
 * Ported from Rust codex-rs/core/src/auth/storage.rs AuthCredentialsStoreMode
 */
enum class AuthCredentialsStoreMode {
    /** Store in auth.json file */
    File,

    /** Store in system keychain */
    Keychain,

    /** Memory only (no persistence) */
    Memory
}

/**
 * Reason for refresh token failure.
 *
 * Ported from Rust codex-rs/core/src/error.rs RefreshTokenFailedReason
 */
enum class RefreshTokenFailedReason {
    /** Token has expired */
    Expired,

    /** Token was already used (exhausted) */
    Exhausted,

    /** Token was revoked */
    Revoked,

    /** Unknown failure reason */
    Other
}
