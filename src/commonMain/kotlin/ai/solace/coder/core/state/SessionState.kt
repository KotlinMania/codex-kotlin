// port-lint: source core/src/state/session.rs
package ai.solace.coder.core.state

import ai.solace.coder.core.context.ContextManager
import ai.solace.coder.core.context.TruncationPolicy
import ai.solace.coder.core.session.SessionConfiguration
import ai.solace.coder.protocol.RateLimitSnapshot
import ai.solace.coder.protocol.TokenUsage
import ai.solace.coder.protocol.TokenUsageInfo
import ai.solace.coder.protocol.ResponseItem

/** Persistent, session-scoped state previously stored directly on `Session`. */
internal class SessionState(
    var sessionConfiguration: SessionConfiguration,
    var history: ContextManager = ContextManager(),
    var latestRateLimits: RateLimitSnapshot? = null
) {
    companion object {
        /** Create a new session state mirroring previous `State::default()` semantics. */
        fun new(sessionConfiguration: SessionConfiguration): SessionState {
            return SessionState(sessionConfiguration)
        }
    }

    /** History helpers */
    fun recordItems(items: Iterable<ResponseItem>, policy: TruncationPolicy) {
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

    /** Token/rate limit helpers */
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
        latestRateLimits = snapshot
    }

    fun tokenInfoAndRateLimits(): Pair<TokenUsageInfo?, RateLimitSnapshot?> {
        return tokenInfo() to latestRateLimits
    }

    fun setTokenUsageFull(contextWindow: Long) {
        history.setTokenUsageFull(contextWindow)
    }

    fun getTotalTokenUsage(): Long {
        return history.getTotalTokenUsage()
    }
}
