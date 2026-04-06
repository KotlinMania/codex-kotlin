// port-lint: source core/src/bash.rs
package ai.solace.coder.core.bash

import ai.solace.coder.exec.shell.ShellDetector
import ai.solace.coder.exec.shell.ShellType

/**
 * Extract `(<shell>, <script>)` from a `bash -lc "<script>"`/`zsh -lc "<script>"`/`sh -lc "<script>"`
 * invocation. Returns null if the command shape or shell type is not supported.
 */
fun extractBashCommand(command: List<String>): Pair<String, String>? {
    if (command.size != 3) {
        return null
    }

    val shell = command[0]
    val flag = command[1]
    val script = command[2]

    if (flag != "-lc" && flag != "-c") {
        return null
    }

    val shellDetector = ShellDetector()
    val shellType = shellDetector.detectShellType(shell)
    if (shellType != ShellType.Zsh && shellType != ShellType.Bash && shellType != ShellType.Sh) {
        return null
    }

    return Pair(shell, script)
}

/**
 * Returns the sequence of plain commands within a `bash -lc "..."` or `zsh -lc "..."` invocation when
 * the script only contains word-only commands joined by safe operators.
 *
 * This is a faithful transliteration of the Rust behavior in `core/src/bash.rs`, but implemented
 * without tree-sitter so it can run on Kotlin/Native without JVM-only dependencies.
 */
fun parseShellLcPlainCommands(command: List<String>): List<List<String>>? {
    val (_, script) = extractBashCommand(command) ?: return null
    return parseWordOnlyCommandsSequence(script)
}

private enum class TokenKind {
    Word,
    Op
}

private data class Token(
    val kind: TokenKind,
    val value: String
)

private fun parseWordOnlyCommandsSequence(src: String): List<List<String>>? {
    val tokens = tokenize(src) ?: return null
    if (tokens.isEmpty()) {
        return null
    }

    val commands = mutableListOf<MutableList<String>>()
    var current = mutableListOf<String>()

    fun finishCommand(): Boolean {
        if (current.isEmpty()) {
            return false
        }
        if (looksLikeVariableAssignmentPrefix(current[0])) {
            return false
        }
        commands.add(current)
        current = mutableListOf()
        return true
    }

    for (token in tokens) {
        when (token.kind) {
            TokenKind.Word -> current.add(token.value)
            TokenKind.Op -> {
                if (!finishCommand()) {
                    return null
                }
            }
        }
    }

    if (!finishCommand()) {
        return null
    }
    return commands
}

private fun tokenize(src: String): List<Token>? {
    val tokens = mutableListOf<Token>()
    var i = 0

    fun peek(offset: Int = 0): Char? {
        val idx = i + offset
        return if (idx in 0 until src.length) src[idx] else null
    }

    fun isWhitespace(c: Char): Boolean = c == ' ' || c == '\t' || c == '\n' || c == '\r'

    fun skipWhitespace() {
        while (true) {
            val c = peek() ?: return
            if (!isWhitespace(c)) return
            i += 1
        }
    }

    fun readOperator(): String? {
        return when (val c = peek()) {
            '&' -> {
                if (peek(1) == '&') {
                    i += 2
                    "&&"
                } else {
                    null
                }
            }
            '|' -> {
                if (peek(1) == '|') {
                    i += 2
                    "||"
                } else {
                    i += 1
                    "|"
                }
            }
            ';' -> {
                i += 1
                ";"
            }
            else -> null
        }
    }

    fun rejectOnDisallowedChar(c: Char): Boolean {
        return c == '(' || c == ')' || c == '<' || c == '>' || c == '{' || c == '}' || c == '[' || c == ']'
    }

    fun readSingleQuoted(): String? {
        // opening quote already at i
        i += 1
        val start = i
        while (true) {
            val c = peek() ?: return null
            if (c == '\'') {
                val value = src.substring(start, i)
                i += 1
                return value
            }
            i += 1
        }
    }

    fun readDoubleQuoted(): String? {
        i += 1
        val start = i
        while (true) {
            val c = peek() ?: return null
            if (c == '"') {
                val value = src.substring(start, i)
                // Reject expansions/escapes inside double quotes to match the Rust tree-sitter shape check.
                if (value.indexOf('$') >= 0 || value.indexOf('`') >= 0 || value.indexOf('\\') >= 0) {
                    return null
                }
                i += 1
                return value
            }
            if (c == '\n' || c == '\r') {
                return null
            }
            i += 1
        }
    }

    fun readBareWord(): String? {
        val start = i
        while (true) {
            val c = peek() ?: break
            if (isWhitespace(c)) break
            // operators terminate a word
            if (c == '&' || c == '|' || c == ';') break
            // reject a bunch of constructs we never allow
            if (rejectOnDisallowedChar(c)) return null
            if (c == '$' || c == '`') return null
            if (c == '\\') return null
            i += 1
        }
        if (i == start) {
            return null
        }
        return src.substring(start, i)
    }

    while (true) {
        skipWhitespace()
        val c = peek() ?: break

        if (rejectOnDisallowedChar(c) || c == '<' || c == '>') {
            return null
        }

        val op = readOperator()
        if (op != null) {
            tokens.add(Token(TokenKind.Op, op))
            continue
        }

        val word = when (c) {
            '\'' -> readSingleQuoted()
            '"' -> readDoubleQuoted()
            else -> readBareWord()
        } ?: return null
        tokens.add(Token(TokenKind.Word, word))
    }

    // Reject trailing operator (would have produced Op token at end).
    if (tokens.lastOrNull()?.kind == TokenKind.Op) {
        return null
    }
    return tokens
}

private fun looksLikeVariableAssignmentPrefix(token: String): Boolean {
    // Reject `FOO=bar <cmd>` as in the Rust tree-sitter-based implementation.
    if (token.startsWith('-')) {
        return false
    }
    val eqIndex = token.indexOf('=')
    if (eqIndex <= 0) {
        return false
    }
    val name = token.substring(0, eqIndex)
    if (!isShellVarName(name)) {
        return false
    }
    return true
}

private fun isShellVarName(name: String): Boolean {
    if (name.isEmpty()) return false
    val first = name[0]
    if (!(first == '_' || first in 'A'..'Z' || first in 'a'..'z')) {
        return false
    }
    for (c in name) {
        val ok = c == '_' || c in 'A'..'Z' || c in 'a'..'z' || c in '0'..'9'
        if (!ok) return false
    }
    return true
}
