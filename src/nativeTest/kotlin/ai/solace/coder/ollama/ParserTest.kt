package ai.solace.coder.ollama

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ParserTest {
    @Test
    fun testPullEventsDecoderStatusAndSuccess() {
        val v = Json.parseToJsonElement("""{"status":"verifying"}""")
        val events = pullEventsFromValue(v)
        assertEquals(1, events.size)
        assertTrue(events[0] is PullEvent.Status)
        assertEquals("verifying", (events[0] as PullEvent.Status).status)

        val v2 = Json.parseToJsonElement("""{"status":"success"}""")
        val events2 = pullEventsFromValue(v2)
        assertEquals(2, events2.size)
        assertTrue(events2[0] is PullEvent.Status)
        assertEquals("success", (events2[0] as PullEvent.Status).status)
        assertTrue(events2[1] is PullEvent.Success)
    }

    @Test
    fun testPullEventsDecoderProgress() {
        val v = Json.parseToJsonElement("""{"digest":"sha256:abc","total":100}""")
        val events = pullEventsFromValue(v)
        assertEquals(1, events.size)
        assertTrue(events[0] is PullEvent.ChunkProgress)
        val progress = events[0] as PullEvent.ChunkProgress
        assertEquals("sha256:abc", progress.digest)
        assertEquals(100L, progress.total)
        assertEquals(null, progress.completed)

        val v2 = Json.parseToJsonElement("""{"digest":"sha256:def","completed":42}""")
        val events2 = pullEventsFromValue(v2)
        assertEquals(1, events2.size)
        assertTrue(events2[0] is PullEvent.ChunkProgress)
        val progress2 = events2[0] as PullEvent.ChunkProgress
        assertEquals("sha256:def", progress2.digest)
        assertEquals(null, progress2.total)
        assertEquals(42L, progress2.completed)
    }
}
