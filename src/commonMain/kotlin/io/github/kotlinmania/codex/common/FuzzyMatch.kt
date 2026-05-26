// port-lint: source fuzzy_match.rs
package io.github.kotlinmania.codex.common

import kotlin.math.max

/**
 * Simple case-insensitive subsequence matcher used for fuzzy filtering.
 *
 * Returns the indices (character positions) of the matched characters in the
 * ORIGINAL `haystack` string and a score where smaller is better.
 *
 * Unicode correctness: we perform the match on a lowercased copy of the
 * haystack and needle but maintain a mapping from each character in the
 * lowercased haystack back to the original character index in `haystack`.
 * This ensures the returned indices can be safely used by consumers for
 * highlighting, even when lowercasing expands certain characters
 * (e.g., ß → ss, İ → i̇).
 */
fun fuzzyMatch(haystack: String, needle: String): Pair<List<Int>, Int>? {
    if (needle.isEmpty()) {
        return Pair(emptyList(), Int.MAX_VALUE)
    }

    val loweredChars = mutableListOf<Char>()
    val loweredToOrigCharIdx = mutableListOf<Int>()
    for ((origIdx, ch) in haystack.withIndex()) {
        for (lc in ch.lowercase()) {
            loweredChars.add(lc)
            loweredToOrigCharIdx.add(origIdx)
        }
    }

    val loweredNeedle: List<Char> = needle.lowercase().toList()

    val resultOrigIndices = mutableListOf<Int>()
    var lastLowerPos: Int? = null
    var cur = 0
    for (nc in loweredNeedle) {
        var foundAt: Int? = null
        while (cur < loweredChars.size) {
            if (loweredChars[cur] == nc) {
                foundAt = cur
                cur += 1
                break
            }
            cur += 1
        }
        val pos = foundAt ?: return null
        resultOrigIndices.add(loweredToOrigCharIdx[pos])
        lastLowerPos = pos
    }

    val firstLowerPos = if (resultOrigIndices.isEmpty()) {
        0
    } else {
        val targetOrig = resultOrigIndices[0]
        val idx = loweredToOrigCharIdx.indexOfFirst { it == targetOrig }
        if (idx < 0) 0 else idx
    }
    // last defaults to first for single-hit; score = extra span between first/last hit
    // minus needle len (>=0).
    // Strongly reward prefix matches by subtracting 100 when the first hit is at index 0.
    val lastLower = lastLowerPos ?: firstLowerPos
    val window = (lastLower - firstLowerPos + 1) - loweredNeedle.size
    var score = max(window, 0)
    if (firstLowerPos == 0) {
        score -= 100
    }

    val sortedDedup = resultOrigIndices.distinct().sorted()
    return Pair(sortedDedup, score)
}

/** Convenience wrapper to get only the indices for a fuzzy match. */
fun fuzzyIndices(haystack: String, needle: String): List<Int>? {
    val result = fuzzyMatch(haystack, needle) ?: return null
    return result.first.distinct().sorted()
}
