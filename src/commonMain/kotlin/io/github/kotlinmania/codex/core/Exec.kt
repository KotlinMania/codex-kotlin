// port-lint: source core/src/exec.rs
package io.github.kotlinmania.codex.core

import io.github.kotlinmania.codex.core.CodexErr
import io.github.kotlinmania.codex.core.CodexResult
import io.github.kotlinmania.codex.exec.process.SandboxType
import io.github.kotlinmania.codex.exec.sandbox.CommandSpec
import io.github.kotlinmania.codex.exec.sandbox.ExecEnv
import io.github.kotlinmania.codex.exec.sandbox.SandboxManager
import io.github.kotlinmania.codex.exec.shell.ShellDetector
import io.github.kotlinmania.codex.protocol.SandboxPolicy
            // Convert to exception so caller try/catch will turn it into a failure CodexResult
            throw io.github.kotlinmania.codex.core.CodexException(
                    CodexErr.Sandbox(io.github.kotlinmania.codex.core.SandboxErr.Denied(execOutput))
            )
        }

        return execOutput
    }

    /** Platform-specific process creation */
    private fun platformCreateProcess(
            program: String,
            args: List<String>,
            cwd: String,
            env: Map<String, String>
    ): ProcessHandle {
        // This will be implemented with expect/actual
        return createPlatformProcess(program, args, cwd, env)
    }

    /** Platform-specific process group killing */
    private fun platformKillChildProcessGroup(process: ProcessHandle) {
        // This will be implemented with expect/actual
        killPlatformChildProcessGroup(process)
    }
}

/** Check if execution likely failed due to sandbox restrictions */
fun isLikelySandboxDenied(sandboxType: SandboxType, execOutput: ExecToolCallOutput): Boolean {
    if (sandboxType == SandboxType.None || execOutput.exitCode == 0) return false

    // Quick rejects: well-known non-sandbox shell exit codes
    val quickRejectExitCodes = setOf(2, 126, 127)
    if (quickRejectExitCodes.contains(execOutput.exitCode)) return false

    val sandboxDeniedKeywords =
            listOf(
                    "operation not permitted",
                    "permission denied",
                    "read-only file system",
                    "seccomp",
                    "sandbox",
                    "landlock",
                    "failed to write file"
            )

    val hasSandboxKeyword =
            listOf(execOutput.stderr.text, execOutput.stdout.text, execOutput.aggregatedOutput.text)
                    .any { section ->
                        section.lowercase().let { lower ->
                            sandboxDeniedKeywords.any { keyword -> lower.contains(keyword) }
                        }
                    }

    return hasSandboxKeyword
}

/** Extension function to split list into first element and rest */
private fun <T> List<T>.splitFirst(): Pair<T, List<T>>? {
    return if (isEmpty()) null else first() to drop(1)
}

/** Extension function to convert ByteArray to UTF-8 string with lossy conversion */
private fun StreamOutput<ByteArray>.fromUtf8Lossy(): StreamOutput<String> {
    return StreamOutput(text = text.decodeToString(), truncatedAfterLines = truncatedAfterLines)
}

/** Extension function for ByteArray concatenation */
private operator fun ByteArray.plus(other: ByteArray): ByteArray {
    val result = ByteArray(this.size + other.size)
    this.copyInto(result, 0, 0, this.size)
    other.copyInto(result, this.size, 0, other.size)
    return result
}

/** Extension function for List<ByteArray> to ByteArray */
private fun List<ByteArray>.toByteArray(): ByteArray {
    val totalSize = sumOf { it.size }
    val result = ByteArray(totalSize)
    var offset = 0
    for (chunk in this) {
        chunk.copyInto(result, offset)
        offset += chunk.size
    }
    return result
}
