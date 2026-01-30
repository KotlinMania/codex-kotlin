// port-lint: source tui/src/cli.rs
package ai.solace.coder.tui

import ai.solace.coder.core.config.CliConfigOverrides
import ai.solace.coder.protocol.ApprovalMode
import ai.solace.coder.protocol.SandboxMode
import java.nio.file.Path

/**
 * Command-line interface arguments for the Codex TUI.
 * 
 * This class represents the parsed command-line arguments passed to the
 * Codex terminal application. It includes options for prompts, model selection,
 * sandboxing, approval policies, and session management.
 * 
 * Ported from Rust codex-rs/tui/src/cli.rs
 */
data class Cli(
    /**
     * Optional user prompt to start the session.
     */
    val prompt: String? = null,
    
    /**
     * Optional image(s) to attach to the initial prompt.
     */
    val images: List<Path> = emptyList(),
    
    /**
     * Internal: show resume picker UI.
     * Set by the top-level `codex resume` subcommand.
     */
    val resumePicker: Boolean = false,
    
    /**
     * Internal: resume the most recent session.
     * Set by the top-level `codex resume` subcommand.
     */
    val resumeLast: Boolean = false,
    
    /**
     * Internal: resume a specific recorded session by id (UUID).
     * Set by the top-level `codex resume <SESSION_ID>` wrapper.
     */
    val resumeSessionId: String? = null,
    
    /**
     * Internal: show all sessions (disables cwd filtering and shows CWD column).
     */
    val resumeShowAll: Boolean = false,
    
    /**
     * Model the agent should use.
     * Command line flag: --model, -m
     */
    val model: String? = null,
    
    /**
     * Convenience flag to select the local open source model provider.
     * Equivalent to -c model_provider=oss; verifies a local LM Studio
     * or Ollama server is running.
     * Command line flag: --oss
     */
    val oss: Boolean = false,
    
    /**
     * Specify which local provider to use (lmstudio or ollama).
     * If not specified with --oss, will use config default or show selection.
     * Command line flag: --local-provider
     */
    val ossProvider: String? = null,
    
    /**
     * Configuration profile from config.toml to specify default options.
     * Command line flag: --profile, -p
     */
    val configProfile: String? = null,
    
    /**
     * Select the sandbox policy to use when executing model-generated
     * shell commands.
     * Command line flag: --sandbox, -s
     */
    val sandboxMode: SandboxMode? = null,
    
    /**
     * Configure when the model requires human approval before executing
     * a command.
     * Command line flag: --ask-for-approval, -a
     */
    val approvalPolicy: ApprovalMode? = null,
    
    /**
     * Convenience alias for low-friction sandboxed automatic execution
     * (-a on-request, --sandbox workspace-write).
     * Command line flag: --full-auto
     */
    val fullAuto: Boolean = false,
    
    /**
     * Skip all confirmation prompts and execute commands without sandboxing.
     * EXTREMELY DANGEROUS. Intended solely for running in environments that
     * are externally sandboxed.
     * Command line flag: --dangerously-bypass-approvals-and-sandbox, --yolo
     * Conflicts with: approval_policy, full_auto
     */
    val dangerouslyBypassApprovalsAndSandbox: Boolean = false,
    
    /**
     * Tell the agent to use the specified directory as its working root.
     * Command line flag: --cd, -C
     */
    val cwd: Path? = null,
    
    /**
     * Enable web search (off by default). When enabled, the native Responses
     * `web_search` tool is available to the model (no per-call approval).
     * Command line flag: --search
     */
    val webSearch: Boolean = false,
    
    /**
     * Additional directories that should be writable alongside the
     * primary workspace.
     * Command line flag: --add-dir
     */
    val addDir: List<Path> = emptyList(),
    
    /**
     * Configuration overrides from command line.
     * Not directly exposed as a flag; populated by -c key=value options.
     */
    val configOverrides: CliConfigOverrides = CliConfigOverrides()
)
