// port-lint: source core/src/bash.rs
package ai.solace.coder.core.bash

import io.github.treesitter.ktreesitter.Language
import io.github.treesitter.ktreesitter.Node
import io.github.treesitter.ktreesitter.Parser
import io.github.treesitter.ktreesitter.Tree
import io.github.treesitter.ktreesitter.bash.TreeSitterBash

/**
 * Parse the provided bash source using tree-sitter-bash, returning a Tree on
 * success or null if parsing failed.
 *
 * Ported from Rust codex-rs/core/src/bash.rs try_parse_shell
 */
private fun tryParseShell(shellLcArg: String): Tree? {
    val lang = Language(TreeSitterBash.language())
    val parser = Parser(lang)
    return try {
        parser.parse(shellLcArg)
    } catch (_: Exception) {
        null
    }
}

/** Allowed named node kinds for a "word only commands sequence". */
private val ALLOWED_KINDS = setOf(
    // top level containers
    "program",
    "list",
    "pipeline",
    // commands & words
    "command",
    "command_name",
    "word",
    "string",
    "string_content",
    "raw_string",
    "number",
    "concatenation",
)

/** Allowed punctuation / operator tokens; anything else causes reject. */
private val ALLOWED_PUNCT_TOKENS = setOf("&&", "||", ";", "|", "\"", "'")

/**
 * Parse a script which may contain multiple simple commands joined only by
 * the safe logical/pipe/sequencing operators: `&&`, `||`, `;`, `|`.
 *
 * Returns the list of command word vectors if every command is a plain word-only
 * command and the parse tree does not contain disallowed constructs
 * (parentheses, redirections, substitutions, control flow, etc.). Otherwise
 * returns null.
 *
 * Ported from Rust codex-rs/core/src/bash.rs try_parse_word_only_commands_sequence
 */
private fun tryParseWordOnlyCommandsSequence(tree: Tree, src: String): List<List<String>>? {
    val root = tree.rootNode
    if (root.hasError) {
        return null
    }

    val stack = ArrayDeque<Node>()
    stack.addLast(root)
    val commandNodes = mutableListOf<Node>()

    while (stack.isNotEmpty()) {
        val node = stack.removeLast()
        val kind = node.type

        if (node.isNamed) {
            if (kind !in ALLOWED_KINDS) {
                return null
            }
            if (kind == "command") {
                commandNodes.add(node)
            }
        } else {
            // Reject any punctuation / operator tokens that are not explicitly allowed.
            if (kind.any { it in "&;|" } && kind !in ALLOWED_PUNCT_TOKENS) {
                return null
            }
            if (kind !in ALLOWED_PUNCT_TOKENS && kind.trim().isNotEmpty()) {
                // Any other punctuation like parentheses, braces, redirects, backticks, etc are rejected.
                return null
            }
        }

        for (child in node.children) {
            stack.addLast(child)
        }
    }

    // Walk uses a stack (LIFO), so re-sort by position to restore source order.
    commandNodes.sortBy { it.startByte }

    val commands = mutableListOf<List<String>>()
    for (node in commandNodes) {
        val words = parsePlainCommandFromNode(node, src) ?: return null
        commands.add(words)
    }
    return commands
}

/**
 * Extract a text substring from the source using a node's byte range.
 */
private fun nodeText(node: Node, src: String): String? {
    val startByte = node.startByte.toInt()
    val endByte = node.endByte.toInt()
    if (startByte < 0 || endByte > src.length || startByte > endByte) return null
    return src.substring(startByte, endByte)
}

/**
 * Parse a single command node into a list of word strings.
 *
 * Ported from Rust codex-rs/core/src/bash.rs parse_plain_command_from_node
 */
private fun parsePlainCommandFromNode(cmd: Node, src: String): List<String>? {
    if (cmd.type != "command") {
        return null
    }
    val words = mutableListOf<String>()

    for (child in cmd.namedChildren) {
        when (child.type) {
            "command_name" -> {
                val wordNode = child.namedChild(0u) ?: return null
                if (wordNode.type != "word") {
                    return null
                }
                words.add(nodeText(wordNode, src) ?: return null)
            }
            "word", "number" -> {
                words.add(nodeText(child, src) ?: return null)
            }
            "string" -> {
                if (child.childCount == 3u
                    && child.child(0u)?.type == "\""
                    && child.child(1u)?.type == "string_content"
                    && child.child(2u)?.type == "\""
                ) {
                    words.add(nodeText(child.child(1u)!!, src) ?: return null)
                } else {
                    return null
                }
            }
            "raw_string" -> {
                val rawString = nodeText(child, src) ?: return null
                val stripped = rawString.removePrefix("'").removeSuffix("'")
                if (stripped.length != rawString.length - 2) {
                    return null
                }
                words.add(stripped)
            }
            "concatenation" -> {
                // Handle concatenated arguments like -g"*.py"
                val concatenated = StringBuilder()
                for (part in child.namedChildren) {
                    when (part.type) {
                        "word", "number" -> {
                            concatenated.append(nodeText(part, src) ?: return null)
                        }
                        "string" -> {
                            if (part.childCount == 3u
                                && part.child(0u)?.type == "\""
                                && part.child(1u)?.type == "string_content"
                                && part.child(2u)?.type == "\""
                            ) {
                                concatenated.append(nodeText(part.child(1u)!!, src) ?: return null)
                            } else {
                                return null
                            }
                        }
                        "raw_string" -> {
                            val rawString = nodeText(part, src) ?: return null
                            val stripped = rawString.removePrefix("'").removeSuffix("'")
                            if (stripped.length != rawString.length - 2) {
                                return null
                            }
                            concatenated.append(stripped)
                        }
                        else -> return null
                    }
                }
                if (concatenated.isEmpty()) {
                    return null
                }
                words.add(concatenated.toString())
            }
            else -> return null
        }
    }
    return words
}

/**
 * Detect the shell type from a path string.
 *
 * Ported from Rust codex-rs/core/src/shell.rs detect_shell_type
 */
private fun detectShellType(shellPath: String): String? {
    val fileName = shellPath.substringAfterLast('/').substringAfterLast('\\')
        .removeSuffix(".exe")
        .lowercase()
    return when (fileName) {
        "zsh" -> "zsh"
        "bash" -> "bash"
        "sh" -> "sh"
        else -> null
    }
}

/**
 * Extracts the bash command components if this is a valid bash/zsh/sh -lc or -c invocation.
 *
 * Ported from Rust codex-rs/core/src/bash.rs extract_bash_command
 */
actual fun extractBashCommand(command: List<String>): Pair<String, String>? {
    if (command.size != 3) return null
    val (shell, flag, script) = command
    if (flag != "-lc" && flag != "-c") return null
    val shellType = detectShellType(shell)
    if (shellType == null || shellType !in listOf("zsh", "bash", "sh")) return null
    return shell to script
}

/**
 * Returns the sequence of plain commands within a `bash -lc "..."` or
 * `zsh -lc "..."` invocation when the script only contains word-only commands
 * joined by safe operators.
 *
 * Ported from Rust codex-rs/core/src/bash.rs parse_shell_lc_plain_commands
 */
actual fun parseShellLcPlainCommands(command: List<String>): List<List<String>>? {
    val (_, script) = extractBashCommand(command) ?: return null
    val tree = tryParseShell(script) ?: return null
    return tryParseWordOnlyCommandsSequence(tree, script)
}
