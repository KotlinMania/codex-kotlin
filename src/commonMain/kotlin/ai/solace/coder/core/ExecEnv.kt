// port-lint: source core/src/exec_env.rs
package ai.solace.coder.core

import ai.solace.coder.core.config.EnvironmentVariablePattern
import ai.solace.coder.core.config.ShellEnvironmentPolicy
import ai.solace.coder.core.config.ShellEnvironmentPolicyInherit

/**
 * Construct an environment map based on the rules in the specified policy.
 *
 * The resulting map can be passed to process execution after clearing the
 * inherited environment, ensuring no unintended variables leak to the spawned
 * process.
 *
 * Note: Kotlin Multiplatform doesn't provide a universal way to enumerate the
 * current process environment in common code, so [createEnv] is best-effort and
 * delegates to [populateEnv].
 */
fun createEnv(policy: ShellEnvironmentPolicy): Map<String, String> {
    // TODO: Wire this to platform-specific environment enumeration.
    return populateEnv(emptyList(), policy)
}

internal fun populateEnv(
    vars: Iterable<Pair<String, String>>,
    policy: ShellEnvironmentPolicy,
): Map<String, String> {
    val envMap: MutableMap<String, String> =
        when (policy.inherit) {
            ShellEnvironmentPolicyInherit.All -> vars.toMap(mutableMapOf())
            ShellEnvironmentPolicyInherit.None -> mutableMapOf()
            ShellEnvironmentPolicyInherit.Core -> {
                val coreVars =
                    setOf(
                        "HOME",
                        "LOGNAME",
                        "PATH",
                        "SHELL",
                        "USER",
                        "USERNAME",
                        "TMPDIR",
                        "TEMP",
                        "TMP",
                    )
                vars
                    .asSequence()
                    .filter { (k, _) -> coreVars.contains(k) }
                    .toMap(mutableMapOf())
            }
        }

    fun matchesAny(name: String, patterns: List<EnvironmentVariablePattern>): Boolean =
        patterns.any { it.matches(name) }

    if (!policy.ignoreDefaultExcludes) {
        val defaultExcludes =
            listOf(
                EnvironmentVariablePattern.newCaseInsensitive("*KEY*"),
                EnvironmentVariablePattern.newCaseInsensitive("*SECRET*"),
                EnvironmentVariablePattern.newCaseInsensitive("*TOKEN*"),
            )
        val keysToRemove = envMap.keys.filter { k -> matchesAny(k, defaultExcludes) }
        keysToRemove.forEach { envMap.remove(it) }
    }

    if (policy.exclude.isNotEmpty()) {
        val keysToRemove = envMap.keys.filter { k -> matchesAny(k, policy.exclude) }
        keysToRemove.forEach { envMap.remove(it) }
    }

    for ((key, value) in policy.setVars) {
        envMap[key] = value
    }

    if (policy.includeOnly.isNotEmpty()) {
        val keysToRemove = envMap.keys.filter { k -> !matchesAny(k, policy.includeOnly) }
        keysToRemove.forEach { envMap.remove(it) }
    }

    return envMap
}

