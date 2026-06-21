// port-lint: source error.rs
package io.github.kotlinmania.codex.core

import io.github.kotlinmania.codex.core.ExecToolCallOutput
import io.github.kotlinmania.codex.core.KnownPlan
import io.github.kotlinmania.codex.core.PlanType
import io.github.kotlinmania.codex.core.ProcessedResponseItem
import io.github.kotlinmania.codex.core.context.TruncationPolicy
import io.github.kotlinmania.codex.core.context.truncateText
import io.github.kotlinmania.codex.protocol.CodexErrorInfo
import io.github.kotlinmania.codex.protocol.ConversationId
import io.github.kotlinmania.codex.protocol.ErrorEvent
import io.github.kotlinmania.codex.protocol.RateLimitSnapshot
import io.github.kotlinmania.codex.utils.readiness.CancelErr
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/** Limit UI error messages to a reasonable size while keeping useful context. */
private const val ERROR_MESSAGE_UI_MAX_BYTES: Int = 2 * 1024 // 4 KiB

private const val CLOUDFLARE_BLOCKED_MESSAGE: String =
    "Access blocked by Cloudflare. This usually happens when connecting from a restricted region"

/**
 * Reason a token refresh failed.
 */
enum class RefreshTokenFailedReason {
    Expired,
    Exhausted,
    Revoked,
    Other,
}

/**
 * Error from a failed token refresh attempt.
 */
data class RefreshTokenFailedError(
    val reason: RefreshTokenFailedReason,
    override val message: String,
) : Exception(message) {
    companion object {
        fun new(reason: RefreshTokenFailedReason, message: String): RefreshTokenFailedError =
            RefreshTokenFailedError(reason = reason, message = message)
    }

    override fun toString(): String = message
}

/**
 * Error from an unexpected HTTP status code.
 */
data class UnexpectedResponseError(
    val status: Int,
    val body: String,
    val requestId: String? = null,
) {
    private fun friendlyMessage(): String? {
        if (status != 403) {
            return null
        }

        if (!body.contains("Cloudflare") || !body.contains("blocked")) {
            return null
        }

        var message = "$CLOUDFLARE_BLOCKED_MESSAGE (status $status)"
        val requestId = requestId
        if (requestId != null) {
            message += ", request id: $requestId"
        }

        return message
    }

    override fun toString(): String = fmt()

    fun fmt(): String {
        val friendly = friendlyMessage()
        return if (friendly != null) {
            friendly
        } else {
            val requestId = requestId
            val suffix = if (requestId != null) ", request id: $requestId" else ""
            "unexpected status " + status + ": " + body + suffix
        }
    }
}

/**
 * Error returned when the retry limit has been exhausted.
 */
data class RetryLimitReachedError(
    val status: Int,
    val requestId: String? = null,
) {
    override fun toString(): String = fmt()

    fun fmt(): String {
        val requestId = requestId
        val suffix = if (requestId != null) ", request id: $requestId" else ""
        val status = status
        return "exceeded retry limit, last status: " + status + suffix
    }
}

/**
 * Error raised when the underlying HTTP connection failed.
 */
data class ConnectionFailedError(
    val source: Throwable,
    val status: Int? = null,
) {
    override fun toString(): String = fmt()

    fun fmt(): String {
        val source = source
        return "Connection failed: " + source
    }
}

/**
 * Error raised when a response stream fails mid-transfer.
 */
data class ResponseStreamFailed(
    val source: Throwable,
    val requestId: String? = null,
    val status: Int? = null,
) {
    override fun toString(): String = fmt()

    fun fmt(): String {
        val source = source
        val requestId = requestId
        val requestSuffix = if (requestId != null) ", request id: $requestId" else ""
        return "Error while reading the server response: " + source + requestSuffix
    }
}

/**
 * Error indicating a usage limit has been reached.
 */
data class UsageLimitReachedError(
    val planType: PlanType? = null,
    val resetsAt: Instant? = null,
    val rateLimits: RateLimitSnapshot? = null,
) {
    override fun toString(): String = fmt()

    fun fmt(): String {
        val planType = planType
        val resetsAt = resetsAt
        val message =
            when (planType) {
                is PlanType.Known ->
                    when (planType.plan) {
                        KnownPlan.Plus ->
                            "You've hit your usage limit. Upgrade to Pro (https://openai.com/chatgpt/pricing), " +
                                "visit https://chatgpt.com/codex/settings/usage to purchase more credits" +
                                retrySuffixAfterOr(resetsAt)
                        KnownPlan.Team, KnownPlan.Business ->
                            "You've hit your usage limit. To get more access now, send a request to your admin" +
                                retrySuffixAfterOr(resetsAt)
                        KnownPlan.Free ->
                            "You've hit your usage limit. Upgrade to Plus to continue using Codex " +
                                "(https://openai.com/chatgpt/pricing)."
                        KnownPlan.Pro ->
                            "You've hit your usage limit. Visit https://chatgpt.com/codex/settings/usage to purchase more credits" +
                                retrySuffixAfterOr(resetsAt)
                        KnownPlan.Enterprise, KnownPlan.Edu ->
                            "You've hit your usage limit.${retrySuffix(resetsAt)}"
                    }
                else ->
                    "You've hit your usage limit.${retrySuffix(resetsAt)}"
            }

        return message
    }
}

/**
 * Environment-variable configuration error.
 */
data class EnvVarError(
    /** Name of the environment variable that is missing. */
    val varName: String,
    /** Optional instructions to help the user set a valid value. */
    val instructions: String? = null,
) {
    override fun toString(): String = fmt()

    fun fmt(): String {
        var message = "Missing environment variable: `$varName`."
        if (instructions != null) {
            message += " $instructions"
        }
        return message
    }
}

/** Sandbox error types. */
sealed class SandboxErr {
    data class Denied(
        val output: ExecToolCallOutput,
    ) : SandboxErr() {
        override fun toString(): String =
            "sandbox denied exec error, exit code: ${output.exitCode}, " +
                "stdout: ${output.stdout.text}, stderr: ${output.stderr.text}"
    }

    data class SeccompInstall(
        val message: String? = null,
    ) : SandboxErr() {
        override fun toString(): String = "seccomp setup error"
    }

    data class SeccompBackend(
        val message: String? = null,
    ) : SandboxErr() {
        override fun toString(): String = "seccomp backend error"
    }

    data class Timeout(
        val output: ExecToolCallOutput,
    ) : SandboxErr() {
        override fun toString(): String = "command timed out"
    }

    data class Signal(
        val signal: Int,
    ) : SandboxErr() {
        override fun toString(): String = "command was killed by a signal"
    }

    object LandlockRestrict : SandboxErr() {
        override fun toString(): String = "Landlock was not able to fully enforce all sandbox rules"
    }
}

/**
 * Codex error types matching the upstream CodexErr enum.
 */
sealed class CodexErr {
    /** Minimal shim mirroring Rust `CodexErr::downcastRef`. */
    inline fun <reified T : Any> downcastRef(): T? {
        val any: Any = this
        return any as? T
    }

    companion object {
        fun from(err: CancelErr): CodexErr = TurnAborted(danglingArtifacts = emptyList())
    }

    fun toException(): CodexException = CodexException(this)

    /**
     * Produce an `ErrorEvent` for this error.
     */
    fun toErrorEvent(messagePrefix: String? = null): ErrorEvent {
        val errorMessage = toString()
        val message: String =
            when (messagePrefix) {
                null -> errorMessage
                else -> "$messagePrefix: $errorMessage"
            }
        return ErrorEvent(message = message, codexErrorInfo = toCodexProtocolError())
    }

    /**
     * Translate core error to client-facing protocol error.
     */
    fun toCodexProtocolError(): CodexErrorInfo =
        when (this) {
            is ContextWindowExceeded ->
                CodexErrorInfo.ContextWindowExceeded
            is UsageLimitReached,
            is QuotaExceeded,
            is UsageNotIncluded,
            ->
                CodexErrorInfo.UsageLimitExceeded
            is RetryLimit ->
                CodexErrorInfo.ResponseTooManyFailedAttempts(httpStatusCode = httpStatusCodeValue())
            is ConnectionFailed ->
                CodexErrorInfo.HttpConnectionFailed(httpStatusCode = httpStatusCodeValue())
            is ResponseStreamFailed ->
                CodexErrorInfo.ResponseStreamConnectionFailed(httpStatusCode = httpStatusCodeValue())
            is RefreshTokenFailed ->
                CodexErrorInfo.Unauthorized
            is SessionConfiguredNotFirstEvent,
            is InternalServerError,
            is InternalAgentDied,
            ->
                CodexErrorInfo.InternalServerError
            is UnsupportedOperation,
            is ConversationNotFound,
            ->
                CodexErrorInfo.BadRequest
            is Sandbox ->
                CodexErrorInfo.SandboxError
            else ->
                CodexErrorInfo.Other
        }

    /**
     * Return the HTTP status code for this error, if any.
     */
    fun httpStatusCodeValue(): Int? {
        val httpStatusCode: Int? =
            when (this) {
                is RetryLimit -> error.status
                is UnexpectedStatus -> error.status
                is ConnectionFailed -> error.status
                is ResponseStreamFailed -> error.status
                else -> null
            }
        return httpStatusCode
    }

    // -----------------------------------------------------------------
    // Core variants
    // -----------------------------------------------------------------

    data class TurnAborted(
        val danglingArtifacts: List<ProcessedResponseItem> = emptyList(),
    ) : CodexErr() {
        override fun toString(): String =
            "turn aborted. Something went wrong? Hit `/feedback` to report the issue."
    }

    data class Stream(
        val message: String,
        val retryDelay: kotlin.time.Duration? = null,
    ) : CodexErr() {
        override fun toString(): String = "stream disconnected before completion: $message"
    }

    object ContextWindowExceeded : CodexErr() {
        override fun toString(): String =
            "Codex ran out of room in the model's context window. Start a new conversation " +
                "or clear earlier history before retrying."
    }

    data class ConversationNotFound(
        val id: ConversationId,
    ) : CodexErr() {
        override fun toString(): String = "no conversation with id: $id"
    }

    object SessionConfiguredNotFirstEvent : CodexErr() {
        override fun toString(): String =
            "session configured event was not the first event in the stream"
    }

    /** Returned when the spawned child process timed out (parity with Rust `CodexErr::Timeout`). */
    object Timeout : CodexErr() {
        override fun toString(): String = "timeout waiting for child process to exit"
    }

    object Spawn : CodexErr() {
        override fun toString(): String = "spawn failed: child stdout/stderr not captured"
    }

    object Interrupted : CodexErr() {
        override fun toString(): String =
            "interrupted (Ctrl-C). Something went wrong? Hit `/feedback` to report the issue."
    }

    data class UnexpectedStatus(
        val error: UnexpectedResponseError,
    ) : CodexErr() {
        override fun toString(): String = error.toString()
    }

    data class UsageLimitReached(
        val error: UsageLimitReachedError = UsageLimitReachedError(),
        /** Legacy field for callers that constructed with (message, rateLimits). */
        val legacyMessage: String? = null,
    ) : CodexErr() {
        // Legacy constructor retained for existing call sites that passed a raw message string.
        constructor(message: String, rateLimits: RateLimitSnapshot? = null) :
            this(UsageLimitReachedError(rateLimits = rateLimits), legacyMessage = message)

        override fun toString(): String = legacyMessage ?: error.toString()
    }

    data class ResponseStreamFailed(
        val error: io.github.kotlinmania.codex.core.ResponseStreamFailed,
    ) : CodexErr() {
        override fun toString(): String = error.toString()
    }

    data class ConnectionFailed(
        val error: ConnectionFailedError,
    ) : CodexErr() {
        override fun toString(): String = error.toString()
    }

    object QuotaExceeded : CodexErr() {
        override fun toString(): String = "Quota exceeded. Check your plan and billing details."
    }

    object UsageNotIncluded : CodexErr() {
        override fun toString(): String =
            "To use Codex with your ChatGPT plan, upgrade to Plus: https://openai.com/chatgpt/pricing."
    }

    object InternalServerError : CodexErr() {
        override fun toString(): String =
            "We're currently experiencing high demand, which may cause temporary errors."
    }

    data class RetryLimit(
        val error: RetryLimitReachedError,
    ) : CodexErr() {
        override fun toString(): String = error.toString()
    }

    object InternalAgentDied : CodexErr() {
        override fun toString(): String = "internal error; agent loop died unexpectedly"
    }

    data class Sandbox(
        val error: SandboxErr,
    ) : CodexErr() {
        override fun toString(): String = "sandbox error: $error"
    }

    object LandlockSandboxExecutableNotProvided : CodexErr() {
        override fun toString(): String = "codex-linux-sandbox was required but not provided"
    }

    data class UnsupportedOperation(
        val message: String,
    ) : CodexErr() {
        override fun toString(): String = "unsupported operation: $message"
    }

    data class RefreshTokenFailed(
        val error: RefreshTokenFailedError,
    ) : CodexErr() {
        /** Legacy constructor accepting a plain message string. */
        constructor(message: String) :
            this(RefreshTokenFailedError(RefreshTokenFailedReason.Other, message))

        val message: String get() = error.message

        override fun toString(): String = error.toString()
    }

    data class Fatal(
        val message: String,
    ) : CodexErr() {
        override fun toString(): String = "Fatal error: $message"
    }

    // -----------------------------------------------------------------
    // Automatic conversions for common external error types
    // -----------------------------------------------------------------

    /** Generic I/O error. */
    data class Io(
        val message: String,
    ) : CodexErr() {
        override fun toString(): String = message
    }

    /** JSON (de)serialization error. */
    data class Json(
        val message: String,
    ) : CodexErr() {
        override fun toString(): String = message
    }

    data class LandlockRuleset(
        val message: String,
    ) : CodexErr() {
        override fun toString(): String = message
    }

    data class LandlockPathFd(
        val message: String,
    ) : CodexErr() {
        override fun toString(): String = message
    }

    /** Join error from structured concurrency. */
    data class TokioJoin(
        val message: String,
    ) : CodexErr() {
        override fun toString(): String = message
    }

    data class EnvVar(
        val error: EnvVarError,
    ) : CodexErr() {
        /** Legacy constructor accepting just the variable name. */
        constructor(varName: String) : this(EnvVarError(varName = varName))

        val varName: String get() = error.varName

        override fun toString(): String = error.toString()
    }
}

// -----------------------------------------------------------------
// Retry-suffix helpers (mirror Rust `error.rs` private fns).
// -----------------------------------------------------------------

/**
 * Test-only override for "now" when formatting retry timestamps, matching the
 * upstream `NOW_OVERRIDE` thread-local.
 */
internal var nowOverride: Instant? = null

internal fun nowForRetry(): Instant {
    val now = nowOverride
    if (now != null) {
        return now
    }
    return Clock.System.now()
}

internal fun retrySuffix(resetsAt: Instant?): String =
    if (resetsAt != null) {
        " Try again at ${formatRetryTimestamp(resetsAt)}."
    } else {
        " Try again later."
    }

internal fun retrySuffixAfterOr(resetsAt: Instant?): String =
    if (resetsAt != null) {
        " or try again at ${formatRetryTimestamp(resetsAt)}."
    } else {
        " or try again later."
    }

internal fun formatRetryTimestamp(resetsAt: Instant): String {
    val localZone = TimeZone.currentSystemDefault()
    val localReset: LocalDateTime = resetsAt.toLocalDateTime(localZone)
    val localNow: LocalDateTime = nowForRetry().toLocalDateTime(localZone)
    return if (localReset.date == localNow.date) {
        formatTimeHMSP(localReset)
    } else {
        val day = localReset.dayOfMonth
        val suffix = daySuffix(day)
        val month = monthAbbrev(localReset.monthNumber)
        "$month $day$suffix, ${localReset.year} ${formatTimeHMSP(localReset)}"
    }
}

private fun formatTimeHMSP(dt: LocalDateTime): String {
    val hour24 = dt.hour
    val hour12 =
        when {
            hour24 == 0 -> 12
            hour24 > 12 -> hour24 - 12
            else -> hour24
        }
    val ampm = if (hour24 < 12) "AM" else "PM"
    val minute = dt.minute.toString().padStart(2, '0')
    return "$hour12:$minute $ampm"
}

private fun monthAbbrev(month: Int): String =
    when (month) {
        1 -> "Jan"
        2 -> "Feb"
        3 -> "Mar"
        4 -> "Apr"
        5 -> "May"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Aug"
        9 -> "Sep"
        10 -> "Oct"
        11 -> "Nov"
        12 -> "Dec"
        else -> "???"
    }

internal fun daySuffix(day: Int): String =
    when (day) {
        in 11..13 -> "th"
        else ->
            when (day % 10) {
                1 -> "st"
                2 -> "nd" // codespell:ignore
                3 -> "rd"
                else -> "th"
            }
    }

/**
 * Produce a user-facing error message for display in the UI.
 */
fun getErrorMessageUi(e: CodexErr): String {
    val message =
        when (e) {
            is CodexErr.Sandbox ->
                when (val err = e.error) {
                    is SandboxErr.Denied -> {
                        val output = err.output
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
                    is SandboxErr.Timeout ->
                        "error: command timed out after ${err.output.duration.inWholeMilliseconds} ms"
                    else -> e.toString()
                }
            else -> e.toString()
        }
    return truncateText(message, TruncationPolicy.Bytes(ERROR_MESSAGE_UI_MAX_BYTES))
}
