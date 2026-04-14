// port-lint: source core/src/tools/handlers/grep_files.rs
package ai.solace.coder.core.tools.handlers

import ai.solace.coder.core.FunctionCallError
import ai.solace.coder.core.tools.ToolHandler
import ai.solace.coder.core.tools.ToolInvocation
import ai.solace.coder.core.tools.ToolKind
import ai.solace.coder.core.tools.ToolOutput
import ai.solace.coder.core.tools.ToolPayload
import ai.solace.coder.core.ExecExpiration
import ai.solace.coder.core.ExecParams
import ai.solace.coder.exec.process.ProcessExecutor
import ai.solace.coder.protocol.SandboxPolicy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path.Companion.toPath
import kotlin.time.Duration.Companion.seconds

/**
 * Handler for the grep_files tool.
 * Uses ripgrep (rg) to search for patterns in files.
 *
 * Ported from Rust codex-rs/core/src/tools/handlers/grep_files.rs
 */
class GrepFilesHandler : ToolHandler {
    private val processExecutor: ProcessExecutor = ProcessExecutor()


    override val kind: ToolKind = ToolKind.Function

    override suspend fun handle(invocation: ToolInvocation): Result<ToolOutput> {
        val payload = invocation.payload
        if (payload !is ToolPayload.Function) {
            return Result.failure(
                FunctionCallError.RespondToModel("grep_files handler received unsupported payload")
            )
        }

        val args = try {
            json.decodeFromString<GrepFilesArgs>(payload.arguments)
        } catch (e: Exception) {
            return Result.failure(
                FunctionCallError.RespondToModel("failed to parse function arguments: ${e.message}")
            )
        }

        val pattern = args.pattern.trim()
        if (pattern.isEmpty()) {
            return Result.failure(
                FunctionCallError.RespondToModel("pattern must not be empty")
            )
        }

        if (args.limit == 0) {
            return Result.failure(
                FunctionCallError.RespondToModel("limit must be greater than zero")
            )
        }

        val limit = minOf(args.limit, MAX_LIMIT)
        val searchPath = invocation.turn.resolvePath(args.path)

        // Verify path exists
        val path = searchPath.toPath()
        if (!FileSystem.SYSTEM.exists(path)) {
            return Result.failure(
                FunctionCallError.RespondToModel("unable to access `$searchPath`: path does not exist")
            )
        }

        val include = args.include?.trim()?.takeIf { it.isNotEmpty() }

        return runRgSearch(
            pattern = pattern,
            include = include,
            searchPath = searchPath,
            limit = limit,
            cwd = invocation.turn.cwd,
            sandboxCwd = invocation.turn.cwd
        )
    }

    companion object {
        private const val DEFAULT_LIMIT = 100
        private const val MAX_LIMIT = 2000
        private val COMMAND_TIMEOUT = 30.seconds

        private val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    /**
     * Run ripgrep search and return matching file paths.
     */
    private suspend fun runRgSearch(
        pattern: String,
        include: String?,
        searchPath: String,
        limit: Int,
        cwd: String,
        sandboxCwd: String
    ): Result<ToolOutput> {
        val command = buildRgCommand(pattern, include, searchPath)

        val params = ExecParams(
            command = command,
            cwd = cwd,
            expiration = ExecExpiration.Timeout(COMMAND_TIMEOUT)
        )

        // Grep is read-only, so use ReadOnly sandbox policy
        val sandboxPolicy = SandboxPolicy.ReadOnly

        val execResult = try {
            processExecutor.execute(
                params = params,
                sandboxPolicy = sandboxPolicy,
                sandboxCwd = sandboxCwd
            )
        } catch (e: Exception) {
            return Result.failure(
                FunctionCallError.RespondToModel(
                    "failed to launch rg: ${e.message}. Ensure ripgrep is installed and on PATH."
                )
            )
        }

        val output = when (execResult) {
            is ai.solace.coder.core.error.CodexResult.Success -> execResult.value
            is ai.solace.coder.core.error.CodexResult.Failure -> return Result.failure(
                FunctionCallError.RespondToModel(
                    "failed to launch rg: ${execResult.error}. Ensure ripgrep is installed and on PATH."
                )
            )
        }

        if (output.timedOut) {
            return Result.failure(
                FunctionCallError.RespondToModel("rg timed out after 30 seconds")
            )
        }

        return when (output.exitCode) {
            0 -> {
                val results = parseResults(output.stdout.text, limit)
                if (results.isEmpty()) {
                    Result.success(
                        ToolOutput.Function(
                            content = "No matches found.",
                            success = false
                        )
                    )
                } else {
                    Result.success(
                        ToolOutput.Function(
                            content = results.joinToString("\n"),
                            success = true
                        )
                    )
                }
            }
            1 -> {
                // Exit code 1 means no matches
                Result.success(
                    ToolOutput.Function(
                        content = "No matches found.",
                        success = false
                    )
                )
            }
            else -> {
                Result.failure(
                    FunctionCallError.RespondToModel("rg failed: ${output.stderr.text}")
                )
            }
        }
    }

    /**
     * Build the ripgrep command.
     */
    private fun buildRgCommand(pattern: String, include: String?, searchPath: String): List<String> {
        val command = mutableListOf(
            "rg",
            "--files-with-matches",
            "--sortr=modified",
            "--regexp", pattern,
            "--no-messages"
        )

        if (include != null) {
            command.add("--glob")
            command.add(include)
        }

        command.add("--")
        command.add(searchPath)

        return command
    }

    /**
     * Parse results from stdout, respecting the limit.
     */
    private fun parseResults(stdout: String, limit: Int): List<String> {
        return stdout.lines()
            .filter { it.isNotBlank() }
            .take(limit)
    }
}

/**
 * Arguments for the grep_files tool.
 */
@Serializable
private data class GrepFilesArgs(
    val pattern: String,
    val include: String? = null,
    val path: String? = null,
    val limit: Int = 100
)
