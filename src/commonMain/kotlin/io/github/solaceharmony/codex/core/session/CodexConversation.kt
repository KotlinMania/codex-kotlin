// port-lint: source core/src/codexConversation.rs
package io.github.solaceharmony.codex.core.session

import io.github.solaceharmony.codex.core.CodexResult
import io.github.solaceharmony.codex.protocol.Event
import io.github.solaceharmony.codex.protocol.Op
import io.github.solaceharmony.codex.protocol.Submission

class CodexConversation internal constructor(
    private val codex: Codex,
    private val rolloutPath: String,
) {
    suspend fun submit(op: Op): CodexResult<String> = codex.submit(op)

    suspend fun submitWithId(sub: Submission): CodexResult<Unit> = codex.submitWithId(sub)

    suspend fun nextEvent(): CodexResult<Event> = codex.nextEvent()

    fun rolloutPath(): String = rolloutPath
}
