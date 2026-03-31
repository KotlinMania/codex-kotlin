// port-lint: source execpolicy-legacy/src/policy.rs
package ai.solace.coder.execpolicy.legacy

/*
 * Copyright 2019 The Starlark in Rust Authors.
 * Copyright (c) Facebook, Inc. and its affiliates.
 * Copyright (c) 2025 Sydney Renee, The Solace Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Regex pattern and reason describing a forbidden program.
 *
 * Ported from Rust policy_parser.rs ForbiddenProgramRegex.
 */
data class ForbiddenProgramRegex(
    val regex: Regex,
    val reason: String,
)

class LegacyPolicy private constructor(
    private val programs: Map<String, List<ProgramSpec>>,
    private val forbiddenProgramRegexes: List<ForbiddenProgramRegex>,
    private val forbiddenSubstringsPattern: Regex?,
) {
    companion object {
        fun new(
            programs: Map<String, List<ProgramSpec>>,
            forbiddenProgramRegexes: List<ForbiddenProgramRegex>,
            forbiddenSubstrings: List<String>,
        ): Result<LegacyPolicy> {
            val forbiddenSubstringsPattern = if (forbiddenSubstrings.isEmpty()) {
                null
            } else {
                val escapedSubstrings = forbiddenSubstrings
                    .joinToString("|") { Regex.escape(it) }
                try {
                    Regex("($escapedSubstrings)")
                } catch (e: Exception) {
                    return Result.failure(e)
                }
            }
            return Result.success(LegacyPolicy(
                programs = programs,
                forbiddenProgramRegexes = forbiddenProgramRegexes,
                forbiddenSubstringsPattern = forbiddenSubstringsPattern,
            ))
        }
    }

    fun check(execCall: ExecCall): ExecPolicyResult<MatchedExec> {
        val program = execCall.program
        val args = execCall.args

        for (forbidden in forbiddenProgramRegexes) {
            if (forbidden.regex.containsMatchIn(program)) {
                return ExecPolicyOutcome.Ok(MatchedExec.Forbidden(
                    cause = Forbidden.Program(
                        program = program,
                        execCall = execCall,
                    ),
                    reason = forbidden.reason,
                ))
            }
        }

        for (arg in args) {
            val pattern = forbiddenSubstringsPattern
            if (pattern != null && pattern.containsMatchIn(arg)) {
                return ExecPolicyOutcome.Ok(MatchedExec.Forbidden(
                    cause = Forbidden.Arg(
                        arg = arg,
                        execCall = execCall,
                    ),
                    reason = "arg `$arg` contains forbidden substring",
                ))
            }
        }

        var lastErr: ExecPolicyResult<MatchedExec> = ExecPolicyOutcome.Err(
            ExecPolicyError.NoSpecForProgram(program = program)
        )
        val specList = programs[program]
        if (specList != null) {
            for (spec in specList) {
                when (val result = spec.check(execCall)) {
                    is ExecPolicyOutcome.Ok -> return result
                    is ExecPolicyOutcome.Err -> {
                        lastErr = result
                    }
                }
            }
        }
        return lastErr
    }

    fun checkEachGoodListIndividually(): List<PositiveExampleFailedCheck> {
        val violations = mutableListOf<PositiveExampleFailedCheck>()
        for ((_, specList) in programs) {
            for (spec in specList) {
                violations.addAll(spec.verifyShouldMatchList())
            }
        }
        return violations
    }

    fun checkEachBadListIndividually(): List<NegativeExamplePassedCheck> {
        val violations = mutableListOf<NegativeExamplePassedCheck>()
        for ((_, specList) in programs) {
            for (spec in specList) {
                violations.addAll(spec.verifyShouldNotMatchList())
            }
        }
        return violations
    }
}
