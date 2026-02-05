package ai.solace.coder.core

import ai.solace.coder.core.config.EnvironmentVariablePattern
import ai.solace.coder.core.config.ShellEnvironmentPolicy
import ai.solace.coder.core.config.ShellEnvironmentPolicyInherit
import kotlin.test.Test
import kotlin.test.assertEquals

class ExecEnvTest {
    private fun makeVars(pairs: List<Pair<String, String>>): List<Pair<String, String>> = pairs

    @Test
    fun coreInheritAndDefaultExcludes() {
        val vars =
            makeVars(
                listOf(
                    "PATH" to "/usr/bin",
                    "HOME" to "/home/user",
                    "API_KEY" to "secret",
                    "SECRET_TOKEN" to "t",
                ),
            )

        val policy = ShellEnvironmentPolicy() // defaults: inherit All, default excludes on
        val result = populateEnv(vars, policy)

        val expected =
            mapOf(
                "PATH" to "/usr/bin",
                "HOME" to "/home/user",
            )

        assertEquals(expected, result)
    }

    @Test
    fun includeOnly() {
        val vars = makeVars(listOf("PATH" to "/usr/bin", "FOO" to "bar"))

        val policy =
            ShellEnvironmentPolicy(
                ignoreDefaultExcludes = true,
                includeOnly = listOf(EnvironmentVariablePattern.newCaseInsensitive("*PATH")),
            )

        val result = populateEnv(vars, policy)
        assertEquals(mapOf("PATH" to "/usr/bin"), result)
    }

    @Test
    fun setOverrides() {
        val vars = makeVars(listOf("PATH" to "/usr/bin"))

        val policy =
            ShellEnvironmentPolicy(
                ignoreDefaultExcludes = true,
                setVars = mapOf("NEW_VAR" to "42"),
            )

        val result = populateEnv(vars, policy)
        assertEquals(mapOf("PATH" to "/usr/bin", "NEW_VAR" to "42"), result)
    }

    @Test
    fun inheritAll() {
        val vars = makeVars(listOf("PATH" to "/usr/bin", "FOO" to "bar"))

        val policy =
            ShellEnvironmentPolicy(
                inherit = ShellEnvironmentPolicyInherit.All,
                ignoreDefaultExcludes = true,
            )

        val result = populateEnv(vars, policy)
        assertEquals(vars.toMap(), result)
    }

    @Test
    fun inheritAllWithDefaultExcludes() {
        val vars = makeVars(listOf("PATH" to "/usr/bin", "API_KEY" to "secret"))

        val policy =
            ShellEnvironmentPolicy(
                inherit = ShellEnvironmentPolicyInherit.All,
            )

        val result = populateEnv(vars, policy)
        assertEquals(mapOf("PATH" to "/usr/bin"), result)
    }

    @Test
    fun inheritNone() {
        val vars = makeVars(listOf("PATH" to "/usr/bin", "HOME" to "/home"))

        val policy =
            ShellEnvironmentPolicy(
                inherit = ShellEnvironmentPolicyInherit.None,
                ignoreDefaultExcludes = true,
                setVars = mapOf("ONLY_VAR" to "yes"),
            )

        val result = populateEnv(vars, policy)
        assertEquals(mapOf("ONLY_VAR" to "yes"), result)
    }
}

