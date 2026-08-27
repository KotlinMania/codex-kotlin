package io.github.kotlinmania.codex.utils

actual object Environment {
    actual fun get(name: String): String? = System.getenv(name)
    actual fun isSet(name: String): Boolean = System.getenv(name) != null
    actual val HOME: String? get() = System.getProperty("user.home") ?: get("HOME")
    actual val USER: String? get() = System.getProperty("user.name") ?: get("USER")
    actual val PATH: String? get() = get("PATH")
    actual val SHELL: String? get() = get("SHELL")
    actual val TMPDIR: String get() = System.getProperty("java.io.tmpdir") ?: get("TMPDIR") ?: "/tmp"
    actual val PWD: String? get() = System.getProperty("user.dir") ?: get("PWD")
}
