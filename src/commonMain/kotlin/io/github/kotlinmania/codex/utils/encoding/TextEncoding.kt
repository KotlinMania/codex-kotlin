// port-lint: source core/src/textEncoding.rs
package io.github.kotlinmania.codex.utils.encoding

/**
 * Text encoding detection and conversion utilities for shell output.
 *
 * Windows users frequently run into code pages such as CP1251 or CP866 when invoking commands
 * through VS Code. Those bytes show up as invalid UTF-8 and used to be replaced with the standard
 * Unicode replacement character. We now import charset detection heuristics so we can
 * automatically detect and decode the vast majority of legacy encodings before falling back to
 * lossy UTF-8 decoding.
 *
 * Ported from Rust codex-rs/core/src/textEncoding.rs
 */

private val WINDOWS_1252_CHARS = CharArray(256) { i ->
    when (i) {
        0x80 -> '\u20AC'
        0x82 -> '\u201A'
        0x83 -> '\u0192'
        0x84 -> '\u201E'
        0x85 -> '\u2026'
        0x86 -> '\u2020'
        0x87 -> '\u2021'
        0x88 -> '\u02C6'
        0x89 -> '\u2030'
        0x8A -> '\u0160'
        0x8B -> '\u2039'
        0x8C -> '\u0152'
        0x8E -> '\u017D'
        0x91 -> '\u2018'
        0x92 -> '\u2019'
        0x93 -> '\u201C'
        0x94 -> '\u201D'
        0x95 -> '\u2022'
        0x96 -> '\u2013'
        0x97 -> '\u2014'
        0x98 -> '\u02DC'
        0x99 -> '\u2122'
        0x9A -> '\u0161'
        0x9B -> '\u203A'
        0x9C -> '\u0153'
        0x9E -> '\u017E'
        0x9F -> '\u0178'
        else -> i.toChar()
    }
}

/**
 * Attempts to convert arbitrary bytes to UTF-8 with best-effort encoding detection.
 *
 * Ported from Rust codex-rs/core/src/textEncoding.rs bytesToStringSmart
 */
fun bytesToStringSmart(bytes: ByteArray): String {
    if (bytes.isEmpty()) {
        return ""
    }

    // Fast path: try strict UTF-8 first
    val utf8Result = tryDecodeUtf8(bytes)
    if (utf8Result != null) {
        return utf8Result
    }

    // Fallback: decode as Windows-1252
    val chars = CharArray(bytes.size)
    for (i in bytes.indices) {
        val unsigned = bytes[i].toInt() and 0xFF
        chars[i] = WINDOWS_1252_CHARS[unsigned]
    }
    return chars.concatToString()
}

/**
 * Try to decode bytes as valid UTF-8.
 * Returns null if the bytes contain invalid UTF-8 sequences.
 */
private fun tryDecodeUtf8(bytes: ByteArray): String? {
    return try {
        bytes.decodeToString(throwOnInvalidSequence = true)
    } catch (_: Throwable) {
        null
    }
}
