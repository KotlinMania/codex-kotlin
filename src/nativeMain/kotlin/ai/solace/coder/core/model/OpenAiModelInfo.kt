// port-lint: source codex-rs/core/src/openai_model_info.rs
package ai.solace.coder.core.model

// Shared constants for commonly used window/token sizes.
const val CONTEXT_WINDOW_272K: Long = 272_000

/**
 * Metadata about a model, particularly OpenAI models.
 * We may want to consider including details like the pricing for
 * input tokens, output tokens, etc., though users will need to be able to
 * override this in config.toml, as this information can get out of date.
 * Though this would help present more accurate pricing information in the UI.
 */
data class ModelInfo(
    /** Size of the context window in tokens. This is the maximum size of the input context. */
    val contextWindow: Long,
    /**
     * Token threshold where we should automatically compact conversation history. This considers
     * input tokens + output tokens of this turn.
     */
    val autoCompactTokenLimit: Long?
) {
    companion object {
        fun new(contextWindow: Long): ModelInfo =
            ModelInfo(
                contextWindow = contextWindow,
                autoCompactTokenLimit = defaultAutoCompactLimit(contextWindow)
            )

        private fun defaultAutoCompactLimit(contextWindow: Long): Long =
            (contextWindow * 9) / 10
    }
}

fun getModelInfo(modelFamily: ModelFamily): ModelInfo? {
    val slug = modelFamily.slug
    return when (slug) {
        // OSS models have a 128k shared token pool.
        // Arbitrarily splitting it: 3/4 input context, 1/4 output.
        // https://openai.com/index/gpt-oss-model-card/
        "gpt-oss-20b" -> ModelInfo.new(96_000)
        "gpt-oss-120b" -> ModelInfo.new(96_000)
        // https://platform.openai.com/docs/models/o3
        "o3" -> ModelInfo.new(200_000)

        // https://platform.openai.com/docs/models/o4-mini
        "o4-mini" -> ModelInfo.new(200_000)

        // https://platform.openai.com/docs/models/codex-mini-latest
        "codex-mini-latest" -> ModelInfo.new(200_000)

        // As of Jun 25, 2025, gpt-4.1 defaults to gpt-4.1-2025-04-14.
        // https://platform.openai.com/docs/models/gpt-4.1
        "gpt-4.1", "gpt-4.1-2025-04-14" -> ModelInfo.new(1_047_576)

        // As of Jun 25, 2025, gpt-4o defaults to gpt-4o-2024-08-06.
        // https://platform.openai.com/docs/models/gpt-4o
        "gpt-4o", "gpt-4o-2024-08-06" -> ModelInfo.new(128_000)

        // https://platform.openai.com/docs/models/gpt-4o?snapshot=gpt-4o-2024-05-13
        "gpt-4o-2024-05-13" -> ModelInfo.new(128_000)

        // https://platform.openai.com/docs/models/gpt-4o?snapshot=gpt-4o-2024-11-20
        "gpt-4o-2024-11-20" -> ModelInfo.new(128_000)

        // https://platform.openai.com/docs/models/gpt-3.5-turbo
        "gpt-3.5-turbo" -> ModelInfo.new(16_385)

        else -> when {
            slug.startsWith("gpt-5-codex")
                    || slug.startsWith("gpt-5.1-codex")
                    || slug.startsWith("gpt-5.1-codex-max") -> ModelInfo.new(CONTEXT_WINDOW_272K)

            slug.startsWith("gpt-5") -> ModelInfo.new(CONTEXT_WINDOW_272K)

            slug.startsWith("codex-") -> ModelInfo.new(CONTEXT_WINDOW_272K)

            else -> null
        }
    }
}
