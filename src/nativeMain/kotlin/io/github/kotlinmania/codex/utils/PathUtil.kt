// port-lint: ignore
// Native (POSIX) path canonicalization via realpath(3).
package io.github.kotlinmania.codex.utils

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import platform.posix.PATH_MAX
import platform.posix.realpath

@OptIn(ExperimentalForeignApi::class)
actual fun canonicalizePath(path: String): String =
    memScoped {
        val buffer = allocArray<ByteVar>(PATH_MAX)
        val result = realpath(path, buffer)
        if (result != null) result.toKString() else path
    }
