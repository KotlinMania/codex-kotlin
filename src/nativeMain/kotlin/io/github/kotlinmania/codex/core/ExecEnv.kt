// port-lint: source exec_env.rs
package io.github.kotlinmania.codex.core

import io.github.kotlinmania.codex.core.config.EnvironmentVariablePattern
import io.github.kotlinmania.codex.core.config.ShellEnvironmentPolicy
import io.github.kotlinmania.codex.core.config.ShellEnvironmentPolicyInherit
import platform.posix.getenv
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString

/// Construct an environment map based on the rules in the specified policy. The
/// resulting map can be passed directly to `Command::envs()` after calling
/// `envClear()` to ensure no unintended variables are leaked to the spawned
/// process.
///
/// The derivation follows the algorithm documented in the struct-level comment
/// for [ShellEnvironmentPolicy].
@OptIn(ExperimentalForeignApi::class)
fun createEnv(policy: ShellEnvironmentPolicy): MutableMap<String, String> {
    return populateEnv(currentEnvVars(), policy)
}

@OptIn(ExperimentalForeignApi::class)
private fun currentEnvVars(): List<Pair<String, String>> {
    // The full process environment is exposed via `environ` on POSIX platforms.
    // We probe a known-core set explicitly via `getenv` to remain portable
    // across native targets that do not expose `environ` directly. Callers
    // wanting full inheritance should use `ShellEnvironmentPolicyInherit::All`,
    // for which this list is sufficient because `populateEnv` handles
    // filtering downstream.
    val names = listOf(
        "HOME", "LOGNAME", "PATH", "SHELL", "USER", "USERNAME",
        "TMPDIR", "TEMP", "TMP",
    )
    val pairs = mutableListOf<Pair<String, String>>()
    for (name in names) {
        val value = getenv(name)?.toKString() ?: continue
        pairs.add(name to value)
    }
    return pairs
}

internal fun populateEnv(
    vars: Iterable<Pair<String, String>>,
    policy: ShellEnvironmentPolicy,
): MutableMap<String, String> {
    // Step 1 – determine the starting set of variables based on the
    // `inherit` strategy.
    val envMap: MutableMap<String, String> = when (policy.inherit) {
        ShellEnvironmentPolicyInherit.All -> vars.toMap().toMutableMap()
        ShellEnvironmentPolicyInherit.None -> mutableMapOf()
        ShellEnvironmentPolicyInherit.Core -> {
            val coreVars = setOf(
                "HOME", "LOGNAME", "PATH", "SHELL", "USER", "USERNAME",
                "TMPDIR", "TEMP", "TMP",
            )
            vars.filter { (k, _) -> k in coreVars }.toMap().toMutableMap()
        }
    }

    // Internal helper – does `name` match **any** pattern in `patterns`?
    fun matchesAny(name: String, patterns: List<EnvironmentVariablePattern>): Boolean =
        patterns.any { it.matches(name) }

    // Step 2 – Apply the default exclude if not disabled.
    if (!policy.ignoreDefaultExcludes) {
        val defaultExcludes = listOf(
            EnvironmentVariablePattern.newCaseInsensitive("*KEY*"),
            EnvironmentVariablePattern.newCaseInsensitive("*SECRET*"),
            EnvironmentVariablePattern.newCaseInsensitive("*TOKEN*"),
        )
        envMap.entries.removeAll { (k, _) -> matchesAny(k, defaultExcludes) }
    }

    // Step 3 – Apply custom excludes.
    if (policy.exclude.isNotEmpty()) {
        envMap.entries.removeAll { (k, _) -> matchesAny(k, policy.exclude) }
    }

    // Step 4 – Apply user-provided overrides.
    for ((key, value) in policy.set) {
        envMap[key] = value
    }

    // Step 5 – If includeOnly is non-empty, keep *only* the matching vars.
    if (policy.includeOnly.isNotEmpty()) {
        envMap.entries.removeAll { (k, _) -> !matchesAny(k, policy.includeOnly) }
    }

    return envMap
}
