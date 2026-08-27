package io.github.kotlinmania.codex.utils.git

import java.io.File

internal actual fun platformIsDirectory(path: String): Boolean =
    File(path).isDirectory

internal actual fun platformDeleteFile(path: String): Boolean =
    File(path).delete()

internal actual fun platformExecuteGit(
    cwd: String,
    args: List<String>,
    extraEnv: Map<String, String>,
): Pair<String, Int> {
    return try {
        val processArgs = listOf("git") + args
        val processBuilder = ProcessBuilder(processArgs)
            .directory(File(cwd))
            .redirectErrorStream(true)

        processBuilder.environment().putAll(extraEnv)

        val process = processBuilder.start()
        val output = process.inputStream.bufferedReader().readText()
        val exitCode = process.waitFor()

        Pair(output, exitCode)
    } catch (_: Throwable) {
        Pair("", 1)
    }
}

internal actual fun platformExecuteCommand(args: List<String>): Int {
    return try {
        val process = ProcessBuilder(args).start()
        process.waitFor()
    } catch (_: Throwable) {
        1
    }
}
