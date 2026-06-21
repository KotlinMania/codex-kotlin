// port-lint: source core/src/conversation_manager.rs
package io.github.kotlinmania.codex.core.session

import io.github.kotlinmania.codex.protocol.ContentItem
import io.github.kotlinmania.codex.protocol.InitialHistory
import io.github.kotlinmania.codex.protocol.ReasoningItemReasoningSummary
import io.github.kotlinmania.codex.protocol.ResponseItem
import io.github.kotlinmania.codex.protocol.RolloutItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private fun userMsg(text: String): ResponseItem =
    ResponseItem.Message(
        role = "user",
        content = listOf(ContentItem.OutputText(text = text)),
        id = null,
    )

private fun assistantMsg(text: String): ResponseItem =
    ResponseItem.Message(
        role = "assistant",
        content = listOf(ContentItem.OutputText(text = text)),
        id = null,
    )

class ConversationManagerTest {
    @Test
    fun dropsFromLastUserOnly() {
        val items =
            listOf(
                userMsg("u1"),
                assistantMsg("a1"),
                assistantMsg("a2"),
                userMsg("u2"),
                assistantMsg("a3"),
                ResponseItem.Reasoning(
                    id = "r1",
                    summary = listOf(ReasoningItemReasoningSummary.SummaryText(text = "s")),
                    content = null,
                    encryptedContent = null,
                ),
                ResponseItem.FunctionCall(
                    id = null,
                    name = "tool",
                    arguments = "{}",
                    callId = "c1",
                ),
                assistantMsg("a4"),
            )

        val initial: List<RolloutItem> = items.map { RolloutItem.ResponseItemHolder(it) }
        val truncated = truncateBeforeNthUserMessage(InitialHistory.Forked(initial), 1)
        val gotItems = truncated.getRolloutItems()
        val expectedItems =
            listOf(
                RolloutItem.ResponseItemHolder(items[0]),
                RolloutItem.ResponseItemHolder(items[1]),
                RolloutItem.ResponseItemHolder(items[2]),
            )
        assertEquals(expectedItems, gotItems)

        val initial2: List<RolloutItem> = items.map { RolloutItem.ResponseItemHolder(it) }
        val truncated2 = truncateBeforeNthUserMessage(InitialHistory.Forked(initial2), 2)
        assertTrue(truncated2 is InitialHistory.New)
    }
}
