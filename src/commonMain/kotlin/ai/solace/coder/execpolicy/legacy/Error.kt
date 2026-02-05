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
 * Note: The Rust version has more variants; this file can be extended as more
 * of `execpolicy-legacy/` is ported.
 */
@Serializable
@JsonClassDiscriminator("type")
sealed class ExecPolicyError {
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
    @SerialName("SedCommandNotProvablySafe")
    data class SedCommandNotProvablySafe(
        val command: String,
    ) : ExecPolicyError()

    @Serializable
    @SerialName("InternalInvariantViolation")
    data class InternalInvariantViolation(
        val message: String,
    ) : ExecPolicyError()
}
