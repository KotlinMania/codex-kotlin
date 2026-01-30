// port-lint: source core/src/terminal.rs
package ai.solace.coder.core.terminal

import kotlinx.cinterop.toKString
import platform.posix.getenv

/**
 * Native implementation of environment variable access.
 */
internal actual fun getEnv(name: String): String? {
    return getenv(name)?.toKString()
}
