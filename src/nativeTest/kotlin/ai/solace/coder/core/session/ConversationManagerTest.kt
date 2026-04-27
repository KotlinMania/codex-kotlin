// port-lint: source core/src/conversation_manager.rs
package ai.solace.coder.core.session

import ai.solace.coder.protocol.ContentItem
import ai.solace.coder.protocol.InitialHistory
import ai.solace.coder.protocol.ReasoningItemReasoningSummary
import ai.solace.coder.protocol.ResponseItem
import ai.solace.coder.protocol.RolloutItem
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
    fun drops_from_last_user_only() {
        val items = listOf(
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
        val expectedItems = listOf(
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
