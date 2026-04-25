// port-lint: source ollama/src/parser.rs
package ai.solace.coder.ollama

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

// Convert a single JSON object representing a pull update into one or more events.
internal fun pull_events_from_value(value: JsonElement): List<PullEvent> {
    val events = mutableListOf<PullEvent>()
    val status = value.get("status").and_then { it.as_str() }
    if (status != null) {
        events.add(PullEvent.Status(status.to_string()))
        if (status == "success") {
            events.add(PullEvent.Success)
        }
    }

    val digest =
        value.get("digest")
            .and_then { it.as_str() }
            .unwrap_or("")
            .to_string()

    val total = value.get("total").and_then { it.as_u64() }
    val completed = value.get("completed").and_then { it.as_u64() }
    if (total.is_some() || completed.is_some()) {
        events.add(
            PullEvent.ChunkProgress(
                digest = digest,
                total = total,
                completed = completed,
            )
        )
    }
    return events
}

private fun JsonElement.get(key: String): JsonElement? = (this as? JsonObject)?.get(key)

private inline fun <T, R> T?.and_then(f: (T) -> R?): R? = if (this != null) f(this) else null

private fun JsonElement.as_str(): String? = (this as? JsonPrimitive)?.contentOrNull

private fun JsonElement.as_u64(): Long? = (this as? JsonPrimitive)?.longOrNull

private fun <T> T?.unwrap_or(default: T): T = this ?: default

private fun Any?.is_some(): Boolean = this != null

private fun String.to_string(): String = this
