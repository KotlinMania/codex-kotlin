// port-lint: source ollama/src/url.rs
package ai.solace.coder.ollama

/**
 * Identify whether a base_url points at an OpenAI-compatible root (".../v1").
 */
internal fun isOpenaiCompatibleBaseUrl(baseUrl: String): Boolean {
    return baseUrl.trimEnd('/').endsWith("/v1")
}

/**
 * Convert a provider base_url into the native Ollama host root.
 * For example, "http://localhost:11434/v1" -> "http://localhost:11434".
 */
fun baseUrlToHostRoot(baseUrl: String): String {
    val trimmed = baseUrl.trimEnd('/')
    return if (trimmed.endsWith("/v1")) {
        trimmed
            .removeSuffix("/v1")
            .trimEnd('/')
    } else {
        trimmed
    }
}
