// port-lint: ignore
// transliterated from upstream module root (async-utils crate)
package io.github.kotlinmania.codex.asyncutils

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.selects.select

enum class CancelErr {
    Cancelled,
}

/**
 * Awaits the given block, returning [CancelErr.Cancelled] if [token] is cancelled first.
 */
suspend fun <T> orCancel(token: Job, block: suspend () -> T): Result<T> = coroutineScope {
    val res: Deferred<T> = async { block() }
    select<Result<T>> {
        token.onJoin {
            res.cancel()
            Result.failure(CancelErrException(CancelErr.Cancelled))
        }
        res.onAwait { value -> Result.success(value) }
    }
}

class CancelErrException(val err: CancelErr) : RuntimeException(err.name)
