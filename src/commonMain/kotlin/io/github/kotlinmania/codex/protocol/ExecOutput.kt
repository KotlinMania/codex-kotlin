// port-lint: source protocol/src/exec_output.rs
package io.github.kotlinmania.codex.protocol

import io.github.kotlinmania.codex.utils.encoding.bytesToStringSmart
import kotlin.time.Duration
import kotlinx.serialization.Serializable

@Serializable
data class StreamOutput(
    val text: String,
    val truncatedAfterLines: Long? = null,
) {
    companion object {
        fun of(text: String): StreamOutput = StreamOutput(text = text)
        fun fromUtf8Lossy(bytes: ByteArray, truncatedAfterLines: Long? = null): StreamOutput =
            StreamOutput(
                text = bytesToStringSmart(bytes),
                truncatedAfterLines = truncatedAfterLines,
            )
    }
}

@Serializable
data class ExecToolCallOutput(
    val exitCode: Int = 0,
    val stdout: StreamOutput = StreamOutput(text = ""),
    val stderr: StreamOutput = StreamOutput(text = ""),
    val aggregatedOutput: StreamOutput = StreamOutput(text = ""),
    val duration: Duration = Duration.ZERO,
    val timedOut: Boolean = false,
) {
    companion object {
        fun default(): ExecToolCallOutput = ExecToolCallOutput()
    }
}
