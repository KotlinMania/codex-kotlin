// port-lint: source codex-rs/codex-api/src/endpoint/streaming.rs
package ai.solace.coder.api.endpoint

import ai.solace.coder.api.AuthProvider
import ai.solace.coder.api.addAuthHeaders
import ai.solace.coder.api.common.ResponseStream
import ai.solace.coder.api.provider.Provider
import ai.solace.coder.api.telemetry.RequestTelemetry
import ai.solace.coder.api.telemetry.SseTelemetry
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
internal typealias StreamSpawner = suspend (
    httpClient: HttpClient,
    request: HttpRequestBuilder.() -> Unit,
    idleTimeout: Duration,
    telemetry: SseTelemetry?,
) -> ResponseStream

/**
 * Internal streaming client that handles HTTP streaming with auth and telemetry.
 *
 * Note: full retry policy / `run_with_request_telemetry` plumbing is not yet
 * ported. Today the stream is spawned directly; once retry telemetry lands the
 * Rust `run_with_request_telemetry(self.provider.retry.to_policy(), ..., builder, |req| transport.stream(req))`
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
        spawner: StreamSpawner,
    ): Result<ResponseStream> {
        return try {
            // Build the request configuration block. Mirrors Rust's `builder` closure
            // in run_with_request_telemetry: each retry attempt re-applies the same
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

