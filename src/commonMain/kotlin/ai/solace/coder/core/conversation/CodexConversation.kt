// port-lint: source core/src/codex_conversation.rs
package ai.solace.coder.core.conversation

import ai.solace.coder.core.error.CodexResult
import ai.solace.coder.core.session.Codex
import ai.solace.coder.protocol.Event
import ai.solace.coder.protocol.Op
import ai.solace.coder.protocol.Submission
import java.nio.file.Path

/**
 * Conduit for the bidirectional stream of messages that compose a conversation
 * in Codex.
 * 
 * This class wraps a [Codex] instance and provides a simplified interface for
 * submitting operations and receiving events, along with rollout path tracking.
 * 
 * Ported from Rust codex-rs/core/src/codex_conversation.rs
 */
class CodexConversation internal constructor(
    private val codex: Codex,
    private val rolloutPath: Path
) {
    /**
     * Submit an operation to the conversation and return the submission ID.
     * 
     * @param op The operation to submit
     * @return The submission ID on success
     */
    suspend fun submit(op: Op): CodexResult<String> {
        return codex.submit(op)
    }
    
    /**
     * Submit an operation with a specific submission ID.
     * 
     * Use sparingly: this is intended to be removed soon.
     * 
     * @param sub The submission with explicit ID
     */
    suspend fun submitWithId(sub: Submission): CodexResult<Unit> {
        return codex.submitWithId(sub)
    }
    
    /**
     * Wait for and return the next event from the conversation stream.
     * 
     * @return The next event
     */
    suspend fun nextEvent(): CodexResult<Event> {
        return codex.nextEvent()
    }
    
    /**
     * Get the path where the rollout for this conversation is stored.
     * 
     * @return Path to the rollout file
     */
    fun rolloutPath(): Path {
        return rolloutPath
    }
}
