@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

// port-lint: source codex-rs/core/src/environmentContext.rs
package io.github.solaceharmony.codex.utils

import kotlinx.cinterop.toKString
import platform.posix.getenv

/**
 * Platform-agnostic environment variable access — native (POSIX) implementation.
 */
actual object Environment {
    actual fun get(name: String): String? = getenv(name)?.toKString()

    actual fun isSet(name: String): Boolean = getenv(name) != null

    actual val HOME: String? get() = get("HOME")
    actual val USER: String? get() = get("USER")
    actual val PATH: String? get() = get("PATH")
    actual val SHELL: String? get() = get("SHELL")
    actual val TMPDIR: String get() = get("TMPDIR") ?: "/tmp"
    actual val PWD: String? get() = get("PWD")
}
