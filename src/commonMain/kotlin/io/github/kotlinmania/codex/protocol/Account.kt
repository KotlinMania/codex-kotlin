// port-lint: source account.rs
package io.github.kotlinmania.codex.protocol

/**
 * Account plan type.
 *
 * Ported from Rust codex-rs/protocol/src/account.rs
 */
enum class PlanType(
    val raw: String,
) {
    Free("free"),
    Go("go"),
    Plus("plus"),
    Pro("pro"),
    ProLite("prolite"),
    Team("team"),
    SelfServeBusinessUsageBased("self_serve_business_usage_based"),
    Business("business"),
    EnterpriseCbpUsageBased("enterprise_cbp_usage_based"),
    Enterprise("enterprise"),
    Edu("edu"),
    Unknown("unknown"),
    ;

    fun isTeamLike(): Boolean =
        this == Team || this == SelfServeBusinessUsageBased

    fun isBusinessLike(): Boolean =
        this == Business || this == EnterpriseCbpUsageBased

    fun isWorkspaceAccount(): Boolean =
        this == Team ||
            this == SelfServeBusinessUsageBased ||
            this == Business ||
            this == EnterpriseCbpUsageBased ||
            this == Enterprise ||
            this == Edu

    companion object {
        fun from(authPlan: AuthPlan): PlanType =
            authPlan.known?.let { from(it) } ?: Unknown

        fun from(knownPlan: KnownPlan): PlanType =
            when (knownPlan) {
                KnownPlan.Free -> Free
                KnownPlan.Go -> Go
                KnownPlan.Plus -> Plus
                KnownPlan.Pro -> Pro
                KnownPlan.ProLite -> ProLite
                KnownPlan.Team -> Team
                KnownPlan.SelfServeBusinessUsageBased -> SelfServeBusinessUsageBased
                KnownPlan.Business -> Business
                KnownPlan.EnterpriseCbpUsageBased -> EnterpriseCbpUsageBased
                KnownPlan.Enterprise -> Enterprise
                KnownPlan.Edu -> Edu
            }

        fun fromRaw(raw: String): PlanType =
            entries.firstOrNull { it.raw == raw } ?: Unknown
    }
}

/**
 * Account state returned by a model provider before it is adapted to an app-facing wire type.
 */
sealed class ProviderAccount {
    data object ApiKey : ProviderAccount()

    data class Chatgpt(
        val email: String,
        val planType: PlanType,
    ) : ProviderAccount()

    data object AmazonBedrock : ProviderAccount()
}
