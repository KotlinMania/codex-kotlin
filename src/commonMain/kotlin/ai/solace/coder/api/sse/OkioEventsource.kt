package ai.solace.coder.api.sse

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.BufferedSource

/**
 * Extension to turn a BufferedSource into a Flow of SseEvents.
 * Mirrors Rust eventsource_stream::Eventsource trait.
 */
fun BufferedSource.eventsource(): Flow<SseEvent> = flow {
    var event: String? = null
    val data = StringBuilder()
    var id: String? = null
    var retry: Long? = null

    while (!exhausted()) {
        val line = readUtf8Line() ?: break
        if (line.isEmpty()) {
            if (data.isNotEmpty()) {
                emit(SseEvent(data = data.toString().removeSuffix("\n"), event = event, id = id, retry = retry))
                data.clear()
                event = null
                // Note: id and retry persist until overridden per spec, but often implementations reset them.
                // Rust's eventsource-stream behavior is what we mirror.
            }
            continue
        }

        if (line.startsWith(":")) {
            // Comment, ignore
            continue
        }

        val colonIndex = line.indexOf(':')
        val field: String
        val value: String
        if (colonIndex != -1) {
            field = line.substring(0, colonIndex)
            value = line.substring(colonIndex + 1).removePrefix(" ")
        } else {
            field = line
            value = ""
        }

        when (field) {
            "event" -> event = value
            "data" -> {
                data.append(value).append("\n")
            }
            "id" -> id = value
            "retry" -> retry = value.toLongOrNull()
        }
    }

    // Emit final event if any
    if (data.isNotEmpty()) {
        emit(SseEvent(data = data.toString().removeSuffix("\n"), event = event, id = id, retry = retry))
    }
}
