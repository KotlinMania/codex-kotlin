// port-lint: source apply-patch/src/seek_sequence.rs
package ai.solace.coder.applypatch

/**
 * Attempt to find the sequence of [pattern] lines within [lines] beginning at or after [start].
 * Returns the starting index of the match or null if not found. Matches are attempted with
 * decreasing strictness: exact match, then ignoring trailing whitespace, then ignoring leading
 * and trailing whitespace. When [eof] is true, we first try starting at the end-of-file (so that
 * patterns intended to match file endings are applied at the end), and fall back to searching
 * from [start] if needed.
 *
 * Special cases handled defensively:
 *  - Empty [pattern] -> returns [start] (no-op match)
 *  - pattern.size > lines.size -> returns null (cannot match)
 */
fun seekSequence(
    lines: List<String>,
    pattern: List<String>,
    start: Int,
    eof: Boolean,
): Int? {
    if (pattern.isEmpty()) {
        return start
    }

    // When the pattern is longer than the available input there is no possible
    // match.
    if (pattern.size > lines.size) {
        return null
    }

    val searchStart = if (eof && lines.size >= pattern.size) {
        lines.size - pattern.size
    } else {
        start
    }

    val maxIdx = lines.size - pattern.size

    // Exact match first.
    for (i in searchStart..maxIdx) {
        if (lines.subList(i, i + pattern.size) == pattern) {
            return i
        }
    }

    // Then rstrip match.
    for (i in searchStart..maxIdx) {
        var ok = true
        for ((pIdx, pat) in pattern.withIndex()) {
            if (lines[i + pIdx].trimEnd() != pat.trimEnd()) {
                ok = false
                break
            }
        }
        if (ok) return i
    }

    // Finally, trim both sides to allow more lenience.
    for (i in searchStart..maxIdx) {
        var ok = true
        for ((pIdx, pat) in pattern.withIndex()) {
            if (lines[i + pIdx].trim() != pat.trim()) {
                ok = false
                break
            }
        }
        if (ok) return i
    }

    // Final, most permissive pass — attempt to match after *normalising*
    // common Unicode punctuation to their ASCII equivalents so that diffs
    // authored with plain ASCII characters can still be applied to source
    // files that contain typographic dashes / quotes, etc.
    for (i in searchStart..maxIdx) {
        var ok = true
        for ((pIdx, pat) in pattern.withIndex()) {
            if (normalise(lines[i + pIdx]) != normalise(pat)) {
                ok = false
                break
            }
        }
        if (ok) return i
    }

    return null
}

private fun normalise(s: String): String {
    return s.trim().map { c ->
        when (c) {
            // Various dash / hyphen code-points -> ASCII '-'
            '\u2010', '\u2011', '\u2012', '\u2013', '\u2014', '\u2015', '\u2212' -> '-'
            // Fancy single quotes -> '\''
            '\u2018', '\u2019', '\u201A', '\u201B' -> '\''
            // Fancy double quotes -> '"'
            '\u201C', '\u201D', '\u201E', '\u201F' -> '"'
            // Non-breaking space and other odd spaces -> normal space
            '\u00A0', '\u2002', '\u2003', '\u2004', '\u2005', '\u2006',
            '\u2007', '\u2008', '\u2009', '\u200A', '\u202F', '\u205F',
            '\u3000' -> ' '
            else -> c
        }
    }.joinToString("")
}
