// port-lint: source codex-rs/protocol/src/parse_command.rs
package ai.solace.coder.protocol

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Parsed command types.
 *
 * Ported from Rust codex-rs/protocol/src/parse_command.rs
 */

@Serializable
sealed class ParsedCommand {
    @Serializable
    @SerialName("read")
    data class Read(
        val cmd: String,
        val name: String,
        /**
         * (Best effort) Path to the file being read by the command. When
         * possible, this is an absolute path, though when relative, it should
         * be resolved against the `cwd` that will be used to run the command
         * to derive the absolute path.
         */
        val path: String
    ) : ParsedCommand()

    @Serializable
    @SerialName("list_files")
    data class ListFiles(
        val cmd: String,
        val path: String? = null
    ) : ParsedCommand()

    @Serializable
    @SerialName("search")
    data class Search(
        val cmd: String,
        val query: String? = null,
        val path: String? = null
    ) : ParsedCommand()

    @Serializable
    @SerialName("unknown")
    data class Unknown(
        val cmd: String
    ) : ParsedCommand()
}

/**
 * Best-effort classification of a shell command. Mirrors Rust's `parse_command`
 * in codex-rs/core/src/parse_command.rs.
 *
 * NOTE: The full Rust implementation is ~900 lines of pattern-matching for
 * recognizing reads, list-files, searches, etc. This Kotlin version returns
 * a single `Unknown` entry so that event emitters have a well-typed payload
 * until the full parser is ported.
 */
fun parseCommand(command: List<String>): List<ParsedCommand> {
    if (command.isEmpty()) return emptyList()
    return listOf(ParsedCommand.Unknown(cmd = command.joinToString(" ")))
}
