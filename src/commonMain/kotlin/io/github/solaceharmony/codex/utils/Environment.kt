// port-lint: source codex-rs/core/src/environmentContext.rs
package io.github.solaceharmony.codex.utils

/**
 * Platform-agnostic environment variable access.
 *
 * This provides a clean Kotlin API around platform-specific environment-variable
 * lookup, centralizing all environment variable access through a single point.
 */
expect object Environment {
    /**
     * Get an environment variable by name.
     *
     * @param name The name of the environment variable
     * @return The value of the environment variable, or null if not set
     */
    fun get(name: String): String?

    /**
     * Check if an environment variable is set.
     *
     * @param name The name of the environment variable
     * @return true if the variable is set (even if empty), false otherwise
     */
    fun isSet(name: String): Boolean

    /** Common environment variables. */
    val HOME: String?
    val USER: String?
    val PATH: String?
    val SHELL: String?
    val TMPDIR: String
    val PWD: String?
}

/**
 * Get an environment variable by name with a default value.
 *
 * @param name The name of the environment variable
 * @param default The default value to return if the variable is not set
 * @return The value of the environment variable, or the default if not set
 */
fun Environment.getOrDefault(name: String, default: String): String = get(name) ?: default

/**
 * Get an environment variable, throwing if not set.
 *
 * @param name The name of the environment variable
 * @return The value of the environment variable
 * @throws IllegalStateException if the variable is not set
 */
fun Environment.require(name: String): String = get(name)
    ?: error("Required environment variable '$name' is not set")
