package io.github.kotlinmania.codex.utils

actual object Environment {
    actual fun get(name: String): String? = null
    actual fun isSet(name: String): Boolean = false
    actual val HOME: String? get() = null
    actual val USER: String? get() = null
    actual val PATH: String? get() = null
    actual val SHELL: String? get() = null
    actual val TMPDIR: String get() = "/tmp"
    actual val PWD: String? get() = null
}
