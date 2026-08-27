package io.github.kotlinmania.codex.utils

import java.io.File

actual fun canonicalizePath(path: String): String =
    try {
        File(path).canonicalPath
    } catch (_: Throwable) {
        path
    }
