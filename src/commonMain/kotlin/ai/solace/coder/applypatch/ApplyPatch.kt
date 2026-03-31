// port-lint: source apply-patch/src/lib.rs
package ai.solace.coder.applypatch

/**
 * Error types for apply-patch operations.
 */
sealed class ApplyPatchError : Exception() {
    data class ParseError(val error: ai.solace.coder.applypatch.ParseError) : ApplyPatchError() {
        override val message: String get() = error.message ?: "Parse error"
    }

    data class IoError(val context: String, val source: Exception) : ApplyPatchError() {
        override val message: String get() = "$context: ${source.message}"
    }

    /** Error that occurs while computing replacements when applying patch chunks. */
    data class ComputeReplacements(override val message: String) : ApplyPatchError()

    /** A raw patch body was provided without an explicit `apply_patch` invocation. */
    data object ImplicitInvocation : ApplyPatchError() {
        override val message: String
            get() = "patch detected without explicit call to apply_patch. Rerun as [\"apply_patch\", \"<patch>\"]"
    }
}

/**
 * Both the raw PATCH argument to `apply_patch` as well as the PATCH argument
 * parsed into hunks.
 */
data class ApplyPatchArgs(
    val patch: String,
    val hunks: List<Hunk>,
    var workdir: String? = null,
)

/**
 * Represents a file change from applying a patch.
 */
sealed class ApplyPatchFileChange {
    data class Add(val content: String) : ApplyPatchFileChange()
    data class Delete(val content: String) : ApplyPatchFileChange()
    data class Update(
        val unifiedDiff: String,
        val movePath: String? = null,
        /** new_content that will result after the unified_diff is applied. */
        val newContent: String,
    ) : ApplyPatchFileChange()
}

/**
 * ApplyPatchAction is the result of parsing an `apply_patch` command.
 * By construction, all paths should be absolute paths.
 */
data class ApplyPatchAction(
    val changes: Map<String, ApplyPatchFileChange>,
    /** The raw patch argument that can be used with `apply_patch` as an exec call. */
    val patch: String,
    /** The working directory that was used to resolve relative paths in the patch. */
    val cwd: String,
) {
    fun isEmpty(): Boolean = changes.isEmpty()
}

/** Tracks file paths affected by applying a patch. */
data class AffectedPaths(
    val added: List<String>,
    val modified: List<String>,
    val deleted: List<String>,
)

/** Intended result of a file update for apply_patch. */
data class ApplyPatchFileUpdate(
    val unifiedDiff: String,
    val content: String,
)

/** Detailed instructions for gpt-4.1 on how to use the `apply_patch` tool. */
// const val APPLY_PATCH_TOOL_INSTRUCTIONS: String — loaded from resource file

/**
 * Compute a list of replacements needed to transform [originalLines] into the
 * new lines, given the patch [chunks]. Each replacement is returned as
 * (startIndex, oldLen, newLines).
 */
fun computeReplacements(
    originalLines: List<String>,
    path: String,
    chunks: List<UpdateFileChunk>,
): Result<List<Triple<Int, Int, List<String>>>> {
    val replacements = mutableListOf<Triple<Int, Int, List<String>>>()
    var lineIndex = 0

    for (chunk in chunks) {
        // If a chunk has a `changeContext`, use seekSequence to find it, then
        // adjust our `lineIndex` to continue from there.
        val ctxLine = chunk.changeContext
        if (ctxLine != null) {
            val idx = seekSequence(originalLines, listOf(ctxLine), lineIndex, eof = false)
            if (idx != null) {
                lineIndex = idx + 1
            } else {
                return Result.failure(
                    ApplyPatchError.ComputeReplacements(
                        "Failed to find context '$ctxLine' in $path"
                    )
                )
            }
        }

        if (chunk.oldLines.isEmpty()) {
            // Pure addition (no old lines). Add them at the end or just
            // before the final empty line if one exists.
            val insertionIdx = if (originalLines.lastOrNull()?.isEmpty() == true) {
                originalLines.size - 1
            } else {
                originalLines.size
            }
            replacements.add(Triple(insertionIdx, 0, chunk.newLines))
            continue
        }

        // Try to match existing lines in the file with the old lines from the chunk.
        var pattern: List<String> = chunk.oldLines
        var found = seekSequence(originalLines, pattern, lineIndex, chunk.isEndOfFile)
        var newSlice: List<String> = chunk.newLines

        if (found == null && pattern.lastOrNull()?.isEmpty() == true) {
            // Retry without the trailing empty line which represents the final
            // newline in the file.
            pattern = pattern.dropLast(1)
            if (newSlice.lastOrNull()?.isEmpty() == true) {
                newSlice = newSlice.dropLast(1)
            }
            found = seekSequence(originalLines, pattern, lineIndex, chunk.isEndOfFile)
        }

        if (found != null) {
            replacements.add(Triple(found, pattern.size, newSlice))
            lineIndex = found + pattern.size
        } else {
            return Result.failure(
                ApplyPatchError.ComputeReplacements(
                    "Failed to find expected lines in $path:\n${chunk.oldLines.joinToString("\n")}"
                )
            )
        }
    }

    replacements.sortBy { it.first }
    return Result.success(replacements)
}

/**
 * Apply the (startIndex, oldLen, newLines) replacements to [lines],
 * returning the modified file contents as a list of lines.
 */
fun applyReplacements(
    lines: MutableList<String>,
    replacements: List<Triple<Int, Int, List<String>>>,
): List<String> {
    // Apply replacements in descending order so earlier replacements
    // don't shift positions of later ones.
    for ((startIdx, oldLen, newSegment) in replacements.reversed()) {
        // Remove old lines.
        repeat(oldLen) {
            if (startIdx < lines.size) {
                lines.removeAt(startIdx)
            }
        }
        // Insert new lines.
        for ((offset, newLine) in newSegment.withIndex()) {
            lines.add(startIdx + offset, newLine)
        }
    }
    return lines
}

/**
 * Print the summary of changes in git-style format.
 */
fun formatSummary(affected: AffectedPaths): String {
    val sb = StringBuilder()
    sb.appendLine("Success. Updated the following files:")
    for (path in affected.added) {
        sb.appendLine("A $path")
    }
    for (path in affected.modified) {
        sb.appendLine("M $path")
    }
    for (path in affected.deleted) {
        sb.appendLine("D $path")
    }
    return sb.toString()
}
