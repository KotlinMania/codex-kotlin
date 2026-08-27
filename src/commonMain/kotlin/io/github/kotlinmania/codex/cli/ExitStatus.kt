// port-lint: source cli/src/exit_status.rs
package io.github.kotlinmania.codex.cli

import io.github.kotlinmania.codex.core.platformExitProcess
import io.github.kotlinmania.codex.core.platformIsWindows

/**
 * Result describing how a child process terminated.
 *
 * Mirrors the fields inspected on `std::process::ExitStatus` by the Rust
 * original, which exposes the raw exit code on all platforms and, on Unix,
 * also the terminating signal.
 */
data class ProcessExitStatus(
    val code: Int?,
    val signal: Int? = null,
)

/**
 * Translate a child process exit status into a process exit, matching the
 * semantics of `cli/src/exitStatus.rs`.
 *
 * On Unix, a child killed by a signal exits with `128 + signal`. When no code
 * or signal is available, fall back to `1`.
 *
 * This function never returns.
 */
fun handleExitStatus(status: ProcessExitStatus): Nothing {
    val isWindows = platformIsWindows()
    val code = status.code
    if (code != null) {
        platformExitProcess(code)
    } else if (!isWindows && status.signal != null) {
        platformExitProcess(128 + status.signal)
    } else {
        platformExitProcess(1)
    }
}
