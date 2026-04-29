// port-lint: source fuzzy_match.rs
package ai.solace.coder.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FuzzyMatchTest {
    @Test
    fun ascii_basic_indices() {
        val result = fuzzyMatch("hello", "hl") ?: error("expected a match")
        val (idx, score) = result
        assertEquals(listOf(0, 2), idx)
        assertEquals(-99, score)
    }

    @Test
    fun unicode_dotted_i_istanbul_highlighting() {
        val result = fuzzyMatch("İstanbul", "is") ?: error("expected a match")
        val (idx, score) = result
        assertEquals(listOf(0, 1), idx)
        assertEquals(-99, score)
    }

    @Test
    fun unicode_german_sharp_s_casefold() {
        assertNull(fuzzyMatch("straße", "strasse"))
    }

    @Test
    fun prefer_contiguous_match_over_spread() {
        val resultA = fuzzyMatch("abc", "abc") ?: error("expected a match")
        val resultB = fuzzyMatch("a-b-c", "abc") ?: error("expected a match")
        assertEquals(-100, resultA.second)
        assertEquals(-98, resultB.second)
        assertTrue(resultA.second < resultB.second)
    }

    @Test
    fun start_of_string_bonus_applies() {
        val resultA = fuzzyMatch("file_name", "file") ?: error("expected a match")
        val resultB = fuzzyMatch("my_file_name", "file") ?: error("expected a match")
        assertEquals(-100, resultA.second)
        assertEquals(0, resultB.second)
        assertTrue(resultA.second < resultB.second)
    }

    @Test
    fun empty_needle_matches_with_max_score_and_no_indices() {
        val result = fuzzyMatch("anything", "") ?: error("empty needle should match")
        val (idx, score) = result
        assertTrue(idx.isEmpty())
        assertEquals(Int.MAX_VALUE, score)
    }

    @Test
    fun case_insensitive_matching_basic() {
        val result = fuzzyMatch("FooBar", "foO") ?: error("expected a match")
        val (idx, score) = result
        assertEquals(listOf(0, 1, 2), idx)
        assertEquals(-100, score)
    }

    @Test
    fun indices_are_deduped_for_multichar_lowercase_expansion() {
        val needle = "\u0069\u0307" // "i" + combining dot above
        val result = fuzzyMatch("İ", needle) ?: error("expected a match")
        val (idx, score) = result
        assertEquals(listOf(0), idx)
        assertEquals(-100, score)
    }
}
