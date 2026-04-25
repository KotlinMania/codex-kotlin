// port-lint: source core/src/exec_policy.rs
package ai.solace.coder.core.exec_policy

/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * Copyright (c) 2025 Sydney Renee, The Solace Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 */

import ai.solace.coder.core.bash.parseShellLcPlainCommands
import ai.solace.coder.core.command_safety.requiresInitialApproval
import ai.solace.coder.core.tools.ApprovalRequirement
import ai.solace.coder.exec.sandbox.SandboxPermissions
import ai.solace.coder.execpolicy.Decision
import ai.solace.coder.execpolicy.Evaluation
import ai.solace.coder.execpolicy.Policy
import ai.solace.coder.protocol.AskForApproval
import ai.solace.coder.protocol.SandboxPolicy

private const val FORBIDDEN_REASON: String = "execpolicy forbids this command"
private const val PROMPT_REASON: String = "execpolicy requires approval for this command"

/**
 * Evaluate [command] against [policy] under [approvalPolicy] and translate the
 * resulting [Evaluation] into an [ApprovalRequirement]. Returns null if the
 * policy does not match the command (caller falls back to the default
 * dangerous-command heuristic).
 *
 * Mirrors `evaluate_with_policy` in `core/src/exec_policy.rs`.
 */
private fun evaluateWithPolicy(
    policy: Policy,
    command: List<String>,
    approvalPolicy: AskForApproval,
): ApprovalRequirement? {
    val commands = parseShellLcPlainCommands(command) ?: listOf(command)
    return when (val evaluation = policy.checkMultiple(commands)) {
        is Evaluation.Match -> when (evaluation.decision) {
            Decision.Forbidden -> ApprovalRequirement.Forbidden(reason = FORBIDDEN_REASON)
            Decision.Prompt -> {
                val reason = PROMPT_REASON
                if (approvalPolicy == AskForApproval.Never) {
                    ApprovalRequirement.Forbidden(reason = reason)
                } else {
                    ApprovalRequirement.NeedsApproval(reason = reason)
                }
            }
            Decision.Allow -> ApprovalRequirement.Skip(bypassSandbox = true)
        }
        Evaluation.NoMatch -> null
    }
}

/**
 * Build the [ApprovalRequirement] for [command] given the active execpolicy
 * [policy], approval policy [approvalPolicy], sandbox configuration
 * [sandboxPolicy], and effective [sandboxPermissions].
 *
 * Mirrors `create_approval_requirement_for_command` in `core/src/exec_policy.rs`.
 */
fun createApprovalRequirementForCommand(
    policy: Policy,
    command: List<String>,
    approvalPolicy: AskForApproval,
    sandboxPolicy: SandboxPolicy,
    sandboxPermissions: SandboxPermissions,
): ApprovalRequirement {
    evaluateWithPolicy(policy, command, approvalPolicy)?.let { return it }

    return if (requiresInitialApproval(approvalPolicy, sandboxPolicy, command, sandboxPermissions)) {
        ApprovalRequirement.NeedsApproval(reason = null)
    } else {
        ApprovalRequirement.Skip(bypassSandbox = false)
    }
}
