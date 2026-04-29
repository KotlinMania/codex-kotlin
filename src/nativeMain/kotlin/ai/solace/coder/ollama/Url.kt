// port-lint: source url.rs
package ai.solace.coder.ollama

import kotlin.test.Test

/** Identify whether a baseUrl points at an OpenAI-compatible root (".../v1"). */
internal fun isOpenaiCompatibleBaseUrl(baseUrl: String): Boolean {
    return baseUrl.trimEnd('/').endsWith("/v1")
}

/**
 * Convert a provider baseUrl into the native Ollama host root.
 * For example, "http://localhost:11434/v1" -> "http://localhost:11434".
 */
fun baseUrlToHostRoot(baseUrl: String): String {
    val trimmed = baseUrl.trimEnd('/')
    return if (trimmed.endsWith("/v1")) {
        trimmed.trimEnd('/').removeSuffix("/v1").trimEnd('/')
    } else {
        trimmed
    }
}

@Test
internal fun testBaseUrlToHostRoot() {
    check(baseUrlToHostRoot("http://localhost:11434/v1") == "http://localhost:11434")
    check(baseUrlToHostRoot("http://localhost:11434") == "http://localhost:11434")
    check(baseUrlToHostRoot("http://localhost:11434/") == "http://localhost:11434")
}
