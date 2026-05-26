// port-lint: source cli/src/exit_status.rs
@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlin.experimental.ExperimentalNativeApi::class)

package io.github.kotlinmania.codex.cli

import kotlin.native.Platform
import kotlin.native.OsFamily
import kotlin.system.exitProcess

/**
 * Result describing how a child process terminated.
 *
 * Mirrors the fields inspected on `std::process::ExitStatus` by the Rust
 * original, which exposes the raw exit code on all platforms and, on Unix,
 * also the terminating signal.
 */
data class ProcessExitStatus(val code: Int?, val signal: Int? = null)

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
    val isWindows = Platform.osFamily == OsFamily.WINDOWS
    val code = status.code
    if (code != null) {
        exitProcess(code)
    } else if (!isWindows && status.signal != null) {
        exitProcess(128 + status.signal)
    } else {
        exitProcess(1)
    }
}
