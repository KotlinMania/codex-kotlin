// port-lint: source codex-rs/core/src/functionTool.rs
package ai.solace.coder.core

/**
 * Error type for function call failures.
 *
 * Represents errors that can occur when executing function/tool calls.
 */
sealed class FunctionCallError : Exception() {
    /**
     * Error message to send back to the model.
     */
    data class RespondToModel(val text: String) : FunctionCallError() {
        override val message: String get() = text
        override fun toString(): String = text
    }

    /**
     * The function call was denied (e.g., by user or policy).
     */
    data class Denied(val reason: String) : FunctionCallError() {
        override val message: String get() = reason
        override fun toString(): String = reason
    }

    /**
     * LocalShellCall is missing a callId or id.
     */
    data object MissingLocalShellCallId : FunctionCallError() {
        override val message: String get() = "LocalShellCall without call_id or id"
        override fun toString(): String = message
    }

    /**
     * A fatal error that cannot be recovered from.
     */
    data class Fatal(val reason: String) : FunctionCallError() {
        override val message: String get() = "Fatal error: $reason"
        override fun toString(): String = message
    }
}
