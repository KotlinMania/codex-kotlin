// port-lint: source execpolicy-legacy/src/arg_resolver.rs
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

@Serializable
data class PositionalArg(
    val index: Int,
    val value: String,
)

fun resolveObservedArgsWithPatterns(
    program: String,
    args: List<PositionalArg>,
    argPatterns: List<ArgMatcher>,
): ExecPolicyResult<List<MatchedArg>> {
    // Naive matching implementation. Among `argPatterns`, there is allowed to
    // be at most one vararg pattern. Assuming `argPatterns` is non-empty, we
    // end up with either:
    //
    // - all `argPatterns` in `prefixPatterns`
    // - `argPatterns` split across `prefixPatterns` (which could be empty),
    //   one `varargPattern`, and `suffixPatterns` (which could also empty).
    //
    // From there, we start by matching everything in `prefixPatterns`.
    // Then we calculate how many positional args should be matched by
    // `suffixPatterns` and use that to determine how many args are left to
    // be matched by `varargPattern` (which could be zero).
    //
    // After associating positional args with `varargPattern`, we match the
    // `suffixPatterns` with the remaining args.
    val partitioned = when (val result = partitionArgs(program, argPatterns)) {
        is ExecPolicyOutcome.Err -> return result
        is ExecPolicyOutcome.Ok -> result.value
    }

    val matchedArgs = mutableListOf<MatchedArg>()

    val prefix = when (val result = getRangeChecked(args, 0, partitioned.numPrefixArgs)) {
        is ExecPolicyOutcome.Err -> return result
        is ExecPolicyOutcome.Ok -> result.value
    }
    var prefixArgIndex = 0
    for (pattern in partitioned.prefixPatterns) {
        val n = pattern.cardinality().isExact()
            ?: return ExecPolicyOutcome.Err(ExecPolicyError.InternalInvariantViolation(
                message = "expected exact cardinality",
            ))
        for (positionalArg in prefix.subList(prefixArgIndex, prefixArgIndex + n)) {
            val matchedArg = when (val result = MatchedArg.new(positionalArg.index, pattern.argType(), positionalArg.value)) {
                is ExecPolicyOutcome.Err -> return result
                is ExecPolicyOutcome.Ok -> result.value
            }
            matchedArgs.add(matchedArg)
        }
        prefixArgIndex += n
    }

    if (partitioned.numSuffixArgs > args.size) {
        return ExecPolicyOutcome.Err(ExecPolicyError.NotEnoughArgs(
            program = program,
            args = args.map { PositionalArg(it.index, it.value) },
            argPatterns = argPatterns,
        ))
    }

    val initialSuffixArgsIndex = args.size - partitioned.numSuffixArgs
    if (prefixArgIndex > initialSuffixArgsIndex) {
        return ExecPolicyOutcome.Err(ExecPolicyError.PrefixOverlapsSuffix)
    }

    val varargPattern = partitioned.varargPattern
    if (varargPattern != null) {
        val vararg = when (val result = getRangeChecked(args, prefixArgIndex, initialSuffixArgsIndex)) {
            is ExecPolicyOutcome.Err -> return result
            is ExecPolicyOutcome.Ok -> result.value
        }
        when (varargPattern.cardinality()) {
            ArgMatcherCardinality.One -> {
                return ExecPolicyOutcome.Err(ExecPolicyError.InternalInvariantViolation(
                    message = "vararg pattern should not have cardinality of one",
                ))
            }
            ArgMatcherCardinality.AtLeastOne -> {
                if (vararg.isEmpty()) {
                    return ExecPolicyOutcome.Err(ExecPolicyError.VarargMatcherDidNotMatchAnything(
                        program = program,
                        matcher = varargPattern,
                    ))
                } else {
                    for (positionalArg in vararg) {
                        val matchedArg = when (val result = MatchedArg.new(positionalArg.index, varargPattern.argType(), positionalArg.value)) {
                            is ExecPolicyOutcome.Err -> return result
                            is ExecPolicyOutcome.Ok -> result.value
                        }
                        matchedArgs.add(matchedArg)
                    }
                }
            }
            ArgMatcherCardinality.ZeroOrMore -> {
                for (positionalArg in vararg) {
                    val matchedArg = when (val result = MatchedArg.new(positionalArg.index, varargPattern.argType(), positionalArg.value)) {
                        is ExecPolicyOutcome.Err -> return result
                        is ExecPolicyOutcome.Ok -> result.value
                    }
                    matchedArgs.add(matchedArg)
                }
            }
        }
    }

    val suffix = when (val result = getRangeChecked(args, initialSuffixArgsIndex, args.size)) {
        is ExecPolicyOutcome.Err -> return result
        is ExecPolicyOutcome.Ok -> result.value
    }
    var suffixArgIndex = 0
    for (pattern in partitioned.suffixPatterns) {
        val n = pattern.cardinality().isExact()
            ?: return ExecPolicyOutcome.Err(ExecPolicyError.InternalInvariantViolation(
                message = "expected exact cardinality",
            ))
        for (positionalArg in suffix.subList(suffixArgIndex, suffixArgIndex + n)) {
            val matchedArg = when (val result = MatchedArg.new(positionalArg.index, pattern.argType(), positionalArg.value)) {
                is ExecPolicyOutcome.Err -> return result
                is ExecPolicyOutcome.Ok -> result.value
            }
            matchedArgs.add(matchedArg)
        }
        suffixArgIndex += n
    }

    return if (matchedArgs.size < args.size) {
        val extraArgs = when (val result = getRangeChecked(args, matchedArgs.size, args.size)) {
            is ExecPolicyOutcome.Err -> return result
            is ExecPolicyOutcome.Ok -> result.value
        }
        ExecPolicyOutcome.Err(ExecPolicyError.UnexpectedArguments(
            program = program,
            args = extraArgs,
        ))
    } else {
        ExecPolicyOutcome.Ok(matchedArgs)
    }
}

private data class PartitionedArgs(
    val numPrefixArgs: Int = 0,
    val numSuffixArgs: Int = 0,
    val prefixPatterns: List<ArgMatcher> = emptyList(),
    val suffixPatterns: List<ArgMatcher> = emptyList(),
    val varargPattern: ArgMatcher? = null,
)

private fun partitionArgs(program: String, argPatterns: List<ArgMatcher>): ExecPolicyResult<PartitionedArgs> {
    var inPrefix = true
    var numPrefixArgs = 0
    var numSuffixArgs = 0
    val prefixPatterns = mutableListOf<ArgMatcher>()
    val suffixPatterns = mutableListOf<ArgMatcher>()
    var varargPattern: ArgMatcher? = null

    for (pattern in argPatterns) {
        val exact = pattern.cardinality().isExact()
        if (exact != null) {
            if (inPrefix) {
                prefixPatterns.add(pattern)
                numPrefixArgs += exact
            } else {
                suffixPatterns.add(pattern)
                numSuffixArgs += exact
            }
        } else {
            val existing = varargPattern
            if (existing == null) {
                varargPattern = pattern
                inPrefix = false
            } else {
                return ExecPolicyOutcome.Err(ExecPolicyError.MultipleVarargPatterns(
                    program = program,
                    first = existing,
                    second = pattern,
                ))
            }
        }
    }

    return ExecPolicyOutcome.Ok(PartitionedArgs(
        numPrefixArgs = numPrefixArgs,
        numSuffixArgs = numSuffixArgs,
        prefixPatterns = prefixPatterns,
        suffixPatterns = suffixPatterns,
        varargPattern = varargPattern,
    ))
}

private fun <T> getRangeChecked(list: List<T>, start: Int, end: Int): ExecPolicyResult<List<T>> {
    return if (start > end) {
        ExecPolicyOutcome.Err(ExecPolicyError.RangeStartExceedsEnd(
            start = start,
            end = end,
        ))
    } else if (end > list.size) {
        ExecPolicyOutcome.Err(ExecPolicyError.RangeEndOutOfBounds(
            end = end,
            len = list.size,
        ))
    } else {
        ExecPolicyOutcome.Ok(list.subList(start, end))
    }
}
