// port-lint: source ollama/src/parser.rs
package io.github.kotlinmania.codex.ollama

import kotlinx.serialization.json.*

/**
 * Convert a single JSON object representing a pull update into one or more events.
 * Ported from Rust ollama/src/parser.rs
 */
fun pullEventsFromValue(value: JsonElement): List<PullEvent> {
    val events = mutableListOf<PullEvent>()
    val obj = value.jsonObject

    obj["status"]?.jsonPrimitive?.contentOrNull?.let { status ->
        events.add(PullEvent.Status(status))
        if (status == "success") {
            events.add(PullEvent.Success)
        }
    }

    val digest = obj["digest"]?.jsonPrimitive?.contentOrNull ?: ""
    val total = obj["total"]?.jsonPrimitive?.longOrNull
    val completed = obj["completed"]?.jsonPrimitive?.longOrNull

    if (total != null || completed != null) {
        events.add(PullEvent.ChunkProgress(
                digest = digest,
                total = total,
                completed = completed
        ))
    }

    return events
}
