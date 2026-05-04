// port-lint: ignore
// Platform-agnostic path helpers backed by an expect/actual layer.
package io.github.solaceharmony.codex.utils

/**
 * Resolve symlinks in `path` and return its canonical absolute form,
 * or the input unchanged if canonicalization isn't supported on the
 * current platform.
 */
expect fun canonicalizePath(path: String): String
