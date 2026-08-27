package io.github.kotlinmania.codex.core

import kotlinx.cinterop.ExperimentalForeignApi
import platform.posix._S_IREAD
import platform.posix._S_IWRITE
import platform.posix.chmod

@OptIn(ExperimentalForeignApi::class)
actual fun platformSetOwnerReadWritePermissions(path: String): Int = chmod(path, _S_IREAD or _S_IWRITE)
