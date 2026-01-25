// port-lint: source codex-rs/codex-api/src/lib.rs
package ai.solace.coder.api

// Re-exports are modeled as typealiases or wrapper functions in Kotlin to mirror Rust's high-level API structure.

/** Re-export from telemetry */
typealias RequestTelemetry = ai.solace.coder.api.telemetry.RequestTelemetry

/** Re-export from telemetry */
typealias SseTelemetry = ai.solace.coder.api.telemetry.SseTelemetry

/** Re-export from common */
typealias CompactionInput = ai.solace.coder.api.common.CompactionInput

/** Re-export from common */
typealias Prompt = ai.solace.coder.api.common.Prompt

/** Re-export from protocol via common */
typealias ResponseEvent = ai.solace.coder.protocol.ResponseEvent

/** Re-export from common */
typealias ResponseStream = ai.solace.coder.api.common.ResponseStream

/** Re-export from common */
typealias ResponsesApiRequest = ai.solace.coder.api.common.ResponsesApiRequest

/** Re-export wrapper for createTextParamForRequest */
fun createTextParamForRequest(
    verbosity: ai.solace.coder.api.common.VerbosityConfig?,
    outputSchema: kotlinx.serialization.json.JsonElement?,
) = ai.solace.coder.api.common.createTextParamForRequest(verbosity, outputSchema)

/** Re-export from endpoint */
typealias ChatClient<A> = ai.solace.coder.api.endpoint.ChatClient<A>

/** Re-export from endpoint */
typealias CompactClient<A> = ai.solace.coder.api.endpoint.CompactClient<A>

/** Re-export from endpoint */
typealias ResponsesClient<A> = ai.solace.coder.api.endpoint.ResponsesClient<A>

/** Re-export from endpoint */
typealias ResponsesOptions = ai.solace.coder.api.endpoint.ResponsesOptions

/** Re-export from error */
typealias ApiError = ai.solace.coder.api.error.ApiError

/** Re-export from provider */
typealias Provider = ai.solace.coder.api.provider.Provider

/** Re-export from provider */
typealias WireApi = ai.solace.coder.api.provider.WireApi

/** Re-export from requests */
typealias ChatRequest = ai.solace.coder.api.requests.ChatRequest

/** Re-export from requests */
typealias ChatRequestBuilder = ai.solace.coder.api.requests.ChatRequestBuilder

/** Re-export from requests */
typealias ResponsesRequest = ai.solace.coder.api.requests.ResponsesRequest

/** Re-export from requests */
typealias ResponsesRequestBuilder = ai.solace.coder.api.requests.ResponsesRequestBuilder

// TODO: streamFromFixture once SSE testing utils are ported

