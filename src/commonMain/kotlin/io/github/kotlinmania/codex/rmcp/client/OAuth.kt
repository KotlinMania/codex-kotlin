// port-lint: source rmcp-client/src/oauth.rs
package io.github.kotlinmania.codex.rmcp.client

import io.github.kotlinmania.codex.core.config.OAuthCredentialsStoreMode
import io.github.kotlinmania.serde.SerdeError
import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.Deserialize
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.I64Deserialize
import io.github.kotlinmania.serdecore.de.IgnoredAny
import io.github.kotlinmania.serdecore.de.MapAccess
import io.github.kotlinmania.serdecore.de.StringDeserialize
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.ser.Serialize
import io.github.kotlinmania.serdecore.ser.Serializer
import kotlin.time.Clock

private class SerdeStr(private val s: String) : Serialize {
    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> = serializer.serializeStr(s)
}

private class SerdeI64(private val v: Long) : Serialize {
    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> = serializer.serializeI64(v)
}

/**
 * Stored credentials for OAuth2 authorization.
 */
data class StoredOAuthTokens(
    val serverName: String,
    val url: String,
    val clientId: String,
    val tokenResponse: WrappedOAuthTokenResponse,
    val expiresAt: Long? = null,
) : Serialize {
    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> {
        val count = 4 + (if (expiresAt != null) 1 else 0)
        val structResult = serializer.serializeStruct("StoredOAuthTokens", count)
        if (structResult.isFailure) return SerdeResult.failure((structResult as SerdeResult.Failure).error)
        val struct = structResult.getOrThrow()

        struct.serializeField("server_name", SerdeStr(serverName))
        struct.serializeField("url", SerdeStr(url))
        struct.serializeField("client_id", SerdeStr(clientId))
        struct.serializeField("token_response", tokenResponse)
        if (expiresAt != null) {
            struct.serializeField("expires_at", SerdeI64(expiresAt))
        }
        return struct.end()
    }

    companion object : Deserialize<StoredOAuthTokens> {
        override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<StoredOAuthTokens> =
            deserializer.deserializeMap(
                object : Visitor<StoredOAuthTokens> {
                    override fun expecting(): String = "a map representing StoredOAuthTokens"

                    override fun <A : MapAccess> visitMap(access: A): SerdeResult<StoredOAuthTokens> {
                        var serverName: String? = null
                        var url: String? = null
                        var clientId: String? = null
                        var tokenResponse: WrappedOAuthTokenResponse? = null
                        var expiresAt: Long? = null

                        while (true) {
                            val keyResult = access.nextKey<String>(StringDeserialize)
                            if (keyResult.isFailure) return SerdeResult.failure((keyResult as SerdeResult.Failure).error)
                            val key = keyResult.getOrNull() ?: break
                            when (key) {
                                "server_name" -> serverName = access.nextValue<String>(StringDeserialize).getOrNull()
                                "url" -> url = access.nextValue<String>(StringDeserialize).getOrNull()
                                "client_id" -> clientId = access.nextValue<String>(StringDeserialize).getOrNull()
                                "token_response" -> tokenResponse = access.nextValue<WrappedOAuthTokenResponse>(WrappedOAuthTokenResponse).getOrNull()
                                "expires_at" -> expiresAt = access.nextValue<Long>(I64Deserialize).getOrNull()
                                else -> access.nextValue<IgnoredAny>(IgnoredAny)
                            }
                        }
                        if (serverName == null || url == null || clientId == null || tokenResponse == null) {
                            return SerdeResult.failure(SerdeError("missing required fields in StoredOAuthTokens"))
                        }
                        return SerdeResult.success(
                            StoredOAuthTokens(serverName, url, clientId, tokenResponse, expiresAt),
                        )
                    }
                },
            )
    }
}

/**
 * Wrap OAuthTokenResponse to allow for serialization and comparison.
 */
data class WrappedOAuthTokenResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long? = null,
    val refreshToken: String? = null,
    val scope: String? = null,
) : Serialize {
    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> {
        val count = 2 + (if (expiresIn != null) 1 else 0) + (if (refreshToken != null) 1 else 0) + (if (scope != null) 1 else 0)
        val structResult = serializer.serializeStruct("WrappedOAuthTokenResponse", count)
        if (structResult.isFailure) return SerdeResult.failure((structResult as SerdeResult.Failure).error)
        val struct = structResult.getOrThrow()

        struct.serializeField("access_token", SerdeStr(accessToken))
        struct.serializeField("token_type", SerdeStr(tokenType))
        if (expiresIn != null) {
            struct.serializeField("expires_in", SerdeI64(expiresIn))
        }
        if (refreshToken != null) {
            struct.serializeField("refresh_token", SerdeStr(refreshToken))
        }
        if (scope != null) {
            struct.serializeField("scope", SerdeStr(scope))
        }
        return struct.end()
    }

    companion object : Deserialize<WrappedOAuthTokenResponse> {
        override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<WrappedOAuthTokenResponse> =
            deserializer.deserializeMap(
                object : Visitor<WrappedOAuthTokenResponse> {
                    override fun expecting(): String = "a map representing WrappedOAuthTokenResponse"

                    override fun <A : MapAccess> visitMap(access: A): SerdeResult<WrappedOAuthTokenResponse> {
                        var accessToken: String? = null
                        var tokenType = "Bearer"
                        var expiresIn: Long? = null
                        var refreshToken: String? = null
                        var scope: String? = null

                        while (true) {
                            val keyResult = access.nextKey<String>(StringDeserialize)
                            if (keyResult.isFailure) return SerdeResult.failure((keyResult as SerdeResult.Failure).error)
                            val key = keyResult.getOrNull() ?: break
                            when (key) {
                                "access_token" -> accessToken = access.nextValue<String>(StringDeserialize).getOrNull()
                                "token_type" -> tokenType = access.nextValue<String>(StringDeserialize).getOrNull() ?: "Bearer"
                                "expires_in" -> expiresIn = access.nextValue<Long>(I64Deserialize).getOrNull()
                                "refresh_token" -> refreshToken = access.nextValue<String>(StringDeserialize).getOrNull()
                                "scope" -> scope = access.nextValue<String>(StringDeserialize).getOrNull()
                                else -> access.nextValue<IgnoredAny>(IgnoredAny)
                            }
                        }
                        if (accessToken == null) {
                            return SerdeResult.failure(SerdeError("missing required field: access_token"))
                        }
                        return SerdeResult.success(
                            WrappedOAuthTokenResponse(accessToken, tokenType, expiresIn, refreshToken, scope),
                        )
                    }
                },
            )
    }
}

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
