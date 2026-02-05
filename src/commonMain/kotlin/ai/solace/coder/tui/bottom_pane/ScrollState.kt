// port-lint: source tui/src/bottom_pane/scroll_state.rs
package ai.solace.coder.tui.bottom_pane

import kotlin.math.min

/**
 * Generic scroll/selection state for a vertical list menu.
 *
 * Encapsulates the common behavior of a selectable list that supports:
 * - Optional selection (null when list is empty)
 * - Wrap-around navigation on Up/Down
 * - Maintaining a scroll window (`scrollTop`) so the selected row stays visible
 */
data class ScrollState(
    /** Selected row index, or null when the list is empty. */
    var selectedIdx: Int? = null,
    /** First visible row index for the current scroll window. */
    var scrollTop: Int = 0,
) {
    companion object {
        /** Create a new empty scroll state. */
        fun new(): ScrollState =
            ScrollState(
                selectedIdx = null,
                scrollTop = 0,
            )
    }

    /** Reset selection and scroll. */
    fun reset() {
        this.selectedIdx = null
        this.scrollTop = 0
    }

    /** Clamp selection to be within the [0, len-1] range, or null when empty. */
    fun clampSelection(len: Int) {
        this.selectedIdx =
            when (len) {
                0 -> null
                else -> min(this.selectedIdx ?: 0, len - 1)
            }

        if (len == 0) {
            this.scrollTop = 0
        }
    }

    /** Move selection up by one, wrapping to the bottom when necessary. */
    fun moveUpWrap(len: Int) {
        if (len == 0) {
            this.selectedIdx = null
            this.scrollTop = 0
            return
        }

        this.selectedIdx =
            this.selectedIdx?.let { idx ->
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
            this.selectedIdx = null
            this.scrollTop = 0
            return
        }

        this.selectedIdx =
            this.selectedIdx?.let { idx ->
                if (idx + 1 < len) {
                    idx + 1
                } else {
                    0
                }
            } ?: 0
    }

    /**
     * Adjust [scrollTop] so that the current [selectedIdx] is visible within
     * the window of [visibleRows].
     */
    fun ensureVisible(len: Int, visibleRows: Int) {
        if (len == 0 || visibleRows == 0) {
            this.scrollTop = 0
            return
        }

        this.selectedIdx?.let { sel ->
            if (sel < this.scrollTop) {
                this.scrollTop = sel
            } else {
                val bottom = this.scrollTop + visibleRows - 1
                if (sel > bottom) {
                    this.scrollTop = sel + 1 - visibleRows
                }
            }
        } ?: run {
            this.scrollTop = 0
        }
    }
}
