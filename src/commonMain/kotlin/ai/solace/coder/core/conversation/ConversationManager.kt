// port-lint: source core/src/conversation_manager.rs
package ai.solace.coder.core.conversation

import ai.solace.coder.core.auth.AuthManager
import ai.solace.coder.core.auth.CodexAuth
import ai.solace.coder.core.config.Config
import ai.solace.coder.core.error.CodexError
import ai.solace.coder.core.error.CodexResult
import ai.solace.coder.core.session.Codex
import ai.solace.coder.core.session.CodexSpawnOk
import ai.solace.coder.core.session.spawnCodex
import ai.solace.coder.protocol.ConversationId
import ai.solace.coder.protocol.Event
import ai.solace.coder.protocol.EventMsg
import ai.solace.coder.protocol.InitialHistory
import ai.solace.coder.protocol.RolloutItem
import ai.solace.coder.protocol.SessionConfiguredEvent
import ai.solace.coder.protocol.SessionSource
import ai.solace.coder.protocol.TurnItem
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okio.Path
import okio.Path.Companion.toPath
import ai.solace.coder.protocol.ResponseItem

/**
 * Represents a newly created Codex conversation, including the first event
 * (which is [EventMsg.SessionConfigured]).
 */
data class NewConversation(
    val conversationId: ConversationId,
    val conversation: CodexConversation,
    val sessionConfigured: SessionConfiguredEvent
)

/**
 * ConversationManager is responsible for creating conversations and
 * maintaining them in memory.
 * 
 * Ported from Rust codex-rs/core/src/conversation_manager.rs
 */
class ConversationManager(
    private val authManager: AuthManager,
    private val sessionSource: SessionSource
) {
    private val conversations = mutableMapOf<ConversationId, CodexConversation>()
    private val conversationsMutex = Mutex()
    
    companion object {
        /**
         * Construct with a dummy AuthManager containing the provided CodexAuth.
         * Used for integration tests: should not be used by ordinary business logic.
         */
        fun withAuth(auth: CodexAuth): ConversationManager {
            return ConversationManager(
                AuthManager.fromAuthForTesting(auth),
                SessionSource.Exec
            )
        }
    }
    
    fun sessionSource(): SessionSource = sessionSource
    
    /**
     * Create a new conversation with the given configuration.
     */
    suspend fun newConversation(config: Config): CodexResult<NewConversation> {
        return spawnConversation(config, authManager)
    }
    
    private suspend fun spawnConversation(
        config: Config,
        authManager: AuthManager
    ): CodexResult<NewConversation> {
        val spawnResult = spawnCodex(
            config,
            authManager,
            InitialHistory.New,
            sessionSource
        ).getOrElse { return CodexResult.failure(it) }
        
        return finalizeSpawn(spawnResult.codex, spawnResult.conversationId)
    }
    
    private suspend fun finalizeSpawn(
        codex: Codex,
        conversationId: ConversationId
    ): CodexResult<NewConversation> {
        // The first event must be `SessionConfigured`. Validate and forward it
        // to the caller so that they can display it in the conversation history.
        val event = codex.nextEvent().getOrElse { return CodexResult.failure(it) }
        
        val sessionConfigured = when {
            event.id == Codex.INITIAL_SUBMIT_ID && event.msg is EventMsg.SessionConfigured -> {
                (event.msg as EventMsg.SessionConfigured).payload
            }
            else -> {
                return CodexResult.failure(CodexError.SessionConfiguredNotFirstEvent)
            }
        }
        
        val conversation = CodexConversation(
            codex,
            sessionConfigured.rolloutPath.toPath()
        )
        
        conversationsMutex.withLock {
            conversations[conversationId] = conversation
        }
        
        return CodexResult.success(NewConversation(
            conversationId,
            conversation,
            sessionConfigured
        ))
    }
    
    /**
     * Get an existing conversation by ID.
     */
    suspend fun getConversation(conversationId: ConversationId): CodexResult<CodexConversation> {
        return conversationsMutex.withLock {
            conversations[conversationId]?.let { CodexResult.success(it) }
                ?: CodexResult.failure(CodexError.ConversationNotFound(conversationId))
        }
    }
    
    /**
     * Resume a conversation from a rollout file.
     */
    suspend fun resumeConversationFromRollout(
        config: Config,
        rolloutPath: Path,
        authManager: AuthManager
    ): CodexResult<NewConversation> {
        // TODO: Port RolloutRecorder
        val initialHistory = InitialHistory.New // RolloutRecorder.getRolloutHistory(rolloutPath)
        return resumeConversationWithHistory(config, initialHistory, authManager)
    }
    
    /**
     * Resume a conversation with the given initial history.
     */
    suspend fun resumeConversationWithHistory(
        config: Config,
        initialHistory: InitialHistory,
        authManager: AuthManager
    ): CodexResult<NewConversation> {
        val spawnResult = spawnCodex(
            config,
            authManager,
            initialHistory,
            sessionSource
        ).getOrElse { return CodexResult.failure(it) }
        
        return finalizeSpawn(spawnResult.codex, spawnResult.conversationId)
    }
    
    /**
     * Removes the conversation from the manager's internal map, though the
     * conversation is stored as CodexConversation, it is possible that
     * other references to it exist elsewhere. Returns the conversation if the
     * conversation was found and removed.
     */
    suspend fun removeConversation(conversationId: ConversationId): CodexConversation? {
        return conversationsMutex.withLock {
            conversations.remove(conversationId)
        }
    }
    
    /**
     * Fork an existing conversation by taking messages up to the given position
     * (not including the message at the given position) and starting a new
     * conversation with identical configuration (unless overridden by the
     * caller's `config`). The new conversation will have a fresh id.
     */
    suspend fun forkConversation(
        nthUserMessage: Int,
        config: Config,
        path: Path
    ): CodexResult<NewConversation> {
        // Compute the prefix up to the cut point.
        // TODO: Port RolloutRecorder
        val history = InitialHistory.New // RolloutRecorder.getRolloutHistory(path)
        val truncatedHistory = truncateBeforeNthUserMessage(history, nthUserMessage)
        
        // Spawn a new conversation with the computed initial history.
        val spawnResult = spawnCodex(
            config,
            authManager,
            truncatedHistory,
            sessionSource
        ).getOrElse { return CodexResult.failure(it) }
        
        return finalizeSpawn(spawnResult.codex, spawnResult.conversationId)
    }
}

/**
 * Return a prefix of `items` obtained by cutting strictly before the nth user message
 * (0-based) and all items that follow it.
 */
private fun truncateBeforeNthUserMessage(history: InitialHistory, n: Int): InitialHistory {
    // Work directly on rollout items, and cut the vector at the nth user message input.
    val items = history.getRolloutItems()
    
    // Find indices of user message inputs in rollout order.
    val userPositions = mutableListOf<Int>()
    items.forEachIndexed { idx, item ->
        if (item is RolloutItem.ResponseItemHolder) {
            // TODO: Port parse_turn_item
            // For now, check if it's a user message by role
            when (val responseItem = item.payload) {
                is ai.solace.coder.protocol.ResponseItem.Message -> {
                    if (responseItem.role == "user") {
                        userPositions.add(idx)
                    }
                }
                else -> {}
            }
        }
    }
    
    // If fewer than or equal to n user messages exist, treat as empty (out of range).
    if (userPositions.size <= n) {
        return InitialHistory.New
    }
    
    // Cut strictly before the nth user message (do not keep the nth itself).
    val cutIdx = userPositions[n]
    val rolled = items.take(cutIdx)
    
    return if (rolled.isEmpty()) {
        InitialHistory.New
    } else {
        InitialHistory.Forked(rolled)
    }
}
