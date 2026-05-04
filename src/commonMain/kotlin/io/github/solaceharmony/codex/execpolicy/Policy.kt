// port-lint: source execpolicy/src/policy.rs
package io.github.solaceharmony.codex.execpolicy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A policy indexing rules by their program name (the first command token). */
data class Policy(
    val rulesByProgram: Map<String, List<Rule>>,
) {
    fun rules(): Map<String, List<Rule>> = rulesByProgram

    fun check(cmd: List<String>): Evaluation {
        val first = cmd.firstOrNull() ?: return Evaluation.NoMatch
        val rules = rulesByProgram[first] ?: return Evaluation.NoMatch

        val matchedRules: List<RuleMatch> = rules.mapNotNull { it.matches(cmd) }
        val bestDecision = matchedRules.map { it.decision() }.maxOrNull()
        return if (bestDecision != null) {
            Evaluation.Match(decision = bestDecision, matchedRules = matchedRules)
        } else {
            Evaluation.NoMatch
        }
    }

    fun checkMultiple(commands: Iterable<List<String>>): Evaluation {
        val matchedRules: List<RuleMatch> = commands.flatMap { command ->
            when (val ev = check(command)) {
                is Evaluation.Match -> ev.matchedRules
                Evaluation.NoMatch -> emptyList()
            }
        }

        val bestDecision = matchedRules.map { it.decision() }.maxOrNull()
        return if (bestDecision != null) {
            Evaluation.Match(decision = bestDecision, matchedRules = matchedRules)
        } else {
            Evaluation.NoMatch
        }
    }

    companion object {
        fun empty(): Policy = Policy(emptyMap())
    }
}

@Serializable
sealed class Evaluation {
    @Serializable
    @SerialName("noMatch")
    data object NoMatch : Evaluation()

    @Serializable
    @SerialName("match")
    data class Match(
        val decision: Decision,
        @SerialName("matchedRules") val matchedRules: List<RuleMatch>,
    ) : Evaluation()

    fun isMatch(): Boolean = this is Match
}
