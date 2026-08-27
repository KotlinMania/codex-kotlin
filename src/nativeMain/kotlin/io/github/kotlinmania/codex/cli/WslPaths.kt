// port-lint: source cli/src/wsl_paths.rs
@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlin.experimental.ExperimentalNativeApi::class)

package io.github.kotlinmania.codex.cli

import kotlinx.cinterop.refTo
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread
import platform.posix.getenv
import kotlin.native.OsFamily
import kotlin.native.Platform

/**
 * WSL-specific path helpers used by the updater logic.
 *
 * See https://github.com/openai/codex/issues/6086.
 */
fun isWsl(): Boolean {
    if (Platform.osFamily != OsFamily.LINUX) {
        return false
    }
    val distro = getenv("WSL_DISTRO_NAME")
    if (distro != null) {
        return true
    }
    val file = fopen("/proc/version", "r") ?: return false
    return try {
        val buffer = ByteArray(1024)
        val read = fread(buffer.refTo(0), 1u, 1024u, file)
        if (read > 0u) {
            val content = buffer.decodeToString(0, read.toInt())
            content.lowercase().contains("microsoft")
        } else {
            false
        }
    } catch (_: Throwable) {
        false
    } finally {
        fclose(file)
    }
}

/**
 * Convert a Windows absolute path (`C:\foo\bar` or `C:/foo/bar`) to a WSL mount path (`/mnt/c/foo/bar`).
 * Returns `null` if the input does not look like a Windows drive path.
 */
fun winPathToWsl(path: String): String? {
    if (path.length < 3) return null
    val drive = path[0]
    val sep = path[2]
    if (path[1] != ':' || (sep != '\\' && sep != '/') || !drive.isAsciiAlphabetic()) {
        return null
    }
    val driveLower = drive.lowercaseChar()
    val tail = path.substring(3).replace('\\', '/')
    return if (tail.isEmpty()) {
        "/mnt/$driveLower"
    } else {
        "/mnt/$driveLower/$tail"
    }
}

/**
 * If under WSL and given a Windows-style path, return the equivalent `/mnt/<drive>/…` path.
 * Otherwise returns the input unchanged.
 */
fun normalizeForWsl(path: String): String {
    if (!isWsl()) {
        return path
    }
    val mapped = winPathToWsl(path)
    if (mapped != null) {
        return mapped
    }
    return path
}

private fun Char.isAsciiAlphabetic(): Boolean = (this in 'A'..'Z') || (this in 'a'..'z')
