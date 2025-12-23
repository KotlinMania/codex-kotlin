// port-lint: source core/src/unified_exec/session_manager.rs
package ai.solace.coder.core.unified_exec

// import ai.solace.coder.core.tools.createApprovalRequirementForCommand // TODO: Implement this
import ai.solace.coder.core.context.TruncationPolicy
import ai.solace.coder.core.context.formattedTruncateText
import ai.solace.coder.core.session.Session as CodexSession
import ai.solace.coder.core.session.TurnContext
import ai.solace.coder.exec.process.SandboxType
import ai.solace.coder.exec.sandbox.CommandSpec
import ai.solace.coder.exec.sandbox.ExecEnv
import ai.solace.coder.exec.sandbox.SandboxManager
import ai.solace.coder.utils.concurrent.Notify
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.coroutines.*
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class SessionEntry(
        val session: UnifiedExecSession,
        val sessionRef: CodexSession,
        val turnRef: TurnContext,
        val callId: String,
        val processId: String,
        val command: List<String>,
        val cwd: String,
        val startedAt: Instant,
        var lastUsed: Instant
)

sealed class SessionStatus {
    data class Alive(val exitCode: Int?, val callId: String, val processId: String) :
            SessionStatus()

    data class Exited(val exitCode: Int?, val entry: SessionEntry) : SessionStatus()

    object Unknown : SessionStatus()
}

@OptIn(ExperimentalTime::class)
class UnifiedExecSessionManager {
    private val sessions = Mutex() // Guards HashMap<String, SessionEntry>
    private val sessionsMap = HashMap<String, SessionEntry>()

    private val usedSessionIds = Mutex() // Guards HashSet<String>
    private val usedSessionIdsSet = HashSet<String>()

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    suspend fun allocateProcessId(): String {
        while (true) {
            val store = usedSessionIds.withLock { usedSessionIdsSet.toSet() }

            val processId =
                    if (true) { // TODO: Check for test/deterministic mode
                        Random.nextInt(1_000, 100_000).toString()
                    } else {
                        val next =
                                store.mapNotNull { it.toIntOrNull() }.maxOrNull()?.let {
                                    maxOf(it, 999) + 1
                                }
                                        ?: 1000
                        next.toString()
                    }

            if (store.contains(processId)) {
                continue
            }

            usedSessionIds.withLock { usedSessionIdsSet.add(processId) }
            return processId
        }
    }

    suspend fun execCommand(
            request: ExecCommandRequest,
            context: UnifiedExecContext
    ): UnifiedExecResponse {
        val cwd = request.workdir ?: context.turn.cwd

        val session =
                openSessionWithSandbox(
                        request.command,
                        cwd,
                        request.withEscalatedPermissions,
                        request.justification,
                        context
                )

        val maxTokens = resolveMaxTokens(request.maxOutputTokens)
        val yieldTimeMs = clampYieldTime(request.yieldTimeMs)

        val start = Clock.System.now()
        val handles = session.outputHandles()
        val deadline = start + yieldTimeMs.milliseconds

        val collected =
                collectOutputUntilDeadline(
                        handles.outputState, // Pass wrapper
                        handles.outputNotify,
                        handles.cancellationToken,
                        deadline
                )

        val wallTime = Clock.System.now() - start

        val text = collected.toByteArray().decodeToString()
        val output = formattedTruncateText(text, TruncationPolicy.Tokens(maxTokens))
        val hasExited = session.hasExited()
        val exitCode = session.exitCode()
        val chunkId = generateChunkId()

        val processId =
                if (hasExited) {
                    null
                } else {
                    storeSession(session, context, request.command, cwd, start, request.processId)
                    request.processId
                }

        val originalTokenCount = TruncationPolicy.approxTokenCount(text)

        val response =
                UnifiedExecResponse(
                        eventCallId = context.callId,
                        chunkId = chunkId,
                        wallTime = wallTime,
                        output = output,
                        processId = processId,
                        exitCode = exitCode,
                        originalTokenCount = originalTokenCount,
                        sessionCommand = request.command
                )

        if (!hasExited) {
            emitWaitingStatus(context.session, context.turn, request.command)
        }

        if (hasExited) {
            val exit = response.exitCode ?: -1
            emitExecEndFromContext(
                    context,
                    request.command,
                    cwd,
                    response.output,
                    exit,
                    response.wallTime,
                    request.processId
            )
        }

        return response
    }

    suspend fun writeStdin(processId: String, data: String) {
        val entry =
                sessions.withLock { sessionsMap[processId] }
                        ?: throw UnifiedExecError.UnknownSessionId(processId)

        entry.lastUsed = Clock.System.now()
        entry.session.writeStdin(data)
    }

    suspend fun openSessionWithExecEnv(env: ExecEnv): UnifiedExecSession {
        val (program, args) =
                if (env.command.isNotEmpty()) {
                    env.command.first() to env.command.drop(1)
                } else {
                    throw UnifiedExecError.MissingCommandLine()
                }

        // Use PTY lib to spawn process
        val spawned =
                ai.solace.coder.utils.pty.spawnPtyProcess(program, args, env.cwd, env.env, scope)

        return UnifiedExecSession.fromSpawned(spawned, env.sandbox, scope).getOrThrow()
    }

    suspend fun openSessionWithSandbox(
            command: List<String>,
            cwd: String,
            withEscalatedPermissions: Boolean?,
            justification: String?,
            context: UnifiedExecContext
    ): UnifiedExecSession {
        if (command.isEmpty()) {
            throw UnifiedExecError.MissingCommandLine()
        }
        val (program, args) = command.first() to command.drop(1)

        val spec =
                CommandSpec(
                        program = program,
                        args = args,
                        cwd = cwd,
                        env = emptyMap(), // TODO: Inherit or config
                        expiration =
                                ai.solace.coder.core.ExecExpiration
                                        .DefaultTimeout, // Unified execs are usually
                        // interactive/long-running?
                        withEscalatedPermissions = withEscalatedPermissions,
                        justification = justification
                )

        // Default policy for unified exec (terminal) is usually permissive or standard
        val policy = ai.solace.coder.protocol.SandboxPolicy.DangerFullAccess
        val sandboxType = ai.solace.coder.core.platformGetSandbox() ?: SandboxType.None

        val manager = SandboxManager()
        val transformResult = manager.transform(spec, policy, sandboxType, cwd, null)

        if (transformResult.isFailure()) {
            throw UnifiedExecError.SandboxError(
                    transformResult.onFailure {}.getOrNull()?.toString()
                            ?: "Sandbox transform failed"
            )
        }

        val execEnv = transformResult.getOrThrow()
        return openSessionWithExecEnv(execEnv)
    }

    private suspend fun collectOutputUntilDeadline(
            outputState: OutputBufferStateWrapper,
            outputNotify: Notify,
            cancellationToken: Job,
            deadline: Instant
    ): List<Byte> {
        val postExitOutputGrace = 25.milliseconds
        val collected = ArrayList<Byte>()
        var exitSignalReceived = cancellationToken.isCancelled

        while (true) {
            val chunks = outputState.mutex.withLock { outputState.state.drain() }

            if (chunks.isEmpty()) {
                exitSignalReceived = exitSignalReceived || cancellationToken.isCancelled
                val remaining = deadline - Clock.System.now()
                if (remaining <= Duration.ZERO) {
                    break
                }

                if (exitSignalReceived) {
                    val grace =
                            if (remaining < postExitOutputGrace) remaining else postExitOutputGrace
                    withTimeoutOrNull(grace) { outputNotify.notified() } ?: break
                    continue
                }

                val waitResult =
                        withTimeoutOrNull<Unit>(remaining) {
                            coroutineScope {
                                val notifiedJob: Deferred<Unit> = async { outputNotify.notified() }
                                val joinedJob: Deferred<Unit> = async { cancellationToken.join() }
                                select<Unit> {
                                    notifiedJob.onAwait {}
                                    joinedJob.onAwait { exitSignalReceived = true }
                                }
                                        .also {
                                            notifiedJob.cancel()
                                            joinedJob.cancel()
                                        }
                            }
                        }
                if (waitResult == null) break
                continue
            }

            chunks.forEach { collected.addAll(it.toList()) }
        }

        return collected
    }

    private suspend fun storeSession(
            session: UnifiedExecSession,
            context: UnifiedExecContext,
            command: List<String>,
            cwd: String,
            startedAt: Instant,
            processId: String
    ) {
        sessions.withLock {
            pruneSessionsIfNeeded(sessionsMap)
            val entry =
                    SessionEntry(
                            session,
                            context.session,
                            context.turn,
                            context.callId,
                            processId,
                            command,
                            cwd,
                            startedAt,
                            startedAt
                    )
            sessionsMap[processId] = entry
        }
    }

    private fun pruneSessionsIfNeeded(sessions: MutableMap<String, SessionEntry>) {
        if (sessions.size < 32) { // MAX_UNIFIED_EXEC_SESSIONS = 32
            return
        }

        val meta =
                sessions.map { (id, entry) ->
                    Triple(id, entry.lastUsed, entry.session.hasExited())
                }

        val sessionId = sessionIdToPruneFromMeta(meta)
        if (sessionId != null) {
            sessions.remove(sessionId)
        }
    }

    private fun sessionIdToPruneFromMeta(meta: List<Triple<String, Instant, Boolean>>): String? {
        if (meta.isEmpty()) return null

        val byRecency = meta.sortedByDescending { it.second }
        val protected = byRecency.take(8).map { it.first }.toSet()

        // Try to find an exited one first that is not protected
        val toPrune =
                byRecency.find { it.third && !protected.contains(it.first) }
                        ?: byRecency.find { !protected.contains(it.first) }
                                ?: byRecency.lastOrNull()

        return toPrune?.first
    }

    private suspend fun emitWaitingStatus(
            session: CodexSession,
            turn: TurnContext,
            command: List<String>
    ) {
        // Emit event
    }

    private suspend fun emitExecEndFromContext(
            context: UnifiedExecContext,
            command: List<String>,
            cwd: String,
            aggregatedOutput: String,
            exitCode: Int,
            duration: Duration,
            processId: String?
    ) {
        // Emit event
    }
}
