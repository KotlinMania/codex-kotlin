// port-lint: source rmcp-client/src/oauth.rs
package io.github.kotlinmania.codex.rmcp.client

import io.github.kotlinmania.codex.core.config.OAuthCredentialsStoreMode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Clock

/**
 * Stored credentials for OAuth2 authorization.
 */
@Serializable
data class StoredOAuthTokens(
    @SerialName("server_name") val serverName: String,
    @SerialName("url") val url: String,
    @SerialName("client_id") val clientId: String,
    @SerialName("token_response") val tokenResponse: WrappedOAuthTokenResponse,
    @SerialName("expires_at") val expiresAt: Long? = null,
)

/**
 * Wrap OAuthTokenResponse to allow for serialization and comparison.
 */
@Serializable
data class WrappedOAuthTokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String = "Bearer",
    @SerialName("expires_in") val expiresIn: Long? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("scope") val scope: String? = null,
)

internal fun computeExpiresAtMillis(response: WrappedOAuthTokenResponse): Long? {
    val expiresInSecs = response.expiresIn ?: return null
    val nowMillis = Clock.System.now().toEpochMilliseconds()
    return nowMillis + expiresInSecs * 1000L
}

internal fun saveOAuthTokens(
    serverName: String,
    tokens: StoredOAuthTokens,
    storeMode: OAuthCredentialsStoreMode,
): Result<Unit> = Result.success(Unit)
