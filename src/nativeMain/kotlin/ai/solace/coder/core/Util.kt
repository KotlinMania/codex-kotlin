// port-lint: source codex-rs/core/src/util.rs
package ai.solace.coder.core

import kotlin.math.pow
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private const val INITIAL_DELAY_MS: Long = 200L
private const val BACKOFF_FACTOR: Double = 2.0

fun backoff(attempt: Long): Duration {
    val clampedAttempt = if (attempt == 0L) 0 else (attempt - 1L)
    val exp = BACKOFF_FACTOR.pow(clampedAttempt.toInt())
    val base = (INITIAL_DELAY_MS.toDouble() * exp).toLong()
    val jitter = 0.9 + Random.nextDouble() * 0.2
    return (base.toDouble() * jitter).toLong().milliseconds
}

fun errorOrPanic(message: String) {
    // In Kotlin we behave as release: log and continue.
    println("ERROR: $message")
}

fun tryParseErrorMessage(text: String): String {
    val parsed: JsonObject? = try {
        val element = Json.parseToJsonElement(text)
        if (element is JsonObject) element else null
    } catch (_: Throwable) {
        null
    }
    if (parsed != null) {
        val error = parsed["error"]
        if (error is JsonObject) {
            val message = error["message"]
            if (message is JsonPrimitive && message.isString) {
                return message.content
            }
        }
    }
    if (text.isEmpty()) {
        return "Unknown error"
    }
    return text
}
