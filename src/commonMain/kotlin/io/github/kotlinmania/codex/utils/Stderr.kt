// port-lint: ignore
// Platform-agnostic helper for writing inline progress text to stderr.
package io.github.kotlinmania.codex.utils

/**
 * Write [text] to standard error and flush immediately. Used for inline
 * progress reporters that need every chunk visible without buffering.
 */
expect fun writeStderrInline(text: String)
