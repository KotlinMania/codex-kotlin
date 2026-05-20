package io.github.solaceharmony.codex.tui.bottompane

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ScrollStateTest {
    @Test
    fun wrapNavigationAndVisibility() {
        val state = ScrollState.new()
        val len = 10
        val visibleRows = 5

        state.clampSelection(len)
        assertEquals(0, state.selectedIdx)
        state.ensureVisible(len, visibleRows)
        assertEquals(0, state.scrollTop)

        state.moveUpWrap(len)
        state.ensureVisible(len, visibleRows)
        assertEquals(len - 1, state.selectedIdx)
        val selected = assertNotNull(state.selectedIdx)
        assertTrue(state.scrollTop <= selected)

        state.moveDownWrap(len)
        state.ensureVisible(len, visibleRows)
        assertEquals(0, state.selectedIdx)
        assertEquals(0, state.scrollTop)
    }
}
