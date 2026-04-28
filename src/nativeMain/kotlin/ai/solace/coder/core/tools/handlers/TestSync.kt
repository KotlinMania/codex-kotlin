// port-lint: source core/src/tools/handlers/test_sync.rs
package ai.solace.coder.core.tools.handlers

import ai.solace.coder.core.tools.ToolHandler
import ai.solace.coder.core.tools.ToolInvocation
import ai.solace.coder.core.tools.ToolKind
import ai.solace.coder.core.tools.ToolOutput
import ai.solace.coder.core.tools.ToolPayload
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class TestSyncHandler : ToolHandler {
    override val kind: ToolKind = ToolKind.Function

    override suspend fun handle(invocation: ToolInvocation): Result<ToolOutput> {
        val arguments = when (val p = invocation.payload) {
            is ToolPayload.Function -> p.arguments
            else -> return Result.failure(
                IllegalArgumentException("test_sync_tool handler received unsupported payload")
            )
        }

        val args = try {
            Json.decodeFromString<TestSyncArgs>(arguments)
        } catch (err: Throwable) {
            return Result.failure(
                IllegalArgumentException("failed to parse function arguments: $err")
            )
        }

        args.sleepBeforeMs?.let { delayMs ->
            if (delayMs > 0) delay(delayMs)
        }

        args.barrier?.let { barrier ->
            val barrierResult = waitOnBarrier(barrier)
            if (barrierResult.isFailure) return barrierResult.map { error("unreachable") }
        }

        args.sleepAfterMs?.let { delayMs ->
            if (delayMs > 0) delay(delayMs)
        }

        return Result.success(
            ToolOutput.Function(
                content = "ok",
                contentItems = null,
                success = true,
            )
        )
    }
}

private const val DEFAULT_TIMEOUT_MS: Long = 1_000

@Serializable
private data class BarrierArgs(
    val id: String,
    val participants: Int,
    val timeoutMs: Long = DEFAULT_TIMEOUT_MS,
)

@Serializable
private data class TestSyncArgs(
    val sleepBeforeMs: Long? = null,
    val sleepAfterMs: Long? = null,
    val barrier: BarrierArgs? = null,
)

private class BarrierState(
    val participants: Int,
) {
    val mutex = Mutex()
    var arrived: Int = 0
    var leaderClaimed: Boolean = false
    val gate: CompletableDeferred<Unit> = CompletableDeferred()
}

private val barrierMapMutex = Mutex()
private val barrierMap = mutableMapOf<String, BarrierState>()

private suspend fun waitOnBarrier(args: BarrierArgs): Result<Unit> {
    if (args.participants == 0) {
        return Result.failure(
            IllegalArgumentException("barrier participants must be greater than zero")
        )
    }

    if (args.timeoutMs == 0L) {
        return Result.failure(
            IllegalArgumentException("barrier timeout must be greater than zero")
        )
    }

    val barrierId = args.id
    val state = barrierMapMutex.withLock {
        val existing = barrierMap[barrierId]
        if (existing != null) {
            if (existing.participants != args.participants) {
                return Result.failure(
                    IllegalArgumentException(
                        "barrier $barrierId already registered with ${existing.participants} participants"
                    )
                )
            }
            existing
        } else {
            val fresh = BarrierState(args.participants)
            barrierMap[barrierId] = fresh
            fresh
        }
    }

    val isLeader: Boolean = state.mutex.withLock {
        state.arrived += 1
        if (state.arrived >= state.participants) {
            state.gate.complete(Unit)
            if (!state.leaderClaimed) {
                state.leaderClaimed = true
                true
            } else {
                false
            }
        } else {
            false
        }
    }

    val completed = withTimeoutOrNull(args.timeoutMs) { state.gate.await() }
    if (completed == null) {
        return Result.failure(IllegalStateException("test_sync_tool barrier wait timed out"))
    }

    if (isLeader) {
        barrierMapMutex.withLock {
            val current = barrierMap[barrierId]
            if (current === state) {
                barrierMap.remove(barrierId)
            }
        }
    }

    return Result.success(Unit)
}
