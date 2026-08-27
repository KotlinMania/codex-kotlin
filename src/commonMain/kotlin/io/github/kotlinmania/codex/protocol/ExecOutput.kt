// port-lint: source protocol/src/exec_output.rs
package io.github.kotlinmania.codex.protocol

import io.github.kotlinmania.codex.utils.encoding.decodeSmartUtf8OrWindows1252
import kotlin.time.Duration
import kotlinx.serialization.Serializable

@Serializable
data class StreamOutput<T>(
    val text: T,
    val truncatedAfterLines: Long? = null,
) {
    companion object {
        fun of(text: String): StreamOutput<String> = StreamOutput(text = text)
    }
}

fun StreamOutput<ByteArray>.fromUtf8Lossy(): StreamOutput<String> {
    return StreamOutput(
        text = decodeSmartUtf8OrWindows1252(text),
        truncatedAfterLines = truncatedAfterLines,
    )
}

@Serializable
data class ExecToolCallOutput(
    val exitCode: Int = 0,
    val stdout: StreamOutput<String> = StreamOutput(text = ""),
    val stderr: StreamOutput<String> = StreamOutput(text = ""),
    val aggregatedOutput: StreamOutput<String> = StreamOutput(text = ""),
    val duration: Duration = Duration.ZERO,
    val timedOut: Boolean = false,
) {
    companion object {
        fun default(): ExecToolCallOutput = ExecToolCallOutput()
    }
}
