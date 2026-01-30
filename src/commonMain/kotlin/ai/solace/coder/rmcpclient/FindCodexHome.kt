// port-lint: source rmcp-client/src/find_codex_home.rs
package ai.solace.coder.rmcpclient

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

/**
 * This was copied from codex-core but codex-core depends on this crate.
 * TODO: move this to a shared crate lower in the dependency tree.
 *
 * Returns the path to the Codex configuration directory, which can be
 * specified by the `CODEX_HOME` environment variable. If not set, defaults to
 * `~/.codex`.
 *
 * - If `CODEX_HOME` is set, the value will be canonicalized and this
 *   function will throw if the path does not exist.
 * - If `CODEX_HOME` is not set, this function does not verify that the
 *   directory exists.
 */
internal fun findCodexHome(getEnv: (String) -> String? = ::getPlatformEnv): Path {
    // Honor the `CODEX_HOME` environment variable when it is set to allow users
    // (and tests) to override the default location.
    val codexHomeEnv = getEnv("CODEX_HOME")
    if (codexHomeEnv != null && codexHomeEnv.isNotEmpty()) {
        val path = codexHomeEnv.toPath()
        // Canonicalize - will throw if path doesn't exist
        return FileSystem.SYSTEM.canonicalize(path)
    }

    val homeDir = getEnv("HOME") ?: getEnv("USERPROFILE") 
        ?: throw IllegalStateException("Could not find home directory")
    return homeDir.toPath().resolve(".codex")
}

/**
 * Get environment variable (multiplatform).
 */
internal expect fun getPlatformEnv(name: String): String?
