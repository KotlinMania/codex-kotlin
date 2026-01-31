// port-lint: source codex-client/src/request.rs
package ai.solace.coder.client

import io.ktor.http.*
import kotlinx.serialization.json.JsonElement
import kotlin.time.Duration

/**
 * Represents an HTTP request.
 *
 * Ported from Rust codex-rs/codex-client/src/request.rs
 */
data class Request(
    var method: HttpMethod,
    var url: String,
    var headers: HeadersBuilder = HeadersBuilder(),
    var body: JsonElement? = null,
    var timeout: Duration? = null
) {
    companion object {
        fun new(method: HttpMethod, url: String): Request {
            return Request(
                method = method,
                url = url
            )
        }
    }

    /**
     * Set the request body from a JSON element.
     */
    fun withJson(body: JsonElement): Request {
        this.body = body
        return this
    }
}

/**
 * Represents an HTTP response.
 */
data class Response(
    val status: HttpStatusCode,
    val headers: Headers,
    val body: ByteArray
)
