@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

// port-lint: source codex-rs/core/src/environment_context.rs
package ai.solace.coder.utils

import kotlinx.cinterop.toKString
import platform.posix.getenv

/**
 * Platform-agnostic environment variable access.
 *
 * This provides a clean Kotlin API around the platform.posix.getenv function,
 * centralizing all environment variable access through a single point.
 */
actual object Environment {
    /**
     * Get an environment variable by name.
     *
     * @param name The name of the environment variable
     * @return The value of the environment variable, or null if not set
     */
    actual fun get(name: String): String? = getenv(name)?.toKString()

    /**
     * Get an environment variable by name with a default value.
     *
     * @param name The name of the environment variable
     * @param default The default value to return if the variable is not set
     * @return The value of the environment variable, or the default if not set
     */
    actual fun getOrDefault(name: String, default: String): String = get(name) ?: default

    /**
     * Check if an environment variable is set.
     *
     * @param name The name of the environment variable
     * @return true if the variable is set (even if empty), false otherwise
     */
    actual fun isSet(name: String): Boolean = getenv(name) != null

    /**
     * Get an environment variable, throwing if not set.
     *
     * @param name The name of the environment variable
     * @return The value of the environment variable
     * @throws IllegalStateException if the variable is not set
     */
    actual fun require(name: String): String = get(name)
        ?: error("Required environment variable '$name' is not set")

    // Common environment variables
    actual val HOME: String? get() = get("HOME")
    actual val USER: String? get() = get("USER")
    actual val PATH: String? get() = get("PATH")
    actual val SHELL: String? get() = get("SHELL")
    actual val TMPDIR: String get() = getOrDefault("TMPDIR", "/tmp")
    actual val PWD: String? get() = get("PWD")
}
