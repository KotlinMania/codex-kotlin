// port-lint: source tui/src/bottom_pane/scroll_state.rs
package io.github.solaceharmony.codex.tui.bottompane

import kotlin.math.min

/**
 * Generic scroll/selection state for a vertical list menu.
 *
 * Encapsulates optional selection, wrap-around navigation, and maintaining a scroll window so the
 * selected row stays visible.
 */
data class ScrollState(
    /** Selected row index, or null when the list is empty. */
    var selectedIdx: Int? = null,
    /** First visible row index for the current scroll window. */
    var scrollTop: Int = 0,
) {
    companion object {
        /** Create a new empty scroll state. */
        fun new(): ScrollState = ScrollState()
    }

    /** Reset selection and scroll. */
    fun reset() {
        selectedIdx = null
        scrollTop = 0
    }

    /** Clamp selection to the valid row range, or clear it when the list is empty. */
    fun clampSelection(len: Int) {
        selectedIdx =
            when (len) {
                0 -> null
                else -> min(selectedIdx ?: 0, len - 1)
            }

        if (len == 0) {
            scrollTop = 0
        }
    }

    /** Move selection up by one, wrapping to the bottom when necessary. */
    fun moveUpWrap(len: Int) {
        if (len == 0) {
            selectedIdx = null
            scrollTop = 0
            return
        }

        selectedIdx =
            selectedIdx?.let { idx ->
                if (idx > 0) {
                    idx - 1
                } else {
                    len - 1
                }
            } ?: 0
    }

    /** Move selection down by one, wrapping to the top when necessary. */
    fun moveDownWrap(len: Int) {
        if (len == 0) {
            selectedIdx = null
            scrollTop = 0
            return
        }

        selectedIdx =
            selectedIdx?.let { idx ->
                if (idx + 1 < len) {
                    idx + 1
                } else {
                    0
                }
            } ?: 0
    }

    /** Adjust [scrollTop] so [selectedIdx] is visible inside [visibleRows]. */
    fun ensureVisible(len: Int, visibleRows: Int) {
        if (len == 0 || visibleRows == 0) {
            scrollTop = 0
            return
        }

        val selected = selectedIdx
        if (selected == null) {
            scrollTop = 0
            return
        }

        if (selected < scrollTop) {
            scrollTop = selected
            return
        }

        val bottom = scrollTop + visibleRows - 1
        if (selected > bottom) {
            scrollTop = selected + 1 - visibleRows
        }
    }
}
