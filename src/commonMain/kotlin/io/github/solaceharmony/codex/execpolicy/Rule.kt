// port-lint: source execpolicy/src/rule.rs
package io.github.solaceharmony.codex.execpolicy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Matches a single command token, either a fixed string or one of several allowed alternatives. */
sealed class PatternToken {
    data class Single(val expected: String) : PatternToken()
    data class Alts(val alternatives: List<String>) : PatternToken()

    fun matches(token: String): Boolean = when (this) {
        is Single -> expected == token
        is Alts -> alternatives.any { it == token }
    }

    fun alternatives(): List<String> = when (this) {
        is Single -> listOf(expected)
        is Alts -> alternatives
    }
}

/**
 * Prefix matcher for commands with support for alternative match tokens.
 * First token is fixed since we key by the first token in policy.
 */
data class PrefixPattern(
    val first: String,
    val rest: List<PatternToken>,
) {
    fun matchesPrefix(cmd: List<String>): List<String>? {
        val patternLength = rest.size + 1
        if (cmd.size < patternLength || cmd[0] != first) {
            return null
        }

        for ((patternToken, cmdToken) in rest.zip(cmd.subList(1, patternLength))) {
            if (!patternToken.matches(cmdToken)) {
                return null
            }
        }

        return cmd.subList(0, patternLength).toList()
    }
}

@Serializable
sealed class RuleMatch {
    @Serializable
    @SerialName("prefixRuleMatch")
    data class PrefixRuleMatch(
        @SerialName("matchedPrefix") val matchedPrefix: List<String>,
        val decision: Decision,
    ) : RuleMatch()

    fun decision(): Decision = when (this) {
        is PrefixRuleMatch -> decision
    }
}

data class PrefixRule(
    val pattern: PrefixPattern,
    val decision: Decision,
) : Rule {
    override fun program(): String = pattern.first

    override fun matches(cmd: List<String>): RuleMatch? =
        pattern.matchesPrefix(cmd)?.let { matchedPrefix ->
            RuleMatch.PrefixRuleMatch(matchedPrefix = matchedPrefix, decision = decision)
        }
}

interface Rule {
    fun program(): String
    fun matches(cmd: List<String>): RuleMatch?
}

private fun tryJoin(tokens: List<String>): String =
    tokens.joinToString(" ") { token ->
        if (token.isEmpty() || token.any { it.isWhitespace() || it in "\"'\\$`" }) {
            "'" + token.replace("'", "'\\''") + "'"
        } else {
            token
        }
    }

/** Count how many rules match each provided example and error if any example is unmatched. */
internal fun validateMatchExamples(rules: List<Rule>, matches: List<List<String>>) {
    val unmatchedExamples = mutableListOf<String>()

    for (example in matches) {
        if (rules.any { it.matches(example) != null }) {
            continue
        }
        unmatchedExamples.add(tryJoin(example))
    }

    if (unmatchedExamples.isNotEmpty()) {
        throw ExecPolicyError.ExampleDidNotMatch(
            rules = rules.map { it.toString() },
            examples = unmatchedExamples,
        )
    }
}

/** Ensure that no rule matches any provided negative example. */
internal fun validateNotMatchExamples(rules: List<Rule>, notMatches: List<List<String>>) {
    for (example in notMatches) {
        val rule = rules.firstOrNull { it.matches(example) != null }
        if (rule != null) {
            throw ExecPolicyError.ExampleDidMatch(
                rule = rule.toString(),
                example = tryJoin(example),
            )
        }
    }
}
