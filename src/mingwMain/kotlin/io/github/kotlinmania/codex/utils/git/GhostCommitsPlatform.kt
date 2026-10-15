package io.github.kotlinmania.codex.utils.git

internal actual fun platformIsDirectory(path: String): Boolean = false

internal actual fun platformDeleteFile(path: String): Boolean = false

internal actual fun platformExecuteGit(
    cwd: String,
    args: List<String>,
    extraEnv: Map<String, String>,
): Pair<String, Int> = Pair("", 1)

internal actual fun platformExecuteCommand(args: List<String>): Int = 1
