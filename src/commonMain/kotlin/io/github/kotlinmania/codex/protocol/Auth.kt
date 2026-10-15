// port-lint: source auth.rs
package io.github.kotlinmania.codex.protocol

/**
 * Account plan type in auth context.
 *
 * Ported from Rust codex-rs/protocol/src/auth.rs
 */
data class AuthPlan(
    val known: KnownPlan? = null,
    val unknown: String? = null,
) {
    val raw: String
        get() = known?.raw ?: unknown.orEmpty()

    val displayName: String
        get() = known?.displayName ?: unknown.orEmpty()

    fun isWorkspaceAccount(): Boolean = known?.isWorkspaceAccount() ?: false

    companion object {
        fun known(plan: KnownPlan): AuthPlan = AuthPlan(known = plan)

        fun unknown(raw: String): AuthPlan = AuthPlan(unknown = raw)

        fun fromRaw(raw: String): AuthPlan =
            when (raw.lowercase()) {
                "free" -> known(KnownPlan.Free)
                "go" -> known(KnownPlan.Go)
                "plus" -> known(KnownPlan.Plus)
                "pro" -> known(KnownPlan.Pro)
                "prolite" -> known(KnownPlan.ProLite)
                "team" -> known(KnownPlan.Team)
                "self_serve_business_usage_based" -> known(KnownPlan.SelfServeBusinessUsageBased)
                "business" -> known(KnownPlan.Business)
                "enterprise_cbp_usage_based" -> known(KnownPlan.EnterpriseCbpUsageBased)
                "enterprise", "hc" -> known(KnownPlan.Enterprise)
                "education", "edu" -> known(KnownPlan.Edu)
                else -> unknown(raw)
            }
    }
}

enum class KnownPlan(
    val displayName: String,
    val raw: String,
) {
    Free("Free", "free"),
    Go("Go", "go"),
    Plus("Plus", "plus"),
    Pro("Pro", "pro"),
    ProLite("Pro Lite", "prolite"),
    Team("Team", "team"),
    SelfServeBusinessUsageBased("Self Serve Business Usage Based", "self_serve_business_usage_based"),
    Business("Business", "business"),
    EnterpriseCbpUsageBased("Enterprise CBP Usage Based", "enterprise_cbp_usage_based"),
    Enterprise("Enterprise", "enterprise"),
    Edu("Edu", "edu"),
    ;

    fun isWorkspaceAccount(): Boolean =
        this == Team ||
            this == SelfServeBusinessUsageBased ||
            this == Business ||
            this == EnterpriseCbpUsageBased ||
            this == Enterprise ||
            this == Edu
}

data class RefreshTokenFailedError(
    val reason: RefreshTokenFailedReason,
    val message: String,
)

enum class RefreshTokenFailedReason {
    Expired,
    Exhausted,
    Revoked,
    Other,
}
