// port-lint: source core/src/exec.rs
package io.github.kotlinmania.codex.core

import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay

const val DEFAULT_EXEC_COMMAND_TIMEOUT_MS: Long = 10_000L

enum class ExecExpirationOutcome {
    TimedOut,
    Cancelled,
}

sealed class ExecExpiration {
    data class Timeout(val duration: Duration) : ExecExpiration()
    data object DefaultTimeout : ExecExpiration()
    data class Cancellation(val isCancelled: () -> Boolean) : ExecExpiration()
    data class TimeoutOrCancellation(val timeout: Duration, val isCancelled: () -> Boolean) : ExecExpiration()

    fun timeoutMs(): Long? = when (this) {
        is Timeout -> duration.inWholeMilliseconds
        DefaultTimeout -> DEFAULT_EXEC_COMMAND_TIMEOUT_MS
        is Cancellation -> null
        is TimeoutOrCancellation -> timeout.inWholeMilliseconds
    }

    suspend fun waitWithOutcome(): ExecExpirationOutcome = when (this) {
        is Timeout -> {
            delay(duration)
            ExecExpirationOutcome.TimedOut
        }
        DefaultTimeout -> {
            delay(DEFAULT_EXEC_COMMAND_TIMEOUT_MS.milliseconds)
            ExecExpirationOutcome.TimedOut
        }
        is Cancellation -> {
            while (!isCancelled()) {
                delay(50)
            }
            ExecExpirationOutcome.Cancelled
        }
        is TimeoutOrCancellation -> {
            val start = kotlinx.datetime.Clock.System.now()
            while (!isCancelled()) {
                if ((kotlinx.datetime.Clock.System.now() - start) >= timeout) {
                    return ExecExpirationOutcome.TimedOut
                }
                delay(50)
            }
            ExecExpirationOutcome.Cancelled
        }
    }

    companion object {
        fun fromTimeoutMs(timeoutMs: Long?): ExecExpiration {
            return if (timeoutMs != null) Timeout(timeoutMs.milliseconds) else DefaultTimeout
        }
    }
}
