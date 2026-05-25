// port-lint: source rmcp-client/src/findCodexHome.rs
package io.github.kotlinmania.codex.rmcp.client

import io.github.kotlinmania.codex.utils.Environment
import io.github.kotlinmania.codex.utils.canonicalizePath

/**
 * Returns the path to the Codex configuration directory, which can be
 * specified by the `CODEX_HOME` environment variable. If not set, defaults to
 * `~/.codex`.
 *
 * - If `CODEX_HOME` is set, the value will be canonicalized and this function
 *   will fail if the path does not exist.
 * - If `CODEX_HOME` is not set, this function does not verify that the
 *   directory exists.
 */
fun findCodexHome(): Result<String> {
    val codexHome = Environment.get("CODEX_HOME")
    if (!codexHome.isNullOrEmpty()) {
        val resolved = canonicalizePath(codexHome)
        return if (resolved == codexHome && !codexHome.startsWith('/')) {
            Result.failure(IllegalStateException("Could not canonicalize path: $codexHome"))
        } else {
            Result.success(resolved)
        }
    }

    val home = Environment.HOME
        ?: return Result.failure(
            IllegalStateException("Could not find home directory")
        )
    val separator = if (home.endsWith('/')) "" else "/"
    return Result.success("$home$separator.codex")
}
