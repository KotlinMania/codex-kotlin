// port-lint: source common/src/fuzzy_match.rs
package ai.solace.coder.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FuzzyMatchTest {

    @Test
    fun asciiBasicIndices() {
        val result = fuzzyMatch("hello", "hl")
        assertNotNull(result)
        val (idx, score) = result
        assertEquals(listOf(0, 2), idx)
        // 'h' at 0, 'l' at 2 -> window 1; start-of-string bonus applies (-100)
        assertEquals(-99, score)
    }

    @Test
    fun unicodeDottedIIstanbulHighlighting() {
        val result = fuzzyMatch("İstanbul", "is")
        assertNotNull(result)
        val (idx, score) = result
        assertEquals(listOf(0, 1), idx)
        // Matches at lowered positions 0 and 2 -> window 1; start-of-string bonus applies
        assertEquals(-99, score)
    }

    @Test
    fun unicodeGermanSharpSCasefold() {
        assertNull(fuzzyMatch("straße", "strasse"))
    }

    @Test
    fun preferContiguousMatchOverSpread() {
        val resultA = fuzzyMatch("abc", "abc")
        assertNotNull(resultA)
        val (_, scoreA) = resultA

        val resultB = fuzzyMatch("a-b-c", "abc")
        assertNotNull(resultB)
        val (_, scoreB) = resultB

        // Contiguous window -> 0; start-of-string bonus -> -100
        assertEquals(-100, scoreA)
        // Spread over 5 chars for 3-letter needle -> window 2; with bonus -> -98
        assertEquals(-98, scoreB)
        assertTrue(scoreA < scoreB)
    }

    @Test
    fun startOfStringBonusApplies() {
        val resultA = fuzzyMatch("file_name", "file")
        assertNotNull(resultA)
        val (_, scoreA) = resultA

        val resultB = fuzzyMatch("my_file_name", "file")
        assertNotNull(resultB)
        val (_, scoreB) = resultB

        // Start-of-string contiguous -> window 0; bonus -> -100
        assertEquals(-100, scoreA)
        // Non-prefix contiguous -> window 0; no bonus -> 0
        assertEquals(0, scoreB)
        assertTrue(scoreA < scoreB)
    }

    @Test
    fun emptyNeedleMatchesWithMaxScoreAndNoIndices() {
        val result = fuzzyMatch("anything", "")
        assertNotNull(result)
        val (idx, score) = result
        assertTrue(idx.isEmpty())
        assertEquals(Int.MAX_VALUE, score)
    }

    @Test
    fun caseInsensitiveMatchingBasic() {
        val result = fuzzyMatch("FooBar", "foO")
        assertNotNull(result)
        val (idx, score) = result
        assertEquals(listOf(0, 1, 2), idx)
        // Contiguous prefix match (case-insensitive) -> window 0 with bonus
        assertEquals(-100, score)
    }

    @Test
    fun indicesAreDedupedForMulticharLowercaseExpansion() {
        val needle = "\u0069\u0307" // "i" + combining dot above
        val result = fuzzyMatch("İ", needle)
        assertNotNull(result)
        val (idx, score) = result
        assertEquals(listOf(0), idx)
        // Lowercasing 'İ' expands to two chars; contiguous prefix -> window 0 with bonus
        assertEquals(-100, score)
    }
}
