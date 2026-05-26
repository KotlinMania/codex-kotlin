// port-lint: source cli/src/wsl_paths.rs
@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlin.experimental.ExperimentalNativeApi::class)

package io.github.kotlinmania.codex.cli

import kotlin.native.OsFamily
import kotlin.native.Platform
import kotlinx.cinterop.toKString
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import platform.posix.getenv

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
    val procVersion = Path("/proc/version")
    if (!SystemFileSystem.exists(procVersion)) {
        return false
    }
    return try {
        val version = SystemFileSystem.source(procVersion).buffered().use { it.readString() }
        version.lowercase().contains("microsoft")
    } catch (_: Throwable) {
        false
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
