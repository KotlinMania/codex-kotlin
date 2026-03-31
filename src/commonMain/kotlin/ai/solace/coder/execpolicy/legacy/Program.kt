// port-lint: source execpolicy-legacy/src/program.rs
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

import kotlinx.serialization.Serializable

class ProgramSpec(
    val program: String,
    val systemPath: List<String>,
    val optionBundling: Boolean,
    val combinedFormat: Boolean,
    val allowedOptions: Map<String, Opt>,
    val argPatterns: List<ArgMatcher>,
    private val forbidden: String?,
    private val shouldMatch: List<List<String>>,
    private val shouldNotMatch: List<List<String>>,
) {
    val requiredOptions: Set<String> = allowedOptions
        .filter { (_, opt) -> opt.required }
        .keys
        .toSet()

    companion object {
        fun new(
            program: String,
            systemPath: List<String>,
            optionBundling: Boolean,
            combinedFormat: Boolean,
            allowedOptions: Map<String, Opt>,
            argPatterns: List<ArgMatcher>,
            forbidden: String?,
            shouldMatch: List<List<String>>,
            shouldNotMatch: List<List<String>>,
        ): ProgramSpec = ProgramSpec(
            program = program,
            systemPath = systemPath,
            optionBundling = optionBundling,
            combinedFormat = combinedFormat,
            allowedOptions = allowedOptions,
            argPatterns = argPatterns,
            forbidden = forbidden,
            shouldMatch = shouldMatch,
            shouldNotMatch = shouldNotMatch,
        )
    }

    // TODO(mbolin): The idea is that there should be a set of rules defined for
    // a program and the args should be checked against the rules to determine
    // if the program should be allowed to run.
    fun check(execCall: ExecCall): ExecPolicyResult<MatchedExec> {
        var expectingOptionValue: Pair<String, ArgType>? = null
        val args = mutableListOf<PositionalArg>()
        val matchedFlags = mutableListOf<MatchedFlag>()
        val matchedOpts = mutableListOf<MatchedOpt>()

        for ((index, arg) in execCall.args.withIndex()) {
            val expected = expectingOptionValue
            if (expected != null) {
                // If we are expecting an option value, then the next argument
                // should be the value for the option.
                // This had better not be another option!
                val (name, argType) = expected
                if (arg.startsWith("-")) {
                    return ExecPolicyOutcome.Err(ExecPolicyError.OptionFollowedByOptionInsteadOfValue(
                        program = program,
                        option = name,
                        value = arg,
                    ))
                }

                val matched = when (val result = MatchedOpt.new(name, arg, argType)) {
                    is ExecPolicyOutcome.Err -> return result
                    is ExecPolicyOutcome.Ok -> result.value
                }
                matchedOpts.add(matched)
                expectingOptionValue = null
            } else if (arg == "--") {
                return ExecPolicyOutcome.Err(ExecPolicyError.DoubleDashNotSupportedYet(
                    program = program,
                ))
            } else if (arg.startsWith("-")) {
                val opt = allowedOptions[arg]
                if (opt != null) {
                    when (opt.meta) {
                        is OptMeta.Flag -> {
                            matchedFlags.add(MatchedFlag(name = arg))
                            // A flag does not expect an argument: continue.
                            continue
                        }
                        is OptMeta.Value -> {
                            expectingOptionValue = Pair(arg, opt.meta.argType)
                            continue
                        }
                    }
                }

                return ExecPolicyOutcome.Err(ExecPolicyError.UnknownOption(
                    program = program,
                    option = arg,
                ))
            } else {
                args.add(PositionalArg(
                    index = index,
                    value = arg,
                ))
            }
        }

        if (expectingOptionValue != null) {
            val (name, _) = expectingOptionValue
            return ExecPolicyOutcome.Err(ExecPolicyError.OptionMissingValue(
                program = program,
                option = name,
            ))
        }

        val matchedArgs = when (val result = resolveObservedArgsWithPatterns(program, args, argPatterns)) {
            is ExecPolicyOutcome.Err -> return result
            is ExecPolicyOutcome.Ok -> result.value
        }

        // Verify all required options are present.
        val matchedOptNames = matchedOpts.map { it.name() }.toSet()
        if (!matchedOptNames.containsAll(requiredOptions)) {
            val missing = (requiredOptions - matchedOptNames).sorted()
            return ExecPolicyOutcome.Err(ExecPolicyError.MissingRequiredOptions(
                program = program,
                options = missing,
            ))
        }

        val exec = ValidExec(
            program = program,
            flags = matchedFlags,
            opts = matchedOpts,
            args = matchedArgs,
            systemPath = systemPath,
        )
        return when (val reason = forbidden) {
            null -> ExecPolicyOutcome.Ok(MatchedExec.Match(exec = exec))
            else -> ExecPolicyOutcome.Ok(MatchedExec.Forbidden(
                cause = Forbidden.Exec(exec = exec),
                reason = reason,
            ))
        }
    }

    fun verifyShouldMatchList(): List<PositiveExampleFailedCheck> {
        val violations = mutableListOf<PositiveExampleFailedCheck>()
        for (good in shouldMatch) {
            val execCall = ExecCall(program = program, args = good)
            when (val result = check(execCall)) {
                is ExecPolicyOutcome.Ok -> {}
                is ExecPolicyOutcome.Err -> {
                    violations.add(PositiveExampleFailedCheck(
                        program = program,
                        args = good,
                        error = result.error,
                    ))
                }
            }
        }
        return violations
    }

    fun verifyShouldNotMatchList(): List<NegativeExamplePassedCheck> {
        val violations = mutableListOf<NegativeExamplePassedCheck>()
        for (bad in shouldNotMatch) {
            val execCall = ExecCall(program = program, args = bad)
            when (check(execCall)) {
                is ExecPolicyOutcome.Ok -> {
                    violations.add(NegativeExamplePassedCheck(
                        program = program,
                        args = bad,
                    ))
                }
                is ExecPolicyOutcome.Err -> {}
            }
        }
        return violations
    }
}

@Serializable
sealed class MatchedExec {
    @Serializable
    data class Match(val exec: ValidExec) : MatchedExec()

    @Serializable
    data class Forbidden(val cause: ai.solace.coder.execpolicy.legacy.Forbidden, val reason: String) : MatchedExec()
}

@Serializable
sealed class Forbidden {
    @Serializable
    data class Program(val program: String, val execCall: ExecCall) : Forbidden()

    @Serializable
    data class Arg(val arg: String, val execCall: ExecCall) : Forbidden()

    @Serializable
    data class Exec(val exec: ValidExec) : Forbidden()
}

data class PositiveExampleFailedCheck(
    val program: String,
    val args: List<String>,
    val error: ExecPolicyError,
)

data class NegativeExamplePassedCheck(
    val program: String,
    val args: List<String>,
)
