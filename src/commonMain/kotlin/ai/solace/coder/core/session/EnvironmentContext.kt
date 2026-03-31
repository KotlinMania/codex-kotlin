// port-lint: source core/src/environment_context.rs
package ai.solace.coder.core.session

import ai.solace.coder.exec.shell.Shell
import ai.solace.coder.protocol.AskForApproval
import ai.solace.coder.protocol.ContentItem
import ai.solace.coder.protocol.ENVIRONMENT_CONTEXT_CLOSE_TAG
import ai.solace.coder.protocol.ENVIRONMENT_CONTEXT_OPEN_TAG
import ai.solace.coder.protocol.ResponseItem
import ai.solace.coder.protocol.SandboxMode
import ai.solace.coder.protocol.SandboxPolicy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network access level derived from the sandbox policy.
 *
 * Ported from Rust codex-rs/core/src/environment_context.rs NetworkAccess enum.
 * Serialized in kebab-case to match the Rust `strum(serialize_all = "kebab-case")`.
 */
@Serializable
enum class NetworkAccess {
    @SerialName("restricted")
    Restricted,

    @SerialName("enabled")
    Enabled;

    /** Kebab-case display string, matching Rust's strum Display derive. */
    override fun toString(): String = when (this) {
        Restricted -> "restricted"
        Enabled -> "enabled"
    }
}

/**
 * Captures the runtime environment sent to the model as an XML system message.
 *
 * Each field is nullable because this type is also used for *diffs* between
 * turns -- only the fields that changed are populated.
 *
 * Ported from Rust codex-rs/core/src/environment_context.rs EnvironmentContext struct.
 */
@Serializable
@SerialName("environment_context")
data class EnvironmentContext(
    val cwd: String? = null,
    @SerialName("approval_policy") val approvalPolicy: AskForApproval? = null,
    @SerialName("sandbox_mode") val sandboxMode: SandboxMode? = null,
    @SerialName("network_access") val networkAccess: NetworkAccess? = null,
    @SerialName("writable_roots") val writableRoots: List<String>? = null,
    val shell: Shell
) {
    companion object {
        /**
         * Constructs an [EnvironmentContext] from raw policy objects.
         *
         * The [sandboxPolicy] is decomposed into [sandboxMode], [networkAccess],
         * and [writableRoots] so the model receives simple, flat values.
         */
        fun create(
            cwd: String? = null,
            approvalPolicy: AskForApproval? = null,
            sandboxPolicy: SandboxPolicy? = null,
            shell: Shell
        ): EnvironmentContext {
            val sandboxMode: SandboxMode? = when (sandboxPolicy) {
                is SandboxPolicy.DangerFullAccess -> SandboxMode.DangerFullAccess
                is SandboxPolicy.ReadOnly -> SandboxMode.ReadOnly
                is SandboxPolicy.WorkspaceWrite -> SandboxMode.WorkspaceWrite
                null -> null
            }

            val networkAccess: NetworkAccess? = when (sandboxPolicy) {
                is SandboxPolicy.DangerFullAccess -> NetworkAccess.Enabled
                is SandboxPolicy.ReadOnly -> NetworkAccess.Restricted
                is SandboxPolicy.WorkspaceWrite -> {
                    if (sandboxPolicy.networkAccess) NetworkAccess.Enabled
                    else NetworkAccess.Restricted
                }
                null -> null
            }

            val writableRoots: List<String>? = when (sandboxPolicy) {
                is SandboxPolicy.WorkspaceWrite -> {
                    sandboxPolicy.writableRoots.ifEmpty { null }
                }
                else -> null
            }

            return EnvironmentContext(
                cwd = cwd,
                approvalPolicy = approvalPolicy,
                sandboxMode = sandboxMode,
                networkAccess = networkAccess,
                writableRoots = writableRoots,
                shell = shell
            )
        }

        /**
         * Produces a diff context that only contains fields whose values
         * changed between [before] and [after]. Unchanged fields are null.
         *
         * The shell is not configurable between turns, so the platform
         * default is used as a placeholder.
         */
        fun diff(before: TurnContext, after: TurnContext, shell: Shell): EnvironmentContext {
            val cwd = if (before.cwd != after.cwd) after.cwd else null
            val approvalPolicy =
                if (before.approvalPolicy != after.approvalPolicy) after.approvalPolicy else null
            val sandboxPolicy =
                if (before.sandboxPolicy != after.sandboxPolicy) after.sandboxPolicy else null

            return create(
                cwd = cwd,
                approvalPolicy = approvalPolicy,
                sandboxPolicy = sandboxPolicy,
                shell = shell
            )
        }

        /**
         * Creates a fully-populated context from the given [TurnContext] and [Shell].
         */
        fun fromTurnContext(turnContext: TurnContext, shell: Shell): EnvironmentContext {
            return create(
                cwd = turnContext.cwd,
                approvalPolicy = turnContext.approvalPolicy,
                sandboxPolicy = turnContext.sandboxPolicy,
                shell = shell
            )
        }
    }

    /**
     * Compares two environment contexts, ignoring the shell.
     *
     * Useful when comparing turn-to-turn, since the initial context
     * includes the shell but it is not configurable between turns.
     */
    fun equalsExceptShell(other: EnvironmentContext): Boolean {
        return cwd == other.cwd
            && approvalPolicy == other.approvalPolicy
            && sandboxMode == other.sandboxMode
            && networkAccess == other.networkAccess
            && writableRoots == other.writableRoots
    }

    /**
     * Serializes this context to XML for inclusion in model messages.
     *
     * Output looks like:
     * ```xml
     * <environment_context>
     *   <cwd>...</cwd>
     *   <approval_policy>...</approval_policy>
     *   <sandbox_mode>...</sandbox_mode>
     *   <network_access>...</network_access>
     *   <writable_roots>
     *     <root>...</root>
     *   </writable_roots>
     *   <shell>...</shell>
     * </environment_context>
     * ```
     */
    fun serializeToXml(): String {
        val lines = mutableListOf(ENVIRONMENT_CONTEXT_OPEN_TAG)

        if (cwd != null) {
            lines.add("  <cwd>$cwd</cwd>")
        }
        if (approvalPolicy != null) {
            lines.add("  <approval_policy>${approvalPolicyToString(approvalPolicy)}</approval_policy>")
        }
        if (sandboxMode != null) {
            lines.add("  <sandbox_mode>${sandboxModeToString(sandboxMode)}</sandbox_mode>")
        }
        if (networkAccess != null) {
            lines.add("  <network_access>$networkAccess</network_access>")
        }
        if (writableRoots != null) {
            lines.add("  <writable_roots>")
            for (root in writableRoots) {
                lines.add("    <root>$root</root>")
            }
            lines.add("  </writable_roots>")
        }

        lines.add("  <shell>${shell.name()}</shell>")
        lines.add(ENVIRONMENT_CONTEXT_CLOSE_TAG)

        return lines.joinToString("\n")
    }

    /**
     * Converts this context into a [ResponseItem.Message] suitable for
     * inclusion in the conversation history as a user message.
     */
    fun toResponseItem(): ResponseItem {
        return ResponseItem.Message(
            role = "user",
            content = listOf(ContentItem.InputText(text = serializeToXml()))
        )
    }
}

/**
 * Returns the kebab-case string for an [AskForApproval] value,
 * matching the Rust Display / strum formatting.
 */
private fun approvalPolicyToString(policy: AskForApproval): String = when (policy) {
    AskForApproval.UnlessTrusted -> "untrusted"
    AskForApproval.OnFailure -> "on-failure"
    AskForApproval.OnRequest -> "on-request"
    AskForApproval.Never -> "never"
}

/**
 * Returns the kebab-case string for a [SandboxMode] value,
 * matching the Rust Display / strum formatting.
 */
private fun sandboxModeToString(mode: SandboxMode): String = when (mode) {
    SandboxMode.ReadOnly -> "read-only"
    SandboxMode.WorkspaceWrite -> "workspace-write"
    SandboxMode.DangerFullAccess -> "danger-full-access"
}
