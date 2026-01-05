package ai.solace.coder.core.exec

import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlinx.coroutines.Job
import kotlin.time.Duration.Companion.milliseconds

@Serializable
data class ExecParams(
    val command: List<String>,
    val cwd: String,
    val expiration: ExecExpiration,
    val env: Map<String, String> = emptyMap(),
    val withEscalatedPermissions: Boolean? = null,
    val justification: String? = null,
    val arg0: String? = null
)

@Serializable
sealed class ExecExpiration {
    @Serializable
    data class Timeout(val duration: Duration) : ExecExpiration()

    @Serializable
    object DefaultTimeout : ExecExpiration()

    @Serializable
    data class Cancellation(val cancelToken: Job) : ExecExpiration()

    companion object {
        fun fromTimeoutMs(timeoutMs: Long?): ExecExpiration {
            return if (timeoutMs != null) {
                Timeout(timeoutMs.milliseconds)
            } else {
                DefaultTimeout
            }
        }
    }
}

@Serializable
data class StreamOutput<T>(
    val text: T,
    val truncatedAfterLines: UInt? = null
)

@Serializable
data class SimpleProcessResult(
    val exitCode: Int,
    val stdout: String,
    val stderr: String
)

@Serializable
data class ExecToolCallOutput(
    val exitCode: Int,
    val stdout: StreamOutput<String>,
    val stderr: StreamOutput<String>,
    val aggregatedOutput: StreamOutput<String>,
    val duration: Duration,
    val timedOut: Boolean
)
