// port-lint: source format_env_display.rs
package io.github.solaceharmony.codex.common

fun formatEnvDisplay(env: Map<String, String>?, envVars: List<String>): String {
    val parts = mutableListOf<String>()

    if (env != null) {
        val pairs = env.entries.toMutableList()
        pairs.sortBy { it.key }
        parts.addAll(pairs.map { (key, _) -> "$key=*****" })
    }

    if (envVars.isNotEmpty()) {
        parts.addAll(envVars.map { v -> "$v=*****" })
    }

    return if (parts.isEmpty()) {
        "-"
    } else {
        parts.joinToString(", ")
    }
}
