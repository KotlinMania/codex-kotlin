// port-lint: source ollama/src/url.rs
package io.github.solaceharmony.codex.ollama

import kotlin.test.Test
import kotlin.test.assertEquals

class UrlTest {
    @Test
    fun testBaseUrlToHostRoot() {
        assertEquals(
            "http://localhost:11434",
            baseUrlToHostRoot("http://localhost:11434/v1"),
        )
        assertEquals(
            "http://localhost:11434",
            baseUrlToHostRoot("http://localhost:11434"),
        )
        assertEquals(
            "http://localhost:11434",
            baseUrlToHostRoot("http://localhost:11434/"),
        )
    }
}
