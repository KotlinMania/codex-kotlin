// port-lint: source core/src/conversationManager.rs
package ai.solace.coder.core.session

import ai.solace.coder.client.auth.AuthManager
import ai.solace.coder.client.auth.CodexAuth
import ai.solace.coder.core.CodexErr
import ai.solace.coder.core.CodexResult
import ai.solace.coder.protocol.ConversationId as ProtocolConversationId
import ai.solace.coder.protocol.Event
import ai.solace.coder.protocol.EventMsg
import ai.solace.coder.protocol.InitialHistory
import ai.solace.coder.protocol.ResponseItem
import ai.solace.coder.protocol.RolloutItem
import ai.solace.coder.protocol.SessionConfiguredEvent
import ai.solace.coder.protocol.SessionSource
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Represents a newly created Codex conversation, including the first event
 * (which is [`EventMsg.SessionConfigured`]).
 *
 * Mirrors Rust `core/src/conversationManager.rs::NewConversation`.
 */
data class NewConversation(
    val conversationId: ProtocolConversationId,
    val conversation: CodexConversation,
    val sessionConfigured: SessionConfiguredEvent,
)

/**
 * [`ConversationManager`] is responsible for creating conversations and
 * maintaining them in memory.
 *
 * Mirrors Rust `core/src/conversationManager.rs::ConversationManager`.
 */
class ConversationManager(
    private val authManager: AuthManager,
    private val sessionSource: SessionSource,
) {
    private val conversationsLock: Mutex = Mutex()
    private val conversations: MutableMap<ProtocolConversationId, CodexConversation> = mutableMapOf()

    /**
     * Construct with a dummy AuthManager containing the provided CodexAuth.
     * Used for integration tests: should not be used by ordinary business logic.
     */
    companion object {
        fun withAuth(auth: CodexAuth): ConversationManager =
            ConversationManager(
                authManager = AuthManager.fromAuthForTesting(auth),
                sessionSource = SessionSource.Exec,
            )
    }

    fun sessionSource(): SessionSource = sessionSource

    suspend fun newConversation(config: Config): CodexResult<NewConversation> =
        spawnConversation(config, authManager)

    private suspend fun spawnConversation(
        config: Config,
        authManager: AuthManager,
    ): CodexResult<NewConversation> {
        val spawnResult = spawnCodex(
            config = config,
            authManager = authManager,
            conversationHistory = InitialHistory.New,
            sessionSource = sessionSource,
        )
        return when (spawnResult) {
            is CodexResult.Success -> finalizeSpawn(spawnResult.value.codex, spawnResult.value.conversationId)
            is CodexResult.Failure -> CodexResult.failure(spawnResult.error)
        }
    }

    private suspend fun finalizeSpawn(
        codex: Codex,
        conversationId: String,
    ): CodexResult<NewConversation> {
        // The first event must be `SessionInitialized`. Validate and forward it
        // to the caller so that they can display it in the conversation
        // history.
        val eventResult = codex.nextEvent()
        val event = when (eventResult) {
            is CodexResult.Success -> eventResult.value
            is CodexResult.Failure -> return CodexResult.failure(eventResult.error)
        }
        val sessionConfigured = run {
            val msg = event.msg
            if (event.id == CODEX_INITIAL_SUBMIT_ID && msg is EventMsg.SessionConfigured) {
                msg.payload
            } else {
                return CodexResult.failure(CodexErr.SessionConfiguredNotFirstEvent)
            }
        }

        val protocolConversationId = ProtocolConversationId.fromString(conversationId).getOrThrow()
        val conversation = CodexConversation(
            codex = codex,
            rolloutPath = sessionConfigured.rolloutPath,
        )
        conversationsLock.withLock {
            conversations[protocolConversationId] = conversation
        }

        return CodexResult.success(
            NewConversation(
                conversationId = protocolConversationId,
                conversation = conversation,
                sessionConfigured = sessionConfigured,
            ),
        )
    }

    suspend fun getConversation(
        conversationId: ProtocolConversationId,
    ): CodexResult<CodexConversation> {
        val found = conversationsLock.withLock { conversations[conversationId] }
        return if (found != null) {
            CodexResult.success(found)
        } else {
            CodexResult.failure(CodexErr.ConversationNotFound(conversationId))
        }
    }

    suspend fun resumeConversationFromRollout(
        config: Config,
        rolloutPath: String,
        authManager: AuthManager,
    ): CodexResult<NewConversation> {
        val initialHistoryResult = RolloutRecorder.getRolloutHistory(rolloutPath)
        val initialHistory = when (initialHistoryResult) {
            is CodexResult.Success -> initialHistoryResult.value
            is CodexResult.Failure -> return CodexResult.failure(initialHistoryResult.error)
        }
        return resumeConversationWithHistory(config, initialHistory, authManager)
    }

    suspend fun resumeConversationWithHistory(
        config: Config,
        initialHistory: InitialHistory,
        authManager: AuthManager,
    ): CodexResult<NewConversation> {
        val spawnResult = spawnCodex(
            config = config,
            authManager = authManager,
            conversationHistory = initialHistory,
            sessionSource = sessionSource,
        )
        return when (spawnResult) {
            is CodexResult.Success -> finalizeSpawn(spawnResult.value.codex, spawnResult.value.conversationId)
            is CodexResult.Failure -> CodexResult.failure(spawnResult.error)
        }
    }

    /**
     * Removes the conversation from the manager internal map. The
     * conversation is shared by reference, so other references to it may exist
     * elsewhere. Returns the conversation if it was found and removed.
     */
    suspend fun removeConversation(conversationId: ProtocolConversationId): CodexConversation? =
        conversationsLock.withLock { conversations.remove(conversationId) }

    /**
     * Fork an existing conversation by taking messages up to the given position
     * (not including the message at the given position) and starting a new
     * conversation with identical configuration (unless overridden by the
     * caller `config`). The new conversation will have a fresh id.
     */
    suspend fun forkConversation(
        nthUserMessage: Int,
        config: Config,
        path: String,
    ): CodexResult<NewConversation> {
        // Compute the prefix up to the cut point.
        val historyResult = RolloutRecorder.getRolloutHistory(path)
        val history = when (historyResult) {
            is CodexResult.Success -> historyResult.value
            is CodexResult.Failure -> return CodexResult.failure(historyResult.error)
        }
        val truncated = truncateBeforeNthUserMessage(history, nthUserMessage)

        // Spawn a new conversation with the computed initial history.
        val spawnResult = spawnCodex(
            config = config,
            authManager = authManager,
            conversationHistory = truncated,
            sessionSource = sessionSource,
        )
        return when (spawnResult) {
            is CodexResult.Success -> finalizeSpawn(spawnResult.value.codex, spawnResult.value.conversationId)
            is CodexResult.Failure -> CodexResult.failure(spawnResult.error)
        }
    }

}

/**
 * Return a prefix of `items` obtained by cutting strictly before the nth user message
 * (0-based) and all items that follow it.
 *
 * Mirrors Rust `core/src/conversationManager.rs::truncateBeforeNthUserMessage`.
 */
internal fun truncateBeforeNthUserMessage(history: InitialHistory, n: Int): InitialHistory {
    // Work directly on rollout items, and cut the vector at the nth user message input.
    val items: List<RolloutItem> = history.getRolloutItems()

    // Find indices of user message inputs in rollout order.
    val userPositions: MutableList<Int> = mutableListOf()
    for ((idx, item) in items.withIndex()) {
        if (item is RolloutItem.ResponseItemHolder) {
            val payload = item.payload
            if (payload is ResponseItem.Message && payload.role == "user") {
                userPositions.add(idx)
            }
        }
    }

    // If fewer than or equal to n user messages exist, treat as empty (out of range).
    if (userPositions.size <= n) {
        return InitialHistory.New
    }

    // Cut strictly before the nth user message (do not keep the nth itself).
    val cutIdx = userPositions[n]
    val rolled: List<RolloutItem> = items.take(cutIdx)

    return if (rolled.isEmpty()) {
        InitialHistory.New
    } else {
        InitialHistory.Forked(rolled)
    }
}

