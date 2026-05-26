// port-lint: ignore
@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package io.github.kotlinmania.codex.utils

import platform.posix.fflush
import platform.posix.fputs
import platform.posix.stderr

actual fun writeStderrInline(text: String) {
    fputs(text, stderr)
    fflush(stderr)
}
