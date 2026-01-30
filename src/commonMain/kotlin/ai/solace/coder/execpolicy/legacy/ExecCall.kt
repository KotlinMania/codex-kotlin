// port-lint: source execpolicy-legacy/src/exec_call.rs
package ai.solace.coder.execpolicy.legacy

import kotlinx.serialization.Serializable

/**
 * Represents a program execution call with program name and arguments.
 * 
 * This is used in execution policy evaluation to represent the command
 * that would be executed.
 * 
 * Ported from Rust codex-rs/execpolicy-legacy/src/exec_call.rs
 */
@Serializable
data class ExecCall(
    val program: String,
    val args: List<String>
) {
    companion object {
        /**
         * Create an ExecCall from a program name and array of arguments.
         * 
         * @param program The program to execute
         * @param args The arguments to pass
         */
        fun new(program: String, args: List<String>): ExecCall {
            return ExecCall(program, args)
        }
    }
    
    /**
     * Format as a shell command string for display.
     * 
     * @return String representation: "program arg1 arg2 ..."
     */
    override fun toString(): String {
        return buildString {
            append(program)
            for (arg in args) {
                append(" ")
                append(arg)
            }
        }
    }
}
