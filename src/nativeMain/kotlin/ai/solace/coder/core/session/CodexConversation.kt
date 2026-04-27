// port-lint: source core/src/codexConversation.rs
package ai.solace.coder.core.session

import ai.solace.coder.core.CodexResult
import ai.solace.coder.protocol.Event
import ai.solace.coder.protocol.Op
import ai.solace.coder.protocol.Submission

class CodexConversation internal constructor(
    private val codex: Codex,
    private val rolloutPath: String,
) {
    suspend fun submit(op: Op): CodexResult<String> = codex.submit(op)

    suspend fun submitWithId(sub: Submission): CodexResult<Unit> = codex.submitWithId(sub)

    suspend fun nextEvent(): CodexResult<Event> = codex.nextEvent()

    fun rolloutPath(): String = rolloutPath
}
