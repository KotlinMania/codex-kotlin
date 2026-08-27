package io.github.kotlinmania.codex.utils.git

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import platform.posix.S_IFDIR
import platform.posix.S_IFMT
import platform.posix.chdir
import platform.posix.fgets
import platform.posix.getcwd
import platform.posix.pclose
import platform.posix.popen
import platform.posix.setenv
import platform.posix.stat
import platform.posix.unlink
import platform.posix.unsetenv

@OptIn(ExperimentalForeignApi::class)
internal actual fun platformIsDirectory(path: String): Boolean {
    memScoped {
        val st = alloc<stat>()
        if (stat(path, st.ptr) != 0) {
            return false
        }
        return (st.st_mode.toInt() and S_IFMT) == S_IFDIR
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun platformDeleteFile(path: String): Boolean {
    return unlink(path) == 0
}

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

    val gitArgs = listOf("git") + args
    val command =
        gitArgs.joinToString(" ") { arg ->
            if (arg.contains(' ') || arg.contains('"') || arg.contains('\'')) {
                "\"${arg.replace("\"", "\\\"")}\""
            } else {
                arg
            }
        }

    val buffer = ByteArray(1024)
    val savedCwd = getcwd(buffer.refTo(0), buffer.size.toULong())?.toKString() ?: ""

    if (chdir(cwd) != 0) {
        return Pair("", 1)
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
        if (savedCwd.isNotEmpty()) {
            chdir(savedCwd)
        }

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

    val status = platform.posix.system(command)
    return extractExitCode(status)
}
