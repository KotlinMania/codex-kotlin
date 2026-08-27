package io.github.kotlinmania.codex.asyncutils

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.selects.select

internal enum class CancelErr {
    Cancelled,
}

/**
 * Awaits the given block, returning [CancelErr.Cancelled] if [token] is cancelled first.
 */
internal suspend fun <T> orCancel(token: Job, block: suspend () -> T): Result<T> =
    coroutineScope {
        val res: Deferred<T> = async { block() }
        select<Result<T>> {
            token.onJoin {
                res.cancel()
                Result.failure(CancelErrException(CancelErr.Cancelled))
            }
            res.onAwait { value -> Result.success(value) }
        }
    }

internal class CancelErrException(
    val err: CancelErr,
) : RuntimeException(err.name)
