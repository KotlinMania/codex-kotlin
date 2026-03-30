// port-lint: source common/src/fuzzy_match.rs
package ai.solace.coder.common

/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * Copyright (c) 2025 Sydney Renee, The Solace Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Simple case-insensitive subsequence matcher used for fuzzy filtering.
 *
 * Returns the indices (character positions) of the matched characters in the
 * ORIGINAL [haystack] string and a score where smaller is better, or null if
 * no match is found.
 *
 * Unicode correctness: we perform the match on a lowercased copy of the
 * haystack and needle but maintain a mapping from each character in the
 * lowercased haystack back to the original character index in [haystack].
 * This ensures the returned indices can be safely used with character-index
 * consumers for highlighting, even when lowercasing expands certain characters
 * (e.g., ß -> ss, İ -> i̇).
 */
fun fuzzyMatch(haystack: String, needle: String): Pair<List<Int>, Int>? {
    if (needle.isEmpty()) {
        return Pair(emptyList(), Int.MAX_VALUE)
    }

    val loweredChars = mutableListOf<Char>()
    val loweredToOrigCharIdx = mutableListOf<Int>()
    for ((origIdx, ch) in haystack.toList().withIndex()) {
        for (lc in ch.lowercase()) {
            loweredChars.add(lc)
            loweredToOrigCharIdx.add(origIdx)
        }
    }

    val loweredNeedle = needle.lowercase().toList()

    val resultOrigIndices = mutableListOf<Int>()
    var lastLowerPos: Int? = null
    var cur = 0
    for (nc in loweredNeedle) {
        var foundAt: Int? = null
        while (cur < loweredChars.size) {
            if (loweredChars[cur] == nc) {
                foundAt = cur
                cur++
                break
            }
            cur++
        }
        val pos = foundAt ?: return null
        resultOrigIndices.add(loweredToOrigCharIdx[pos])
        lastLowerPos = pos
    }

    val firstLowerPos = if (resultOrigIndices.isEmpty()) {
        0
    } else {
        val targetOrig = resultOrigIndices[0]
        loweredToOrigCharIdx.indexOfFirst { it == targetOrig }.coerceAtLeast(0)
    }
    // last defaults to first for single-hit; score = extra span between first/last hit
    // minus needle len (>=0).
    // Strongly reward prefix matches by subtracting 100 when the first hit is at index 0.
    val lastPos = lastLowerPos ?: firstLowerPos
    val window = (lastPos - firstLowerPos + 1) - loweredNeedle.size
    var score = maxOf(window, 0)
    if (firstLowerPos == 0) {
        score -= 100
    }

    val dedupedIndices = resultOrigIndices.distinct().sorted()
    return Pair(dedupedIndices, score)
}

/**
 * Convenience wrapper to get only the indices for a fuzzy match.
 */
fun fuzzyIndices(haystack: String, needle: String): List<Int>? {
    return fuzzyMatch(haystack, needle)?.first?.distinct()?.sorted()
}
