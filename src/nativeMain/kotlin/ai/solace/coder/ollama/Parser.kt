// port-lint: source ollama/src/parser.rs
package ai.solace.coder.ollama

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.longOrNull

// Convert a single JSON object representing a pull update into one or more events.
internal fun pullEventsFromValue(value: JsonElement): List<PullEvent> {
    val events = mutableListOf<PullEvent>()
    val status = value.field("status")?.asStr()
    if (status != null) {
        events.add(PullEvent.Status(status))
        if (status == "success") {
            events.add(PullEvent.Success)
        }
    }
    val digest = value.field("digest")?.asStr() ?: ""
    val total = value.field("total")?.asU64()
    val completed = value.field("completed")?.asU64()
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

private fun JsonElement.field(key: String): JsonElement? = (this as? JsonObject)?.get(key)

private fun JsonElement.asStr(): String? = (this as? JsonPrimitive)?.contentOrNull

private fun JsonElement.asU64(): Long? = (this as? JsonPrimitive)?.longOrNull
