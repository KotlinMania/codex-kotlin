// port-lint: source core/src/bash.rs
package ai.solace.coder.core.bash

/**
 * Returns the sequence of plain commands within a `bash -lc "..."` or
 * `zsh -lc "..."` invocation when the script only contains word-only commands
 * joined by safe operators.
 *
 * This is an expect declaration; the actual implementation uses tree-sitter
 * which is platform-specific.
 *
 * @param command The shell command list (e.g., ["bash", "-lc", "echo hello && ls"])
 * @return List of command word vectors, or null if parsing failed or unsupported
 */
expect fun parseShellLcPlainCommands(command: List<String>): List<List<String>>?

/**
 * Extracts the bash command components if this is a valid bash/zsh -lc or -c invocation.
 *
 * @param command The command list
 * @return Pair of (shell, script) or null if not a valid bash command
 */
expect fun extractBashCommand(command: List<String>): Pair<String, String>?
