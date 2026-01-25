// port-lint: source codex-rs/core/src/sandboxing/mod.rs
package ai.solace.coder.core.sandboxing

/**
 * Sandbox permissions levels.
 *
 * Mirrors Rust's SandboxPermissions from sandboxing/mod.rs
 */
enum class SandboxPermissions {
    UseDefault,
    RequireEscalated;

    fun requiresEscalatedPermissions(): Boolean {
        return this == RequireEscalated
    }

    companion object {
        fun from(withEscalatedPermissions: Boolean): SandboxPermissions {
            return if (withEscalatedPermissions) {
                RequireEscalated
            } else {
                UseDefault
            }
        }
    }
}
