// port-lint: source execpolicy/src/decision.rs
package ai.solace.coder.execpolicy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Decision {
    /** Command may run without further approval. */
    @SerialName("allow")
    Allow,

    /** Request explicit user approval; rejected outright when running with `approvalPolicy="never"`. */
    @SerialName("prompt")
    Prompt,

    /** Command is blocked without further consideration. */
    @SerialName("forbidden")
    Forbidden;

    companion object {
        fun parse(raw: String): Decision = when (raw) {
            "allow" -> Allow
            "prompt" -> Prompt
            "forbidden" -> Forbidden
            else -> throw ExecPolicyError.InvalidDecision(raw)
        }
    }
}
