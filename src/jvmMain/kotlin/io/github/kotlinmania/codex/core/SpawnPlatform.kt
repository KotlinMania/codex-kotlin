@file:JvmName("SpawnJvmKt")

package io.github.kotlinmania.codex.core

import io.github.kotlinmania.codex.exec.process.SandboxType
import java.io.File
import java.io.InputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual class ProcessHandle(
    private val process: Process,
) {
    actual val pid: Int get() = try { process.pid().toInt() } catch (_: Throwable) { 0 }
    actual val stdout: ByteArray? get() = null
    actual val stderr: ByteArray? get() = null

    private val stdoutStream: InputStream = process.inputStream
    private val stderrStream: InputStream = process.errorStream

    actual suspend fun onAwait(): Int = withContext(Dispatchers.IO) {
        process.waitFor()
    }

    actual fun readStdout(buffer: ByteArray): Int {
        return stdoutStream.read(buffer)
    }

    actual fun readStderr(buffer: ByteArray): Int {
        return stderrStream.read(buffer)
    }

    actual fun close() {
        try { stdoutStream.close() } catch (_: Throwable) {}
        try { stderrStream.close() } catch (_: Throwable) {}
    }

    actual fun kill() {
        process.destroyForcibly()
    }

    actual fun isAlive(): Boolean = process.isAlive
}

actual fun createPlatformProcess(
    program: String,
    args: List<String>,
    cwd: String,
    env: Map<String, String>
): ProcessHandle {
    val cmd = listOf(program) + args
    val builder = ProcessBuilder(cmd)
    if (cwd.isNotBlank()) {
        builder.directory(File(cwd))
    }
    builder.environment().putAll(env)
    return ProcessHandle(builder.start())
}

actual fun killPlatformChildProcessGroup(process: ProcessHandle) {
    process.kill()
}

actual fun platformGetUserShellPath(): String? =
    System.getenv("SHELL") ?: if (platformIsWindows()) "cmd.exe" else "/bin/sh"

actual fun platformFileExists(path: String): Boolean = File(path).exists()

actual fun platformFindInPath(binaryName: String): String? {
    val pathVar = System.getenv("PATH") ?: return null
    val delimiter = if (platformIsWindows()) ";" else ":"
    for (dir in pathVar.split(delimiter)) {
        val candidate = File(dir, binaryName)
        if (candidate.exists() && candidate.canExecute()) {
            return candidate.absolutePath
        }
        if (platformIsWindows()) {
            val exe = File(dir, "$binaryName.exe")
            if (exe.exists() && exe.canExecute()) {
                return exe.absolutePath
            }
        }
    }
    return null
}

actual fun platformIsWindows(): Boolean =
    System.getProperty("os.name")?.lowercase()?.contains("win") == true

actual fun platformIsMacOS(): Boolean =
    System.getProperty("os.name")?.lowercase()?.contains("mac") == true

actual fun platformGetSandbox(): SandboxType? = null

actual fun platformGetMacosDirParams(): List<Pair<String, String>> = emptyList()

actual fun platformSetOwnerReadWritePermissions(path: String): Int {
    val file = File(path)
    return if (file.setReadable(true, true) && file.setWritable(true, true)) 0 else -1
}

actual fun platformExitProcess(code: Int): Nothing {
    kotlin.system.exitProcess(code)
}
