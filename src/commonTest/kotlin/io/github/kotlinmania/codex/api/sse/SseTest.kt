package io.github.kotlinmania.codex.api.sse

import io.github.kotlinmania.codex.protocol.ResponseEvent
import io.github.kotlinmania.codex.protocol.ResponseItem
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.time.Duration.Companion.seconds

class SseTest {
    @Test
    fun testStreamFromFixture() = runTest {
        val fixturePath = "codex-rs/core/tests/cli_responses_fixture.sse"
        val streamResult = streamFromFixture(fixturePath, 10.seconds)
        
        val stream = streamResult.getOrThrow()
        
        // 1. response.created
        val ev1 = stream.next()
        assertNotNull(ev1)
        assertIs<ResponseEvent.Created>(ev1.getOrThrow())
        
        // 2. response.output_item.done
        val ev2 = stream.next()
        assertNotNull(ev2)
        val itemDone = ev2.getOrThrow()
        assertIs<ResponseEvent.OutputItemDone>(itemDone)
        val item = itemDone.item
        assertIs<ResponseItem.Message>(item)
        assertEquals("assistant", item.role)
        assertEquals("fixture hello", (item.content[0] as io.github.kotlinmania.codex.protocol.ContentItem.OutputText).text)
        
        // 3. response.completed
        val ev3 = stream.next()
        assertNotNull(ev3)
        val completed = ev3.getOrThrow()
        assertIs<ResponseEvent.Completed>(completed)
        assertEquals("resp1", completed.responseId)
        
        // End of stream
        val ev4 = stream.next()
        assertEquals(null, ev4)
    }
}
