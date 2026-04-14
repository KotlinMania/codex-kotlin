// port-lint: source codex-rs/common/src/format_env_display.rs
package ai.solace.coder.common

import kotlin.test.Test
import kotlin.test.assertEquals

class FormatEnvDisplayTest {
    @Test
    fun returns_dash_when_empty() {
        assertEquals("-", formatEnvDisplay(null, emptyList()))

        val emptyMap = emptyMap<String, String>()
        assertEquals("-", formatEnvDisplay(emptyMap, emptyList()))
    }

    @Test
    fun formats_sorted_env_pairs() {
        val env = mutableMapOf<String, String>()
        env["B"] = "two"
        env["A"] = "one"

        assertEquals("A=*****, B=*****", formatEnvDisplay(env, emptyList()))
    }

    @Test
    fun formats_env_vars_with_dollar_prefix() {
        val vars = listOf("TOKEN", "PATH")

        assertEquals("TOKEN=*****, PATH=*****", formatEnvDisplay(null, vars))
    }

    @Test
    fun combines_env_pairs_and_vars() {
        val env = mutableMapOf<String, String>()
        env["HOME"] = "/tmp"
        val vars = listOf("TOKEN")

        assertEquals(
            "HOME=*****, TOKEN=*****",
            formatEnvDisplay(env, vars),
        )
    }
}
