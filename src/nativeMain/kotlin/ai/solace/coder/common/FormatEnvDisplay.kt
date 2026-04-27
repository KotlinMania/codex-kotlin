// port-lint: source codex-rs/common/src/formatEnvDisplay.rs
package ai.solace.coder.common

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
