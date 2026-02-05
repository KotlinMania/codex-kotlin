// port-lint: source tui/src/bottom_pane/bottom_pane_view.rs
package ai.solace.coder.tui.bottom_pane

import ai.solace.coder.tui.render.Renderable
import io.github.kotlinmania.kasuari.event.KeyEvent

/** Trait implemented by every view that can be shown in the bottom pane. */
interface BottomPaneView : Renderable {
    /**
     * Handle a key event while the view is active. A redraw is always scheduled after this call.
     */
    fun handleKeyEvent(keyEvent: KeyEvent) {}

    /** Return `true` if the view has finished and should be removed. */
    fun isComplete(): Boolean = false

    /** Handle Ctrl-C while this view is active. */
    fun onCtrlC(): CancellationEvent = CancellationEvent.NotHandled

    /** Optional paste handler. Return true if the view modified its state and needs a redraw. */
    fun handlePaste(pasted: String): Boolean = false

    /** Try to handle approval request; return the original value if not consumed. */
    fun tryConsumeApprovalRequest(request: ApprovalRequest): ApprovalRequest? = request
}
