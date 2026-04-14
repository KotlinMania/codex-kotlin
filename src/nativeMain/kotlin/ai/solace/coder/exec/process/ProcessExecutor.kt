// port-lint: source core/src/exec.rs (process_exec_tool_call)
package ai.solace.coder.exec.process

/**
 * Alias for the core `Exec` class used by tool handlers and session code as a
 * process-execution entry point. Rust exposes this as the free function
 * `process_exec_tool_call`; Kotlin groups the equivalent methods on `Exec`.
 */
typealias ProcessExecutor = ai.solace.coder.core.Exec
