// port-lint: source format_env_display.rs
package io.github.solaceharmony.codex.common

import kotlin.test.Test
import kotlin.test.assertEquals

class FormatEnvDisplayTest {
    @Test
    fun returnsDashWhenEmpty() {
        assertEquals("-", formatEnvDisplay(null, emptyList()))

        val emptyMap = emptyMap<String, String>()
        assertEquals("-", formatEnvDisplay(emptyMap, emptyList()))
    }

    @Test
    fun formatsSortedEnvPairs() {
        val env = mutableMapOf<String, String>()
        env["B"] = "two"
        env["A"] = "one"

        assertEquals("A=*****, B=*****", formatEnvDisplay(env, emptyList()))
    }

    @Test
    fun formatsEnvVarsWithDollarPrefix() {
        val vars = listOf("TOKEN", "PATH")

        assertEquals("TOKEN=*****, PATH=*****", formatEnvDisplay(null, vars))
    }

    @Test
    fun combinesEnvPairsAndVars() {
        val env = mutableMapOf<String, String>()
        env["HOME"] = "/tmp"
        val vars = listOf("TOKEN")

        assertEquals(
            "HOME=*****, TOKEN=*****",
            formatEnvDisplay(env, vars),
        )
    }
}
