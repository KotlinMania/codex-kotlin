// port-lint: source execpolicy-legacy/src/error.rs
package ai.solace.coder.execpolicy.legacy

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

typealias ExecPolicyResult<T> = ExecPolicyOutcome<T>

/**
 * Lightweight `Result<T, Error>` used by the legacy execpolicy port.
 */
sealed class ExecPolicyOutcome<out T> {
    data class Ok<T>(val value: T) : ExecPolicyOutcome<T>()
    data class Err(val error: ExecPolicyError) : ExecPolicyOutcome<Nothing>()
}

/**
 * Errors produced while validating a command against a legacy exec policy.
 *
 * Ported from Rust codex-rs/execpolicy-legacy/src/error.rs
 */
@Serializable
@JsonClassDiscriminator("type")
sealed class ExecPolicyError {
    @Serializable
    @SerialName("NoSpecForProgram")
    data class NoSpecForProgram(
        val program: String,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("OptionMissingValue")
    data class OptionMissingValue(
        val program: String,
        val option: String,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("OptionFollowedByOptionInsteadOfValue")
    data class OptionFollowedByOptionInsteadOfValue(
        val program: String,
        val option: String,
        val value: String,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("UnknownOption")
    data class UnknownOption(
        val program: String,
        val option: String,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("UnexpectedArguments")
    data class UnexpectedArguments(
        val program: String,
        val args: List<PositionalArg>,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("DoubleDashNotSupportedYet")
    data class DoubleDashNotSupportedYet(
        val program: String,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("MultipleVarargPatterns")
    data class MultipleVarargPatterns(
        val program: String,
        val first: ArgMatcher,
        val second: ArgMatcher,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("RangeStartExceedsEnd")
    data class RangeStartExceedsEnd(
        val start: Int,
        val end: Int,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("RangeEndOutOfBounds")
    data class RangeEndOutOfBounds(
        val end: Int,
        val len: Int,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("PrefixOverlapsSuffix")
    data object PrefixOverlapsSuffix : ExecPolicyError()

    @Serializable
    @SerialName("NotEnoughArgs")
    data class NotEnoughArgs(
        val program: String,
        val args: List<PositionalArg>,
        val argPatterns: List<ArgMatcher>,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("InternalInvariantViolation")
    data class InternalInvariantViolation(
        val message: String,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("VarargMatcherDidNotMatchAnything")
    data class VarargMatcherDidNotMatchAnything(
        val program: String,
        val matcher: ArgMatcher,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("EmptyFileName")
    data object EmptyFileName : ExecPolicyError()

    @Serializable
    @SerialName("LiteralValueDidNotMatch")
    data class LiteralValueDidNotMatch(
        val expected: String,
        val actual: String,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("InvalidPositiveInteger")
    data class InvalidPositiveInteger(
        val value: String,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("MissingRequiredOptions")
    data class MissingRequiredOptions(
        val program: String,
        val options: List<String>,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("SedCommandNotProvablySafe")
    data class SedCommandNotProvablySafe(
        val command: String,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("ReadablePathNotInReadableFolders")
    data class ReadablePathNotInReadableFolders(
        val file: String,
        val folders: List<String>,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("WriteablePathNotInWriteableFolders")
    data class WriteablePathNotInWriteableFolders(
        val file: String,
        val folders: List<String>,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("CannotCheckRelativePath")
    data class CannotCheckRelativePath(
        val file: String,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("CannotCanonicalizePath")
    data class CannotCanonicalizePath(
        val file: String,
        val error: String,
    ) : ExecPolicyError()
}
