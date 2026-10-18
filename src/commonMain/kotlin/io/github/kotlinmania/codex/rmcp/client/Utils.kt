// port-lint: source rmcp-client/src/utils.rs
package io.github.kotlinmania.codex.rmcp.client

import io.github.kotlinmania.codex.utils.Environment

internal fun buildDefaultHeaders(
    httpHeaders: Map<String, String>?,
    envHttpHeaders: Map<String, String>?,
): Map<String, String> {
    val headers = mutableMapOf<String, String>()
    if (httpHeaders != null) {
        headers.putAll(httpHeaders)
    }
    if (envHttpHeaders != null) {
        for ((name, envVar) in envHttpHeaders) {
            val value = Environment.get(envVar)
            if (!value.isNullOrBlank()) {
                headers[name] = value
            }
        }
    }
    return headers
}
