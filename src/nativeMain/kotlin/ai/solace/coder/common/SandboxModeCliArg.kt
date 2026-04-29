// port-lint: source sandbox_mode_cli_arg.rs
//! Standard type to import with the `--sandbox` (`-s`) CLI option.
//!
//! This mirrors the variants of [`SandboxPolicy`], but without any of the
//! associated data so it can be expressed as a simple flag on the command-line.
//! Users that need to tweak the advanced options for `workspace-write` can
//! continue to do so via `-c` overrides or their `config.toml`.
package ai.solace.coder.common

import ai.solace.coder.protocol.SandboxMode

enum class SandboxModeCliArg {
    ReadOnly,
    WorkspaceWrite,
    DangerFullAccess,
}

fun SandboxModeCliArg.toSandboxMode(): SandboxMode = when (this) {
    SandboxModeCliArg.ReadOnly -> SandboxMode.ReadOnly
    SandboxModeCliArg.WorkspaceWrite -> SandboxMode.WorkspaceWrite
    SandboxModeCliArg.DangerFullAccess -> SandboxMode.DangerFullAccess
}
