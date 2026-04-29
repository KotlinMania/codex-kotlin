// port-lint: ignore
// transliterated from upstream module root (async-utils crate)
package ai.solace.coder.asyncutils

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AsyncUtilsTest {
    @Test
    fun returnsOkWhenFutureCompletesFirst() = runTest {
        val token = Job()
        val value: suspend () -> Int = { 42 }

        val result = orCancel(token) { value() }

        assertEquals(Result.success(42), result)
    }

    @Test
    fun returnsErrWhenTokenCancelledFirst() = runTest {
        val token = Job()

        launch {
            delay(10)
            token.cancel()
        }

        val result = orCancel(token) {
            delay(100)
            7
        }

        assertTrue(result.isFailure)
        val err = result.exceptionOrNull() as CancelErrException
        assertEquals(CancelErr.Cancelled, err.err)
    }

    @Test
    fun returnsErrWhenTokenAlreadyCancelled() = runTest {
        val token = Job()
        token.cancel()

        val result = orCancel(token) {
            delay(50)
            5
        }

        assertTrue(result.isFailure)
        val err = result.exceptionOrNull() as CancelErrException
        assertEquals(CancelErr.Cancelled, err.err)
    }
}
