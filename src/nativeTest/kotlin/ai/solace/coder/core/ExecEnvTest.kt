// port-lint: source codex-rs/core/src/execEnv.rs
package ai.solace.coder.core

import ai.solace.coder.core.config.EnvironmentVariablePattern
import ai.solace.coder.core.config.ShellEnvironmentPolicy
import ai.solace.coder.core.config.ShellEnvironmentPolicyInherit
import kotlin.test.Test
import kotlin.test.assertEquals

class ExecEnvTest {

    private fun makeVars(pairs: List<Pair<String, String>>): List<Pair<String, String>> = pairs

    @Test
    fun testCoreInheritAndDefaultExcludes() {
        val vars = makeVars(listOf(
            "PATH" to "/usr/bin",
            "HOME" to "/home/user",
            "API_KEY" to "secret",
            "SECRET_TOKEN" to "t",
        ))

        val policy = ShellEnvironmentPolicy(
            inherit = ShellEnvironmentPolicyInherit.Core,
        )
        val result = populateEnv(vars, policy)

        val expected = mapOf(
            "PATH" to "/usr/bin",
            "HOME" to "/home/user",
        )

        assertEquals(expected, result)
    }

    @Test
    fun testIncludeOnly() {
        val vars = makeVars(listOf("PATH" to "/usr/bin", "FOO" to "bar"))

        val policy = ShellEnvironmentPolicy(
            // skip default excludes so nothing is removed prematurely
            ignoreDefaultExcludes = true,
            includeOnly = listOf(EnvironmentVariablePattern.newCaseInsensitive("*PATH")),
        )

        val result = populateEnv(vars, policy)

        val expected = mapOf("PATH" to "/usr/bin")
        assertEquals(expected, result)
    }

    @Test
    fun testSetOverrides() {
        val vars = makeVars(listOf("PATH" to "/usr/bin"))

        val policy = ShellEnvironmentPolicy(
            ignoreDefaultExcludes = true,
        )
        policy.set["NEW_VAR"] = "42"

        val result = populateEnv(vars, policy)

        val expected = mapOf(
            "PATH" to "/usr/bin",
            "NEW_VAR" to "42",
        )
        assertEquals(expected, result)
    }

    @Test
    fun testInheritAll() {
        val vars = makeVars(listOf("PATH" to "/usr/bin", "FOO" to "bar"))

        val policy = ShellEnvironmentPolicy(
            inherit = ShellEnvironmentPolicyInherit.All,
            ignoreDefaultExcludes = true, // keep everything
        )

        val result = populateEnv(vars, policy)
        val expected = vars.toMap()
        assertEquals(expected, result)
    }

    @Test
    fun testInheritAllWithDefaultExcludes() {
        val vars = makeVars(listOf("PATH" to "/usr/bin", "API_KEY" to "secret"))

        val policy = ShellEnvironmentPolicy(
            inherit = ShellEnvironmentPolicyInherit.All,
        )

        val result = populateEnv(vars, policy)
        val expected = mapOf("PATH" to "/usr/bin")
        assertEquals(expected, result)
    }

    @Test
    fun testInheritNone() {
        val vars = makeVars(listOf("PATH" to "/usr/bin", "HOME" to "/home"))

        val policy = ShellEnvironmentPolicy(
            inherit = ShellEnvironmentPolicyInherit.None,
            ignoreDefaultExcludes = true,
        )
        policy.set["ONLY_VAR"] = "yes"

        val result = populateEnv(vars, policy)
        val expected = mapOf("ONLY_VAR" to "yes")
        assertEquals(expected, result)
    }
}
