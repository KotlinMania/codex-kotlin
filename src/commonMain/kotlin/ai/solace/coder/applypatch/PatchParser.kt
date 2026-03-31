// port-lint: source apply-patch/src/parser.rs
package ai.solace.coder.applypatch

/**
 * This module is responsible for parsing & validating a patch into a list of "hunks".
 * (It does not attempt to actually check that the patch can be applied to the filesystem.)
 *
 * The official Lark grammar for the apply-patch format is:
 *
 * start: begin_patch hunk+ end_patch
 * begin_patch: "*** Begin Patch" LF
 * end_patch: "*** End Patch" LF?
 *
 * hunk: add_hunk | delete_hunk | update_hunk
 * add_hunk: "*** Add File: " filename LF add_line+
 * delete_hunk: "*** Delete File: " filename LF
 * update_hunk: "*** Update File: " filename LF change_move? change?
 * filename: /(.+)/
 * add_line: "+" /(.+)/ LF -> line
 *
 * change_move: "*** Move to: " filename LF
 * change: (change_context | change_line)+ eof_line?
 * change_context: ("@@" | "@@ " /(.+)/) LF
 * change_line: ("+" | "-" | " ") /(.+)/ LF
 * eof_line: "*** End of File" LF
 */

sealed class ParseError : Exception() {
    data class InvalidPatchError(override val message: String) : ParseError()
    data class InvalidHunkError(override val message: String, val lineNumber: Int) : ParseError()
}

sealed class Hunk {
    data class AddFile(
        val path: String,
        val contents: String,
    ) : Hunk()

    data class DeleteFile(
        val path: String,
    ) : Hunk()

    data class UpdateFile(
        val path: String,
        val movePath: String? = null,
        /** Chunks should be in order. */
        val chunks: List<UpdateFileChunk>,
    ) : Hunk()

    /** Resolve the path relative to the given cwd. */
    fun resolvePath(cwd: String): String {
        val p = when (this) {
            is AddFile -> path
            is DeleteFile -> path
            is UpdateFile -> path
        }
        // Simple path join — if p is absolute, use it; otherwise join with cwd
        return if (p.startsWith("/")) p else "$cwd/$p"
    }
}

data class UpdateFileChunk(
    /**
     * A single line of context used to narrow down the position of the chunk
     * (this is usually a class, method, or function definition.)
     */
    val changeContext: String? = null,
    /**
     * A contiguous block of lines that should be replaced with [newLines].
     * [oldLines] must occur strictly after [changeContext].
     */
    val oldLines: List<String>,
    val newLines: List<String>,
    /**
     * If set to true, [oldLines] must occur at the end of the source file.
     * (Tolerance around trailing newlines should be encouraged.)
     */
    val isEndOfFile: Boolean = false,
)

private const val BEGIN_PATCH_MARKER = "*** Begin Patch"
private const val END_PATCH_MARKER = "*** End Patch"
private const val ADD_FILE_MARKER = "*** Add File: "
private const val DELETE_FILE_MARKER = "*** Delete File: "
private const val UPDATE_FILE_MARKER = "*** Update File: "
private const val MOVE_TO_MARKER = "*** Move to: "
private const val EOF_MARKER = "*** End of File"
private const val CHANGE_CONTEXT_MARKER = "@@ "
private const val EMPTY_CHANGE_CONTEXT_MARKER = "@@"

/**
 * Currently, the only OpenAI model that knowingly requires lenient parsing is
 * gpt-4.1. We allow lenient parsing for all models.
 */
private const val PARSE_IN_STRICT_MODE = false

fun parsePatch(patch: String): Result<ApplyPatchArgs> {
    val mode = if (PARSE_IN_STRICT_MODE) ParseMode.Strict else ParseMode.Lenient
    return parsePatchText(patch, mode)
}

private enum class ParseMode {
    Strict,
    Lenient,
}

private fun parsePatchText(patch: String, mode: ParseMode): Result<ApplyPatchArgs> {
    val allLines = patch.trim().lines()
    val lines: List<String>

    val strictCheck = checkPatchBoundariesStrict(allLines)
    if (strictCheck.isSuccess) {
        lines = allLines
    } else {
        when (mode) {
            ParseMode.Strict -> return Result.failure(strictCheck.exceptionOrNull()!!)
            ParseMode.Lenient -> {
                val lenientResult = checkPatchBoundariesLenient(allLines, strictCheck.exceptionOrNull() as ParseError)
                if (lenientResult.isFailure) return Result.failure(lenientResult.exceptionOrNull()!!)
                lines = lenientResult.getOrThrow()
            }
        }
    }

    val hunks = mutableListOf<Hunk>()
    // The above checks ensure that lines.size >= 2.
    val lastLineIndex = (lines.size - 1).coerceAtLeast(0)
    var remainingLines = lines.subList(1, lastLineIndex)
    var lineNumber = 2
    while (remainingLines.isNotEmpty()) {
        val (hunk, hunkLines) = parseOneHunk(remainingLines, lineNumber)
            .getOrElse { return Result.failure(it) }
        hunks.add(hunk)
        lineNumber += hunkLines
        remainingLines = remainingLines.subList(hunkLines, remainingLines.size)
    }

    val patchStr = lines.joinToString("\n")
    return Result.success(ApplyPatchArgs(hunks = hunks, patch = patchStr))
}

private fun checkPatchBoundariesStrict(lines: List<String>): Result<Unit> {
    val firstLine = lines.firstOrNull()
    val lastLine = lines.lastOrNull()
    return checkStartAndEndLinesStrict(firstLine, lastLine)
}

private fun checkPatchBoundariesLenient(
    originalLines: List<String>,
    originalParseError: ParseError,
): Result<List<String>> {
    if (originalLines.size < 4) return Result.failure(originalParseError)

    val first = originalLines.first()
    val last = originalLines.last()

    if ((first == "<<EOF" || first == "<<'EOF'" || first == "<<\"EOF\"") && last.endsWith("EOF")) {
        val innerLines = originalLines.subList(1, originalLines.size - 1)
        val strictCheck = checkPatchBoundariesStrict(innerLines)
        return if (strictCheck.isSuccess) {
            Result.success(innerLines)
        } else {
            Result.failure(strictCheck.exceptionOrNull()!!)
        }
    }

    return Result.failure(originalParseError)
}

private fun checkStartAndEndLinesStrict(firstLine: String?, lastLine: String?): Result<Unit> {
    val first = firstLine?.trim()
    val last = lastLine?.trim()

    return when {
        first == BEGIN_PATCH_MARKER && last == END_PATCH_MARKER -> Result.success(Unit)
        first != BEGIN_PATCH_MARKER -> Result.failure(
            ParseError.InvalidPatchError("The first line of the patch must be '*** Begin Patch'")
        )
        else -> Result.failure(
            ParseError.InvalidPatchError("The last line of the patch must be '*** End Patch'")
        )
    }
}

/**
 * Attempts to parse a single hunk from the start of lines.
 * Returns the parsed hunk and the number of lines parsed (or a ParseError).
 */
private fun parseOneHunk(lines: List<String>, lineNumber: Int): Result<Pair<Hunk, Int>> {
    // Be tolerant of case mismatches and extra padding around marker strings.
    val firstLine = lines[0].trim()

    val addPath = firstLine.removePrefix(ADD_FILE_MARKER)
    if (addPath != firstLine) {
        // Add File
        val contents = StringBuilder()
        var parsedLines = 1
        for (addLine in lines.drop(1)) {
            if (addLine.startsWith("+")) {
                contents.append(addLine.substring(1))
                contents.append('\n')
                parsedLines++
            } else {
                break
            }
        }
        return Result.success(
            Hunk.AddFile(path = addPath, contents = contents.toString()) to parsedLines
        )
    }

    val deletePath = firstLine.removePrefix(DELETE_FILE_MARKER)
    if (deletePath != firstLine) {
        // Delete File
        return Result.success(Hunk.DeleteFile(path = deletePath) to 1)
    }

    val updatePath = firstLine.removePrefix(UPDATE_FILE_MARKER)
    if (updatePath != firstLine) {
        // Update File
        var remaining = lines.drop(1)
        var parsedLines = 1

        // Optional: move file line
        val movePath = remaining.firstOrNull()?.removePrefix(MOVE_TO_MARKER)?.let { stripped ->
            if (stripped != remaining.first()) stripped else null
        }

        if (movePath != null) {
            remaining = remaining.drop(1)
            parsedLines++
        }

        val chunks = mutableListOf<UpdateFileChunk>()
        while (remaining.isNotEmpty()) {
            // Skip over any completely blank lines that may separate chunks.
            if (remaining[0].trim().isEmpty()) {
                parsedLines++
                remaining = remaining.drop(1)
                continue
            }

            if (remaining[0].startsWith("***")) {
                break
            }

            val (chunk, chunkLines) = parseUpdateFileChunk(
                remaining, lineNumber + parsedLines, chunks.isEmpty()
            ).getOrElse { return Result.failure(it) }
            chunks.add(chunk)
            parsedLines += chunkLines
            remaining = remaining.drop(chunkLines)
        }

        if (chunks.isEmpty()) {
            return Result.failure(ParseError.InvalidHunkError(
                message = "Update file hunk for path '$updatePath' is empty",
                lineNumber = lineNumber,
            ))
        }

        return Result.success(
            Hunk.UpdateFile(path = updatePath, movePath = movePath, chunks = chunks) to parsedLines
        )
    }

    return Result.failure(ParseError.InvalidHunkError(
        message = "'$firstLine' is not a valid hunk header. Valid hunk headers: '*** Add File: {path}', '*** Delete File: {path}', '*** Update File: {path}'",
        lineNumber = lineNumber,
    ))
}

private fun parseUpdateFileChunk(
    lines: List<String>,
    lineNumber: Int,
    allowMissingContext: Boolean,
): Result<Pair<UpdateFileChunk, Int>> {
    if (lines.isEmpty()) {
        return Result.failure(ParseError.InvalidHunkError(
            message = "Update hunk does not contain any lines",
            lineNumber = lineNumber,
        ))
    }

    // If we see an explicit context marker @@ or @@ <context>, consume it
    val changeContext: String?
    val startIndex: Int
    when {
        lines[0] == EMPTY_CHANGE_CONTEXT_MARKER -> {
            changeContext = null
            startIndex = 1
        }
        lines[0].startsWith(CHANGE_CONTEXT_MARKER) -> {
            changeContext = lines[0].removePrefix(CHANGE_CONTEXT_MARKER)
            startIndex = 1
        }
        else -> {
            if (!allowMissingContext) {
                return Result.failure(ParseError.InvalidHunkError(
                    message = "Expected update hunk to start with a @@ context marker, got: '${lines[0]}'",
                    lineNumber = lineNumber,
                ))
            }
            changeContext = null
            startIndex = 0
        }
    }

    if (startIndex >= lines.size) {
        return Result.failure(ParseError.InvalidHunkError(
            message = "Update hunk does not contain any lines",
            lineNumber = lineNumber + 1,
        ))
    }

    val oldLines = mutableListOf<String>()
    val newLines = mutableListOf<String>()
    var isEndOfFile = false
    var parsedLines = 0

    for (line in lines.drop(startIndex)) {
        when {
            line == EOF_MARKER -> {
                if (parsedLines == 0) {
                    return Result.failure(ParseError.InvalidHunkError(
                        message = "Update hunk does not contain any lines",
                        lineNumber = lineNumber + 1,
                    ))
                }
                isEndOfFile = true
                parsedLines++
                break
            }
            line.isEmpty() -> {
                // Interpret as an empty line.
                oldLines.add("")
                newLines.add("")
                parsedLines++
            }
            else -> {
                when (line[0]) {
                    ' ' -> {
                        oldLines.add(line.substring(1))
                        newLines.add(line.substring(1))
                        parsedLines++
                    }
                    '+' -> {
                        newLines.add(line.substring(1))
                        parsedLines++
                    }
                    '-' -> {
                        oldLines.add(line.substring(1))
                        parsedLines++
                    }
                    else -> {
                        if (parsedLines == 0) {
                            return Result.failure(ParseError.InvalidHunkError(
                                message = "Unexpected line found in update hunk: '$line'. Every line should start with ' ' (context line), '+' (added line), or '-' (removed line)",
                                lineNumber = lineNumber + 1,
                            ))
                        }
                        // Assume this is the start of the next hunk.
                        break
                    }
                }
            }
        }
    }

    return Result.success(
        UpdateFileChunk(
            changeContext = changeContext,
            oldLines = oldLines,
            newLines = newLines,
            isEndOfFile = isEndOfFile,
        ) to (parsedLines + startIndex)
    )
}
