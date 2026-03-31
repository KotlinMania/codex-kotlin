// port-lint: source tui/src/update_action.rs
package ai.solace.coder.tui

import ai.solace.coder.utils.Environment

/** Update action the CLI should perform after the TUI exits. */
enum class UpdateAction {
    /** Update via `npm install -g @openai/codex@latest`. */
    NpmGlobalLatest,

    /** Update via `bun install -g @openai/codex@latest`. */
    BunGlobalLatest,

    /** Update via `brew upgrade codex`. */
    BrewUpgrade
}

/** Returns the list of command-line arguments for invoking the update. */
fun UpdateAction.commandArgs(): Pair<String, List<String>> = when (this) {
    UpdateAction.NpmGlobalLatest -> "npm" to listOf("install", "-g", "@openai/codex")
    UpdateAction.BunGlobalLatest -> "bun" to listOf("install", "-g", "@openai/codex")
    UpdateAction.BrewUpgrade -> "brew" to listOf("upgrade", "codex")
}

/** Returns string representation of the command-line arguments for invoking the update. */
fun UpdateAction.commandStr(): String {
    val (command, args) = commandArgs()
    val joined = shellJoin(listOf(command) + args)
    return joined ?: "$command ${args.joinToString(" ")}"
}

/** Returns the appropriate update action for this environment, if any. */
fun getUpdateAction(
    isMacos: Boolean,
    currentExe: String,
    getEnv: (String) -> String? = Environment::get
): UpdateAction? {
    val managedByNpm = getEnv("CODEX_MANAGED_BY_NPM") != null
    val managedByBun = getEnv("CODEX_MANAGED_BY_BUN") != null
    return detectUpdateAction(isMacos, currentExe, managedByNpm, managedByBun)
}

internal fun detectUpdateAction(
    isMacos: Boolean,
    currentExe: String,
    managedByNpm: Boolean,
    managedByBun: Boolean,
): UpdateAction? {
    return if (managedByNpm) {
        UpdateAction.NpmGlobalLatest
    } else if (managedByBun) {
        UpdateAction.BunGlobalLatest
    } else if (
        isMacos && (currentExe.startsWith("/opt/homebrew") || currentExe.startsWith("/usr/local"))
    ) {
        UpdateAction.BrewUpgrade
    } else {
        null
    }
}

private fun shellJoin(argv: List<String>): String? {
    // Rough port of `shlex::try_join` behavior: return null if it cannot safely quote.
    // We use POSIX-ish single-quote quoting which is good enough for display purposes.
    return try {
        argv.joinToString(" ") { shQuote(it) }
    } catch (_: Throwable) {
        null
    }
}

private fun shQuote(s: String): String {
    if (s.isEmpty()) return "''"

    val needsQuoting = s.any { ch ->
        ch.isWhitespace() || ch == '\'' || ch == '"' || ch == '\\' || ch == '$' || ch == '`'
    }

    if (!needsQuoting) return s

    // Single-quote and escape embedded single quotes:  abc'def  ->  'abc'"'"'def'
    val parts = s.split("'")
    return buildString {
        append("'")
        for ((idx, part) in parts.withIndex()) {
            append(part)
            if (idx != parts.lastIndex) {
                append("'\"'\"'")
            }
        }
        append("'")
    }
}
