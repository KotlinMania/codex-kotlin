// port-lint: source common/src/config_summary.rs
package ai.solace.coder.common

import ai.solace.coder.api.provider.WireApi
import ai.solace.coder.core.config.Config

/**
 * Build a list of key/value pairs summarizing the effective configuration.
 */
fun createConfigSummaryEntries(config: Config, model: String): List<Pair<String, String>> {
    val entries = mutableListOf(
        "workdir" to config.cwd,
        "model" to model,
        "provider" to (config.modelProviderId ?: ""),
        "approval" to config.approvalPolicy.name,
        "sandbox" to summarizeSandboxPolicy(config.sandboxPolicy),
    )
    if (config.modelProvider.wireApi == WireApi.Responses) {
        val reasoningEffort = config.modelReasoningEffort?.toString()
        entries.add("reasoning effort" to (reasoningEffort ?: "none"))
        entries.add("reasoning summaries" to config.modelReasoningSummary.toString())
    }

    return entries
}
