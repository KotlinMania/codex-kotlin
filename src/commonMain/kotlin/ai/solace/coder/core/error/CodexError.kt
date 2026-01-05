// port-lint: source core/src/error.rs
package ai.solace.coder.core.error

import ai.solace.coder.core.ExecToolCallOutput
import ai.solace.coder.core.context.TruncationPolicy
import ai.solace.coder.core.context.truncateText
import ai.solace.coder.protocol.CodexErrorInfo
import ai.solace.coder.protocol.ConversationId
import ai.solace.coder.protocol.ErrorEvent
import ai.solace.coder.protocol.RateLimitSnapshot
import ai.solace.coder.protocol.ResponseInputItem
import ai.solace.coder.protocol.ResponseItem
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration

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

/** Limit UI error messages to a reasonable size while keeping useful context. */
private const val ERROR_MESSAGE_UI_MAX_BYTES = 2 * 1024 // 2 KiB

/** Processed response item from agent loop. */
data class ProcessedResponseItem(
        val item: ResponseItem,
        val response: ResponseInputItem?
)

/** Sandbox error types matching Rust's SandboxErr enum. */
sealed class SandboxErr : Exception() {
    /** Error from sandbox execution. */
    data class Denied(val output: ExecToolCallOutput) : SandboxErr() {
        override val message: String
            get() = "sandbox denied exec error, exit code: ${output.exitCode}, " +
                    "stdout: ${output.stdout.text}, stderr: ${output.stderr.text}"
    }

    /** Command timed out. */
    data class Timeout(val output: ExecToolCallOutput) : SandboxErr() {
        override val message: String
            get() = "command timed out"
    }

    /** Command was killed by a signal. */
    data class Signal(val signalNumber: Int) : SandboxErr() {
        override val message: String
            get() = "command was killed by a signal"
    }

    /** Landlock was not able to fully enforce all sandbox rules. */
    object LandlockRestrict : SandboxErr() {
        override val message: String
            get() = "Landlock was not able to fully enforce all sandbox rules"
    }
}

/** Connection failed error. */
data class ConnectionFailedError(val source: Exception) {
    override fun toString(): String = "Connection failed: ${source.message}"
}

/** Response stream failed error. */
data class ResponseStreamFailed(
        val source: Exception,
        val requestId: String? = null
) {
    override fun toString(): String {
        val suffix = requestId?.let { ", request id: $it" } ?: ""
        return "Error while reading the server response: ${source.message}$suffix"
    }
}

/** Refresh token failure reasons. */
enum class RefreshTokenFailedReason {
    Expired,
    Exhausted,
    Revoked,
    Other
}

/** Refresh token failed error. */
data class RefreshTokenFailedError(
        val reason: RefreshTokenFailedReason,
        override val message: String
) : Exception(message) {
    companion object {
        fun new(reason: RefreshTokenFailedReason, message: String): RefreshTokenFailedError {
            return RefreshTokenFailedError(reason, message)
        }
    }
}

private const val CLOUDFLARE_BLOCKED_MESSAGE: String =
        "Access blocked by Cloudflare. This usually happens when connecting from a restricted region"

/** Unexpected HTTP response error. */
data class UnexpectedResponseError(
        val status: Int,
        val body: String,
        val requestId: String? = null
) : Exception() {
    private fun friendlyMessage(): String? {
        if (status != 403) {
            return null
        }

        if (!body.contains("Cloudflare") || !body.contains("blocked")) {
            return null
        }

        var message = "$CLOUDFLARE_BLOCKED_MESSAGE (status $status)"
        requestId?.let { id ->
            message += ", request id: $id"
        }

        return message
    }

    override fun toString(): String {
        val friendly = friendlyMessage()
        return if (friendly != null) {
            friendly
        } else {
            val suffix = requestId?.let { ", request id: $it" } ?: ""
            "unexpected status $status: $body$suffix"
        }
    }
}

/** Retry limit reached error. */
data class RetryLimitReachedError(
        val status: Int,
        val requestId: String? = null
) {
    override fun toString(): String {
        val suffix = requestId?.let { ", request id: $it" } ?: ""
        return "exceeded retry limit, last status: $status$suffix"
    }
}

/** Known plan types. */
enum class KnownPlan {
    Free,
    Plus,
    Pro,
    Team,
    Business,
    Enterprise,
    Edu
}

/** Plan type - known or unknown. */
sealed class PlanType {
    data class Known(val plan: KnownPlan) : PlanType()
    data class Unknown(val name: String) : PlanType()
}

/** Usage limit reached error. */
data class UsageLimitReachedError(
        val planType: PlanType? = null,
        val resetsAt: Instant? = null,
        val rateLimits: RateLimitSnapshot? = null
) {
    override fun toString(): String {
        val message = when (planType) {
            is PlanType.Known -> when (planType.plan) {
                KnownPlan.Plus -> "You've hit your usage limit. Upgrade to Pro (https://openai.com/chatgpt/pricing), visit https://chatgpt.com/codex/settings/usage to purchase more credits${retrySuffixAfterOr(resetsAt)}"
                KnownPlan.Team, KnownPlan.Business -> "You've hit your usage limit. To get more access now, send a request to your admin${retrySuffixAfterOr(resetsAt)}"
                KnownPlan.Free -> "You've hit your usage limit. Upgrade to Plus to continue using Codex (https://openai.com/chatgpt/pricing)."
                KnownPlan.Pro -> "You've hit your usage limit. Visit https://chatgpt.com/codex/settings/usage to purchase more credits${retrySuffixAfterOr(resetsAt)}"
                KnownPlan.Enterprise, KnownPlan.Edu -> "You've hit your usage limit.${retrySuffix(resetsAt)}"
            }
            is PlanType.Unknown, null -> "You've hit your usage limit.${retrySuffix(resetsAt)}"
        }
        return message
    }
}

private fun retrySuffix(resetsAt: Instant?): String {
    return if (resetsAt != null) {
        val formatted = formatRetryTimestamp(resetsAt)
        " Try again at $formatted."
    } else {
        " Try again later."
    }
}

private fun retrySuffixAfterOr(resetsAt: Instant?): String {
    return if (resetsAt != null) {
        val formatted = formatRetryTimestamp(resetsAt)
        " or try again at $formatted."
    } else {
        " or try again later."
    }
}

private fun formatRetryTimestamp(resetsAt: Instant): String {
    val localReset = resetsAt.toLocalDateTime(TimeZone.currentSystemDefault())
    val localNow = nowForRetry().toLocalDateTime(TimeZone.currentSystemDefault())

    return if (localReset.date == localNow.date) {
        // Same day - show time only
        formatTimeOnly(localReset)
    } else {
        // Different day - show full date and time
        val suffix = daySuffix(localReset.dayOfMonth)
        formatFullDateTime(localReset, suffix)
    }
}

private fun formatTimeOnly(dt: LocalDateTime): String {
    val hour = if (dt.hour == 0) 12 else if (dt.hour > 12) dt.hour - 12 else dt.hour
    val ampm = if (dt.hour < 12) "AM" else "PM"
    return "$hour:${dt.minute.toString().padStart(2, '0')} $ampm"
}

private fun formatFullDateTime(dt: LocalDateTime, daySuffix: String): String {
    val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val month = monthNames[dt.monthNumber - 1]
    val hour = if (dt.hour == 0) 12 else if (dt.hour > 12) dt.hour - 12 else dt.hour
    val ampm = if (dt.hour < 12) "AM" else "PM"
    return "$month ${dt.dayOfMonth}$daySuffix, ${dt.year} $hour:${dt.minute.toString().padStart(2, '0')} $ampm"
}

private fun daySuffix(day: Int): String {
    return when (day) {
        11, 12, 13 -> "th"
        else -> when (day % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }
}

// For testing - allows overriding "now"
internal var nowOverride: Instant? = null

private fun nowForRetry(): Instant {
    return nowOverride ?: Clock.System.now()
}

/** Environment variable error. */
data class EnvVarError(
        /** Name of the environment variable that is missing. */
        val variable: String,
        /** Optional instructions to help the user get a valid value for the variable and set it. */
        val instructions: String? = null
) {
    override fun toString(): String {
        val base = "Missing environment variable: `$variable`."
        return if (instructions != null) {
            "$base $instructions"
        } else {
            base
        }
    }
}

/** Codex error types matching Rust's CodexErr enum. */
sealed class CodexError {
    abstract fun toErrorInfo(): CodexErrorInfo
    abstract fun httpStatusCodeValue(): Int?

    fun toException(): CodexException = CodexException(this)

    /** Translate core error to client-facing protocol error. */
    fun toCodexProtocolError(): CodexErrorInfo = toErrorInfo()

    /** Create an ErrorEvent from this error. */
    fun toErrorEvent(messagePrefix: String? = null): ErrorEvent {
        val errorMessage = toString()
        val message = if (messagePrefix != null) {
            "$messagePrefix: $errorMessage"
        } else {
            errorMessage
        }
        return ErrorEvent(
                message = message,
                codexErrorInfo = toCodexProtocolError()
        )
    }

    /** Turn aborted with dangling artifacts. */
    data class TurnAborted(val danglingArtifacts: List<ProcessedResponseItem> = emptyList()) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String =
                "turn aborted. Something went wrong? Hit `/feedback` to report the issue."
    }

    /**
     * Returned by ResponsesClient when the SSE stream disconnects or errors out
     * after the HTTP handshake has succeeded but before it finished emitting response.completed.
     */
    data class Stream(val message: String, val retryDelay: Duration? = null) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.ResponseStreamDisconnected()
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = "stream disconnected before completion: $message"
    }

    /** Context window exceeded. */
    object ContextWindowExceeded : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.ContextWindowExceeded
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String =
                "Codex ran out of room in the model's context window. Start a new conversation or clear earlier history before retrying."
    }

    /** No conversation with the given ID. */
    data class ConversationNotFound(val conversationId: ConversationId) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.BadRequest
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = "no conversation with id: $conversationId"
    }

    /** Session configured event was not the first event in the stream. */
    object SessionConfiguredNotFirstEvent : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.InternalServerError
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = "session configured event was not the first event in the stream"
    }

    /** Returned by run_command_stream when the spawned child process timed out (10s). */
    object Timeout : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = "timeout waiting for child process to exit"
    }

    /**
     * Returned by run_command_stream when the child could not be spawned
     * (its stdout/stderr pipes could not be captured).
     */
    object Spawn : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = "spawn failed: child stdout/stderr not captured"
    }

    /**
     * Returned by run_command_stream when the user pressed Ctrl-C (SIGINT).
     * Session uses this to surface a polite FunctionCallOutput back to the model instead of crashing the CLI.
     */
    object Interrupted : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String =
                "interrupted (Ctrl-C). Something went wrong? Hit `/feedback` to report the issue."
    }

    /** Unexpected HTTP status code. */
    data class UnexpectedStatus(val error: UnexpectedResponseError) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.HttpConnectionFailed(error.status)
        override fun httpStatusCodeValue(): Int = error.status
        override fun toString(): String = error.toString()
    }

    /** Usage limit reached. */
    data class UsageLimitReached(val error: UsageLimitReachedError) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.UsageLimitExceeded
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = error.toString()
    }

    /** Response stream failed. */
    data class ResponseStreamFailed(val error: ai.solace.coder.core.error.ResponseStreamFailed) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo {
            val statusCode = null // ResponseStreamFailed doesn't expose status code directly
            return CodexErrorInfo.ResponseStreamConnectionFailed(statusCode)
        }
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = error.toString()
    }

    /** Connection failed. */
    data class ConnectionFailed(val error: ConnectionFailedError) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.HttpConnectionFailed(null)
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = error.toString()
    }

    /** Quota exceeded. */
    object QuotaExceeded : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.UsageLimitExceeded
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = "Quota exceeded. Check your plan and billing details."
    }

    /** Usage not included in plan. */
    object UsageNotIncluded : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.UsageLimitExceeded
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String =
                "To use Codex with your ChatGPT plan, upgrade to Plus: https://openai.com/chatgpt/pricing."
    }

    /** Internal server error. */
    object InternalServerError : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.InternalServerError
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String =
                "We're currently experiencing high demand, which may cause temporary errors."
    }

    /** Retry limit exceeded. */
    data class RetryLimit(val error: RetryLimitReachedError) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo =
                CodexErrorInfo.ResponseTooManyFailedAttempts(httpStatusCodeValue())
        override fun httpStatusCodeValue(): Int = error.status
        override fun toString(): String = error.toString()
    }

    /** Agent loop died unexpectedly. */
    object InternalAgentDied : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.InternalServerError
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = "internal error; agent loop died unexpectedly"
    }

    /** Sandbox error. */
    data class Sandbox(val error: SandboxErr) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.SandboxError
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = "sandbox error: ${error.message}"
    }

    /** Landlock sandbox executable not provided. */
    object LandlockSandboxExecutableNotProvided : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = "codex-linux-sandbox was required but not provided"
    }

    /** Unsupported operation. */
    data class UnsupportedOperation(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.BadRequest
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = "unsupported operation: $message"
    }

    /** Refresh token failed. */
    data class RefreshTokenFailed(val error: RefreshTokenFailedError) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Unauthorized
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = error.message
    }

    /** Fatal error. */
    data class Fatal(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = "Fatal error: $message"
    }

    /** IO error. */
    data class Io(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = message
    }

    /** JSON parsing error. */
    data class Json(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = message
    }

    /** Tokio join error (coroutine error in Kotlin). */
    data class TokioJoin(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = message
    }

    /** Environment variable error. */
    data class EnvVar(val error: EnvVarError) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = error.toString()
    }

    // Additional types for compatibility with existing code

    /** HTTP error (simplified version). */
    data class Http(val statusCode: Int, val message: String? = null) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo =
                when (statusCode) {
                    401 -> CodexErrorInfo.Unauthorized
                    400 -> CodexErrorInfo.BadRequest
                    500, 502, 503, 504 -> CodexErrorInfo.InternalServerError
                    else -> CodexErrorInfo.HttpConnectionFailed(statusCode)
                }
        override fun httpStatusCodeValue(): Int = statusCode
        override fun toString(): String = message ?: "HTTP error $statusCode"
    }

    /** Git error. */
    data class GitError(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = message
    }

    /** Image processing error. */
    data class ImageProcessingError(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = message
    }

    /** Encoding error. */
    data class EncodingError(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = message
    }

    /** File system error. */
    data class FileSystemError(val message: String) : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.Other
        override fun httpStatusCodeValue(): Int? = null
        override fun toString(): String = message
    }

    /** Sandbox error types (nested sealed class for backwards compatibility). */
    sealed class SandboxError : CodexError() {
        override fun toErrorInfo(): CodexErrorInfo = CodexErrorInfo.SandboxError

        data class Unsupported(val message: String) : SandboxError() {
            override fun httpStatusCodeValue(): Int? = null
            override fun toString(): String = message
        }

        data class CreationFailed(val message: String) : SandboxError() {
            override fun httpStatusCodeValue(): Int? = null
            override fun toString(): String = message
        }

        data class ApplicationFailed(val message: String) : SandboxError() {
            override fun httpStatusCodeValue(): Int? = null
            override fun toString(): String = message
        }

        data class ConfigurationError(val message: String) : SandboxError() {
            override fun httpStatusCodeValue(): Int? = null
            override fun toString(): String = message
        }

        data class Denied(val message: String) : SandboxError() {
            override fun httpStatusCodeValue(): Int? = null
            override fun toString(): String = message
        }
    }

    companion object {
        /** Create from CancelErr (coroutine cancellation). */
        fun fromCancelErr(): CodexError = TurnAborted(emptyList())
    }
}

/** Exception wrapper for CodexError. */
class CodexException(val error: CodexError) : Exception(error.toString())

/** Type alias for backwards compatibility. */
typealias CodexErr = CodexError

/** Get a UI-friendly error message, with truncation for large outputs. */
fun getErrorMessageUi(e: CodexError): String {
    val message = when (e) {
        is CodexError.Sandbox -> {
            val sandboxErr = e.error
            when (sandboxErr) {
                is SandboxErr.Denied -> {
                    val output = sandboxErr.output
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
                is SandboxErr.Timeout -> {
                    val output = sandboxErr.output
                    "error: command timed out after ${output.duration.inWholeMilliseconds} ms"
                }
                else -> e.toString()
            }
        }
        else -> e.toString()
    }

    return truncateText(message, TruncationPolicy.Bytes(ERROR_MESSAGE_UI_MAX_BYTES))
}
