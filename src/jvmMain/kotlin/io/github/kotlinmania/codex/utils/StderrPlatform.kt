@file:JvmName("StderrJvmKt")

package io.github.kotlinmania.codex.utils

actual fun writeStderrInline(text: String) {
    System.err.print(text)
    System.err.flush()
}
