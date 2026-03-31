// port-lint: source common/src/approval_mode_cli_arg.rs
package ai.solace.coder.common

import ai.solace.coder.protocol.AskForApproval

/**
 * Standard type to use with the `--approval-mode` CLI option.
 */
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

    /**
     * The model decides when to ask the user for approval.
     */
    OnRequest,

    /**
     * Never ask for user approval.
     * Execution failures are immediately returned to the model.
     */
    Never;

    fun toAskForApproval(): AskForApproval = when (this) {
        Untrusted -> AskForApproval.UnlessTrusted
        OnFailure -> AskForApproval.OnFailure
        OnRequest -> AskForApproval.OnRequest
        Never -> AskForApproval.Never
    }
}
