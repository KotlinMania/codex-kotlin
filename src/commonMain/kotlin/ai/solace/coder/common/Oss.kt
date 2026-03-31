// port-lint: source common/src/oss.rs
package ai.solace.coder.common

import ai.solace.coder.core.config.Config
import ai.solace.coder.core.model.LMSTUDIO_OSS_PROVIDER_ID
import ai.solace.coder.core.model.OLLAMA_OSS_PROVIDER_ID

/** Default OSS model for Ollama when `--oss` is passed without an explicit `-m`. */
const val OLLAMA_DEFAULT_OSS_MODEL: String = "gpt-oss:20b"

/** Default OSS model for LM Studio when `--oss` is passed without an explicit `-m`. */
const val LMSTUDIO_DEFAULT_OSS_MODEL: String = "openai/gpt-oss-20b"

/** Returns the default model for a given OSS provider. */
fun getDefaultModelForOssProvider(providerId: String): String? {
    return when (providerId) {
        LMSTUDIO_OSS_PROVIDER_ID -> LMSTUDIO_DEFAULT_OSS_MODEL
        OLLAMA_OSS_PROVIDER_ID -> OLLAMA_DEFAULT_OSS_MODEL
        else -> null
    }
}

/**
 * Ensures the specified OSS provider is ready (models downloaded, service reachable).
 *
 * Note: The actual client calls (`OllamaClient.ensureOssReady` /
 * `LMStudioClient.ensureOssReady`) are not yet ported. This function will
 * delegate to them once the client modules are translated.
 */
suspend fun ensureOssProviderReady(
    providerId: String,
    config: Config,
) {
    when (providerId) {
        LMSTUDIO_OSS_PROVIDER_ID -> {
            // TODO: codex_lmstudio::ensure_oss_ready(config) — LMStudioClient not yet ported
            throw UnsupportedOperationException("LM Studio OSS provider not yet ported")
        }
        OLLAMA_OSS_PROVIDER_ID -> {
            // TODO: codex_ollama::ensure_oss_ready(config) — OllamaClient not yet ported
            throw UnsupportedOperationException("Ollama OSS provider not yet ported")
        }
        else -> {
            // Unknown provider, skip setup
        }
    }
}
