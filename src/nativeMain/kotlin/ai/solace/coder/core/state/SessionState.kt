// port-lint: source core/src/state/session.rs
package ai.solace.coder.core.state

import ai.solace.coder.core.config.SessionConfiguration
import ai.solace.coder.core.context.ContextManager
import ai.solace.coder.core.context.TruncationPolicy
import ai.solace.coder.protocol.RateLimitSnapshot
import ai.solace.coder.protocol.TokenUsage
import ai.solace.coder.protocol.TokenUsageInfo
import ai.solace.coder.protocol.ResponseItem

/**
 * Persistent, session-scoped state previously stored directly on `Session`.
 */
internal class SessionState(
    var sessionConfiguration: SessionConfiguration,
    val history: ContextManager = ContextManager(),
    var latestRateLimits: RateLimitSnapshot? = null
) {
    /**
     * Create a new session state mirroring previous `State::default()` semantics.
     */
    constructor(sessionConfiguration: SessionConfiguration) : this(
        sessionConfiguration = sessionConfiguration,
        history = ContextManager(),
        latestRateLimits = null
    )

    // History helpers
    fun recordItems(items: Iterable<ResponseItem>, policy: TruncationPolicy) {
        history.recordItems(items, policy)
    }

    fun cloneHistory(): ContextManager {
        return history.copy()
    }

    fun replaceHistory(items: List<ResponseItem>) {
        history.replace(items)
    }

    fun setTokenInfo(info: TokenUsageInfo?) {
        history.setTokenInfo(info)
    }

    // Token/rate limit helpers
    fun updateTokenInfoFromUsage(
        usage: TokenUsage,
        modelContextWindow: Long?
    ) {
        history.updateTokenInfo(usage, modelContextWindow)
    }

    fun tokenInfo(): TokenUsageInfo? {
        return history.tokenInfo()
    }

    fun setRateLimits(snapshot: RateLimitSnapshot) {
        this.latestRateLimits = snapshot
    }

    fun tokenInfoAndRateLimits(): Pair<TokenUsageInfo?, RateLimitSnapshot?> {
        return Pair(tokenInfo(), latestRateLimits)
    }

    fun setTokenUsageFull(contextWindow: Long) {
        history.setTokenUsageFull(contextWindow)
    }

    fun getTotalTokenUsage(): Long {
        return history.getTotalTokenUsage()
    }
}
