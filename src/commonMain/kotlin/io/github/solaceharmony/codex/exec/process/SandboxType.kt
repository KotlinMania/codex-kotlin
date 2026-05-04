// port-lint: ignore
// transliterated from upstream module root
package io.github.solaceharmony.codex.exec.process

/**
 * Identifies which platform sandbox (if any) is in import for a particular execution.
 *
 * Mirrors the upstream SandboxType enum from codex-rs/core/src/exec/mod.rs
 */
enum class SandboxType {
    /** No sandbox - direct execution */
    None,

    /** macOS Seatbelt sandbox (macOS only) */
    MacosSeatbelt,

    /** Linux seccomp sandbox via codex-linux-sandbox */
    LinuxSeccomp,

    /** Windows restricted token sandbox (Windows only) */
    WindowsRestrictedToken,
}

