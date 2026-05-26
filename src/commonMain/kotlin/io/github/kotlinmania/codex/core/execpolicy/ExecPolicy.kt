// port-lint: source core/src/exec_policy.rs
package io.github.kotlinmania.codex.core.execpolicy

/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * Copyright (c) 2025 Sydney Renee, The Solace Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 */

import io.github.kotlinmania.codex.core.bash.parseShellLcPlainCommands
import io.github.kotlinmania.codex.core.commandsafety.requiresInitialApproval
import io.github.kotlinmania.codex.core.tools.ApprovalRequirement
import io.github.kotlinmania.codex.exec.sandbox.SandboxPermissions
import io.github.kotlinmania.codex.execpolicy.Decision
import io.github.kotlinmania.codex.execpolicy.Evaluation
import io.github.kotlinmania.codex.execpolicy.Policy
import io.github.kotlinmania.codex.protocol.AskForApproval
import io.github.kotlinmania.codex.protocol.SandboxPolicy

private const val FORBIDDEN_REASON: String = "execpolicy forbids this command"
private const val PROMPT_REASON: String = "execpolicy requires approval for this command"

/**
 * Evaluate [command] against [policy] under [approvalPolicy] and translate the
 * resulting [Evaluation] into an [ApprovalRequirement]. Returns null if the
 * policy does not match the command (caller falls back to the default
 * dangerous-command heuristic).
 *
 * Mirrors `evaluateWithPolicy` in `core/src/execPolicy.rs`.
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
 * Mirrors `createApprovalRequirementForCommand` in `core/src/execPolicy.rs`.
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
