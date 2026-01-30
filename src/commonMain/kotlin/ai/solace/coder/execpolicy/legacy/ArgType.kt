// port-lint: source execpolicy-legacy/src/arg_type.rs
package ai.solace.coder.execpolicy.legacy

import kotlinx.serialization.Serializable

/**
 * Type constraint for a command argument in execution policy rules.
 */
@Serializable
sealed class ArgType {
    @Serializable
    data class Literal(val value: String) : ArgType() {
        override fun toString(): String = value
    }
    
    /** We cannot say what this argument represents, but it is *not* a file path. */
    @Serializable
    data object OpaqueNonFile : ArgType() {
        override fun toString(): String = "OpaqueNonFile"
    }
    
    /** A file (or directory) that can be expected to be read as part of this command. */
    @Serializable
    data object ReadableFile : ArgType() {
        override fun toString(): String = "ReadableFile"
    }
    
    /** A file (or directory) that can be expected to be written as part of this command. */
    @Serializable
    data object WriteableFile : ArgType() {
        override fun toString(): String = "WriteableFile"
    }
    
    /** Positive integer, like one that is required for `head -n`. */
    @Serializable
    data object PositiveInteger : ArgType() {
        override fun toString(): String = "PositiveInteger"
    }
    
    /** Bespoke arg type for a safe sed command. */
    @Serializable
    data object SedCommand : ArgType() {
        override fun toString(): String = "SedCommand"
    }
    
    /** Type is unknown: it may or may not be a file. */
    @Serializable
    data object Unknown : ArgType() {
        override fun toString(): String = "Unknown"
    }
    
    fun validate(value: String): ExecPolicyResult<Unit> {
        return when (this) {
            is Literal -> {
                if (value != this.value) {
                    ExecPolicyResult.Err(ExecPolicyError.LiteralValueDidNotMatch(
                        expected = this.value,
                        actual = value
                    ))
                } else {
                    ExecPolicyResult.Ok(Unit)
                }
            }
            is ReadableFile -> {
                if (value.isEmpty()) {
                    ExecPolicyResult.Err(ExecPolicyError.EmptyFileName)
                } else {
                    ExecPolicyResult.Ok(Unit)
                }
            }
            is WriteableFile -> {
                if (value.isEmpty()) {
                    ExecPolicyResult.Err(ExecPolicyError.EmptyFileName)
                } else {
                    ExecPolicyResult.Ok(Unit)
                }
            }
            is OpaqueNonFile, is Unknown -> ExecPolicyResult.Ok(Unit)
            is PositiveInteger -> {
                val parsed = value.toULongOrNull()
                when {
                    parsed == null -> ExecPolicyResult.Err(ExecPolicyError.InvalidPositiveInteger(value))
                    parsed == 0UL -> ExecPolicyResult.Err(ExecPolicyError.InvalidPositiveInteger(value))
                    else -> ExecPolicyResult.Ok(Unit)
                }
            }
            is SedCommand -> parseSedCommand(value)
        }
    }
    
    fun mightWriteFile(): Boolean {
        return when (this) {
            is WriteableFile, is Unknown -> true
            is Literal, is OpaqueNonFile, is PositiveInteger, is ReadableFile, is SedCommand -> false
        }
    }
}

