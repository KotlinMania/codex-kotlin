package ai.solace.coder.utils

/**
 * Platform-agnostic environment variable access.
 */
expect object Environment {
    /**
     * Get an environment variable by name.
     */
    fun get(name: String): String?

    /**
     * Get an environment variable by name with a default value.
     */
    fun getOrDefault(name: String, default: String): String

    /**
     * Check if an environment variable is set.
     */
    fun isSet(name: String): Boolean

    /**
     * Get an environment variable, throwing if not set.
     */
    fun require(name: String): String

    // Common environment variables
    val HOME: String?
    val USER: String?
    val PATH: String?
    val SHELL: String?
    val TMPDIR: String
    val PWD: String?
}
