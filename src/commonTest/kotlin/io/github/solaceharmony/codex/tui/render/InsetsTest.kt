package io.github.solaceharmony.codex.tui.render

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import ratatui.layout.Rect

class InsetsTest {
    @Test
    fun insetShrinksAndMovesRect() {
        val rect = Rect.new(2, 3, 10, 8)

        assertEquals(
            Rect.new(4, 4, 6, 6),
            rect.inset(Insets.tlbr(top = 1, left = 2, bottom = 1, right = 2)),
        )
    }

    @Test
    fun insetSaturatesDimensionsAtZero() {
        val rect = Rect.new(0, 0, 3, 2)

        assertEquals(
            Rect.new(5, 5, 0, 0),
            rect.inset(Insets.vh(v = 5, h = 5)),
        )
    }

    @Test
    fun negativeInsetsAreRejected() {
        assertFailsWith<IllegalArgumentException> {
            Insets(left = -1)
        }
    }
}
