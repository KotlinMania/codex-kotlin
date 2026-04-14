// port-lint: source core/src/error.rs
package ai.solace.coder.core.error

import ai.solace.coder.core.ExecToolCallOutput
import ai.solace.coder.protocol.CodexErrorInfo
import ai.solace.coder.protocol.RateLimitSnapshot

/** Result type for Codex operations that may fail. Maps to Rust's Result<T, CodexErr>. */
sealed class CodexResult<out T> {
    data class Success<T>(val value: T) : CodexResult<T>()
    data class Failure(val error: CodexError) : CodexResult<Nothing>()

    fun isSuccess(): Boolean = this is Success
    fun isFailure(): Boolean = this is Failure

    fun getOrNull(): T? =
            when (this) {
                is Success -> value
                is Failure -> null
            }

    fun getOrThrow(): T =
            when (this) {
                is Success -> value
                is Failure -> throw error.toException()
            }

    inline fun <R> map(transform: (T) -> R): CodexResult<R> =
            when (this) {
                is Success -> Success(transform(value))
                is Failure -> this
            }

    inline fun <R> flatMap(transform: (T) -> CodexResult<R>): CodexResult<R> =
            when (this) {
                is Success -> transform(value)
                is Failure -> this
            }

    inline fun onSuccess(action: (T) -> Unit): CodexResult<T> {
        if (this is Success) action(value)
        return this
    }

    inline fun onFailure(action: (CodexError) -> Unit): CodexResult<T> {
        if (this is Failure) action(error)
        return this
    }

    inline fun <R> fold(onSuccess: (T) -> R, onFailure: (CodexError) -> R): R =
            when (this) {
                is Success -> onSuccess(value)
                is Failure -> onFailure(error)
            }

    inline fun getOrElse(onFailure: (CodexError) -> @UnsafeVariance T): T =
            when (this) {
                is Success -> value
                is Failure -> onFailure(error)
            }

    companion object {
        fun <T> success(value: T): CodexResult<T> = Success(value)
        fun <T> failure(error: CodexError): CodexResult<T> = Failure(error)

        inline fun <T> runCatching(block: () -> T): CodexResult<T> =
                try {
                    Success(block())
                } catch (e: Exception) {
                    Failure(CodexError.Io(e.message ?: "Unknown error"))
                }
    }
}

/** Codex error types matching Rust's CodexErr enum. */
sealed class CodexError {
    abstract fun toErrorInfo(): CodexErrorInfo?
    abstract fun httpStatusCodeValue(): Int?

    fun toException(): CodexException = CodexException(this)

    data class Fatal(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
    }

    data class Io(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
    }

    data class Stream(val message: String, val retryDelay: kotlin.time.Duration? = null) :
            CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.ResponseStreamDisconnected()
        override fun httpStatusCodeValue(): Int? = null
    }

    data class Http(val statusCode: Int, val message: String? = null) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo =
                when (statusCode) {
                    401 -> CodexErrorInfo.Unauthorized
                    400 -> CodexErrorInfo.BadRequest
                    500, 502, 503, 504 -> CodexErrorInfo.InternalServerError
                    else -> CodexErrorInfo.HttpConnectionFailed(statusCode)
                }
        override fun httpStatusCodeValue(): Int = statusCode
    }

    object ContextWindowExceeded : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.ContextWindowExceeded
        override fun httpStatusCodeValue(): Int? = null
    }

    data class UsageLimitReached(val message: String, val rateLimits: RateLimitSnapshot? = null) :
            CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.UsageLimitExceeded
        override fun httpStatusCodeValue(): Int? = null
    }

    object UsageNotIncluded : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.UsageLimitExceeded
        override fun httpStatusCodeValue(): Int? = null
    }

    object QuotaExceeded : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.UsageLimitExceeded
        override fun httpStatusCodeValue(): Int? = null
    }

    data class RefreshTokenFailed(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Unauthorized
        override fun httpStatusCodeValue(): Int? = 401
    }

    object Interrupted : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo? = null
        override fun httpStatusCodeValue(): Int? = null
    }

    data class TurnAborted(val danglingArtifacts: List<Any> = emptyList()) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo? = null
        override fun httpStatusCodeValue(): Int? = null
    }

    data class EnvVar(val varName: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
    }

    data class UnsupportedOperation(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
    }

    object InternalAgentDied : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
    }

    data class GitError(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
    }

    data class ImageProcessingError(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
    }

    data class EncodingError(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
    }

    data class FileSystemError(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
    }

    sealed class SandboxError : CodexError() {
        /** Error from sandbox execution: denied by policy. */
        data class Denied(val output: ExecToolCallOutput) : SandboxError() {
            override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
            override fun httpStatusCodeValue(): Int? = null
        }

        /** Command timed out. */
        data class Timeout(val output: ExecToolCallOutput) : SandboxError() {
            override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
            override fun httpStatusCodeValue(): Int? = null
        }

        /** Command was killed by a signal. */
        data class Signal(val signal: Int) : SandboxError() {
            override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
            override fun httpStatusCodeValue(): Int? = null
        }

        /** Linux landlock was not able to fully enforce all sandbox rules. */
        object LandlockRestrict : SandboxError() {
            override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
            override fun httpStatusCodeValue(): Int? = null
        }
    }
}

/** Exception wrapper for CodexError. */
class CodexException(val error: CodexError) : Exception(error.toString())

typealias CodexErr = CodexError

/** Limit UI error messages to a reasonable size while keeping useful context. */
private const val ERROR_MESSAGE_UI_MAX_BYTES: Int = 2 * 1024 // 4 KiB

/**
 * Produce a user-facing error message for display in the UI.
 *
 * Mirrors Rust `core/src/error.rs::get_error_message_ui`.
 */
fun getErrorMessageUi(e: CodexError): String {
    val message = when (e) {
        is CodexError.SandboxError.Denied -> {
            val output = e.output
            val aggregated = output.aggregatedOutput.text.trim()
            if (aggregated.isNotEmpty()) {
                output.aggregatedOutput.text
            } else {
                val stderr = output.stderr.text.trim()
                val stdout = output.stdout.text.trim()
                when {
                    stderr.isNotEmpty() && stdout.isNotEmpty() -> "$stderr\n$stdout"
                    stderr.isNotEmpty() -> output.stderr.text
                    stdout.isNotEmpty() -> output.stdout.text
                    else -> "command failed inside sandbox with exit code ${output.exitCode}"
                }
            }
        }
        is CodexError.SandboxError.Timeout -> {
            "error: command timed out after ${e.output.duration.inWholeMilliseconds} ms"
        }
        else -> e.toString()
    }
    return if (message.length > ERROR_MESSAGE_UI_MAX_BYTES) {
        message.substring(0, ERROR_MESSAGE_UI_MAX_BYTES)
    } else {
        message
    }
}
