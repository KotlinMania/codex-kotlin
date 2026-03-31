// port-lint: source common/src/sandbox_mode_cli_arg.rs
package ai.solace.coder.common

import ai.solace.coder.protocol.SandboxMode

/**
 * Standard type to use with the `--sandbox` (`-s`) CLI option.
 *
 * This mirrors the variants of [SandboxMode], but
 * without any of the associated data so it can be expressed as a simple flag
 * on the command-line.
 */
enum class SandboxModeCliArg {
    ReadOnly,
    WorkspaceWrite,
    DangerFullAccess;

    fun toSandboxMode(): SandboxMode = when (this) {
        ReadOnly -> SandboxMode.ReadOnly
        WorkspaceWrite -> SandboxMode.WorkspaceWrite
        DangerFullAccess -> SandboxMode.DangerFullAccess
    }
}
