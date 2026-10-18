// port-lint: source rmcp-client/src/perform_oauth_login.rs
package io.github.kotlinmania.codex.rmcp.client

import io.github.kotlinmania.codex.core.config.OAuthCredentialsStoreMode
import io.github.kotlinmania.tinyhttp.ListenAddr
import io.github.kotlinmania.tinyhttp.Response
import io.github.kotlinmania.tinyhttp.Server
import io.github.kotlinmania.tinyhttp.StatusCode
import io.github.kotlinmania.urlencoding.dec.decode
import io.github.kotlinmania.urlencoding.enc.encode
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal data class OauthHeaders(
    val httpHeaders: Map<String, String>? = null,
    val envHttpHeaders: Map<String, String>? = null,
)

data class OAuthProviderError(
    val error: String? = null,
    val errorDescription: String? = null,
) {
    override fun toString(): String =
        when {
            error != null && errorDescription != null -> "OAuth provider returned `$error`: $errorDescription"
            error != null -> "OAuth provider returned `$error`"
            errorDescription != null -> "OAuth error: $errorDescription"
            else -> "OAuth provider returned an error"
        }

    companion object {
        fun new(error: String?, errorDescription: String?): OAuthProviderError =
            OAuthProviderError(error, errorDescription)
    }
}

suspend fun performOAuthLogin(
    serverName: String,
    serverUrl: String,
    storeMode: OAuthCredentialsStoreMode,
    httpHeaders: Map<String, String>? = null,
    envHttpHeaders: Map<String, String>? = null,
    scopes: List<String> = emptyList(),
    oauthResource: String? = null,
    callbackPort: Int? = null,
    callbackUrl: String? = null,
): Result<Unit> =
    performOAuthLoginWithBrowserOutput(
        serverName = serverName,
        serverUrl = serverUrl,
        storeMode = storeMode,
        httpHeaders = httpHeaders,
        envHttpHeaders = envHttpHeaders,
        scopes = scopes,
        oauthResource = oauthResource,
        callbackPort = callbackPort,
        callbackUrl = callbackUrl,
        emitBrowserUrl = true,
    )

suspend fun performOAuthLoginSilent(
    serverName: String,
    serverUrl: String,
    storeMode: OAuthCredentialsStoreMode,
    httpHeaders: Map<String, String>? = null,
    envHttpHeaders: Map<String, String>? = null,
    scopes: List<String> = emptyList(),
    oauthResource: String? = null,
    callbackPort: Int? = null,
    callbackUrl: String? = null,
): Result<Unit> =
    performOAuthLoginWithBrowserOutput(
        serverName = serverName,
        serverUrl = serverUrl,
        storeMode = storeMode,
        httpHeaders = httpHeaders,
        envHttpHeaders = envHttpHeaders,
        scopes = scopes,
        oauthResource = oauthResource,
        callbackPort = callbackPort,
        callbackUrl = callbackUrl,
        emitBrowserUrl = false,
    )

private suspend fun performOAuthLoginWithBrowserOutput(
    serverName: String,
    serverUrl: String,
    storeMode: OAuthCredentialsStoreMode,
    httpHeaders: Map<String, String>?,
    envHttpHeaders: Map<String, String>?,
    scopes: List<String>,
    oauthResource: String?,
    callbackPort: Int?,
    callbackUrl: String?,
    emitBrowserUrl: Boolean,
): Result<Unit> {
    val headers = OauthHeaders(httpHeaders, envHttpHeaders)
    val flowResult =
        OauthLoginFlow.new(
            serverName = serverName,
            serverUrl = serverUrl,
            storeMode = storeMode,
            headers = headers,
            scopes = scopes,
            oauthResource = oauthResource,
            launchBrowser = true,
            callbackPort = callbackPort,
            callbackUrl = callbackUrl,
            timeoutSecs = null,
        )
    val flow = flowResult.getOrElse { return Result.failure(it) }
    return flow.finish(emitBrowserUrl)
}

suspend fun performOAuthLoginReturnUrl(
    serverName: String,
    serverUrl: String,
    storeMode: OAuthCredentialsStoreMode,
    httpHeaders: Map<String, String>? = null,
    envHttpHeaders: Map<String, String>? = null,
    scopes: List<String> = emptyList(),
    oauthResource: String? = null,
    timeoutSecs: Long? = null,
    callbackPort: Int? = null,
    callbackUrl: String? = null,
): Result<OauthLoginHandle> {
    val headers = OauthHeaders(httpHeaders, envHttpHeaders)
    val flow =
        OauthLoginFlow
            .new(
                serverName = serverName,
                serverUrl = serverUrl,
                storeMode = storeMode,
                headers = headers,
                scopes = scopes,
                oauthResource = oauthResource,
                launchBrowser = false,
                callbackPort = callbackPort,
                callbackUrl = callbackUrl,
                timeoutSecs = timeoutSecs,
            ).getOrElse { return Result.failure(it) }

    val authorizationUrl = flow.authorizationUrl()
    val completion = flow.spawn()

    return Result.success(OauthLoginHandle(authorizationUrl, completion))
}

internal fun spawnCallbackServer(
    server: Server,
    deferred: CompletableDeferred<CallbackResult>,
    expectedCallbackPath: String,
) {
    CoroutineScope(Dispatchers.Default).launch {
        while (!server.isClosed) {
            val request = server.recv().getOrNull() ?: break
            val path = request.url()
            when (val outcome = parseOAuthCallback(path, expectedCallbackPath)) {
                is CallbackOutcome.Success -> {
                    val response =
                        Response.fromString(
                            "Authentication complete. You may close this window.",
                        )
                    runCatching { request.respond(response) }
                    deferred.complete(CallbackResult.Success(outcome.result))
                    break
                }
                is CallbackOutcome.Error -> {
                    val response =
                        Response
                            .fromString(outcome.error.toString())
                            .withStatusCode(StatusCode(400))
                    runCatching { request.respond(response) }
                    deferred.complete(CallbackResult.Error(outcome.error))
                    break
                }
                is CallbackOutcome.Invalid -> {
                    val response =
                        Response
                            .fromString("Invalid OAuth callback")
                            .withStatusCode(StatusCode(400))
                    runCatching { request.respond(response) }
                }
            }
        }
    }
}

internal data class OauthCallbackResult(
    val code: String,
    val state: String,
)

internal sealed class CallbackResult {
    data class Success(
        val result: OauthCallbackResult,
    ) : CallbackResult()

    data class Error(
        val error: OAuthProviderError,
    ) : CallbackResult()
}

internal sealed class CallbackOutcome {
    data class Success(
        val result: OauthCallbackResult,
    ) : CallbackOutcome()

    data class Error(
        val error: OAuthProviderError,
    ) : CallbackOutcome()

    object Invalid : CallbackOutcome()
}

internal fun parseOAuthCallback(path: String, expectedCallbackPath: String): CallbackOutcome {
    val qIdx = path.indexOf('?')
    if (qIdx < 0) return CallbackOutcome.Invalid
    val route = path.substring(0, qIdx)
    val query = path.substring(qIdx + 1)
    if (route != expectedCallbackPath) return CallbackOutcome.Invalid

    var code: String? = null
    var state: String? = null
    var error: String? = null
    var errorDescription: String? = null

    for (pair in query.split('&')) {
        val eqIdx = pair.indexOf('=')
        if (eqIdx < 0) continue
        val key = pair.substring(0, eqIdx)
        val value = pair.substring(eqIdx + 1)
        val decodedResult = decode(value)
        val decoded = decodedResult.getOrNull() ?: continue
        when (key) {
            "code" -> code = decoded
            "state" -> state = decoded
            "error" -> error = decoded
            "error_description" -> errorDescription = decoded
        }
    }

    if (code != null && state != null) {
        return CallbackOutcome.Success(OauthCallbackResult(code, state))
    }
    if (error != null || errorDescription != null) {
        return CallbackOutcome.Error(OAuthProviderError(error, errorDescription))
    }
    return CallbackOutcome.Invalid
}

class OauthLoginHandle internal constructor(
    private val authorizationUrl: String,
    private val completion: Deferred<Result<Unit>>,
) {
    fun authorizationUrl(): String = authorizationUrl

    internal fun intoParts(): Pair<String, Deferred<Result<Unit>>> =
        authorizationUrl to completion

    suspend fun wait(): Result<Unit> = completion.await()
}

internal class OauthLoginFlow(
    val authUrl: String,
    val deferred: Deferred<CallbackResult>,
    val server: Server,
    val serverName: String,
    val serverUrl: String,
    val storeMode: OAuthCredentialsStoreMode,
    val launchBrowser: Boolean,
    val timeout: Duration,
) {
    fun authorizationUrl(): String = authUrl

    suspend fun finish(emitBrowserUrl: Boolean): Result<Unit> {
        if (launchBrowser) {
            if (emitBrowserUrl) {
                println("Authorize `$serverName` by opening this URL in your browser:\n$authUrl\n")
            }
            // Use webbrowser or log
            println("Authorize `$serverName` by opening this URL in your browser:\n$authUrl\n")
        }

        val callback =
            withTimeoutOrNull(timeout) {
                deferred.await()
            } ?: return Result.failure(IllegalStateException("timed out waiting for OAuth callback"))

        val callbackResult =
            when (callback) {
                is CallbackResult.Success -> callback.result
                is CallbackResult.Error -> return Result.failure(IllegalStateException(callback.error.toString()))
            }

        val stored =
            StoredOAuthTokens(
                serverName = serverName,
                url = serverUrl,
                clientId = "codex",
                tokenResponse =
                    WrappedOAuthTokenResponse(
                        accessToken = callbackResult.code,
                        tokenType = "Bearer",
                        expiresIn = 3600L,
                        refreshToken = null,
                        scope = null,
                    ),
                expiresAt =
                    computeExpiresAtMillis(
                        WrappedOAuthTokenResponse(
                            accessToken = callbackResult.code,
                            tokenType = "Bearer",
                            expiresIn = 3600L,
                        ),
                    ),
            )
        saveOAuthTokens(serverName, stored, storeMode)

        return Result.success(Unit)
    }

    fun spawn(): Deferred<Result<Unit>> {
        val serverNameForLogging = serverName
        val deferredResult = CompletableDeferred<Result<Unit>>()
        CoroutineScope(Dispatchers.Default).launch {
            val result = finish(emitBrowserUrl = false)
            if (result.isFailure) {
                eprintln("Failed to complete OAuth login for '$serverNameForLogging': ${result.exceptionOrNull()?.message}")
            }
            deferredResult.complete(result)
        }
        return deferredResult
    }

    companion object {
        const val DEFAULT_OAUTH_TIMEOUT_SECS: Long = 300L

        fun new(
            serverName: String,
            serverUrl: String,
            storeMode: OAuthCredentialsStoreMode,
            headers: OauthHeaders,
            scopes: List<String>,
            oauthResource: String?,
            launchBrowser: Boolean,
            callbackPort: Int?,
            callbackUrl: String?,
            timeoutSecs: Long?,
        ): Result<OauthLoginFlow> {
            val bindHost = callbackBindHost(callbackUrl)
            val portResult = resolveCallbackPort(callbackPort)
            val port = portResult.getOrElse { return Result.failure(it) } ?: 0

            val server =
                Server.http(bindHost, port).getOrElse {
                    return Result.failure(IllegalStateException(it))
                }

            val redirectUri =
                resolveRedirectUri(server, callbackUrl).getOrElse {
                    return Result.failure(it)
                }
            val callbackPath =
                callbackPathFromRedirectUri(redirectUri).getOrElse {
                    return Result.failure(it)
                }

            val deferred = CompletableDeferred<CallbackResult>()
            spawnCallbackServer(server, deferred, callbackPath)

            val baseAuthUrl = "$serverUrl/oauth/authorize?response_type=code&redirect_uri=${encode(redirectUri)}"
            val authUrl = appendQueryParam(baseAuthUrl, "resource", oauthResource)
            val effectiveTimeoutSecs = (timeoutSecs ?: DEFAULT_OAUTH_TIMEOUT_SECS).coerceAtLeast(1L)
            val timeout = effectiveTimeoutSecs.seconds

            return Result.success(
                OauthLoginFlow(
                    authUrl = authUrl,
                    deferred = deferred,
                    server = server,
                    serverName = serverName,
                    serverUrl = serverUrl,
                    storeMode = storeMode,
                    launchBrowser = launchBrowser,
                    timeout = timeout,
                ),
            )
        }
    }
}

internal fun resolveCallbackPort(callbackPort: Int?): Result<Int?> {
    if (callbackPort != null) {
        if (callbackPort == 0 || callbackPort !in 1..65535) {
            return Result.failure(
                IllegalArgumentException("invalid MCP OAuth callback port `$callbackPort`: port must be between 1 and 65535"),
            )
        }
        return Result.success(callbackPort)
    }
    return Result.success(null)
}

internal fun localRedirectUri(server: Server): Result<String> =
    when (val addr = server.listeningAddr) {
        is ListenAddr.Ip -> {
            val ip = addr.address
            val port = addr.port
            val host = if (ip.contains(':')) "[$ip]" else ip
            Result.success("http://$host:$port/callback")
        }
        else -> Result.failure(IllegalStateException("unable to determine callback address"))
    }

internal fun resolveRedirectUri(server: Server, callbackUrl: String?): Result<String> {
    if (callbackUrl == null) {
        return localRedirectUri(server)
    }
    return Result.success(callbackUrl)
}

internal fun callbackPathFromRedirectUri(redirectUri: String): Result<String> {
    val schemeIdx = redirectUri.indexOf("://")
    if (schemeIdx < 0) {
        return Result.failure(IllegalArgumentException("invalid redirect URI `$redirectUri`"))
    }
    val pathStart = redirectUri.indexOf('/', schemeIdx + 3)
    if (pathStart < 0) {
        return Result.success("/")
    }
    val queryStart = redirectUri.indexOf('?', pathStart)
    val path =
        if (queryStart < 0) {
            redirectUri.substring(pathStart)
        } else {
            redirectUri.substring(pathStart, queryStart)
        }
    return Result.success(path)
}

internal fun callbackBindHost(callbackUrl: String?): String {
    if (callbackUrl == null) return "127.0.0.1"
    val schemeIdx = callbackUrl.indexOf("://")
    val hostPart =
        if (schemeIdx >= 0) {
            val rest = callbackUrl.substring(schemeIdx + 3)
            val slashIdx = rest.indexOf('/')
            val colonIdx = rest.indexOf(':')
            val endIdx =
                when {
                    slashIdx >= 0 && colonIdx >= 0 -> minOf(slashIdx, colonIdx)
                    slashIdx >= 0 -> slashIdx
                    colonIdx >= 0 -> colonIdx
                    else -> rest.length
                }
            rest.substring(0, endIdx)
        } else {
            callbackUrl
        }
    return when (hostPart) {
        "localhost", "127.0.0.1", "::1", "" -> "127.0.0.1"
        else -> "0.0.0.0"
    }
}

internal fun appendQueryParam(url: String, key: String, value: String?): String {
    if (value == null) return url
    val trimmed = value.trim()
    if (trimmed.isEmpty()) return url
    val encoded = encode(trimmed)
    val separator = if (url.contains('?')) "&" else "?"
    return "$url$separator$key=$encoded"
}

private fun eprintln(message: String) {
    println(message)
}
