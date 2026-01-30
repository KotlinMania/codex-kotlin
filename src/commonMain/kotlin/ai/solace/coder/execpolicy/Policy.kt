// port-lint: source execpolicy/src/policy.rs
package ai.solace.coder.execpolicy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Execution policy for command evaluation.
 * 
 * Maps programs to rules and evaluates commands against those rules.
 */
class Policy(
    private val rulesByProgram: Map<String, List<RuleRef>>
) {
    companion object {
        fun new(rulesByProgram: Map<String, List<RuleRef>>): Policy {
            return Policy(rulesByProgram)
        }
        
        fun empty(): Policy {
            return new(emptyMap())
        }
    }
    
    fun rules(): Map<String, List<RuleRef>> {
        return rulesByProgram
    }
    
    fun check(cmd: List<String>): Evaluation {
        val rules = cmd.firstOrNull()?.let { first ->
            rulesByProgram[first]
        } ?: return Evaluation.NoMatch
        
        if (rules.isEmpty()) {
            return Evaluation.NoMatch
        }
        
        val matchedRules: List<RuleMatch> = rules.mapNotNull { rule ->
            rule.matches(cmd)
        }
        
        val decision = matchedRules.mapNotNull { it.decision() }.maxOrNull()
        return if (decision != null) {
            Evaluation.Match(
                decision = decision,
                matchedRules = matchedRules
            )
        } else {
            Evaluation.NoMatch
        }
    }
    
    fun <T> checkMultiple(commands: Iterable<T>): Evaluation
        where T : List<String> {
        val matchedRules: List<RuleMatch> = commands.flatMap { command ->
            when (val eval = check(command)) {
                is Evaluation.Match -> eval.matchedRules
                is Evaluation.NoMatch -> emptyList()
            }
        }
        
        val decision = matchedRules.mapNotNull { it.decision() }.maxOrNull()
        return if (decision != null) {
            Evaluation.Match(
                decision = decision,
                matchedRules = matchedRules
            )
        } else {
            Evaluation.NoMatch
        }
    }
}

/**
 * Result of evaluating commands against a policy.
 */
@Serializable
sealed class Evaluation {
    @Serializable
    @SerialName("noMatch")
    data object NoMatch : Evaluation()
    
    @Serializable
    @SerialName("match")
    data class Match(
        val decision: Decision,
        @SerialName("matchedRules")
        val matchedRules: List<RuleMatch>
    ) : Evaluation()
    
    fun isMatch(): Boolean {
        return this is Match
    }
}
