// port-lint: source core/src/flags.rs
package io.github.kotlinmania.codex.core

import io.github.kotlinmania.codex.utils.Environment

/**
 * Environment-driven flags (the upstream `envFlags!` macro).
 *
 * Fixture path for offline tests (see client.rs).
 */
val CODEX_RS_SSE_FIXTURE: String?
    get() = Environment.get("CODEX_RS_SSE_FIXTURE")
