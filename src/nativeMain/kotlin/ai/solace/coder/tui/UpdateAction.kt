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
    BrewUpgrade;

    /** Returns the list of command-line arguments for invoking the update. */
    fun commandArgs(): Pair<String, List<String>> = when (this) {
        NpmGlobalLatest -> "npm" to listOf("install", "-g", "@openai/codex")
        BunGlobalLatest -> "bun" to listOf("install", "-g", "@openai/codex")
        BrewUpgrade -> "brew" to listOf("upgrade", "codex")
    }

    /** Returns string representation of the command-line arguments for invoking the update. */
    fun commandStr(): String {
        val (command, args) = commandArgs()
        return shlexTryJoin(listOf(command) + args)
            ?: "$command ${args.joinToString(" ")}"
    }
}

/**
 * Pure detection logic for `UpdateAction`. Exposed so callers can supply the
 * current executable path and environment flags; matches
 * `detect_update_action` in the Rust original.
 */
fun detectUpdateAction(
    isMacos: Boolean,
    currentExe: String,
    managedByNpm: Boolean,
    managedByBun: Boolean,
): UpdateAction? = when {
    managedByNpm -> UpdateAction.NpmGlobalLatest
    managedByBun -> UpdateAction.BunGlobalLatest
    isMacos && (currentExe.startsWith("/opt/homebrew") || currentExe.startsWith("/usr/local")) ->
        UpdateAction.BrewUpgrade
    else -> null
}

/**
 * Resolve an `UpdateAction` from the current process environment. Callers
 * supply the resolved path of the currently-running executable because there
 * is no portable cross-platform helper for `std::env::current_exe()` in
 * Kotlin/Native.
 */
fun getUpdateAction(currentExe: String, isMacos: Boolean): UpdateAction? =
    detectUpdateAction(
        isMacos = isMacos,
        currentExe = currentExe,
        managedByNpm = Environment.isSet("CODEX_MANAGED_BY_NPM"),
        managedByBun = Environment.isSet("CODEX_MANAGED_BY_BUN"),
    )

/**
 * Best-effort `shlex::try_join` equivalent. Returns `null` if any token
 * contains a NUL byte (matching the Rust implementation), otherwise returns a
 * POSIX-quoted shell command string.
 */
private fun shlexTryJoin(tokens: List<String>): String? {
    if (tokens.any { it.contains('\u0000') }) {
        return null
    }
    return tokens.joinToString(" ") { token ->
        when {
            token.isEmpty() -> "''"
            token.all { ch -> ch.isLetterOrDigit() || ch in "@%+=:,./-_" } -> token
            else -> "'" + token.replace("'", "'\\''") + "'"
        }
    }
}
