package io.github.kotlinmania.codex.core

import io.github.kotlinmania.codex.exec.process.SandboxType

actual class ProcessHandle(
    actual val pid: Int = 0,
    actual val stdout: ByteArray? = null,
    actual val stderr: ByteArray? = null,
) {
    actual suspend fun onAwait(): Int = 0
    actual fun readStdout(buffer: ByteArray): Int = -1
    actual fun readStderr(buffer: ByteArray): Int = -1
    actual fun close() {}
    actual fun kill() {}
    actual fun isAlive(): Boolean = false
}

actual fun createPlatformProcess(
    program: String,
    args: List<String>,
    cwd: String,
    env: Map<String, String>
): ProcessHandle = ProcessHandle()

actual fun killPlatformChildProcessGroup(process: ProcessHandle) {}

actual fun platformGetUserShellPath(): String? = "/bin/sh"

actual fun platformFileExists(path: String): Boolean = false

actual fun platformFindInPath(binaryName: String): String? = null

actual fun platformIsWindows(): Boolean = false

actual fun platformIsMacOS(): Boolean = false

actual fun platformGetSandbox(): SandboxType? = null

actual fun platformGetMacosDirParams(): List<Pair<String, String>> = emptyList()

actual fun platformSetOwnerReadWritePermissions(path: String): Int = 0

actual fun platformExitProcess(code: Int): Nothing {
    throw RuntimeException("Exit process with code $code")
}
