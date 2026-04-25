// port-lint: source codex-rs/core/src/flags.rs
package ai.solace.coder.core

import platform.posix.getenv
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString

/**
 * Environment-driven flags (Rust's `env_flags!` macro).
 *
 * Fixture path for offline tests (see client.rs).
 */
@OptIn(ExperimentalForeignApi::class)
val CODEX_RS_SSE_FIXTURE: String?
    get() = getenv("CODEX_RS_SSE_FIXTURE")?.toKString()
