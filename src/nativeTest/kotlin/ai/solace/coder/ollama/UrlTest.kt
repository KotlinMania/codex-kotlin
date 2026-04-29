// port-lint: source url.rs
package ai.solace.coder.ollama

import kotlin.test.Test
import kotlin.test.assertEquals

class UrlTest {
    @Test
    fun test_base_url_to_host_root() {
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
