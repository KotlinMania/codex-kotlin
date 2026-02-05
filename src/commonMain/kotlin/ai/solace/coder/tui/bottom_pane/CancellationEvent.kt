// port-lint: source tui/src/bottom_pane/mod.rs
package ai.solace.coder.tui.bottom_pane

/**
 * Signal returned by a view when handling Ctrl-C.
 */
enum class CancellationEvent {
    Handled,
    NotHandled,
}

