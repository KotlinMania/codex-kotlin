// port-lint: source streaming.rs
package io.github.solaceharmony.codex.api.endpoint

import io.github.solaceharmony.codex.api.AuthProvider
import io.github.solaceharmony.codex.api.addAuthHeaders
import io.github.solaceharmony.codex.api.common.ResponseStream
import io.github.solaceharmony.codex.api.provider.Provider
import io.github.solaceharmony.codex.api.telemetry.RequestTelemetry
import io.github.solaceharmony.codex.api.telemetry.SseTelemetry
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.time.Duration
import kotlinx.serialization.json.JsonElement

/**
 * Spawner closure invoked by [StreamingClient.stream] once the request has been
 * fully configured. Mirrors the Rust signature
 * `fn(StreamResponse, Duration, Option<Arc<dyn SseTelemetry>>) -> ResponseStream`,
 * adapted for Kotlin: the request is delivered as a configured
 * [HttpRequestBuilder] block and an [HttpClient].
 */
/**
 * Internal streaming client that handles HTTP streaming with auth and telemetry.
 *
 * Note: full retry policy / `runWithRequestTelemetry` plumbing is not yet
 * ported. Today the stream is spawned directly; once retry telemetry lands the
 * Rust `runWithRequestTelemetry(self.provider.retry.toPolicy(), ..., builder, |req| transport.stream(req))`
 * loop should wrap the spawner call.
 */
internal class StreamingClient<A : AuthProvider>(
    private val httpClient: HttpClient,
    private val provider: Provider,
    private val auth: A,
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
        spawner:
            suspend (
                httpClient: HttpClient,
                request: HttpRequestBuilder.() -> Unit,
                idleTimeout: Duration,
                telemetry: SseTelemetry?,
            ) -> ResponseStream,
    ): Result<ResponseStream> {
        return try {
            // Build the request configuration block. Mirrors the upstream `builder` closure
            // in runWithRequestTelemetry: each retry attempt re-applies the same
            // headers/body, so we capture the configuration as a lambda the spawner
            // can pass to ktor `prepareGet`/`prepareRequest`.
            val builtBuilder = provider.buildRequest(HttpMethod.Post, path) {
                configureExtraHeaders()
                headers.append(HttpHeaders.Accept, "text/event-stream")
                setBody(body.toString())
                addAuthHeaders(auth)
            }

            val configure: HttpRequestBuilder.() -> Unit = {
                takeFrom(builtBuilder)
            }

            val stream = spawner(httpClient, configure, provider.streamIdleTimeout, sseTelemetry)
            Result.success(stream)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
