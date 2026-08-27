// port-lint: source codex-rs/codex-api/src/lib.rs
package io.github.kotlinmania.codex.api

// Re-exports are modeled as typealiases or wrapper functions in Kotlin to mirror Rust's high-level API structure.

/** Re-export from telemetry */
typealias RequestTelemetry = io.github.kotlinmania.codex.api.telemetry.RequestTelemetry

/** Re-export from telemetry */
typealias SseTelemetry = io.github.kotlinmania.codex.api.telemetry.SseTelemetry

/** Re-export from common */
typealias CompactionInput = io.github.kotlinmania.codex.api.common.CompactionInput

/** Re-export from common */
typealias Prompt = io.github.kotlinmania.codex.api.common.Prompt

/** Re-export from protocol via common */
typealias ResponseEvent = io.github.kotlinmania.codex.protocol.ResponseEvent

/** Re-export from common */
typealias ResponseStream = io.github.kotlinmania.codex.api.common.ResponseStream

/** Re-export from common */
typealias ResponsesApiRequest = io.github.kotlinmania.codex.api.common.ResponsesApiRequest

/** Re-export wrapper for createTextParamForRequest */
fun createTextParamForRequest(
    verbosity: io.github.kotlinmania.codex.protocol.Verbosity?,
    outputSchema: kotlinx.serialization.json.JsonElement?,
) = io.github.kotlinmania.codex.api.common
    .createTextParamForRequest(verbosity, outputSchema)

/** Re-export from endpoint */
typealias ChatClient<A> = io.github.kotlinmania.codex.api.endpoint.ChatClient<A>

/** Re-export from endpoint */
typealias CompactClient<A> = io.github.kotlinmania.codex.api.endpoint.CompactClient<A>

/** Re-export from endpoint */
typealias ResponsesClient<A> = io.github.kotlinmania.codex.api.endpoint.ResponsesClient<A>

/** Re-export from endpoint */
typealias ResponsesOptions = io.github.kotlinmania.codex.api.endpoint.ResponsesOptions

/** Re-export from error */
typealias ApiError = io.github.kotlinmania.codex.api.error.ApiError

/** Re-export from provider */
typealias Provider = io.github.kotlinmania.codex.api.provider.Provider

/** Re-export from provider */
typealias WireApi = io.github.kotlinmania.codex.api.provider.WireApi

/** Re-export from requests */
typealias ChatRequest = io.github.kotlinmania.codex.api.requests.ChatRequest

/** Re-export from requests */
typealias ChatRequestBuilder = io.github.kotlinmania.codex.api.requests.ChatRequestBuilder

/** Re-export from requests */
typealias ResponsesRequest = io.github.kotlinmania.codex.api.requests.ResponsesRequest

/** Re-export from requests */
typealias ResponsesRequestBuilder = io.github.kotlinmania.codex.api.requests.ResponsesRequestBuilder

// TODO: streamFromFixture once SSE testing utils are ported
