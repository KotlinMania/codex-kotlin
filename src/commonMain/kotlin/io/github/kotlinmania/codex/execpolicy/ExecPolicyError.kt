// port-lint: source execpolicy/src/error.rs
package io.github.kotlinmania.codex.execpolicy

/**
 * Errors returned by the execpolicy crate.
 *
 * Ported from Rust codex-rs/execpolicy/src/error.rs
 */
sealed class ExecPolicyError(message: String) : RuntimeException(message) {
    class InvalidDecision(val value: String) : ExecPolicyError("invalid decision: $value")

    class InvalidPattern(val value: String) : ExecPolicyError("invalid pattern element: $value")

    class InvalidExample(val value: String) : ExecPolicyError("invalid example: $value")

    class ExampleDidNotMatch(
        val rules: List<String>,
        val examples: List<String>,
    ) : ExecPolicyError(
        "expected every example to match at least one rule. rules: $rules; unmatched examples: $examples",
    )

    class ExampleDidMatch(
        val rule: String,
        val example: String,
    ) : ExecPolicyError("expected example to not match rule `$rule`: $example")

    class Starlark(val starlarkCause: Throwable) : ExecPolicyError("starlark error: ${starlarkCause.message ?: starlarkCause}")
}
