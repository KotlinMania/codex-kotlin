// port-lint: source parser.rs
package ai.solace.coder.ollama

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.longOrNull
import kotlin.test.Test

// Convert a single JSON object representing a pull update into one or more events.
internal fun pullEventsFromValue(value: JsonElement): List<PullEvent> {
    val events = mutableListOf<PullEvent>()
    val obj = value as? JsonObject ?: return events
    val status = (obj.get("status") as? JsonPrimitive)?.contentOrNull
    if (status != null) {
        events.add(PullEvent.Status(status.toString()))
        if (status == "success") {
            events.add(PullEvent.Success)
        }
    }
    val digest = (obj.get("digest") as? JsonPrimitive)?.contentOrNull?.toString() ?: ""
    val total = (obj.get("total") as? JsonPrimitive)?.longOrNull
    val completed = (obj.get("completed") as? JsonPrimitive)?.longOrNull
    if (total != null || completed != null) {
        events.add(
            PullEvent.ChunkProgress(
                digest = digest,
                total = total,
                completed = completed,
            ),
        )
    }
    return events
}

@Test
internal fun testPullEventsDecoderStatusAndSuccess() {
    val v = JsonObject(mapOf("status" to JsonPrimitive("verifying")))
    val events = pullEventsFromValue(v)
    check(events.size == 1 && events[0] == PullEvent.Status("verifying"))

    val v2 = JsonObject(mapOf("status" to JsonPrimitive("success")))
    val events2 = pullEventsFromValue(v2)
    check(events2.size == 2)
    check(events2[0] == PullEvent.Status("success"))
    check(events2[1] == PullEvent.Success)
}

@Test
internal fun testPullEventsDecoderProgress() {
    val v = JsonObject(mapOf("digest" to JsonPrimitive("sha256:abc"), "total" to JsonPrimitive(100)))
    val events = pullEventsFromValue(v)
    check(events.size == 1)
    val e = events[0]
    check(e is PullEvent.ChunkProgress)
    e as PullEvent.ChunkProgress
    check(e.digest == "sha256:abc")
    check(e.total == 100L)
    check(e.completed == null)

    val v2 = JsonObject(mapOf("digest" to JsonPrimitive("sha256:def"), "completed" to JsonPrimitive(42)))
    val events2 = pullEventsFromValue(v2)
    check(events2.size == 1)
    val e2 = events2[0]
    check(e2 is PullEvent.ChunkProgress)
    e2 as PullEvent.ChunkProgress
    check(e2.digest == "sha256:def")
    check(e2.total == null)
    check(e2.completed == 42L)
}
