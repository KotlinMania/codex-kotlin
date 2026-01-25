// port-lint: source core/src/codex.rs
package ai.solace.coder.core.session

import ai.solace.coder.client.auth.AuthManager
import ai.solace.coder.client.auth.AuthMode
import ai.solace.coder.core.context.ContextManager
import ai.solace.coder.core.context.TruncationPolicy
import ai.solace.coder.core.context.truncateText
import ai.solace.coder.core.error.CodexError
import ai.solace.coder.core.error.CodexResult
import ai.solace.coder.core.features.Feature
import ai.solace.coder.core.features.Features
import ai.solace.coder.core.model.ModelFamily
import ai.solace.coder.core.model.ModelProviderInfo
import ai.solace.coder.core.model.findFamilyForModel
import ai.solace.coder.core.tools.ProcessedResponseItem
import ai.solace.coder.core.tools.ToolCall
import ai.solace.coder.core.tools.ToolCallProcessor
import ai.solace.coder.core.tools.ToolCallRuntime
import ai.solace.coder.core.tools.ToolRegistry
import ai.solace.coder.core.tools.ToolRouter
import ai.solace.coder.core.tools.ApplyPatchToolType
import ai.solace.coder.exec.sandbox.ApprovalStore
import ai.solace.coder.protocol.SandboxCommandAssessment
import ai.solace.coder.protocol.SandboxRiskLevel
import ai.solace.coder.exec.shell.Shell
import ai.solace.coder.exec.shell.ShellDetector
import ai.solace.coder.mcp.connection.McpConnectionManager
import ai.solace.coder.mcp.connection.McpServerConfig
import ai.solace.coder.protocol.ResponseEvent
import ai.solace.coder.utils.concurrent.CancellationToken
import ai.solace.coder.protocol.AskForApproval
import ai.solace.coder.protocol.ApplyPatchApprovalRequestEvent
import ai.solace.coder.protocol.BackgroundEventEvent
import ai.solace.coder.protocol.Event
import ai.solace.coder.protocol.EventMsg
import ai.solace.coder.protocol.ExecApprovalRequestEvent
import ai.solace.coder.protocol.ItemCompletedEvent
import ai.solace.coder.protocol.ItemStartedEvent
import ai.solace.coder.protocol.Op
import ai.solace.coder.protocol.RawResponseItemEvent
import ai.solace.coder.protocol.ReviewDecision
import ai.solace.coder.protocol.SandboxPolicy
import ai.solace.coder.protocol.StreamErrorEvent
import ai.solace.coder.protocol.Submission
import ai.solace.coder.protocol.TaskCompleteEvent
import ai.solace.coder.protocol.TaskStartedEvent
import ai.solace.coder.protocol.TokenCountEvent
import ai.solace.coder.protocol.TurnAbortReason
import ai.solace.coder.protocol.TurnAbortedEvent
import ai.solace.coder.protocol.TurnItem
import ai.solace.coder.protocol.UndoCompletedEvent
import ai.solace.coder.protocol.UndoStartedEvent
import ai.solace.coder.protocol.CodexErrorInfo
import ai.solace.coder.protocol.ErrorEvent
import ai.solace.coder.protocol.GetHistoryEntryResponseEvent
import ai.solace.coder.protocol.McpListToolsResponseEvent
import ai.solace.coder.protocol.ListCustomPromptsResponseEvent
import ai.solace.coder.protocol.SessionConfiguredEvent
import ai.solace.coder.protocol.McpTool
import ai.solace.coder.protocol.McpResource
import ai.solace.coder.protocol.McpResourceTemplate
import ai.solace.coder.protocol.McpAuthStatus
import ai.solace.coder.protocol.CustomPrompt
import ai.solace.coder.protocol.ReviewRequest
import ai.solace.coder.protocol.ElicitationAction
import ai.solace.coder.protocol.TokenUsage
import ai.solace.coder.protocol.TokenUsageInfo
import ai.solace.coder.protocol.RateLimitSnapshot
import ai.solace.coder.protocol.HistoryEntry
import ai.solace.coder.protocol.FileChange
import ai.solace.coder.protocol.ReasoningEffort
import ai.solace.coder.protocol.ReasoningEffortConfig
import ai.solace.coder.protocol.ReasoningSummary
import ai.solace.coder.protocol.ReasoningSummaryConfig
import ai.solace.coder.protocol.TurnContextItem
import ai.solace.coder.protocol.TurnDiffEvent
import ai.solace.coder.protocol.AgentMessageContentDeltaEvent
import ai.solace.coder.protocol.ReasoningContentDeltaEvent
import ai.solace.coder.protocol.ReasoningRawContentDeltaEvent
import ai.solace.coder.protocol.AgentReasoningSectionBreakEvent
import ai.solace.coder.protocol.RolloutItem
import ai.solace.coder.protocol.UserMessageItem
import ai.solace.coder.protocol.ReasoningItem
import ai.solace.coder.protocol.ReasoningItemReasoningSummary
import ai.solace.coder.protocol.ReasoningItemContent
import ai.solace.coder.protocol.WebSearchItem
import ai.solace.coder.protocol.FunctionCallOutputPayload
import ai.solace.coder.protocol.InitialHistory
import ai.solace.coder.protocol.SessionSource
import ai.solace.coder.protocol.UserInput as ProtocolUserInput
import ai.solace.coder.protocol.ContentItem
import ai.solace.coder.protocol.ResponseInputItem
import ai.solace.coder.protocol.ResponseItem
import ai.solace.coder.protocol.WarningEvent
import ai.solace.coder.protocol.ContextCompactedEvent
import ai.solace.coder.protocol.ExitedReviewModeEvent
import ai.solace.coder.protocol.ReviewOutputEvent
import ai.solace.coder.protocol.ReviewFinding
import ai.solace.coder.protocol.ReviewCodeLocation
import ai.solace.coder.protocol.ReviewLineRange
import ai.solace.coder.protocol.ExecCommandBeginEvent
import ai.solace.coder.protocol.ExecCommandEndEvent
import ai.solace.coder.protocol.ExecCommandSource
import ai.solace.coder.protocol.ParsedCommand
import ai.solace.coder.core.exec.ExecParams
import ai.solace.coder.core.exec.ExecExpiration
import ai.solace.coder.utils.git.CreateGhostCommitOptions
import kotlin.time.Duration
import kotlin.time.measureTime
import ai.solace.coder.utils.git.GhostSnapshotReport
import ai.solace.coder.utils.git.GitToolingError
import ai.solace.coder.utils.git.ShellGitOperations
import ai.solace.coder.utils.readiness.ReadinessFlag
import ai.solace.coder.utils.readiness.ReadinessToken
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.json.JsonElement
import kotlin.concurrent.atomics.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

/**
 * Timeout for graceful task interruption before forcing abort.
 * Ported from Rust codex-rs/core/src/tasks/mod.rs GRACEFULL_INTERRUPTION_TIMEOUT_MS
 */
private val GRACEFUL_INTERRUPTION_TIMEOUT = 100.milliseconds

/**
 * Timeout for user shell commands (1 hour).
 * Ported from Rust codex-rs/core/src/tasks/user_shell.rs USER_SHELL_TIMEOUT_MS
 */
private val USER_SHELL_TIMEOUT = 60.minutes

/**
 * Maximum tokens for compaction user messages.
 * Ported from Rust codex-rs/core/src/compact.rs COMPACT_USER_MESSAGE_MAX_TOKENS
 */
private const val COMPACT_USER_MESSAGE_MAX_TOKENS = 20_000

/**
 * Prompt template for context compaction.
 * Ported from Rust codex-rs/core/templates/compact/prompt.md
 */
private const val SUMMARIZATION_PROMPT = """You are performing a CONTEXT CHECKPOINT COMPACTION. Create a handoff summary for another LLM that will resume the task.

Include:
- Current progress and key decisions made
- Important context, constraints, or user preferences
- What remains to be done (clear next steps)
- Any critical data, examples, or references needed to continue

Be concise, structured, and focused on helping the next LLM seamlessly continue the work.
"""

/**
 * Prefix for summary messages (used to identify compacted content).
 * Ported from Rust codex-rs/core/templates/compact/summary_prefix.md
 */
private const val SUMMARY_PREFIX = """Another language model started to solve this problem and produced a summary of its thinking process. You also have access to the state of the tools that were used by that language model. Use this to build on the work that has already been done and avoid duplicating work. Here is the summary produced by the other language model, use the information in this summary to assist with your own analysis:"""

/**
 * The high-level interface to the Codex system.
 * It operates as a queue pair where you send submissions and receive events.
 *
 * Ported from Rust codex-rs/core/src/codex.rs Codex struct
 */
@OptIn(ExperimentalAtomicApi::class)
class Codex internal constructor(
    private val nextId: AtomicLong,
    private val txSub: Channel<Submission>,
    private val rxEvent: Channel<Event>
) {
    /**
     * Submit the `op` wrapped in a `Submission` with a unique ID.
     */
    suspend fun submit(op: Op): CodexResult<String> {
        val id = nextId.fetchAndAdd(1L).toString()
        val sub = Submission(id = id, op = op)
        return submitWithId(sub).map { id }
    }

    /**
     * Use sparingly: prefer `submit()` so Codex is responsible for generating
     * unique IDs for each submission.
     */
    suspend fun submitWithId(sub: Submission): CodexResult<Unit> {
        return try {
            txSub.send(sub)
            CodexResult.success(Unit)
        } catch (e: Exception) {
            CodexResult.failure(CodexError.InternalAgentDied)
        }
    }

    /**
     * Receive the next event from the agent.
     */
    suspend fun nextEvent(): CodexResult<Event> {
        return try {
            val event = rxEvent.receive()
            CodexResult.success(event)
        } catch (e: Exception) {
            CodexResult.failure(CodexError.InternalAgentDied)
        }
    }

    /**
     * Get a Flow of events for reactive consumption.
     */
    fun eventFlow(): Flow<Event> = rxEvent.receiveAsFlow()

    companion object {
        const val INITIAL_SUBMIT_ID = ""
        const val SUBMISSION_CHANNEL_CAPACITY = 64
    }
}

/**
 * Wrapper returned by [Codex.spawn] containing the spawned [Codex],
 * the submission id for the initial `ConfigureSession` request and the
 * unique session id.
 *
 * Ported from Rust codex-rs/core/src/codex.rs CodexSpawnOk
 */
data class CodexSpawnOk(
    val codex: Codex,
    val conversationId: String
)

/**
 * Spawn a new [Codex] and initialize the session.
 *
 * Ported from Rust codex-rs/core/src/codex.rs Codex::spawn
 */
@OptIn(ExperimentalAtomicApi::class)
suspend fun spawnCodex(
    config: Config,
    authManager: AuthManager,
    conversationHistory: InitialHistory = InitialHistory.New,
    sessionSource: SessionSource = SessionSource.Cli
): CodexResult<CodexSpawnOk> {
    val txSub = Channel<Submission>(Codex.SUBMISSION_CHANNEL_CAPACITY)
    val txEvent = Channel<Event>(Channel.UNLIMITED)
    val rxEvent = Channel<Event>(Channel.UNLIMITED)

    val userInstructions = getUserInstructions(config)

    val execPolicy = execPolicyFor(config.features, config.codexHome)

    val sessionConfiguration = SessionConfiguration(
        provider = config.modelProvider,
        model = config.model,
        modelReasoningEffort = config.modelReasoningEffort,
        modelReasoningSummary = config.modelReasoningSummary,
        developerInstructions = config.developerInstructions,
        userInstructions = userInstructions,
        baseInstructions = config.baseInstructions,
        compactPrompt = config.compactPrompt,
        approvalPolicy = config.approvalPolicy,
        sandboxPolicy = config.sandboxPolicy,
        cwd = config.cwd,
        features = config.features,
        execPolicy = execPolicy,
        sessionSource = sessionSource
    )

    val session = Session.new(
        sessionConfiguration = sessionConfiguration,
        config = config,
        authManager = authManager,
        txEvent = txEvent,
        initialHistory = conversationHistory,
        sessionSource = sessionSource
    )

    if (session == null) {
        return CodexResult.failure(CodexError.InternalAgentDied)
    }

    val conversationId = session.conversationId

    // Spawn the submission loop
    val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    scope.launch {
        submissionLoop(session, config, txSub)
    }

    // Forward events from session to output channel
    scope.launch {
        for (event in txEvent) {
            rxEvent.send(event)
        }
    }

    val codex = Codex(
        nextId = AtomicLong(0L),
        txSub = txSub,
        rxEvent = rxEvent
    )

    return CodexResult.success(CodexSpawnOk(
        codex = codex,
        conversationId = conversationId
    ))
}

// =============================================================================
// Session
// =============================================================================

/**
 * Context for an initialized model agent.
 *
 * A session has at most 1 running task at a time, and can be interrupted by user input.
 *
 * Ported from Rust codex-rs/core/src/codex.rs Session
 */
@OptIn(ExperimentalAtomicApi::class)
class Session private constructor(
    val conversationId: String,
    private val txEvent: Channel<Event>,
    private val state: SessionState,
    private val stateMutex: Mutex,
    val activeTurn: ActiveTurnHolder,
    val services: SessionServices,
    private val nextInternalSubId: AtomicLong
) {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    /**
     * Get the event sender channel.
     */
    fun getTxEvent(): Channel<Event> = txEvent

    /**
     * Ensure all rollout writes are durably flushed.
     */
    suspend fun flushRollout() {
        services.rollout?.flush()
    }

    /**
     * Generate the next internal submission ID.
     */
    @OptIn(ExperimentalAtomicApi::class)
    fun nextInternalSubId(): String {
        val id = nextInternalSubId.fetchAndAdd(1L)
        return "auto-compact-$id"
    }

    /**
     * Get total token usage.
     */
    suspend fun getTotalTokenUsage(): Long {
        return stateMutex.withLock {
            state.getTotalTokenUsage()
        }
    }

    /**
     * Record initial conversation history.
     */
    suspend fun recordInitialHistory(conversationHistory: InitialHistory) {
        val turnContext = newTurn(SessionSettingsUpdate())
        when (conversationHistory) {
            is InitialHistory.New -> {
                val items = buildInitialContext(turnContext)
                recordConversationItems(turnContext, items)
                flushRollout()
            }
            is InitialHistory.Resumed -> {
                val rolloutItems = conversationHistory.payload.history
                val reconstructed = reconstructHistoryFromRollout(turnContext, rolloutItems)
                if (reconstructed.isNotEmpty()) {
                    recordIntoHistory(reconstructed, turnContext)
                }
                flushRollout()
            }
            is InitialHistory.Forked -> {
                val rolloutItems = conversationHistory.items
                val reconstructed = reconstructHistoryFromRollout(turnContext, rolloutItems)
                if (reconstructed.isNotEmpty()) {
                    recordIntoHistory(reconstructed, turnContext)
                }
                persistRolloutItems(rolloutItems)
                flushRollout()
            }
        }
    }

    /**
     * Update session settings.
     */
    suspend fun updateSettings(updates: SessionSettingsUpdate) {
        stateMutex.withLock {
            state.sessionConfiguration = state.sessionConfiguration.apply(updates)
        }
    }

    /**
     * Create a new turn context.
     */
    suspend fun newTurn(updates: SessionSettingsUpdate): TurnContext {
        val subId = nextInternalSubId()
        return newTurnWithSubId(subId, updates)
    }

    /**
     * Create a new turn context with a specific submission ID.
     */
    suspend fun newTurnWithSubId(subId: String, updates: SessionSettingsUpdate): TurnContext {
        val sessionConfiguration = stateMutex.withLock {
            val config = state.sessionConfiguration.apply(updates)
            state.sessionConfiguration = config
            config
        }

        return makeTurnContext(
            authManager = services.authManager,
            sessionConfiguration = sessionConfiguration,
            conversationId = conversationId,
            subId = subId,
            finalOutputJsonSchema = updates.finalOutputJsonSchema
        )
    }

    /**
     * Build an environment update item if the context changed.
     */
    fun buildEnvironmentUpdateItem(
        previous: TurnContext?,
        next: TurnContext
    ): ResponseItem? {
        if (previous == null) return null

        val prevContext = EnvironmentContext.from(previous)
        val nextContext = EnvironmentContext.from(next)
        if (prevContext.equalsExceptShell(nextContext)) {
            return null
        }
        return EnvironmentContext.diff(previous, next).toResponseItem()
    }

    /**
     * Persist the event to rollout and send it to clients.
     */
    suspend fun sendEvent(turnContext: TurnContext, msg: EventMsg) {
        val event = Event(
            id = turnContext.subId,
            msg = msg
        )
        sendEventRaw(event)

        // Send legacy events if applicable
        // Note: EventMsg doesn't implement asLegacyEvents in this port yet
        // TODO: Implement legacy event conversion when needed
    }

    /**
     * Send a raw event without legacy conversion.
     */
    suspend fun sendEventRaw(event: Event) {
        val rolloutItems = listOf(RolloutItem.EventMsgItem(event.msg))
        persistRolloutItems(rolloutItems)
        try {
            txEvent.send(event)
        } catch (e: Exception) {
            println("ERROR: failed to send event: ${e.message}")
        }
    }

    /**
     * Emit a turn item started event.
     */
    suspend fun emitTurnItemStarted(turnContext: TurnContext, item: TurnItem) {
        sendEvent(
            turnContext,
            EventMsg.ItemStarted(ItemStartedEvent(
                threadId = ai.solace.coder.protocol.ConversationId.fromString(conversationId).getOrThrow(),
                turnId = turnContext.subId,
                item = item
            ))
        )
    }

    /**
     * Emit a turn item completed event.
     */
    suspend fun emitTurnItemCompleted(turnContext: TurnContext, item: TurnItem) {
        sendEvent(
            turnContext,
            EventMsg.ItemCompleted(ItemCompletedEvent(
                threadId = ai.solace.coder.protocol.ConversationId.fromString(conversationId).getOrThrow(),
                turnId = turnContext.subId,
                item = item
            ))
        )
    }

    /**
     * Assess a sandbox command for safety.
     */
    suspend fun assessSandboxCommand(
        turnContext: TurnContext,
        callId: String,
        command: List<String>,
        failureMessage: String?
    ): SandboxCommandAssessment? {
        // TODO: Implement command assessment
        return null
    }

    /**
     * Emit an exec approval request event and await the user's decision.
     */
    suspend fun requestCommandApproval(
        turnContext: TurnContext,
        callId: String,
        command: List<String>,
        cwd: String,
        reason: String?,
        risk: SandboxCommandAssessment?
    ): ReviewDecision {
        val subId = turnContext.subId
        val deferred = CompletableDeferred<ReviewDecision>()

        val prevEntry = activeTurn.withLock { turn ->
            turn?.turnState?.insertPendingApproval(subId, deferred)
        }
        if (prevEntry != null) {
            println("WARN: Overwriting existing pending approval for sub_id: $subId")
        }

        val event = EventMsg.ExecApprovalRequest(ExecApprovalRequestEvent(
            callId = callId,
            turnId = turnContext.subId,
            command = command,
            cwd = cwd,
            reason = reason,
            risk = risk,
            parsedCmd = emptyList()
        ))
        sendEvent(turnContext, event)

        return try {
            deferred.await()
        } catch (e: Exception) {
            ReviewDecision.Denied
        }
    }

    /**
     * Emit a patch approval request event and await the user's decision.
     */
    suspend fun requestPatchApproval(
        turnContext: TurnContext,
        callId: String,
        changes: Map<String, ai.solace.coder.protocol.FileChange>,
        reason: String?,
        grantRoot: String?
    ): CompletableDeferred<ReviewDecision> {
        val subId = turnContext.subId
        val deferred = CompletableDeferred<ReviewDecision>()

        val prevEntry = activeTurn.withLock { turn ->
            turn?.turnState?.insertPendingApproval(subId, deferred)
        }
        if (prevEntry != null) {
            println("WARN: Overwriting existing pending approval for sub_id: $subId")
        }

        val event = EventMsg.ApplyPatchApprovalRequest(ApplyPatchApprovalRequestEvent(
            callId = callId,
            turnId = turnContext.subId,
            changes = changes,
            reason = reason,
            grantRoot = grantRoot
        ))
        sendEvent(turnContext, event)

        return deferred
    }

    /**
     * Notify approval for a pending request.
     */
    suspend fun notifyApproval(subId: String, decision: ReviewDecision) {
        val entry = activeTurn.withLock { turn ->
            turn?.turnState?.removePendingApproval(subId)
        }
        if (entry != null) {
            entry.complete(decision)
        } else {
            println("WARN: No pending approval found for sub_id: $subId")
        }
    }

    /**
     * Records input items: always append to conversation history and
     * persist these response items to rollout.
     */
    suspend fun recordConversationItems(turnContext: TurnContext, items: List<ResponseItem>) {
        recordIntoHistory(items, turnContext)
        persistRolloutResponseItems(items)
        sendRawResponseItems(turnContext, items)
    }

    /**
     * Reconstruct history from rollout items.
     */
    private fun reconstructHistoryFromRollout(
        turnContext: TurnContext,
        rolloutItems: List<ai.solace.coder.protocol.RolloutItem>
    ): List<ResponseItem> {
        val history = ContextManager()
        for (rolloutItem in rolloutItems) {
            when (rolloutItem) {
                is ai.solace.coder.protocol.RolloutItem.ResponseItemHolder -> {
                    history.recordItems(listOf(rolloutItem.payload), turnContext.truncationPolicy)
                }
                is ai.solace.coder.protocol.RolloutItem.Compacted -> {
                    val compacted = rolloutItem.payload
                    if (compacted.replacementHistory != null) {
                        history.replace(compacted.replacementHistory)
                    } else {
                        val snapshot = history.getHistory()
                        val userMessages = collectUserMessages(snapshot)
                        val rebuilt = buildCompactedHistory(
                            buildInitialContext(turnContext),
                            userMessages,
                            compacted.message
                        )
                        history.replace(rebuilt)
                    }
                }
                else -> {}
            }
        }
        return history.getHistory()
    }

    /**
     * Append ResponseItems to the in-memory conversation history only.
     */
    suspend fun recordIntoHistory(items: List<ResponseItem>, turnContext: TurnContext) {
        stateMutex.withLock {
            state.recordItems(items, turnContext.truncationPolicy)
        }
    }

    /**
     * Replace the entire conversation history.
     */
    suspend fun replaceHistory(items: List<ResponseItem>) {
        stateMutex.withLock {
            state.replaceHistory(items)
        }
    }

    /**
     * Persist response items to rollout.
     */
    private suspend fun persistRolloutResponseItems(items: List<ResponseItem>) {
        val rolloutItems = items.map { RolloutItem.ResponseItemHolder(payload = it) }
        persistRolloutItems(rolloutItems)
    }

    /**
     * Check if a feature is enabled.
     */
    suspend fun enabled(feature: Feature): Boolean {
        return stateMutex.withLock {
            state.sessionConfiguration.features.enabled(feature)
        }
    }

    /**
     * Send raw response items as events.
     */
    private suspend fun sendRawResponseItems(turnContext: TurnContext, items: List<ResponseItem>) {
        for (item in items) {
            sendEvent(
                turnContext,
                EventMsg.RawResponseItem(RawResponseItemEvent(item = item))
            )
        }
    }

    /**
     * Build initial context items.
     */
    fun buildInitialContext(turnContext: TurnContext): List<ResponseItem> {
        val items = mutableListOf<ResponseItem>()

        turnContext.developerInstructions?.let { instructions ->
            items.add(DeveloperInstructions(instructions).toResponseItem())
        }

        turnContext.userInstructions?.let { instructions ->
            items.add(UserInstructions(
                text = instructions,
                directory = turnContext.cwd
            ).toResponseItem())
        }

        items.add(EnvironmentContext(
            cwd = turnContext.cwd,
            approvalPolicy = turnContext.approvalPolicy,
            sandboxPolicy = turnContext.sandboxPolicy,
            shell = userShell()
        ).toResponseItem())

        return items
    }

    /**
     * Persist rollout items.
     */
    suspend fun persistRolloutItems(items: List<ai.solace.coder.protocol.RolloutItem>) {
        services.rollout?.recordItems(items)
    }

    /**
     * Clone the current conversation history.
     */
    suspend fun cloneHistory(): ContextManager {
        return stateMutex.withLock {
            state.cloneHistory()
        }
    }

    /**
     * Update token usage info.
     */
    suspend fun updateTokenUsageInfo(turnContext: TurnContext, tokenUsage: TokenUsage?) {
        stateMutex.withLock {
            if (tokenUsage != null) {
                state.updateTokenInfoFromUsage(tokenUsage, turnContext.modelContextWindow)
            }
        }
        sendTokenCountEvent(turnContext)
    }

    /**
     * Recompute token usage from history.
     */
    suspend fun recomputeTokenUsage(turnContext: TurnContext) {
        // TODO: Implement token estimation
        // val estimatedTotalTokens = cloneHistory().estimateTokenCount(turnContext) ?: return
        val estimatedTotalTokens = 0L

        stateMutex.withLock {
            val existingInfo = state.tokenInfo
            val newLastUsage = TokenUsage(
                inputTokens = 0,
                cachedInputTokens = 0,
                outputTokens = 0,
                reasoningOutputTokens = 0,
                totalTokens = maxOf(estimatedTotalTokens, 0)
            )

            state.tokenInfo = if (existingInfo != null) {
                existingInfo.copy(
                    lastTokenUsage = newLastUsage,
                    modelContextWindow = existingInfo.modelContextWindow ?: turnContext.modelContextWindow
                )
            } else {
                TokenUsageInfo(
                    totalTokenUsage = TokenUsage(),
                    lastTokenUsage = newLastUsage,
                    modelContextWindow = turnContext.modelContextWindow
                )
            }
        }
        sendTokenCountEvent(turnContext)
    }

    /**
     * Update rate limits.
     */
    suspend fun updateRateLimits(turnContext: TurnContext, newRateLimits: RateLimitSnapshot) {
        stateMutex.withLock {
            state.rateLimits = newRateLimits
        }
        sendEvent(
            turnContext,
            EventMsg.ResponseRateLimit(ResponseEvent.RateLimits(newRateLimits))
        )
    }

    /**
     * List resources from MCP server.
     */
    suspend fun listResources(server: String, params: ai.solace.coder.protocol.ListResourcesRequestParams?): ai.solace.coder.protocol.ListResourcesResult {
        return services.mcpConnectionManager.listResources(server, params)
    }

    /**
     * List resource templates from MCP server.
     */
    suspend fun listResourceTemplates(server: String, params: ai.solace.coder.protocol.ListResourceTemplatesRequestParams?): ai.solace.coder.protocol.ListResourceTemplatesResult {
        return services.mcpConnectionManager.listResourceTemplates(server, params)
    }

    /**
     * Read resource from MCP server.
     */
    suspend fun readResource(server: String, params: ai.solace.coder.protocol.ReadResourceRequestParams): ai.solace.coder.protocol.ReadResourceResult {
        return services.mcpConnectionManager.readResource(server, params)
    }

    /**
     * Send token count event.
     */
    private suspend fun sendTokenCountEvent(turnContext: TurnContext) {
        val (info, rateLimits) = stateMutex.withLock {
            Pair(state.tokenInfo, state.rateLimits)
        }
        val event = EventMsg.TokenCount(TokenCountEvent(
            info = info,
            rateLimits = rateLimits
        ))
        sendEvent(turnContext, event)
    }

    /**
     * Set total tokens to full (context window exceeded).
     */
    suspend fun setTotalTokensFull(turnContext: TurnContext) {
        val contextWindow = turnContext.modelContextWindow ?: return
        stateMutex.withLock {
            state.tokenInfo = TokenUsageInfo.fullContextWindow(contextWindow)
        }
        sendTokenCountEvent(turnContext)
    }

    /**
     * Record a user input item to conversation history and also persist a
     * corresponding UserMessage EventMsg to rollout.
     */
    suspend fun recordInputAndRolloutUsermsg(
        turnContext: TurnContext,
        responseInput: ResponseInputItem
    ) {
        val responseItem = responseInput.toResponseItem()
        recordConversationItems(turnContext, listOf(responseItem))

        // Create a TurnItem.UserMessage for the user message
        val userMessageItem = UserMessageItem.new(emptyList())
        val turnItem = TurnItem.UserMessage(item = userMessageItem)
        emitTurnItemStarted(turnContext, turnItem)
        emitTurnItemCompleted(turnContext, turnItem)
    }

    /**
     * Notify about a background event.
     */
    suspend fun notifyBackgroundEvent(turnContext: TurnContext, message: String) {
        val event = EventMsg.BackgroundEvent(BackgroundEventEvent(message = message))
        sendEvent(turnContext, event)
    }

    /**
     * Notify about a stream error.
     */
    suspend fun notifyStreamError(
        turnContext: TurnContext,
        message: String,
        codexError: CodexError
    ) {
        val codexErrorInfo = CodexErrorInfo.ResponseStreamDisconnected(
            httpStatusCode = codexError.httpStatusCodeValue()
        )
        val event = EventMsg.StreamError(StreamErrorEvent(
            message = message,
            codexErrorInfo = codexErrorInfo
        ))
        sendEvent(turnContext, event)
    }

    /**
     * Maybe start a ghost snapshot task.
     */
    suspend fun maybeStartGhostSnapshot(
        turnContext: TurnContext,
        cancellationToken: CancellationToken
    ) {
        if (!enabled(Feature.GhostCommit)) {
            return
        }

        val readinessFlag = ReadinessFlag.new()
        val tokenResult = readinessFlag.subscribe()

        tokenResult.fold(
            onSuccess = { token ->
                println("INFO: spawning ghost snapshot task")
                val task = GhostSnapshotTask(token, readinessFlag)
                task.run(
                    SessionTaskContext(this),
                    turnContext,
                    emptyList(),
                    cancellationToken
                )
            },
            onFailure = { error ->
                println("WARN: failed to subscribe to ghost snapshot readiness: $error")
            }
        )
    }

    /**
     * Returns the input if there was no task running to inject into.
     */
    suspend fun injectInput(input: List<UserInput>): Result<Unit> {
        return activeTurn.withLock { turn ->
            if (turn != null) {
                for (item in input) {
                    turn.turnState.pushPendingInput(item.toResponseInputItem())
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("No active turn"))
            }
        }
    }

    /**
     * Get pending input from the active turn.
     */
    suspend fun getPendingInput(): List<ResponseInputItem> {
        return activeTurn.withLock { turn ->
            turn?.turnState?.takePendingInput() ?: emptyList()
        }
    }

    /**
     * Interrupt the current task.
     */
    suspend fun interruptTask() {
        println("INFO: interrupt received: abort current task, if any")
        val hasActiveTurn = activeTurn.withLock { it != null }
        if (hasActiveTurn) {
            abortAllTasks(TurnAbortReason.Interrupted)
        } else {
            cancelMcpStartup()
        }
    }

    /**
     * Get the user notification service.
     */
    fun notifier(): UserNotifier = services.notifier

    /**
     * Get the user's shell.
     */
    fun userShell(): Shell = services.userShell

    /**
     * Check if raw agent reasoning should be shown.
     */
    fun showRawAgentReasoning(): Boolean = services.showRawAgentReasoning

    /**
     * Cancel MCP startup.
     */
    private suspend fun cancelMcpStartup() {
        services.mcpStartupCancellationToken.cancel()
    }

    /**
     * Spawn a new task.
     *
     * Ported from Rust codex-rs/core/src/tasks/mod.rs Session::spawn_task
     */
    suspend fun spawnTask(
        turnContext: TurnContext,
        input: List<UserInput>,
        task: SessionTask
    ) {
        // Abort any existing tasks first (Rust: self.abort_all_tasks(Replaced).await)
        abortAllTasks(TurnAbortReason.Replaced)

        val cancellationToken = CancellationToken()
        val done = CompletableDeferred<Unit>()
        val runningTask = RunningTask(
            done = done,
            kind = task.kind(),
            task = task,
            cancellationToken = cancellationToken,
            turnContext = turnContext
        )

        // Register the task (Rust: self.register_new_active_task(running_task).await)
        registerNewActiveTask(runningTask)

        // Launch the task
        scope.launch {
            val sessionContext = SessionTaskContext(this@Session)
            try {
                val lastAgentMessage = task.run(
                    sessionContext,
                    turnContext,
                    input,
                    cancellationToken
                )

                // Signal task completion
                done.complete(Unit)

                // Flush rollout after task completes (Rust: session_ctx.clone_session().flush_rollout().await)
                flushRollout()

                // Only emit TaskComplete if not cancelled (Rust: if !task_cancellation_token.is_cancelled())
                if (!cancellationToken.isCancelled()) {
                    onTaskFinished(turnContext, lastAgentMessage)
                }
            } catch (e: Exception) {
                done.complete(Unit)
                // Log but don't propagate - task failures are handled via events
                println("WARN: Task ${task.kind()} failed: ${e.message}")
            }
        }
    }

    /**
     * Abort all running tasks.
     *
     * Ported from Rust codex-rs/core/src/tasks/mod.rs Session::abort_all_tasks
     */
    suspend fun abortAllTasks(reason: TurnAbortReason) {
        for (task in takeAllRunningTasks()) {
            handleTaskAbort(task, reason)
        }
    }

    /**
     * Register a new active task, creating a new ActiveTurn.
     *
     * Ported from Rust codex-rs/core/src/tasks/mod.rs Session::register_new_active_task
     */
    private suspend fun registerNewActiveTask(task: RunningTask) {
        activeTurn.withLock {
            val turn = ActiveTurn()
            turn.addTask(task)
            turn
        }
    }

    /**
     * Take all running tasks, clearing the active turn.
     *
     * Ported from Rust codex-rs/core/src/tasks/mod.rs Session::take_all_running_tasks
     */
    private suspend fun takeAllRunningTasks(): List<RunningTask> {
        val currentTurn = activeTurn.get()
        activeTurn.set(null)

        return currentTurn?.let { turn ->
            turn.clearPending()
            turn.drainTasks()
        } ?: emptyList()
    }

    /**
     * Handle graceful abort of a single task.
     *
     * Ported from Rust codex-rs/core/src/tasks/mod.rs handle_task_abort
     */
    private suspend fun handleTaskAbort(
        runningTask: RunningTask,
        reason: TurnAbortReason
    ) {
        val subId = runningTask.turnContext.subId

        // Early return if already cancelled (Rust: if task.cancellation_token.is_cancelled())
        if (runningTask.cancellationToken.isCancelled()) {
            return
        }

        // Signal cancellation
        runningTask.cancellationToken.cancel()

        // Wait for graceful completion with timeout (Rust: select! with sleep)
        val completedGracefully = withTimeoutOrNull(GRACEFUL_INTERRUPTION_TIMEOUT) {
            runningTask.done.await()
            true
        } ?: false

        if (!completedGracefully) {
            println("WARN: task $subId didn't complete gracefully after ${GRACEFUL_INTERRUPTION_TIMEOUT.inWholeMilliseconds}ms")
        }

        // Call the task's abort hook for cleanup (Rust: session_task.abort(session_ctx, ...).await)
        val sessionContext = SessionTaskContext(this@Session)
        try {
            runningTask.task.abort(sessionContext, runningTask.turnContext)
        } catch (e: Exception) {
            println("WARN: task $subId abort hook failed: ${e.message}")
        }

        // Emit turn aborted event per-task (Rust: self.send_event(task.turn_context.as_ref(), event).await)
        sendEvent(
            runningTask.turnContext,
            EventMsg.TurnAborted(TurnAbortedEvent(reason = reason))
        )
    }

    /**
     * Called when a task finishes.
     */
    private suspend fun onTaskFinished(turnContext: TurnContext, lastAgentMessage: String?) {
        activeTurn.withLock { turn ->
            turn?.removeTask(turnContext.subId)
        }

        // Emit task complete event
        sendEvent(
            turnContext,
            EventMsg.TaskComplete(TaskCompleteEvent(
                lastAgentMessage = lastAgentMessage
            ))
        )
    }

    /**
     * Parse an MCP tool name into server and tool parts.
     */
    suspend fun parseMcpToolName(toolName: String): Pair<String, String>? {
        return services.mcpConnectionManager.parseToolName(toolName)
    }

    /**
     * Call an MCP tool.
     */
    suspend fun callTool(
        server: String,
        tool: String,
        arguments: JsonElement?
    ): Result<ai.solace.coder.protocol.CallToolResult> {
        return services.mcpConnectionManager.callTool(server, tool, arguments)
    }

    companion object {
        /**
         * Create a new session.
         */
        @OptIn(ExperimentalAtomicApi::class)
        suspend fun new(
            sessionConfiguration: SessionConfiguration,
            config: Config,
            authManager: AuthManager,
            txEvent: Channel<Event>,
            initialHistory: InitialHistory,
            sessionSource: SessionSource
        ): Session? {
            println("DEBUG: Configuring session: model=${sessionConfiguration.model}; provider=${sessionConfiguration.provider}")

            if (!isAbsolutePath(sessionConfiguration.cwd)) {
                println("ERROR: cwd is not absolute: ${sessionConfiguration.cwd}")
                return null
            }

            val conversationId: String = when (initialHistory) {
                is InitialHistory.New, is InitialHistory.Forked -> generateConversationId()
                is InitialHistory.Resumed -> initialHistory.payload.conversationId.toString()
            }

            // Initialize services
            val rolloutRecorder = RolloutRecorder.new(config, conversationId)
            val defaultShell = ShellDetector().defaultUserShell()

            val state = SessionState(sessionConfiguration)

            val services = SessionServices(
                mcpConnectionManager = McpConnectionManager(),
                mcpStartupCancellationToken = CancellationToken(),
                unifiedExecManager = UnifiedExecSessionManager(),
                notifier = UserNotifier(config.notify),
                rollout = rolloutRecorder,
                userShell = defaultShell,
                showRawAgentReasoning = config.showRawAgentReasoning,
                authManager = authManager,
                toolApprovals = ApprovalStore()
            )

            val session = Session(
                conversationId = conversationId,
                txEvent = txEvent,
                state = state,
                stateMutex = Mutex(),
                activeTurn = ActiveTurnHolder(),
                services = services,
                nextInternalSubId = AtomicLong(0L)
            )

            // Send SessionConfigured event
            val event = Event(
                id = Codex.INITIAL_SUBMIT_ID,
                msg = EventMsg.SessionConfigured(SessionConfiguredEvent(
                    sessionId = ai.solace.coder.protocol.ConversationId.fromString(conversationId).getOrThrow(),
                    model = sessionConfiguration.model,
                    modelProviderId = config.modelProviderId ?: "",
                    approvalPolicy = sessionConfiguration.approvalPolicy,
                    sandboxPolicy = sessionConfiguration.sandboxPolicy,
                    cwd = sessionConfiguration.cwd,
                    reasoningEffort = sessionConfiguration.modelReasoningEffort,
                    historyLogId = 0L,
                    historyEntryCount = 0L,
                    initialMessages = emptyList(),
                    rolloutPath = rolloutRecorder?.rolloutPath ?: ""
                ))
            )
            session.sendEventRaw(event)

            // Initialize MCP connection manager
            val mcpServerConfigs = config.mcpServers.mapValues { (_, v) ->
                ai.solace.coder.mcp.connection.McpServerConfig(
                    command = v.command,
                    args = v.args,
                    env = v.env
                )
            }
            services.mcpConnectionManager.initialize(
                mcpServerConfigs,
                txEvent,
                CancellationToken()
            )

            // Record initial history
            session.recordInitialHistory(initialHistory)

            return session
        }

        /**
         * Create a turn context.
         *
         * Note: In Rust, this is done in make_turn_context() which also creates the ModelClient.
         * The client parameter should be passed in once ModelClient creation is integrated.
         * Model info (model, modelFamily, modelContextWindow, etc.) is accessed via client.
         */
        private fun makeTurnContext(
            authManager: AuthManager?,
            sessionConfiguration: SessionConfiguration,
            conversationId: String,
            subId: String,
            finalOutputJsonSchema: JsonElement? = null,
            client: ai.solace.coder.core.client.ModelClient? = null
        ): TurnContext {
            val modelFamily = findFamilyForModel(sessionConfiguration.model)
                ?: sessionConfiguration.modelFamily

            val toolsConfig = ToolsConfig(
                modelFamily = modelFamily,
                features = sessionConfiguration.features
            )

            return TurnContext(
                subId = subId,
                client = client,  // Model info accessed via client
                cwd = sessionConfiguration.cwd,
                developerInstructions = sessionConfiguration.developerInstructions,
                baseInstructions = sessionConfiguration.baseInstructions,
                compactPrompt = sessionConfiguration.compactPrompt,
                userInstructions = sessionConfiguration.userInstructions,
                approvalPolicy = sessionConfiguration.approvalPolicy,
                sandboxPolicy = sessionConfiguration.sandboxPolicy,
                shellEnvironmentPolicy = sessionConfiguration.shellEnvironmentPolicy,
                toolsConfig = toolsConfig,
                finalOutputJsonSchema = finalOutputJsonSchema,
                codexLinuxSandboxExe = sessionConfiguration.codexLinuxSandboxExe,
                toolCallGate = ReadinessFlag(),
                execPolicy = sessionConfiguration.execPolicy,
                truncationPolicy = TruncationPolicy.Tokens(8000)
            )
        }

        private fun generateConversationId(): String {
            val chars = "0123456789abcdef"
            return buildString {
                append("conv_")
                repeat(16) {
                    append(chars.random())
                }
            }
        }

        private fun isAbsolutePath(path: String): Boolean {
            return path.startsWith("/") || (path.length >= 3 && path[1] == ':' && path[2] == '\\')
        }
    }
}

// =============================================================================
// TurnContext - Context for a single turn of the conversation
// =============================================================================

/**
 * Context needed for a single turn of the conversation.
 *
 * Ported from Rust codex-rs/core/src/codex.rs TurnContext struct (lines 272-292)
 *
 * Note: Model configuration (model, modelFamily, modelContextWindow, reasoningEffort,
 * reasoningSummary) and stream configuration (streamMaxRetries, autoCompactTokenLimit)
 * are accessed via the [client] field, not stored directly here. This matches the
 * Rust architecture where TurnContext.client provides access to ModelClient which
 * holds the Config.
 */
data class TurnContext(
    val subId: String,
    /**
     * The model client for this turn - provides access to OTEL, config, and API calls.
     * Access model info via client.getModel(), client.getModelContextWindow(), etc.
     * TODO: Make this non-nullable once ModelClient creation is properly integrated
     * into the turn lifecycle (see Rust codex-rs/core/src/codex.rs make_turn_context).
     */
    val client: ai.solace.coder.core.client.ModelClient? = null,
    /**
     * The session's current working directory. All relative paths provided by
     * the model as well as sandbox policies are resolved against this path.
     */
    val cwd: String,
    val developerInstructions: String? = null,
    val baseInstructions: String? = null,
    val compactPrompt: String? = null,
    val userInstructions: String? = null,
    val approvalPolicy: AskForApproval,
    val sandboxPolicy: SandboxPolicy,
    val shellEnvironmentPolicy: ShellEnvironmentPolicy = ShellEnvironmentPolicy.Inherit(),
    val toolsConfig: ToolsConfig = ToolsConfig(),
    val finalOutputJsonSchema: JsonElement? = null,
    val codexLinuxSandboxExe: String? = null,
    val toolCallGate: ReadinessFlag? = null,
    val execPolicy: ExecPolicy = ExecPolicy(),
    val truncationPolicy: TruncationPolicy = TruncationPolicy.Tokens(8000)
) {
    // =========================================================================
    // Convenience accessors - delegate to client for model/config info
    // These match the Rust pattern of accessing via turn_context.client.*
    // =========================================================================

    /** Get the model name. Delegates to client.getModel(). */
    val model: String get() = client?.getModel() ?: "unknown"

    /** Get the model family. Delegates to client.getModelFamily(). */
    val modelFamily: ModelFamily get() = client?.getModelFamily() ?: ModelFamily.default()

    /** Get the model context window. Delegates to client.getModelContextWindow(). */
    val modelContextWindow: Long? get() = client?.getModelContextWindow()

    /** Get the reasoning effort config. Delegates to client.getReasoningEffort(). */
    val reasoningEffort: ReasoningEffortConfig? get() = client?.getReasoningEffort()

    /** Get the reasoning summary config. Delegates to client.getReasoningSummary(). */
    val reasoningSummary: ReasoningSummaryConfig get() = client?.getReasoningSummary() ?: ReasoningSummary.Auto

    /** Get the auto-compact token limit. Delegates to client.getAutoCompactTokenLimit(). */
    val autoCompactTokenLimit: Long? get() = client?.getAutoCompactTokenLimit()

    /** Stream max retries - from client config. */
    val streamMaxRetries: Int get() = client?.config()?.streamMaxRetries ?: 3

    /**
     * Whether the model family supports parallel tool calls.
     */
    val modelFamilySupportsParallelToolCalls: Boolean
        get() = toolsConfig.modelFamily.supportsParallelToolCalls

    /**
     * Get the compact prompt, falling back to SUMMARIZATION_PROMPT if not set.
     * Ported from Rust codex-rs/core/src/codex.rs TurnContext::compact_prompt()
     */
    fun compactPromptOrDefault(): String = compactPrompt ?: SUMMARIZATION_PROMPT

    /**
     * Resolves a relative path against the turn's CWD.
     * Ported from Rust codex-rs/core/src/codex.rs TurnContext::resolve_path()
     */
    fun resolvePath(path: String?): String {
        if (path == null) return cwd
        if (path.startsWith("/") || path.matches(Regex("^[A-Za-z]:.*"))) {
            return path
        }
        return if (cwd.endsWith("/") || cwd.endsWith("\\")) {
            "$cwd$path"
        } else {
            "$cwd/$path"
        }
    }

    /**
     * Returns the compact prompt to use for this turn.
     */
    fun getCompactPrompt(): String {
        return compactPrompt ?: DEFAULT_COMPACT_PROMPT
    }

    companion object {
        private val DEFAULT_COMPACT_PROMPT = """
            Summarize the conversation history concisely while preserving:
            - User's original requests and intent
            - Key decisions and technical approach
            - Important context for continuing the work

            Omit:
            - Verbose explanations and redundant details
            - Off-topic discussions and failed attempts
            - Internal debugging and troubleshooting

            Focus on:
            - Current state of the work
            - Next steps to continue
            - Critical constraints and requirements
        """.trimIndent()
    }
}

// =============================================================================
// SessionConfiguration - Configuration that applies across all turns
// =============================================================================

/**
 * Session configuration that applies across all turns.
 *
 * Ported from Rust codex-rs/core/src/codex.rs SessionConfiguration struct (lines 308-354)
 */
data class SessionConfiguration(
    val provider: ModelProviderInfo,
    val model: String,
    val modelReasoningEffort: ReasoningEffortConfig? = null,
    val modelReasoningSummary: ReasoningSummaryConfig = ReasoningSummary.Auto,
    val developerInstructions: String? = null,
    val userInstructions: String? = null,
    val baseInstructions: String? = null,
    val compactPrompt: String? = null,
    val approvalPolicy: AskForApproval,
    val sandboxPolicy: SandboxPolicy,
    val cwd: String,
    val features: Features,
    val execPolicy: ExecPolicy,
    val sessionSource: SessionSource,
    val shellEnvironmentPolicy: ShellEnvironmentPolicy = ShellEnvironmentPolicy.Inherit(),
    val codexLinuxSandboxExe: String? = null,
    val modelFamily: ModelFamily = ModelFamily.default()
) {
    /**
     * Applies updates to create a new configuration.
     */
    fun apply(updates: SessionSettingsUpdate): SessionConfiguration {
        return copy(
            model = updates.model ?: model,
            modelReasoningEffort = updates.reasoningEffort ?: modelReasoningEffort,
            modelReasoningSummary = updates.reasoningSummary ?: modelReasoningSummary,
            approvalPolicy = updates.approvalPolicy ?: approvalPolicy,
            sandboxPolicy = updates.sandboxPolicy ?: sandboxPolicy,
            cwd = updates.cwd ?: cwd
        )
    }
}

/**
 * Updates that can be applied to session settings.
 *
 * Ported from Rust codex-rs/core/src/codex.rs SessionSettingsUpdate struct (lines 381-390)
 */
data class SessionSettingsUpdate(
    val cwd: String? = null,
    val approvalPolicy: AskForApproval? = null,
    val sandboxPolicy: SandboxPolicy? = null,
    val model: String? = null,
    val reasoningEffort: ReasoningEffortConfig? = null,
    val reasoningSummary: ReasoningSummaryConfig? = null,
    val finalOutputJsonSchema: JsonElement? = null
)

// =============================================================================
// Supporting Types for TurnContext
// =============================================================================

/**
 * Shell environment inheritance policy.
 *
 * Ported from Rust codex-rs/core/src/config/types.rs ShellEnvironmentPolicy
 */
sealed class ShellEnvironmentPolicy {
    data class Inherit(
        val filter: ShellEnvironmentInheritFilter = ShellEnvironmentInheritFilter.All
    ) : ShellEnvironmentPolicy()

    data class Sanitize(
        val additionalVars: Map<String, String> = emptyMap()
    ) : ShellEnvironmentPolicy()
}

enum class ShellEnvironmentInheritFilter {
    Core, All, None
}

/**
 * Tool configuration for a turn.
 *
 * Ported from Rust codex-rs/core/src/tools/spec.rs ToolsConfig
 */
// port-lint: ignore-duplicate - Extended version with Features, differs from ToolSpec.ToolsConfig
data class ToolsConfig(
    val shellType: ShellToolType = ShellToolType.Default,
    val applyPatchToolType: ApplyPatchToolType? = null,
    val webSearchRequest: Boolean = false,
    val includeViewImageTool: Boolean = true,
    val experimentalSupportedTools: List<String> = emptyList(),
    val modelFamily: ModelFamily = ModelFamily.default(),
    val features: Features = Features.withDefaults()
)

enum class ShellToolType { Default, UnifiedExec, None }
// ApplyPatchToolType is imported from ai.solace.coder.core.tools.ToolSpec

/**
 * Execution policy for commands.
 *
 * Ported from Rust codex-execpolicy.
 */
data class ExecPolicy(
    val enabled: Boolean = true,
    val defaultAction: ExecPolicyAction = ExecPolicyAction.Ask
)

enum class ExecPolicyAction { Allow, Deny, Ask }

// =============================================================================
// Submission Loop
// =============================================================================

/**
 * Main submission loop that processes operations.
 *
 * Ported from Rust codex-rs/core/src/codex.rs submission_loop
 */
private suspend fun submissionLoop(
    sess: Session,
    config: Config,
    rxSub: Channel<Submission>
) {
    var previousContext: TurnContext? = sess.newTurn(SessionSettingsUpdate())

    for (sub in rxSub) {
        println("DEBUG: Submission: ${sub.op}")

        when (val op = sub.op) {
            is Op.Interrupt -> {
                Handlers.interrupt(sess)
            }
            is Op.OverrideTurnContext -> {
                Handlers.overrideTurnContext(sess, SessionSettingsUpdate(
                    cwd = op.cwd,
                    approvalPolicy = op.approvalPolicy,
                    sandboxPolicy = op.sandboxPolicy,
                    model = op.model,
                    reasoningEffort = op.effort,
                    reasoningSummary = op.summary
                ))
            }
            is Op.UserInput -> {
                previousContext = Handlers.userInputOrTurn(sess, sub.id, op, previousContext)
            }
            is Op.UserTurn -> {
                previousContext = Handlers.userInputOrTurn(sess, sub.id, op, previousContext)
            }
            is Op.ExecApproval -> {
                Handlers.execApproval(sess, op.id, op.decision)
            }
            is Op.PatchApproval -> {
                Handlers.patchApproval(sess, op.id, op.decision)
            }
            is Op.AddToHistory -> {
                Handlers.addToHistory(sess, config, op.text)
            }
            is Op.GetHistoryEntryRequest -> {
                Handlers.getHistoryEntryRequest(sess, config, sub.id, op.offset.toInt(), op.logId)
            }
            is Op.ListMcpTools -> {
                Handlers.listMcpTools(sess, config, sub.id)
            }
            is Op.ListCustomPrompts -> {
                Handlers.listCustomPrompts(sess, sub.id)
            }
            is Op.Undo -> {
                Handlers.undo(sess, sub.id)
            }
            is Op.Compact -> {
                Handlers.compact(sess, sub.id)
            }
            is Op.RunUserShellCommand -> {
                previousContext = Handlers.runUserShellCommand(sess, sub.id, op.command, previousContext)
            }
            is Op.ResolveElicitation -> {
                Handlers.resolveElicitation(sess, op.serverName, op.requestId, op.decision)
            }
            is Op.Shutdown -> {
                if (Handlers.shutdown(sess, sub.id)) {
                    break
                }
            }
            is Op.Review -> {
                Handlers.review(sess, config, sub.id, op.reviewRequest)
            }
            else -> {
                // Ignore unknown ops
            }
        }
    }
    println("DEBUG: Agent loop exited")
}

// =============================================================================
// Operation Handlers
// =============================================================================

/**
 * Operation handlers module.
 *
 * Ported from Rust codex-rs/core/src/codex.rs mod handlers
 */
private object Handlers {
    suspend fun interrupt(sess: Session) {
        sess.interruptTask()
    }

    suspend fun overrideTurnContext(sess: Session, updates: SessionSettingsUpdate) {
        sess.updateSettings(updates)
    }

    suspend fun userInputOrTurn(
        sess: Session,
        subId: String,
        op: Op,
        previousContext: TurnContext?
    ): TurnContext {
        val (protocolItems, updates) = when (op) {
            is Op.UserTurn -> Pair(
                op.items,
                SessionSettingsUpdate(
                    cwd = op.cwd,
                    approvalPolicy = op.approvalPolicy,
                    sandboxPolicy = op.sandboxPolicy,
                    model = op.model,
                    reasoningEffort = op.effort,
                    reasoningSummary = op.summary,
                    finalOutputJsonSchema = op.finalOutputJsonSchema
                )
            )
            is Op.UserInput -> Pair(op.items, SessionSettingsUpdate())
            else -> throw IllegalArgumentException("Unexpected op type")
        }

        // Convert protocol UserInput to session UserInput
        val items = protocolItems.map { input ->
            when (input) {
                is ProtocolUserInput.Text -> UserInput.Text(content = input.text)
                is ProtocolUserInput.Image -> UserInput.Text(content = "[Image: ${input.imageUrl}]")
                is ProtocolUserInput.LocalImage -> UserInput.FileRef(path = input.path)
            }
        }

        val currentContext = sess.newTurnWithSubId(subId, updates)

        // Attempt to inject input into current task
        val injectResult = sess.injectInput(items)
        if (injectResult.isFailure) {
            val envItem = sess.buildEnvironmentUpdateItem(previousContext, currentContext)
            if (envItem != null) {
                sess.recordConversationItems(currentContext, listOf(envItem))
            }

            sess.spawnTask(currentContext, items, RegularTask())
        }

        return currentContext
    }

    suspend fun runUserShellCommand(
        sess: Session,
        subId: String,
        command: String,
        previousContext: TurnContext?
    ): TurnContext {
        val turnContext = sess.newTurnWithSubId(subId, SessionSettingsUpdate())
        sess.spawnTask(
            turnContext,
            emptyList(),
            UserShellCommandTask(command)
        )
        return turnContext
    }

    suspend fun resolveElicitation(
        sess: Session,
        serverName: String,
        requestId: String,
        decision: ElicitationAction
    ) {
        val response = McpConnectionManager.ElicitationResponse(
            action = decision,
            content = null
        )
        try {
            sess.services.mcpConnectionManager.resolveElicitation(serverName, requestId, response)
        } catch (e: Exception) {
            println("WARN: failed to resolve elicitation request in session: ${e.message}")
        }
    }

    suspend fun execApproval(sess: Session, id: String, decision: ReviewDecision) {
        when (decision) {
            ReviewDecision.Abort -> sess.interruptTask()
            else -> sess.notifyApproval(id, decision)
        }
    }

    suspend fun patchApproval(sess: Session, id: String, decision: ReviewDecision) {
        when (decision) {
            ReviewDecision.Abort -> sess.interruptTask()
            else -> sess.notifyApproval(id, decision)
        }
    }

    suspend fun addToHistory(sess: Session, config: Config, text: String) {
        // TODO: Implement message history append
    }

    suspend fun getHistoryEntryRequest(
        sess: Session,
        config: Config,
        subId: String,
        offset: Int,
        logId: Long
    ) {
        // TODO: Implement history entry lookup
        val event = Event(
            id = subId,
            msg = EventMsg.GetHistoryEntryResponse(GetHistoryEntryResponseEvent(
                offset = offset.toLong(),
                logId = logId,
                entry = null
            ))
        )
        sess.sendEventRaw(event)
    }

    suspend fun listMcpTools(sess: Session, config: Config, subId: String) {
        val tools = sess.services.mcpConnectionManager.listAllTools()
        val event = Event(
            id = subId,
            msg = EventMsg.McpListToolsResponse(McpListToolsResponseEvent(
                tools = tools,
                resources = emptyMap(),
                resourceTemplates = emptyMap(),
                authStatuses = emptyMap()
            ))
        )
        sess.sendEventRaw(event)
    }

    suspend fun listCustomPrompts(sess: Session, subId: String) {
        val customPrompts = discoverCustomPrompts()
        val event = Event(
            id = subId,
            msg = EventMsg.ListCustomPromptsResponse(ListCustomPromptsResponseEvent(
                customPrompts = customPrompts
            ))
        )
        sess.sendEventRaw(event)
    }

    suspend fun undo(sess: Session, subId: String) {
        val turnContext = sess.newTurnWithSubId(subId, SessionSettingsUpdate())
        sess.spawnTask(turnContext, emptyList(), UndoTask())
    }

    suspend fun compact(sess: Session, subId: String) {
        val turnContext = sess.newTurnWithSubId(subId, SessionSettingsUpdate())
        sess.spawnTask(
            turnContext,
            listOf(UserInput.Text(turnContext.getCompactPrompt())),
            CompactTask()
        )
    }

    suspend fun shutdown(sess: Session, subId: String): Boolean {
        sess.abortAllTasks(TurnAbortReason.Interrupted)
        sess.services.unifiedExecManager.terminateAllSessions()
        println("INFO: Shutting down Codex instance")

        // Flush and shutdown rollout recorder
        sess.services.rollout?.shutdown()

        val event = Event(
            id = subId,
            msg = EventMsg.ShutdownComplete
        )
        sess.sendEventRaw(event)
        return true
    }

    suspend fun review(sess: Session, config: Config, subId: String, reviewRequest: ReviewRequest) {
        val turnContext = sess.newTurnWithSubId(subId, SessionSettingsUpdate())
        spawnReviewThread(sess, config, turnContext, subId, reviewRequest)
    }
}

// =============================================================================
// Run Task
// =============================================================================

/**
 * Takes a user message as input and runs a loop where, at each turn, the model
 * replies with either:
 *
 * - requested function calls
 * - an assistant message
 *
 * Ported from Rust codex-rs/core/src/codex.rs run_task
 */
suspend fun runTask(
    sess: Session,
    turnContext: TurnContext,
    input: List<UserInput>,
    cancellationToken: CancellationToken
): String? {
    if (input.isEmpty()) {
        return null
    }

    val event = EventMsg.TaskStarted(TaskStartedEvent(
        modelContextWindow = turnContext.modelContextWindow
    ))
    sess.sendEvent(turnContext, event)

    val initialInputForTurn = ResponseInputItem.fromUserInput(input)
    sess.recordInputAndRolloutUsermsg(turnContext, initialInputForTurn)

    sess.maybeStartGhostSnapshot(turnContext, cancellationToken.child())

    var lastAgentMessage: String? = null
    val turnDiffTracker = SharedTurnDiffTracker()

    while (!cancellationToken.isCancelled()) {
        val pendingInput = sess.getPendingInput()
            .map { it.toResponseItem() }

        val turnInput = run {
            sess.recordConversationItems(turnContext, pendingInput)
            sess.cloneHistory().getHistoryForPrompt()
        }

        val turnInputMessages = turnInput
            .filterIsInstance<ResponseItem.Message>()
            .filter { it.role == "user" }
            .flatMap { msg ->
                msg.content.filterIsInstance<ContentItem.InputText>()
                    .map { it.text }
            }

        val turnResult = runTurn(
            sess = sess,
            turnContext = turnContext,
            turnDiffTracker = turnDiffTracker,
            input = turnInput,
            cancellationToken = cancellationToken.child()
        )
