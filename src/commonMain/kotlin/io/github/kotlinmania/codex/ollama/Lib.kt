// transliterated from upstream module root (ollama crate)
package io.github.kotlinmania.codex.ollama

import io.github.kotlinmania.codex.core.config.Config

/** Default OSS model to use when `--oss` is passed without an explicit `-m`. */
const val DEFAULT_OSS_MODEL: String = "gpt-oss:20b"

/**
 * Prepare the local OSS environment when `--oss` is selected.
 *
 * - Ensures a local Ollama server is reachable.
 * - Checks if the model exists locally and pulls it if missing.
 */
internal suspend fun ensureOssReady(config: Config) {
    val model = config.model

    // Verify local Ollama is reachable.
    val ollamaClient = OllamaClient.tryFromOssProvider(config)

    // If the model is not present locally, pull it.
    val models = try {
        ollamaClient.fetchModels()
    } catch (_: Exception) {
        emptyList()
    }
    if (models.none { m -> m == model }) {
        val reporter = CliProgressReporter.new()
        ollamaClient.pullWithReporter(model, reporter)
    }
}

