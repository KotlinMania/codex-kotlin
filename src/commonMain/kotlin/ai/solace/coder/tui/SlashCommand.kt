// port-lint: source tui/src/slash_command.rs
package ai.solace.coder.tui

/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * Copyright (c) 2025 Sydney Renee, The Solace Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Commands that can be invoked by starting a message with a leading slash.
 *
 * Enum order is presentation order in the popup, so more frequently used
 * commands should be listed first. DO NOT ALPHA-SORT!
 */
enum class SlashCommand(
    /** The kebab-case command string (without leading '/'). */
    val command: String,
) {
    Model("model"),
    Approvals("approvals"),
    Experimental("experimental"),
    Skills("skills"),
    Review("review"),
    New("new"),
    Resume("resume"),
    Init("init"),
    Compact("compact"),
    // Undo("undo"),
    Diff("diff"),
    Mention("mention"),
    Status("status"),
    Mcp("mcp"),
    Logout("logout"),
    Quit("quit"),
    Exit("exit"),
    Feedback("feedback"),
    Rollout("rollout"),
    Ps("ps"),
    TestApproval("test-approval");

    /** User-visible description shown in the popup. */
    val description: String
        get() = when (this) {
            Feedback -> "send logs to maintainers"
            New -> "start a new chat during a conversation"
            Init -> "create an AGENTS.md file with instructions for Codex"
            Compact -> "summarize conversation to prevent hitting the context limit"
            Review -> "review my current changes and find issues"
            Resume -> "resume a saved chat"
            // Undo -> "ask Codex to undo a turn"
            Quit, Exit -> "exit Codex"
            Diff -> "show git diff (including untracked files)"
            Mention -> "mention a file"
            Skills -> "use skills to improve how Codex performs specific tasks"
            Status -> "show current session configuration and token usage"
            Ps -> "list background terminals"
            Model -> "choose what model and reasoning effort to use"
            Approvals -> "choose what Codex can do without approval"
            Experimental -> "toggle beta features"
            Mcp -> "list configured MCP tools"
            Logout -> "log out of Codex"
            Rollout -> "print the rollout file path"
            TestApproval -> "test approval request"
        }

    /** Whether this command can be run while a task is in progress. */
    val availableDuringTask: Boolean
        get() = when (this) {
            New,
            Resume,
            Init,
            Compact,
            // Undo,
            Model,
            Approvals,
            Experimental,
            Review,
            Logout -> false
            Diff,
            Mention,
            Skills,
            Status,
            Ps,
            Mcp,
            Feedback,
            Quit,
            Exit,
            Rollout,
            TestApproval -> true
        }

    private val isVisible: Boolean
        get() = when (this) {
            Rollout, TestApproval -> false // cfg!(debug_assertions) equivalent
            else -> true
        }

    companion object {
        /** Parse a command string (without leading '/') into a [SlashCommand]. */
        fun fromCommand(value: String): SlashCommand? =
            entries.find { it.command == value }

        /** Return all built-in commands paired with their command string. */
        fun builtInSlashCommands(): List<Pair<String, SlashCommand>> =
            entries
                .filter { it.isVisible }
                .map { Pair(it.command, it) }
    }
}
