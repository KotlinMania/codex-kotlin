package io.github.kotlinmania.codex.utils.git

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import platform.posix.fgets
import platform.posix.pclose
import platform.posix.popen
import platform.posix.setenv
import platform.posix.unlink
import platform.posix.unsetenv

@OptIn(ExperimentalForeignApi::class)
internal actual fun platformIsDirectory(path: String): Boolean = platformExecuteCommand(listOf("test", "-d", path)) == 0

@OptIn(ExperimentalForeignApi::class)
internal actual fun platformDeleteFile(path: String): Boolean = unlink(path) == 0

private fun extractExitCode(status: Int): Int {
    val ifExited = (status and 0x7f) == 0
    return if (ifExited) {
        (status shr 8) and 0xff
    } else {
        1
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun platformExecuteGit(
    cwd: String,
    args: List<String>,
    extraEnv: Map<String, String>,
): Pair<String, Int> {
    for ((key, value) in extraEnv) {
        setenv(key, value, 1)
    }

    val gitArgs = if (cwd.isNotEmpty()) listOf("git", "-C", cwd) + args else listOf("git") + args
    val command =
        gitArgs.joinToString(" ") { arg ->
            if (arg.contains(' ') || arg.contains('"') || arg.contains('\'')) {
                "\"${arg.replace("\"", "\\\"")}\""
            } else {
                arg
            }
        }

    try {
        val fp = popen(command, "r") ?: return Pair("", 1)

        val output = StringBuilder()
        val readBuffer = ByteArray(4096)

        while (true) {
            val line = fgets(readBuffer.refTo(0), readBuffer.size, fp) ?: break
            output.append(line.toKString())
        }

        val status = pclose(fp)
        val exitCode = extractExitCode(status)

        return Pair(output.toString(), exitCode)
    } finally {
        for ((key, _) in extraEnv) {
            unsetenv(key)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun platformExecuteCommand(args: List<String>): Int {
    val command =
        args.joinToString(" ") { arg ->
            if (arg.contains(' ') || arg.contains('"') || arg.contains('\'')) {
                "\"${arg.replace("\"", "\\\"")}\""
            } else {
                arg
            }
        }

    val fp = popen(command, "r") ?: return -1
    val status = pclose(fp)
    return extractExitCode(status)
}
