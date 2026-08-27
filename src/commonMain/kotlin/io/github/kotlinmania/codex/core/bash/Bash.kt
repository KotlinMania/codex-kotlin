// port-lint: source shell-command/src/bash.rs
package io.github.kotlinmania.codex.core.bash

/**
 * Shell command parsing and extraction utilities.
 * Ported from Rust codex-rs/shell-command/src/bash.rs
 */

fun extractBashCommand(command: List<String>): Pair<String, String>? {
    if (command.size != 3) return null
    val shell = command[0]
    val flag = command[1]
    val script = command[2]

    if (flag != "-lc" && flag != "-c") return null

    val shellNormalized = shell.replace('\\', '/').lowercase()
    val shellName = shellNormalized.substringAfterLast('/')
    if (shellName != "bash" && shellName != "zsh" && shellName != "sh" &&
        !shellName.startsWith("bash.") && !shellName.startsWith("zsh.") && !shellName.startsWith("sh.")) {
        return null
    }

    return shell to script
}

/**
 * Returns the sequence of plain commands within a `bash -lc "..."` or
 * `zsh -lc "..."` invocation when the script only contains word-only commands
 * joined by safe operators (;, &&, ||, |).
 */
fun parseShellLcPlainCommands(command: List<String>): List<List<String>>? {
    val (_, script) = extractBashCommand(command) ?: return null
    return parseWordOnlyScript(script)
}

/**
 * Returns the parsed argv for a single shell command in a here-doc style
 * script (`<<`), as long as the script contains exactly one command.
 */
fun parseShellLcSingleCommandPrefix(command: List<String>): List<String>? {
    val (_, script) = extractBashCommand(command) ?: return null
    if (!script.contains("<<")) return null
    if (script.contains(">") || script.contains("<") && !script.contains("<<")) return null

    val beforeHeredoc = script.substringBefore("<<").trim()
    if (beforeHeredoc.isEmpty()) return null
    return tokenizeCommandWords(beforeHeredoc)
}

private fun parseWordOnlyScript(script: String): List<List<String>>? {
    val trimmed = script.trim()
    if (trimmed.isEmpty()) return null

    // Check for disallowed bash constructs: $(, `, <, >, (, ), {, }, newline, etc.
    var inSingleQuote = false
    var inDoubleQuote = false
    var i = 0
    val tokens = mutableListOf<String>()
    val currentWord = StringBuilder()

    val commands = mutableListOf<MutableList<String>>()
    var currentCommand = mutableListOf<String>()

    fun finishWord() {
        if (currentWord.isNotEmpty()) {
            currentCommand.add(currentWord.toString())
            currentWord.clear()
        }
    }

    fun finishCommand() {
        finishWord()
        if (currentCommand.isNotEmpty()) {
            commands.add(currentCommand)
            currentCommand = mutableListOf()
        }
    }

    while (i < script.length) {
        val c = script[i]

        if (inSingleQuote) {
            if (c == '\'') {
                inSingleQuote = false
            } else {
                currentWord.append(c)
            }
            i++
            continue
        }

        if (inDoubleQuote) {
            if (c == '"') {
                inDoubleQuote = false
            } else if (c == '\\' && i + 1 < script.length) {
                val next = script[i + 1]
                if (next == '"' || next == '\\' || next == '$' || next == '`') {
                    currentWord.append(next)
                    i += 2
                    continue
                } else {
                    currentWord.append('\\')
                    currentWord.append(next)
                    i += 2
                    continue
                }
            } else if (c == '$' || c == '`') {
                // Reject variable / command expansions inside double quotes
                return null
            } else {
                currentWord.append(c)
            }
            i++
            continue
        }

        // Disallowed control characters / operators
        when (c) {
            '\'', '"' -> {
                if (c == '\'') inSingleQuote = true else inDoubleQuote = true
                i++
            }
            '`', '$', '(', ')', '{', '}', '<', '>', '\n', '\r', '\t' -> {
                if (c == '\t') {
                    finishWord()
                    i++
                } else {
                    return null // Disallowed construct
                }
            }
            ' ' -> {
                finishWord()
                i++
            }
            ';' -> {
                finishCommand()
                i++
            }
            '&' -> {
                if (i + 1 < script.length && script[i + 1] == '&') {
                    finishCommand()
                    i += 2
                } else {
                    return null // background job & not allowed
                }
            }
            '|' -> {
                if (i + 1 < script.length && script[i + 1] == '|') {
                    finishCommand()
                    i += 2
                } else {
                    finishCommand()
                    i++
                }
            }
            '\\' -> {
                if (i + 1 < script.length) {
                    val next = script[i + 1]
                    currentWord.append(next)
                    i += 2
                } else {
                    return null
                }
            }
            else -> {
                currentWord.append(c)
                i++
            }
        }
    }

    if (inSingleQuote || inDoubleQuote) return null // Unclosed quote

    finishCommand()

    return if (commands.isEmpty()) null else commands
}

private fun tokenizeCommandWords(cmd: String): List<String>? {
    val words = mutableListOf<String>()
    val current = StringBuilder()
    var inSingle = false
    var inDouble = false
    var i = 0

    fun finish() {
        if (current.isNotEmpty()) {
            words.add(current.toString())
            current.clear()
        }
    }

    while (i < cmd.length) {
        val c = cmd[i]
        if (inSingle) {
            if (c == '\'') inSingle = false else current.append(c)
            i++
            continue
        }
        if (inDouble) {
            if (c == '"') {
                inDouble = false
            } else if (c == '\\' && i + 1 < cmd.length) {
                current.append(cmd[i + 1])
                i += 2
                continue
            } else if (c == '$' || c == '`') {
                return null
            } else {
                current.append(c)
            }
            i++
            continue
        }

        when (c) {
            '\'', '"' -> {
                if (c == '\'') inSingle = true else inDouble = true
                i++
            }
            '`', '$', '(', ')', '<', '>' -> return null
            ' ', '\t' -> {
                finish()
                i++
            }
            else -> {
                current.append(c)
                i++
            }
        }
    }
    if (inSingle || inDouble) return null
    finish()
    return if (words.isEmpty()) null else words
}
