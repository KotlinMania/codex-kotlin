// port-lint: source rmcp-client/src/find_codex_home.rs
@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package ai.solace.coder.rmcp.client

import ai.solace.coder.utils.Environment
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import platform.posix.realpath

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
        return canonicalize(codexHome)
    }

    val home = Environment.HOME
        ?: return Result.failure(
            IllegalStateException("Could not find home directory")
        )
    val separator = if (home.endsWith('/')) "" else "/"
    return Result.success("$home$separator.codex")
}

private fun canonicalize(path: String): Result<String> = memScoped {
    val buffer = allocArray<ByteVar>(4096)
    val resolved = realpath(path, buffer)
        ?: return Result.failure(
            IllegalStateException("Could not canonicalize path: $path")
        )
    Result.success(resolved.toKString())
}
