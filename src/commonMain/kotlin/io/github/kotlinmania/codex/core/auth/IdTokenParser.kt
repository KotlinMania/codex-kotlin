package io.github.kotlinmania.codex.core.auth

/**
 * Platform-specific ID Token parser.
 */
expect fun parseIdToken(jwt: String): Result<IdTokenInfo>
