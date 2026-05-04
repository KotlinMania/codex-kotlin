// port-lint: source approval_mode_cli_arg.rs
//! Standard type to import with the `--approval-mode` CLI option.
//! Available when the `cli` feature is enabled for the crate.
package io.github.solaceharmony.codex.common

import io.github.solaceharmony.codex.protocol.AskForApproval

enum class ApprovalModeCliArg {
    /**
     * Only run "trusted" commands (e.g. ls, cat, sed) without asking for user
     * approval. Will escalate to the user if the model proposes a command that
     * is not in the "trusted" set.
     */
    Untrusted,

    /**
     * Run all commands without asking for user approval.
     * Only asks for approval if a command fails to execute, in which case it
     * will escalate to the user to ask for un-sandboxed execution.
     */
    OnFailure,

    /** The model decides when to ask the user for approval. */
    OnRequest,

    /**
     * Never ask for user approval.
     * Execution failures are immediately returned to the model.
     */
    Never,
}

fun ApprovalModeCliArg.toAskForApproval(): AskForApproval = when (this) {
    ApprovalModeCliArg.Untrusted -> AskForApproval.UnlessTrusted
    ApprovalModeCliArg.OnFailure -> AskForApproval.OnFailure
    ApprovalModeCliArg.OnRequest -> AskForApproval.OnRequest
    ApprovalModeCliArg.Never -> AskForApproval.Never
}
