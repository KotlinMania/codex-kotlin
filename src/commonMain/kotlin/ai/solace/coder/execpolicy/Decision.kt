// port-lint: source execpolicy/src/decision.rs
package ai.solace.coder.execpolicy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Decision for command execution.
 * 
 * Ordered by severity for policy evaluation.
 */
@Serializable
enum class Decision {
    /** Command may run without further approval. */
    @SerialName("allow")
    ALLOW,
    
    /** Request explicit user approval; rejected outright when running with `approval_policy="never"`. */
    @SerialName("prompt")
    PROMPT,
    
    /** Command is blocked without further consideration. */
    @SerialName("forbidden")
    FORBIDDEN;
    
    companion object {
        fun parse(raw: String): Result<Decision> {
            return when (raw) {
                "allow" -> Result.success(ALLOW)
                "prompt" -> Result.success(PROMPT)
                "forbidden" -> Result.success(FORBIDDEN)
                else -> Result.failure(ExecPolicyError.InvalidDecision(raw))
            }
        }
    }
}
