package ai.solace.coder.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

@OptIn(ExperimentalForeignApi::class)
actual object Environment {
    actual fun get(name: String): String? = getenv(name)?.toKString()

    actual fun getOrDefault(name: String, default: String): String =
        get(name) ?: default

    actual fun isSet(name: String): Boolean = get(name) != null

    actual fun require(name: String): String =
        get(name) ?: throw IllegalStateException("Required environment variable '$name' is not set")

    actual val HOME: String? get() = get("HOME")
    actual val USER: String? get() = get("USER")
    actual val PATH: String? get() = get("PATH")
    actual val SHELL: String? get() = get("SHELL")
    actual val TMPDIR: String get() = get("TMPDIR") ?: "/tmp"
    actual val PWD: String? get() = get("PWD")
}
