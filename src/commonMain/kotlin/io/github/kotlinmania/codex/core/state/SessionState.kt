// port-lint: source core/src/state/session.rs
package io.github.kotlinmania.codex.core.state

import io.github.kotlinmania.codex.core.session.SessionConfiguration
import io.github.kotlinmania.codex.core.context.ContextManager
import io.github.kotlinmania.codex.core.context.TruncationPolicy
import io.github.kotlinmania.codex.protocol.RateLimitSnapshot
import io.github.kotlinmania.codex.protocol.TokenUsage
import io.github.kotlinmania.codex.protocol.TokenUsageInfo
import io.github.kotlinmania.codex.protocol.ResponseItem

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
    fun recordItems(items: List<ResponseItem>, policy: TruncationPolicy) {
        history.recordItems(items, policy)
    }

    fun cloneHistory(): ContextManager {
        return history.clone()
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
