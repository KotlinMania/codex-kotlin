// port-lint: source tui/src/slash_command.rs
package io.github.kotlinmania.codex.tui

/** Commands that can be invoked by starting a message with a leading slash. */
enum class SlashCommand(val command: String) {
    // DO NOT ALPHA-SORT! Enum order is presentation order in the popup, so
    // more frequently used commands should be listed first.
    Model("model"),
    Approvals("approvals"),
    Review("review"),
    New("new"),
    Init("init"),
    Compact("compact"),
    Undo("undo"),
    Diff("diff"),
    Mention("mention"),
    Status("status"),
    Mcp("mcp"),
    Logout("logout"),
    Quit("quit"),
    Exit("exit"),
    Feedback("feedback"),
    Rollout("rollout"),
    TestApproval("test-approval");

    /** User-visible description shown in the popup. */
    fun description(): String = when (this) {
        Feedback -> "send logs to maintainers"
        New -> "start a new chat during a conversation"
        Init -> "create an AGENTS.md file with instructions for Codex"
        Compact -> "summarize conversation to prevent hitting the context limit"
        Review -> "review my current changes and find issues"
        Undo -> "ask Codex to undo a turn"
        Quit, Exit -> "exit Codex"
        Diff -> "show git diff (including untracked files)"
        Mention -> "mention a file"
        Status -> "show current session configuration and token usage"
        Model -> "choose what model and reasoning effort to use"
        Approvals -> "choose what Codex can do without approval"
        Mcp -> "list configured MCP tools"
        Logout -> "log out of Codex"
        Rollout -> "print the rollout file path"
        TestApproval -> "test approval request"
    }

    /** Whether this command can be run while a task is in progress. */
    fun availableDuringTask(): Boolean = when (this) {
        New, Init, Compact, Undo, Model, Approvals, Review, Logout -> false
        Diff, Mention, Status, Mcp, Feedback, Quit, Exit -> true
        Rollout -> true
        TestApproval -> true
    }

    private fun isVisible(): Boolean = when (this) {
        Rollout, TestApproval -> DEBUG_ASSERTIONS
        else -> true
    }

    companion object {
        /**
         * Mirrors the upstream `cfg(debugAssertions)`. Kotlin/Native lacks a
         * direct equivalent, so we expose a single switch that can be flipped
         * for development builds. Defaults to `false` to match release
         * semantics.
         */
        var DEBUG_ASSERTIONS: Boolean = false

        /** Return all built-in commands paired with their command string. */
        fun builtInSlashCommands(): List<Pair<String, SlashCommand>> =
            entries.filter { it.isVisible() }.map { it.command to it }
    }
}
