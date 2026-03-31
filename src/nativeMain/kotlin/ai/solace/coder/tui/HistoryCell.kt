package ai.solace.coder.tui

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

/**
 * Get the home directory of the current user.
 *
 * Uses the HOME environment variable on native platforms.
 */
@OptIn(ExperimentalForeignApi::class)
actual fun getHomeDir(): String? = getenv("HOME")?.toKString()
