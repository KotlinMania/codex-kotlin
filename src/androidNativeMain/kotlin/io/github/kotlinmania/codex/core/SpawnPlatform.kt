package io.github.kotlinmania.codex.core

import kotlinx.cinterop.ExperimentalForeignApi
import platform.posix.S_IRUSR
import platform.posix.S_IWUSR
import platform.posix.chmod

@OptIn(ExperimentalForeignApi::class)
actual fun platformSetOwnerReadWritePermissions(path: String): Int = chmod(path, (S_IRUSR or S_IWUSR).toUInt())
