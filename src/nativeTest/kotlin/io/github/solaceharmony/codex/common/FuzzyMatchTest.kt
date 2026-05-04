// port-lint: source fuzzy_match.rs
package io.github.solaceharmony.codex.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FuzzyMatchTest {
    @Test
    fun asciiBasicIndices() {
        val result = fuzzyMatch("hello", "hl") ?: error("expected a match")
        val (idx, score) = result
        assertEquals(listOf(0, 2), idx)
        assertEquals(-99, score)
    }

    @Test
    fun unicodeDottedIIstanbulHighlighting() {
        val result = fuzzyMatch("İstanbul", "is") ?: error("expected a match")
        val (idx, score) = result
        assertEquals(listOf(0, 1), idx)
        assertEquals(-99, score)
    }

    @Test
    fun unicodeGermanSharpSCasefold() {
        assertNull(fuzzyMatch("straße", "strasse"))
    }

    @Test
    fun preferContiguousMatchOverSpread() {
        val resultA = fuzzyMatch("abc", "abc") ?: error("expected a match")
        val resultB = fuzzyMatch("a-b-c", "abc") ?: error("expected a match")
        assertEquals(-100, resultA.second)
        assertEquals(-98, resultB.second)
        assertTrue(resultA.second < resultB.second)
    }

    @Test
    fun startOfStringBonusApplies() {
        val resultA = fuzzyMatch("file_name", "file") ?: error("expected a match")
        val resultB = fuzzyMatch("my_file_name", "file") ?: error("expected a match")
        assertEquals(-100, resultA.second)
        assertEquals(0, resultB.second)
        assertTrue(resultA.second < resultB.second)
    }

    @Test
    fun emptyNeedleMatchesWithMaxScoreAndNoIndices() {
        val result = fuzzyMatch("anything", "") ?: error("empty needle should match")
        val (idx, score) = result
        assertTrue(idx.isEmpty())
        assertEquals(Int.MAX_VALUE, score)
    }

    @Test
    fun caseInsensitiveMatchingBasic() {
        val result = fuzzyMatch("FooBar", "foO") ?: error("expected a match")
        val (idx, score) = result
        assertEquals(listOf(0, 1, 2), idx)
        assertEquals(-100, score)
    }

    @Test
    fun indicesAreDedupedForMulticharLowercaseExpansion() {
        val needle = "\u0069\u0307" // "i" + combining dot above
        val result = fuzzyMatch("İ", needle) ?: error("expected a match")
        val (idx, score) = result
        assertEquals(listOf(0), idx)
        assertEquals(-100, score)
    }
}
