package io.github.kotlinmania.codex.core

import io.github.kotlinmania.codex.exec.process.SandboxType
import io.github.kotlinmania.codex.utils.Environment
import kotlinx.cinterop.ExperimentalForeignApi
import platform.posix.*
import kotlin.experimental.ExperimentalNativeApi

actual class ProcessHandle(
    actual val pid: Int,
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
): ProcessHandle = ProcessHandle(pid = 0)

actual fun killPlatformChildProcessGroup(process: ProcessHandle) {}

actual fun platformGetUserShellPath(): String? = Environment.SHELL ?: "/bin/sh"

@OptIn(ExperimentalForeignApi::class)
actual fun platformFileExists(path: String): Boolean = access(path, F_OK) == 0

actual fun platformFindInPath(binaryName: String): String? {
    val pathVar = Environment.PATH ?: return null
    for (dir in pathVar.split(':')) {
        val candidate = "$dir/$binaryName"
        if (platformFileExists(candidate)) {
            return candidate
        }
    }
    return null
}

@OptIn(ExperimentalNativeApi::class)
actual fun platformIsWindows(): Boolean = Platform.osFamily == OsFamily.WINDOWS

@OptIn(ExperimentalNativeApi::class)
actual fun platformIsMacOS(): Boolean = Platform.osFamily == OsFamily.MACOSX

actual fun platformGetSandbox(): SandboxType? = null

actual fun platformGetMacosDirParams(): List<Pair<String, String>> = emptyList()
actual fun platformExitProcess(code: Int): Nothing {
    kotlin.system.exitProcess(code)
}
