// port-lint: source tui/src/ui_consts.rs
package io.github.solaceharmony.codex.tui

/**
 * Shared UI constants for layout and alignment within the TUI.
 */

/**
 * Width (in terminal columns) reserved for the left gutter/prefix used by
 * live cells and aligned widgets.
 *
 * Semantics:
 * - Chat composer reserves this many columns for the left border + padding.
 * - Status indicator lines begin with this many spaces for alignment.
 * - User history lines account for this many columns (e.g., "▌ ") when wrapping.
 */
internal const val LIVE_PREFIX_COLS: UShort = 2u
internal const val FOOTER_INDENT_COLS: Int = 2
