// port-lint: source ollama/src/parser.rs (tests)
package io.github.kotlinmania.codex.ollama

import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParserTest {
    @Test
    fun testPullEventsDecoderStatusAndSuccess() {
        val v =
            buildJsonObject {
                put("status", JsonPrimitive("verifying"))
            }
        val events = pullEventsFromValue(v)
        assertEquals(1, events.size)
        assertTrue(events[0] is PullEvent.Status)
        assertEquals("verifying", (events[0] as PullEvent.Status).status)

        val v2 =
            buildJsonObject {
                put("status", JsonPrimitive("success"))
            }
        val events2 = pullEventsFromValue(v2)
        assertEquals(2, events2.size)
        assertTrue(events2[0] is PullEvent.Status)
        assertEquals("success", (events2[0] as PullEvent.Status).status)
        assertTrue(events2[1] is PullEvent.Success)
    }

    @Test
    fun testPullEventsDecoderProgress() {
        val v =
            buildJsonObject {
                put("digest", JsonPrimitive("sha256:abc"))
                put("total", JsonPrimitive(100))
            }
        val events = pullEventsFromValue(v)
        assertEquals(1, events.size)
        val e0 = events[0]
        assertTrue(e0 is PullEvent.ChunkProgress)
        assertEquals("sha256:abc", e0.digest)
        assertEquals(100L, e0.total)
        assertEquals(null, e0.completed)

        val v2 =
            buildJsonObject {
                put("digest", JsonPrimitive("sha256:def"))
                put("completed", JsonPrimitive(42))
            }
        val events2 = pullEventsFromValue(v2)
        assertEquals(1, events2.size)
        val e1 = events2[0]
        assertTrue(e1 is PullEvent.ChunkProgress)
        assertEquals("sha256:def", e1.digest)
        assertEquals(null, e1.total)
        assertEquals(42L, e1.completed)
    }
}
