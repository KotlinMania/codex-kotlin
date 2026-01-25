// port-lint: source core/src/features.rs
package ai.solace.coder.core.features

/**
 * Unique features toggled via configuration.
 *
 * Ported from Rust codex-rs/core/src/features.rs Feature enum
 */
enum class Feature {
    /** Create a ghost commit at each turn. */
    GhostCommit,
    /** Use the single unified PTY-backed exec tool. */
    UnifiedExec,
    /** Allow parallel tool calls. */
    ParallelToolCalls,
    /** Enable MCP authentication. */
    McpAuth,
    /** Enable agent review mode. */
    Review,
    /** Enable compact history mode. */
    CompactHistory,
    /** Enable extended thinking. */
    ExtendedThinking
}

/**
 * Holds the effective set of enabled features.
 *
 * Ported from Rust codex-rs/core/src/features.rs Features struct
 */
class Features private constructor(
    private val enabled: MutableSet<Feature>
) {
    /**
     * Check if a feature is enabled.
     */
    fun enabled(feature: Feature): Boolean = enabled.contains(feature)

    /**
     * Enable a feature.
     */
    fun enable(feature: Feature) {
        enabled.add(feature)
    }

    /**
     * Disable a feature.
     */
    fun disable(feature: Feature) {
        enabled.remove(feature)
    }

    /**
     * Create a copy of the features.
     */
    fun copy(): Features = Features(enabled.toMutableSet())

    companion object {
        /**
         * Create with default features enabled.
         * Ported from Rust Features::with_defaults()
         */
        fun withDefaults(): Features {
            val set = mutableSetOf<Feature>()
            // Default features from Rust
            set.add(Feature.GhostCommit)
            set.add(Feature.UnifiedExec)
            set.add(Feature.ParallelToolCalls)
            return Features(set)
        }

        /**
         * Create with no features enabled.
         */
        fun empty(): Features = Features(mutableSetOf())

        /**
         * Create from a map of feature name to enabled state.
         */
        fun fromMap(entries: Map<String, Boolean>): Features {
            val features = withDefaults()
            for ((name, enabled) in entries) {
                val feature = Feature.entries.find {
                    it.name.equals(name, ignoreCase = true) ||
                    it.name.replace("_", "").equals(name.replace("_", ""), ignoreCase = true)
                }
                if (feature != null) {
                    if (enabled) {
                        features.enable(feature)
                    } else {
                        features.disable(feature)
                    }
                }
            }
            return features
        }
    }
}
