// port-lint: source spawn.rs
package io.github.kotlinmania.codex.core

import io.github.kotlinmania.codex.exec.process.SandboxType

/// Experimental environment variable that will be set to some non-empty value
/// if both of the following are true:
///
/// 1. The process was spawned by Codex as part of a shell tool call.
/// 2. SandboxPolicy.hasFullNetworkAccess() was false for the tool call.
///
/// We may try to have just one environment variable for all sandboxing
/// attributes, so this may change in the future.
const val CODEX_SANDBOX_NETWORK_DISABLED_ENV_VAR: String = "CODEX_SANDBOX_NETWORK_DISABLED"

/// Should be set when the process is spawned under a sandbox. Currently, the
/// value is "seatbelt" for macOS, but it may change in the future to
/// accommodate sandboxing configuration and other sandboxing mechanisms.
const val CODEX_SANDBOX_ENV_VAR: String = "CODEX_SANDBOX"

enum class StdioPolicy {
    RedirectForShellTool,
    Inherit,
}

/** Platform-specific process handle */
expect class ProcessHandle {
    val pid: Int
    val stdout:
            ByteArray? // Keep for backward compat or remove? Exec.kt uses it. I will keep it but it
    // might be empty if streamed.
    val stderr: ByteArray?

    suspend fun onAwait(): Int

    fun readStdout(buffer: ByteArray): Int
    fun readStderr(buffer: ByteArray): Int
    fun close()
    fun kill()
    fun isAlive(): Boolean
}

/** Platform-specific process creation */
expect fun createPlatformProcess(
        program: String,
        args: List<String>,
        cwd: String,
        env: Map<String, String>
): ProcessHandle

/** Platform-specific process group killing */
expect fun killPlatformChildProcessGroup(process: ProcessHandle)

/** Platform-specific shell detection */
expect fun platformGetUserShellPath(): String?

expect fun platformFileExists(path: String): Boolean

expect fun platformFindInPath(binaryName: String): String?

expect fun platformIsWindows(): Boolean

expect fun platformIsMacOS(): Boolean

/** Platform-specific sandbox detection */
expect fun platformGetSandbox(): SandboxType?

/** Platform-specific macOS directory parameters */
expect fun platformGetMacosDirParams(): List<Pair<String, String>>

/** Platform-specific: set file permissions to 0600 (owner read/write only) */
expect fun platformSetOwnerReadWritePermissions(path: String): Int

/** Platform-specific process exit */
expect fun platformExitProcess(code: Int): Nothing

