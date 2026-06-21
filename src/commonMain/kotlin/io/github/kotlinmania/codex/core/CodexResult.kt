package io.github.kotlinmania.codex.core

/**
 * Result type for Codex operations that may fail.
 *
 * This exists because Kotlin stdlib `Result<T>` is awkward to import as a public return type across
 * KMP boundaries, and the upstream `type Result<T> = std::result::Result<T, CodexErr>` cannot be
 * expressed without using `typealias` (forbidden in this project).
 */
sealed class CodexResult<out T> {
    data class Success<T>(
        val value: T,
    ) : CodexResult<T>()

    data class Failure(
        val error: CodexErr,
    ) : CodexResult<Nothing>()

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

    inline fun onFailure(action: (CodexErr) -> Unit): CodexResult<T> {
        if (this is Failure) action(error)
        return this
    }

    inline fun <R> fold(onSuccess: (T) -> R, onFailure: (CodexErr) -> R): R =
        when (this) {
            is Success -> onSuccess(value)
            is Failure -> onFailure(error)
        }

    inline fun getOrElse(onFailure: (CodexErr) -> @UnsafeVariance T): T =
        when (this) {
            is Success -> value
            is Failure -> onFailure(error)
        }

    companion object {
        fun <T> success(value: T): CodexResult<T> = Success(value)

        fun <T> failure(error: CodexErr): CodexResult<T> = Failure(error)

        inline fun <T> runCatching(block: () -> T): CodexResult<T> =
            try {
                Success(block())
            } catch (e: Exception) {
                Failure(CodexErr.Io(e.message ?: "Unknown error"))
            }
    }
}

/** Exception wrapper for CodexErr. */
class CodexException(
    val error: CodexErr,
) : Exception(error.toString())
