// port-lint: source tui/src/tui.rs
package ai.solace.coder.tui

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * TUI (Terminal User Interface) management for Codex.
 * 
 * This module handles terminal setup/teardown, event streams, and rendering.
 * It manages both inline and alternate screen modes.
 * 
 * Ported from Rust codex-rs/tui/src/tui.rs
 * 
 * ⚠️ STUB PORT: Requires crossterm/ratatui equivalents for Kotlin
 * This port provides the API structure but terminal operations are TODOs.
 */

/**
 * Terminal type alias.
 * TODO: Port CustomTerminal and CrosstermBackend
 */
typealias Terminal = Any // Placeholder

/**
 * Set terminal modes for TUI operation.
 * - Enable raw mode
 * - Enable bracketed paste
 * - Enable focus change events
 * - Enable keyboard enhancement flags
 * 
 * TODO: Port crossterm terminal mode operations
 */
fun setModes() {
    // TODO: execute(stdout, EnableBracketedPaste)
    // TODO: enable_raw_mode()
    // TODO: execute(stdout, PushKeyboardEnhancementFlags(...))
    // TODO: execute(stdout, EnableFocusChange)
}

/**
 * Restore terminal to original state.
 * Inverse of [setModes].
 * 
 * TODO: Port crossterm terminal mode operations
 */
fun restore() {
    // TODO: execute(stdout, PopKeyboardEnhancementFlags)
    // TODO: execute(stdout, DisableBracketedPaste)
    // TODO: execute(stdout, DisableFocusChange)
    // TODO: disable_raw_mode()
    // TODO: execute(stdout, crossterm::cursor::Show)
}

/**
 * Initialize the terminal (inline viewport; history stays in normal scrollback).
 * 
 * @return Terminal instance
 * @throws Exception if stdin/stdout are not terminals
 */
fun init(): Terminal {
    // TODO: Check if stdin/stdout are terminals
    // if (!stdin().isTerminal()) throw Exception("stdin is not a terminal")
    // if (!stdout().isTerminal()) throw Exception("stdout is not a terminal")
    
    setModes()
    setPanicHook()
    
    // TODO: Create CrosstermBackend and CustomTerminal
    return Any() // Placeholder
}

/**
 * Set panic hook to restore terminal on panic.
 */
private fun setPanicHook() {
    // TODO: Port panic hook setup
    // Kotlin doesn't have Rust's panic hooks, but we could use:
    // Thread.setDefaultUncaughtExceptionHandler { _, _ -> restore() }
}

/**
 * TUI event types.
 */
sealed class TuiEvent {
    data class Key(val keyEvent: KeyEvent) : TuiEvent()
    data class Paste(val text: String) : TuiEvent()
    data object Draw : TuiEvent()
}

/**
 * Frame requester for scheduling redraws.
 */
data class FrameRequester(
    private val frameScheduleTx: Channel<Long>
) {
    /**
     * Schedule a frame to be drawn immediately.
     */
    fun scheduleFrame() {
        frameScheduleTx.trySend(System.currentTimeMillis())
    }
    
    /**
     * Schedule a frame to be drawn after the given duration.
     */
    fun scheduleFrameIn(duration: Duration) {
        val deadline = System.currentTimeMillis() + duration.inWholeMilliseconds
        frameScheduleTx.trySend(deadline)
    }
    
    companion object {
        /**
         * Create a no-op frame requester for tests.
         */
        fun testDummy(): FrameRequester {
            return FrameRequester(Channel(Channel.UNLIMITED))
        }
    }
}

/**
 * Main TUI manager.
 */
class Tui(
    val terminal: Terminal
) {
    private val frameScheduleTx = Channel<Long>(Channel.UNLIMITED)
    private val drawTx = Channel<Unit>(Channel.CONFLATED)
    
    private val pendingHistoryLines = mutableListOf<Line>()
    private var altSavedViewport: Rect? = null
    
    // True when overlay alt-screen UI is active
    private val altScreenActive = AtomicBoolean(false)
    
    // True when terminal/tab is focused
    private val terminalFocused = AtomicBoolean(true)
    
    private val enhancedKeysSupported: Boolean
    
    init {
        spawnFrameScheduler(frameScheduleTx, drawTx)
        
        // Detect keyboard enhancement support
        // TODO: Port supports_keyboard_enhancement()
        enhancedKeysSupported = false
        
        // Cache color support detection
        // TODO: Port supports_color and terminal_palette
    }
    
    fun frameRequester(): FrameRequester = FrameRequester(frameScheduleTx)
    
    fun enhancedKeysSupported(): Boolean = enhancedKeysSupported
    
    /**
     * Emit a desktop notification now if the terminal is unfocused.
     * @return true if a notification was posted
     */
    fun notify(message: String): Boolean {
        if (!terminalFocused.get()) {
            // TODO: execute(stdout, PostNotification(message))
            return true
        }
        return false
    }
    
    /**
     * Create an event stream for terminal events.
     * 
     * TODO: Port crossterm event stream and async_stream
     */
    fun eventStream(): Flow<TuiEvent> = flow {
        // TODO: Port crossterm EventStream
        // TODO: Merge with draw_tx events
        // TODO: Handle suspend/resume on Unix
        // TODO: Handle focus events
    }
    
    /**
     * Enter alternate screen and expand viewport to full terminal size.
     * Saves current inline viewport for restoration.
     * 
     * TODO: Port crossterm alternate screen commands
     */
    fun enterAltScreen() {
        // TODO: execute(terminal.backendMut(), EnterAlternateScreen)
        // TODO: execute(terminal.backendMut(), EnableAlternateScroll)
        // TODO: Save viewport and set to full size
        // TODO: terminal.clear()
        altScreenActive.set(true)
    }
    
    /**
     * Leave alternate screen and restore previously saved inline viewport.
     * 
     * TODO: Port crossterm alternate screen commands
     */
    fun leaveAltScreen() {
        // TODO: execute(terminal.backendMut(), DisableAlternateScroll)
        // TODO: execute(terminal.backendMut(), LeaveAlternateScreen)
        // TODO: Restore saved viewport
        altScreenActive.set(false)
        altSavedViewport = null
    }
    
    /**
     * Insert history lines to be rendered on next draw.
     */
    fun insertHistoryLines(lines: List<Line>) {
        pendingHistoryLines.addAll(lines)
        frameRequester().scheduleFrame()
    }
    
    /**
     * Draw the TUI with the given height and draw function.
     * 
     * @param height Desired height of viewport
     * @param drawFn Function to render the frame
     * 
     * TODO: Port synchronized update and viewport management
     */
    fun draw(height: Int, drawFn: (Frame) -> Unit) {
        // TODO: Port suspend/resume handling (Unix)
        // TODO: Precompute viewport updates
        // TODO: Use stdout.sync_update
        // TODO: Handle pending history lines
        // TODO: Call terminal.draw(drawFn)
    }
}

/**
 * Spawn background scheduler to coalesce frame requests.
 * 
 * TODO: Port to Kotlin coroutines with select
 */
private fun spawnFrameScheduler(
    frameScheduleRx: Channel<Long>,
    drawTx: Channel<Unit>
) {
    // TODO: Launch coroutine that:
    // 1. Receives frame schedule requests
    // 2. Coalesces multiple requests to same deadline
    // 3. Sleeps until deadline
    // 4. Sends draw event
}

// Placeholder types
// TODO: Port these from ratatui/crossterm

data class Rect(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int
) {
    fun bottom(): Int = y + height
    fun top(): Int = y
}

class Frame {
    // TODO: Port ratatui Frame
}

/**
 * OSC 9 desktop notification command.
 * 
 * TODO: Port crossterm Command trait
 */
data class PostNotification(val message: String) {
    fun writeAnsi(): String = "\u001b]9;$message\u0007"
}
