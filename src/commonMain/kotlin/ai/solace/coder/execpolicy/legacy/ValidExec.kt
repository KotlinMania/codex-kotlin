// port-lint: source execpolicy-legacy/src/valid_exec.rs
package ai.solace.coder.execpolicy.legacy

import kotlinx.serialization.Serializable

/** exec() invocation that has been accepted by a `Policy`. */
@Serializable
data class ValidExec(
    val program: String,
    val flags: List<MatchedFlag> = emptyList(),
    val opts: List<MatchedOpt> = emptyList(),
    val args: List<MatchedArg> = emptyList(),
    /**
     * If non-empty, a prioritized list of paths to try instead of [program].
     *
     * For example, `/bin/ls` is harder to compromise than whatever `ls` happens
     * to be in the user's `$PATH`, so `/bin/ls` would be included for `ls`.
     * The caller is free to disregard this list and use [program].
     */
    val systemPath: List<String> = emptyList(),
) {
    companion object {
        fun new(program: String, args: List<MatchedArg>, systemPath: List<String>): ValidExec =
            ValidExec(
                program = program,
                flags = emptyList(),
                opts = emptyList(),
                args = args,
                systemPath = systemPath,
            )
    }

    /**
     * Whether a possible side effect of running this command includes writing
     * a file.
     */
    fun mightWriteFiles(): Boolean =
        opts.any { opt -> opt.type.mightWriteFile() } ||
            args.any { arg -> arg.type.mightWriteFile() }
}

@Serializable
data class MatchedArg(
    val index: Int,
    val type: ArgType,
    val value: String,
) {
    companion object {
        fun new(index: Int, type: ArgType, value: String): ExecPolicyResult<MatchedArg> {
            return when (val validated = type.validate(value)) {
                is ExecPolicyOutcome.Err -> validated
                is ExecPolicyOutcome.Ok -> ExecPolicyOutcome.Ok(MatchedArg(index = index, type = type, value = value))
            }
        }
    }
}

/** A match for an option declared with opt() in a .policy file. */
@Serializable
data class MatchedOpt(
    /** Name of the option that was matched. */
    val name: String,
    /** Value supplied for the option. */
    val value: String,
    /** Type of the value supplied for the option. */
    val type: ArgType,
) {
    companion object {
        fun new(name: String, value: String, type: ArgType): ExecPolicyResult<MatchedOpt> {
            return when (val validated = type.validate(value)) {
                is ExecPolicyOutcome.Err -> validated
                is ExecPolicyOutcome.Ok -> ExecPolicyOutcome.Ok(MatchedOpt(name = name, value = value, type = type))
            }
        }
    }

    fun name(): String = name
}

@Serializable
data class MatchedFlag(
    /** Name of the flag that was matched. */
    val name: String,
) {
    companion object {
        fun new(name: String): MatchedFlag =
            MatchedFlag(name = name)
    }
}

