package ai.solace.coder.api.sse

/**
 * Server-Sent Event structure.
 * Mirrors Rust eventsource_stream::Event.
 */
data class SseEvent(
    val data: String,
    val event: String? = null,
    val id: String? = null,
    val retry: Long? = null,
)
