package io.github.kotlinmania.codex.execpolicy

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class PolicyTest {
    private fun tokens(vararg cmd: String): List<String> = cmd.toList()

    private fun prefixRule(
        program: String,
        rest: List<PatternToken> = emptyList(),
        decision: Decision = Decision.Allow,
    ): PrefixRule = PrefixRule(
        pattern = PrefixPattern(first = program, rest = rest),
        decision = decision,
    )

    @Test
    fun decisionParse_roundTripsValues() {
        assertEquals(Decision.Allow, Decision.parse("allow"))
        assertEquals(Decision.Prompt, Decision.parse("prompt"))
        assertEquals(Decision.Forbidden, Decision.parse("forbidden"))
        assertFailsWith<ExecPolicyError.InvalidDecision> { Decision.parse("bogus") }
    }

    @Test
    fun basicMatch() {
        val rule = prefixRule("git", rest = listOf(PatternToken.Single("status")))
        val policy = Policy(mapOf("git" to listOf(rule)))
        val evaluation = policy.check(tokens("git", "status"))
        assertEquals(
            Evaluation.Match(
                decision = Decision.Allow,
                matchedRules = listOf(
                    RuleMatch.PrefixRuleMatch(
                        matchedPrefix = tokens("git", "status"),
                        decision = Decision.Allow,
                    ),
                ),
            ),
            evaluation,
        )
    }

    @Test
    fun strictestDecisionWinsAcrossMatches() {
        val ruleGit = prefixRule("git", decision = Decision.Prompt)
        val ruleGitCommit = prefixRule(
            "git",
            rest = listOf(PatternToken.Single("commit")),
            decision = Decision.Forbidden,
        )
        val policy = Policy(mapOf("git" to listOf(ruleGit, ruleGitCommit)))
        val commit = policy.check(tokens("git", "commit", "-m", "hi"))
        assertEquals(
            Evaluation.Match(
                decision = Decision.Forbidden,
                matchedRules = listOf(
                    RuleMatch.PrefixRuleMatch(
                        matchedPrefix = tokens("git"),
                        decision = Decision.Prompt,
                    ),
                    RuleMatch.PrefixRuleMatch(
                        matchedPrefix = tokens("git", "commit"),
                        decision = Decision.Forbidden,
                    ),
                ),
            ),
            commit,
        )
    }

    @Test
    fun altsPatternMatchesAnyAlternative() {
        val rule = prefixRule(
            "bash",
            rest = listOf(PatternToken.Alts(listOf("-c", "-l"))),
        )
        val policy = Policy(mapOf("bash" to listOf(rule)))
        val bashEval = policy.check(tokens("bash", "-c", "echo", "hi"))
        assertEquals(
            Evaluation.Match(
                decision = Decision.Allow,
                matchedRules = listOf(
                    RuleMatch.PrefixRuleMatch(
                        matchedPrefix = tokens("bash", "-c"),
                        decision = Decision.Allow,
                    ),
                ),
            ),
            bashEval,
        )
    }

    @Test
    fun noMatchWhenFirstTokenDiffers() {
        val rule = prefixRule("git")
        val policy = Policy(mapOf("git" to listOf(rule)))
        assertEquals(Evaluation.NoMatch, policy.check(tokens("ls")))
        assertEquals(Evaluation.NoMatch, policy.check(emptyList()))
    }

    @Test
    fun checkMultipleAggregatesAcrossCommands() {
        val ruleGit = prefixRule("git", decision = Decision.Prompt)
        val ruleGitCommit = prefixRule(
            "git",
            rest = listOf(PatternToken.Single("commit")),
            decision = Decision.Forbidden,
        )
        val policy = Policy(mapOf("git" to listOf(ruleGit, ruleGitCommit)))
        val evaluation = policy.checkMultiple(
            listOf(
                tokens("git", "status"),
                tokens("git", "commit", "-m", "hi"),
            ),
        )
        assertTrue(evaluation.isMatch())
        evaluation as Evaluation.Match
        assertEquals(Decision.Forbidden, evaluation.decision)
        assertEquals(3, evaluation.matchedRules.size)
    }

    @Test
    fun validateMatchExamplesReportsUnmatched() {
        val rule = prefixRule("git", rest = listOf(PatternToken.Single("status")))
        assertFailsWith<ExecPolicyError.ExampleDidNotMatch> {
            validateMatchExamples(listOf(rule), listOf(tokens("git", "log")))
        }
    }

    @Test
    fun validateNotMatchExamplesFlagsMatches() {
        val rule = prefixRule("git")
        assertFailsWith<ExecPolicyError.ExampleDidMatch> {
            validateNotMatchExamples(listOf(rule), listOf(tokens("git", "anything")))
        }
    }
}
