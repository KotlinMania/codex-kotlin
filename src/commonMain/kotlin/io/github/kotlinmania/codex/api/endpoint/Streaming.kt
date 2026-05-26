// port-lint: source codex-rs/codex-api/src/endpoint/streaming.rs
package io.github.kotlinmania.codex.api.endpoint

import io.github.kotlinmania.codex.api.AuthProvider
import io.github.kotlinmania.codex.api.addAuthHeaders
import io.github.kotlinmania.codex.api.common.ResponseStream
import io.github.kotlinmania.codex.api.provider.Provider
import io.github.kotlinmania.codex.api.sse.ChannelResponseStream
import io.github.kotlinmania.codex.api.sse.processSse
import io.github.kotlinmania.codex.api.sse.processChatSse
import io.github.kotlinmania.codex.api.telemetry.RequestTelemetry
import io.github.kotlinmania.codex.api.telemetry.SseTelemetry
import io.github.kotlinmania.codex.utils.asSource
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement
import okio.buffer
import kotlin.time.Duration

/**
 * Internal streaming client that handles HTTP streaming with auth and telemetry.
 */
internal class StreamingClient<A : AuthProvider>(
    private val httpClient: HttpClient,
    private val provider: Provider,
    private val auth: A,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) {
    private var requestTelemetry: RequestTelemetry? = null
    private var sseTelemetry: SseTelemetry? = null

    fun withTelemetry(
        request: RequestTelemetry?,
        sse: SseTelemetry?,
    ): StreamingClient<A> {
        requestTelemetry = request
        sseTelemetry = sse
        return this
    }

    fun provider(): Provider = provider

    suspend fun stream(
        path: String,
        body: JsonElement,
        configureExtraHeaders: HttpRequestBuilder.() -> Unit,
        isChat: Boolean = false,
    ): Result<ResponseStream> {
        return try {
            val channel = Channel<Result<io.github.kotlinmania.codex.protocol.ResponseEvent>>(1600)
            
            // We use preparePost and execute to get a streaming response
            val requestBuilder = provider.buildRequest(HttpMethod.Post, path) {
                configureExtraHeaders()
                headers.append(HttpHeaders.Accept, "text/event-stream")
                setBody(body.toString())
                addAuthHeaders(auth)
            }

            scope.launch {
                try {
                    httpClient.prepareRequest(requestBuilder).execute { response ->
                        val source = response.bodyAsChannel().asSource().buffer()
                        if (isChat) {
                            processChatSse(source, channel, Duration.INFINITE, sseTelemetry)
                        } else {
                            processSse(source, channel, Duration.INFINITE, sseTelemetry)
                        }
                    }
                } catch (e: Exception) {
                    channel.send(Result.failure(e))
                } finally {
                    channel.close()
                }
            }

            Result.success(ChannelResponseStream(channel))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

