// port-lint: source ollama/src/parser.rs
package io.github.kotlinmania.codex.ollama

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.longOrNull

// Convert a single JSON object representing a pull update into one or more events.
internal fun pullEventsFromValue(value: JsonElement): List<PullEvent> {
    val events = mutableListOf<PullEvent>()
    val obj = value as? JsonObject ?: return events
    val status = (obj["status"] as? JsonPrimitive)?.contentOrNull
    if (status != null) {
        events.add(PullEvent.Status(status))
        if (status == "success") {
            events.add(PullEvent.Success)
        }
    }
    val digest = (obj["digest"] as? JsonPrimitive)?.contentOrNull ?: ""
    val total = (obj["total"] as? JsonPrimitive)?.longOrNull
    val completed = (obj["completed"] as? JsonPrimitive)?.longOrNull
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
