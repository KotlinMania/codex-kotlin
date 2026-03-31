// port-lint: source common/src/config_override.rs
package ai.solace.coder.common

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

/**
 * CLI option that captures arbitrary configuration overrides specified as
 * `-c key=value`. It intentionally keeps both halves **unparsed** so that the
 * calling code can decide how to interpret the right-hand side.
 *
 * Ported from Rust `CliConfigOverrides` — uses [JsonElement] instead of
 * `toml::Value` since the Kotlin project uses kotlinx.serialization.
 */
data class CliConfigOverrides(
    /**
     * Override a configuration value that would otherwise be loaded from
     * `~/.codex/config.toml`. Use a dotted path (`foo.bar.baz`) to override
     * nested values. The `value` portion is parsed as JSON first. If it fails
     * to parse as JSON, the raw string is used as a literal.
     *
     * Examples:
     *   - `-c model="o3"`
     *   - `-c 'sandbox_permissions=["disk-full-read-access"]'`
     *   - `-c shell_environment_policy.inherit=all`
     */
    val rawOverrides: List<String> = emptyList(),
) {
    /**
     * Parse the raw strings captured from the CLI into a list of `(path,
     * value)` tuples where `value` is a [JsonElement].
     */
    fun parseOverrides(): Result<List<Pair<String, JsonElement>>> {
        return try {
            val result = rawOverrides.map { s ->
                // Only split on the *first* '=' so values are free to contain
                // the character.
                val eqIdx = s.indexOf('=')
                if (eqIdx < 0) {
                    throw IllegalArgumentException("Invalid override (missing '='): $s")
                }
                val key = s.substring(0, eqIdx).trim()
                val valueStr = s.substring(eqIdx + 1).trim()

                if (key.isEmpty()) {
                    throw IllegalArgumentException("Empty key in override: $s")
                }

                // Attempt to parse as JSON. If that fails, treat it as a raw
                // string. This allows convenient usage such as
                // `-c model=o3` without the quotes.
                val value: JsonElement = try {
                    Json.parseToJsonElement(valueStr)
                } catch (_: Exception) {
                    // Strip leading/trailing quotes if present
                    val trimmed = valueStr.trim().trim('"', '\'')
                    JsonPrimitive(trimmed)
                }

                key to value
            }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Apply all parsed overrides onto [target]. Intermediate objects will be
     * created as necessary. Values located at the destination path will be
     * replaced.
     */
    fun applyOnValue(target: JsonObject): Result<JsonObject> {
        val overrides = parseOverrides().getOrElse { return Result.failure(it) }
        var current = target
        for ((path, value) in overrides) {
            current = applySingleOverride(current, path, value)
        }
        return Result.success(current)
    }
}

/**
 * Apply a single override onto [root], creating intermediate objects as
 * necessary.
 */
private fun applySingleOverride(root: JsonObject, path: String, value: JsonElement): JsonObject {
    val parts = path.split('.')
    return applyAtDepth(root, parts, 0, value)
}

private fun applyAtDepth(
    obj: JsonObject,
    parts: List<String>,
    depth: Int,
    value: JsonElement,
): JsonObject {
    val part = parts[depth]
    val isLast = depth == parts.lastIndex

    return buildJsonObject {
        for ((k, v) in obj) {
            if (k == part) {
                if (isLast) {
                    put(k, value)
                } else {
                    val nested = v as? JsonObject ?: JsonObject(emptyMap())
                    put(k, applyAtDepth(nested, parts, depth + 1, value))
                }
            } else {
                put(k, v)
            }
        }
        // If the key didn't exist, insert it
        if (part !in obj) {
            if (isLast) {
                put(part, value)
            } else {
                put(part, applyAtDepth(JsonObject(emptyMap()), parts, depth + 1, value))
            }
        }
    }
}
