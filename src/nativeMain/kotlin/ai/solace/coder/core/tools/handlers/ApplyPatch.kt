// port-lint: source core/src/tools/handlers/apply_patch.rs
package ai.solace.coder.core.tools.handlers

import ai.solace.coder.core.error.CodexErr
import ai.solace.coder.core.session.FreeformTool
import ai.solace.coder.core.session.FreeformToolFormat
import ai.solace.coder.core.session.ResponsesApiTool
import ai.solace.coder.core.session.ToolSpec
import ai.solace.coder.core.tools.AdditionalProperties
import ai.solace.coder.core.tools.JsonSchema
import ai.solace.coder.core.tools.ToolError
import ai.solace.coder.core.tools.ToolHandler
import ai.solace.coder.core.tools.ToolInvocation
import ai.solace.coder.core.tools.ToolKind
import ai.solace.coder.core.tools.ToolOutput
import ai.solace.coder.core.tools.ToolPayload
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

/**
 * Handler for the apply_patch tool. Applies unified diff patches to files.
 *
 * Ported from Rust codex-rs/core/src/tools/handlers/apply_patch.rs
 */
class ApplyPatchHandler : ToolHandler {

    override val kind: ToolKind = ToolKind.Function

    override fun matchesKind(payload: ToolPayload): Boolean {
        return payload is ToolPayload.Function || payload is ToolPayload.Custom
    }

    override fun isMutating(invocation: ToolInvocation): Boolean = true

    override suspend fun handle(invocation: ToolInvocation): Result<ToolOutput> {
        val patchInput =
                when (val payload = invocation.payload) {
                    is ToolPayload.Function -> {
                        val args =
                                try {
                                    json.decodeFromString<ApplyPatchArgs>(payload.arguments)
                                } catch (e: Exception) {
                                    return Result.failure(
                                            ToolError.Codex(
                                                    CodexErr.Fatal(
                                                            "failed to parse function arguments: ${e.message}"
                                                    )
                                            )
                                    )
                                }
                        args.input
                    }
                    is ToolPayload.Custom -> payload.input
                    else -> {
                        return Result.failure(
                                ToolError.Codex(
                                        CodexErr.Fatal(
                                                "apply_patch handler received unsupported payload"
                                        )
                                )
                        )
                    }
                }

        val cwd = invocation.turn.cwd

        return try {
            val result = applyPatch(patchInput, cwd)
            result.map { message -> ToolOutput.Function(content = message, success = true) }
        } catch (e: Exception) {
            Result.failure(ToolError.Codex(CodexErr.Fatal("apply_patch failed: ${e.message}")))
        }
    }

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        private const val BEGIN_PATCH = "*** Begin Patch"
        private const val END_PATCH = "*** End Patch"
        private const val ADD_FILE_PREFIX = "*** Add File: "
        private const val DELETE_FILE_PREFIX = "*** Delete File: "
        private const val UPDATE_FILE_PREFIX = "*** Update File: "
        private const val MOVE_TO_PREFIX = "*** Move to: "
        private const val HUNK_MARKER = "@@"
        private const val END_OF_FILE_MARKER = "*** End of File"

        /** Apply a patch to files in the working directory. */
        private fun applyPatch(patchInput: String, cwd: String): Result<String> {
            val lines = patchInput.lines()
            val operations =
                    parsePatch(lines)
                            ?: return Result.failure(
                                    ToolError.Codex(
                                            CodexErr.Fatal(
                                                    "Failed to parse patch: invalid format"
                                            )
                                    )
                            )

            val results = mutableListOf<String>()

            for (op in operations) {
                when (op) {
                    is FileOperation.AddFile -> {
                        val result = addFile(op.path, op.content, cwd)
                        if (result.isFailure) return result.map { "" }
                        results.add("Added file: ${op.path}")
                    }
                    is FileOperation.DeleteFile -> {
                        val result = deleteFile(op.path, cwd)
                        if (result.isFailure) return result.map { "" }
                        results.add("Deleted file: ${op.path}")
                    }
                    is FileOperation.UpdateFile -> {
                        val result = updateFile(op.path, op.newPath, op.hunks, cwd)
                        if (result.isFailure) return result.map { "" }
                        val msg =
                                if (op.newPath != null) {
                                    "Updated and moved file: ${op.path} -> ${op.newPath}"
                                } else {
                                    "Updated file: ${op.path}"
                                }
                        results.add(msg)
                    }
                }
            }

            return Result.success(results.joinToString("\n"))
        }

        /** Parse patch text into file operations. */
        private fun parsePatch(lines: List<String>): List<FileOperation>? {
            val trimmedLines = lines.map { it }
            val startIdx = trimmedLines.indexOfFirst { it.trim() == BEGIN_PATCH }
            val endIdx = trimmedLines.indexOfLast { it.trim() == END_PATCH }

            if (startIdx == -1 || endIdx == -1 || startIdx >= endIdx) {
                return null
            }

            val patchLines = trimmedLines.subList(startIdx + 1, endIdx)
            val operations = mutableListOf<FileOperation>()
            var i = 0

            while (i < patchLines.size) {
                val line = patchLines[i]

                when {
                    line.startsWith(ADD_FILE_PREFIX) -> {
                        val path = line.removePrefix(ADD_FILE_PREFIX).trim()
                        val content = StringBuilder()
                        i++
                        while (i < patchLines.size && patchLines[i].startsWith("+")) {
                            content.appendLine(patchLines[i].removePrefix("+"))
                            i++
                        }
                        operations.add(FileOperation.AddFile(path, content.toString().trimEnd()))
                    }
                    line.startsWith(DELETE_FILE_PREFIX) -> {
                        val path = line.removePrefix(DELETE_FILE_PREFIX).trim()
                        operations.add(FileOperation.DeleteFile(path))
                        i++
                    }
                    line.startsWith(UPDATE_FILE_PREFIX) -> {
                        val path = line.removePrefix(UPDATE_FILE_PREFIX).trim()
                        i++
                        var newPath: String? = null
                        if (i < patchLines.size && patchLines[i].startsWith(MOVE_TO_PREFIX)) {
                            newPath = patchLines[i].removePrefix(MOVE_TO_PREFIX).trim()
                            i++
                        }
                        val hunks = mutableListOf<Hunk>()
                        while (i < patchLines.size && !patchLines[i].startsWith("*** ")) {
                            if (patchLines[i].startsWith(HUNK_MARKER)) {
                                val header = patchLines[i].removePrefix(HUNK_MARKER).trim()
                                i++
                                val hunkLines = mutableListOf<HunkLine>()
                                while (i < patchLines.size &&
                                        !patchLines[i].startsWith(HUNK_MARKER) &&
                                        !patchLines[i].startsWith("*** ")) {
                                    val hunkLine = patchLines[i]
                                    when {
                                        hunkLine.startsWith(" ") ->
                                                hunkLines.add(HunkLine.Context(hunkLine.drop(1)))
                                        hunkLine.startsWith("-") ->
                                                hunkLines.add(HunkLine.Remove(hunkLine.drop(1)))
                                        hunkLine.startsWith("+") ->
                                                hunkLines.add(HunkLine.Add(hunkLine.drop(1)))
                                        hunkLine == END_OF_FILE_MARKER -> {
                                            // Skip end of file marker
                                        }
                                        else -> {
                                            // Treat as context line without prefix
                                            hunkLines.add(HunkLine.Context(hunkLine))
                                        }
                                    }
                                    i++
                                }
                                hunks.add(Hunk(header.takeIf { it.isNotEmpty() }, hunkLines))
                            } else {
                                i++
                            }
                        }
                        operations.add(FileOperation.UpdateFile(path, newPath, hunks))
                    }
                    else -> i++
                }
            }

            return operations
        }

        /** Add a new file. */
        private fun addFile(path: String, content: String, cwd: String): Result<Unit> {
            return try {
                val fullPath = resolvePath(path, cwd).toPath()

                // Create parent directories if needed
                fullPath.parent?.let { parent ->
                    if (!FileSystem.SYSTEM.exists(parent)) {
                        FileSystem.SYSTEM.createDirectories(parent)
                    }
                }

                FileSystem.SYSTEM.sink(fullPath).buffer().use { sink -> sink.writeUtf8(content) }
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(
                        ToolError.Codex(CodexErr.Fatal("Failed to add file $path: ${e.message}"))
                )
            }
        }

        /** Delete a file. */
        private fun deleteFile(path: String, cwd: String): Result<Unit> {
            return try {
                val fullPath = resolvePath(path, cwd).toPath()
                FileSystem.SYSTEM.delete(fullPath)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(
                        ToolError.Codex(
                                CodexErr.Fatal("Failed to delete file $path: ${e.message}")
                        )
                )
            }
        }

        /** Update a file by applying hunks. */
        private fun updateFile(
                path: String,
                newPath: String?,
                hunks: List<Hunk>,
                cwd: String
        ): Result<Unit> {
            return try {
                val fullPath = resolvePath(path, cwd).toPath()

                // Read existing content
                val existingContent =
                        FileSystem.SYSTEM.source(fullPath).buffer().use { source ->
                            source.readUtf8()
                        }
                val existingLines = existingContent.lines().toMutableList()

                // Apply hunks
                for (hunk in hunks) {
                    applyHunk(existingLines, hunk)
                }

                val newContent = existingLines.joinToString("\n")

                // Handle move if specified
                val targetPath =
                        if (newPath != null) {
                            FileSystem.SYSTEM.delete(fullPath)
                            resolvePath(newPath, cwd).toPath()
                        } else {
                            fullPath
                        }

                // Create parent directories if needed (for move)
                targetPath.parent?.let { parent ->
                    if (!FileSystem.SYSTEM.exists(parent)) {
                        FileSystem.SYSTEM.createDirectories(parent)
                    }
                }

                FileSystem.SYSTEM.sink(targetPath).buffer().use { sink ->
                    sink.writeUtf8(newContent)
                }

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(
                        ToolError.Codex(
                                CodexErr.Fatal("Failed to update file $path: ${e.message}")
                        )
                )
            }
        }

        /** Apply a single hunk to file lines. Uses context lines to find the correct location. */
        private fun applyHunk(lines: MutableList<String>, hunk: Hunk) {
            // Find the location to apply the hunk based on context
            val contextLines = hunk.lines.filterIsInstance<HunkLine.Context>()
            val removeLines = hunk.lines.filterIsInstance<HunkLine.Remove>()

            // Try to find the location by matching context/remove lines
            val searchPattern =
                    (contextLines.take(3) + removeLines).map {
                        when (it) {
                            is HunkLine.Context -> it.text
                            is HunkLine.Remove -> it.text
                            else -> ""
                        }
                    }

            var matchIndex = -1
            if (searchPattern.isNotEmpty()) {
                outer@ for (i in lines.indices) {
                    for ((j, pattern) in searchPattern.withIndex()) {
                        if (i + j >= lines.size || lines[i + j].trim() != pattern.trim()) {
                            continue@outer
                        }
                    }
                    matchIndex = i
                    break
                }
            }

            if (matchIndex == -1) {
                // Fallback: apply at end of file
                matchIndex = lines.size
            }

            // Apply changes at matchIndex
            var currentIndex = matchIndex
            for (line in hunk.lines) {
                when (line) {
                    is HunkLine.Context -> currentIndex++
                    is HunkLine.Remove -> {
                        if (currentIndex < lines.size) {
                            lines.removeAt(currentIndex)
                        }
                    }
                    is HunkLine.Add -> {
                        lines.add(currentIndex, line.text)
                        currentIndex++
                    }
                }
            }
        }

        /** Resolve a relative path against the working directory. */
        private fun resolvePath(path: String, cwd: String): String {
            return if (path.startsWith("/") || path.matches(Regex("^[A-Za-z]:.*"))) {
                path
            } else {
                if (cwd.endsWith("/") || cwd.endsWith("\\")) {
                    "$cwd$path"
                } else {
                    "$cwd/$path"
                }
            }
        }
    }
}

/** Arguments for the apply_patch tool. */
@Serializable private data class ApplyPatchArgs(val input: String)

/** File operations parsed from a patch. */
private sealed class FileOperation {
    data class AddFile(val path: String, val content: String) : FileOperation()
    data class DeleteFile(val path: String) : FileOperation()
    data class UpdateFile(val path: String, val newPath: String?, val hunks: List<Hunk>) :
            FileOperation()
}

/** A hunk within an update operation. */
private data class Hunk(val header: String?, val lines: List<HunkLine>)

/** Individual lines within a hunk. */
private sealed class HunkLine {
    data class Context(val text: String) : HunkLine()
    data class Remove(val text: String) : HunkLine()
    data class Add(val text: String) : HunkLine()
}

/** Lark grammar for the apply_patch freeform tool. Mirrors codex-rs/core/src/tools/handlers/tool_apply_patch.lark */
const val APPLY_PATCH_LARK_GRAMMAR: String =
    "start: begin_patch hunk+ end_patch\n" +
    "begin_patch: \"*** Begin Patch\" LF\n" +
    "end_patch: \"*** End Patch\" LF?\n" +
    "\n" +
    "hunk: add_hunk | delete_hunk | update_hunk\n" +
    "add_hunk: \"*** Add File: \" filename LF add_line+\n" +
    "delete_hunk: \"*** Delete File: \" filename LF\n" +
    "update_hunk: \"*** Update File: \" filename LF change_move? change?\n" +
    "\n" +
    "filename: /(.+)/\n" +
    "add_line: \"+\" /(.*)/ LF -> line\n" +
    "\n" +
    "change_move: \"*** Move to: \" filename LF\n" +
    "change: (change_context | change_line)+ eof_line?\n" +
    "change_context: (\"@@\" | \"@@ \" /(.+)/) LF\n" +
    "change_line: (\"+\" | \"-\" | \" \") /(.*)/ LF\n" +
    "eof_line: \"*** End of File\" LF\n" +
    "\n" +
    "%import common.LF\n"

/**
 * Returns a custom tool that can be used to edit files. Well-suited for GPT-5 models.
 * https://platform.openai.com/docs/guides/function-calling#custom-tools
 */
internal fun createApplyPatchFreeformTool(): ToolSpec {
    return ToolSpec.Freeform(
        FreeformTool(
            name = "apply_patch",
            description = "Use the `apply_patch` tool to edit files. This is a FREEFORM tool, so do not wrap the patch in JSON.",
            format = FreeformToolFormat(
                type = "grammar",
                syntax = "lark",
                definition = APPLY_PATCH_LARK_GRAMMAR
            )
        )
    )
}

/** Returns a json tool that can be used to edit files. Should only be used with gpt-oss models. */
internal fun createApplyPatchJsonTool(): ToolSpec {
    val properties = mutableMapOf<String, JsonSchema>()
    properties["input"] = JsonSchema.String(
        description = "The entire contents of the apply_patch command"
    )

    return ToolSpec.Function(
        ResponsesApiTool(
            name = "apply_patch",
            description = """Use the `apply_patch` tool to edit files.
Your patch language is a stripped‑down, file‑oriented diff format designed to be easy to parse and safe to apply. You can think of it as a high‑level envelope:

*** Begin Patch
[ one or more file sections ]
*** End Patch

Within that envelope, you get a sequence of file operations.
You MUST include a header to specify the action you are taking.
Each operation starts with one of three headers:

*** Add File: <path> - create a new file. Every following line is a + line (the initial contents).
*** Delete File: <path> - remove an existing file. Nothing follows.
*** Update File: <path> - patch an existing file in place (optionally with a rename).

May be immediately followed by *** Move to: <new path> if you want to rename the file.
Then one or more “hunks”, each introduced by @@ (optionally followed by a hunk header).
Within a hunk each line starts with:

For instructions on [context_before] and [context_after]:
- By default, show 3 lines of code immediately above and 3 lines immediately below each change. If a change is within 3 lines of a previous change, do NOT duplicate the first change’s [context_after] lines in the second change’s [context_before] lines.
- If 3 lines of context is insufficient to uniquely identify the snippet of code within the file, use the @@ operator to indicate the class or function to which the snippet belongs. For instance, we might have:
@@ class BaseClass
[3 lines of pre-context]
- [old_code]
+ [new_code]
[3 lines of post-context]

- If a code block is repeated so many times in a class or function such that even a single `@@` statement and 3 lines of context cannot uniquely identify the snippet of code, you can use multiple `@@` statements to jump to the right context. For instance:

@@ class BaseClass
@@ 	 def method():
[3 lines of pre-context]
- [old_code]
+ [new_code]
[3 lines of post-context]

The full grammar definition is below:
Patch := Begin { FileOp } End
Begin := "*** Begin Patch" NEWLINE
End := "*** End Patch" NEWLINE
FileOp := AddFile | DeleteFile | UpdateFile
AddFile := "*** Add File: " path NEWLINE { "+" line NEWLINE }
DeleteFile := "*** Delete File: " path NEWLINE
UpdateFile := "*** Update File: " path NEWLINE [ MoveTo ] { Hunk }
MoveTo := "*** Move to: " newPath NEWLINE
Hunk := "@@" [ header ] NEWLINE { HunkLine } [ "*** End of File" NEWLINE ]
HunkLine := (" " | "-" | "+") text NEWLINE

A full patch can combine several operations:

*** Begin Patch
*** Add File: hello.txt
+Hello world
*** Update File: src/app.py
*** Move to: src/main.py
@@ def greet():
-print("Hi")
+print("Hello, world!")
*** Delete File: obsolete.txt
*** End Patch

It is important to remember:

- You must include a header with your intended action (Add/Delete/Update)
- You must prefix new lines with `+` even when creating a new file
- File references can only be relative, NEVER ABSOLUTE.
""",
            strict = false,
            parameters = JsonSchema.Object(
                properties = properties,
                required = listOf("input"),
                additionalProperties = AdditionalProperties.from(false)
            )
        )
    )
}
