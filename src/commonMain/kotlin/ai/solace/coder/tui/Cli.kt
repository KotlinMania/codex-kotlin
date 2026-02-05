// port-lint: source tui/src/cli.rs
package ai.solace.coder.tui

import okio.Path

/**
 * CLI options for the TUI entrypoint.
 *
 * Ported from Rust codex-rs/tui/src/cli.rs
 */
data class Cli(
    /** Optional user prompt to start the session. */
    val prompt: String? = null,
    /** Optional image(s) to attach to the initial prompt. */
    val images: List<Path> = emptyList(),
    /** Internal controls set by the top-level `codex resume` subcommand. */
    val resumePicker: Boolean = false,
    /** Internal controls set by the top-level `codex resume` subcommand. */
    val resumeLast: Boolean = false,
    /** Internal: resume a specific recorded session by id (UUID). */
    val resumeSessionId: String? = null,
    /** Internal: show all sessions (disables cwd filtering and shows CWD column). */
    val resumeShowAll: Boolean = false,
    /** Model the agent should use. */
    val model: String? = null,
    /** Convenience flag to select the local open source model provider. */
    val oss: Boolean = false,
    /** Specify which local provider to use (lmstudio or ollama). */
    val ossProvider: String? = null,
    /** Configuration profile from config.toml to specify default options. */
    val configProfile: String? = null,
    /** Select the sandbox policy to use when executing model-generated shell commands. */
    val sandboxMode: Any? = null,
    /** Configure when the model requires human approval before executing a command. */
    val approvalPolicy: Any? = null,
    /** Convenience alias for low-friction sandboxed automatic execution. */
    val fullAuto: Boolean = false,
    /** Skip all confirmation prompts and execute commands without sandboxing. */
    val dangerouslyBypassApprovalsAndSandbox: Boolean = false,
    /** Tell the agent to use the specified directory as its working root. */
    val cwd: Path? = null,
    /** Enable web search (off by default). */
    val webSearch: Boolean = false,
    /** Additional directories that should be writable alongside the primary workspace. */
    val addDir: List<Path> = emptyList(),
    /** Raw configuration overrides from the CLI (`-c key=value`). */
    val configOverrides: Any? = null
)
