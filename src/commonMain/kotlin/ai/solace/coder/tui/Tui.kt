// port-lint: source tui/src/tui.rs
package ai.solace.coder.tui

import ai.solace.coder.tui.CustomTerminal
import ratatui.layout.Rect
import ratatui.terminal.Backend
import ratatui.text.Line
import io.github.kotlinmania.kasuari.event.Event
import io.github.kotlinmania.kasuari.event.KeyEvent
import io.github.kotlinmania.kasuari.terminal.EnterAlternateScreen
import io.github.kotlinmania.kasuari.terminal.LeaveAlternateScreen
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

sealed class TuiEvent {
    data class Key(val keyEvent: KeyEvent) : TuiEvent()
    data class Paste(val pasted: String) : TuiEvent()
    object Draw : TuiEvent()
}

class Tui<B : Backend>(
    val terminal: CustomTerminal<B>,
    private val scope: CoroutineScope
) {
    private val drawTx = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val pendingHistoryLines = mutableListOf<Line>()
    private var altSavedViewport: Rect? = null

    private var altScreenActive = false
    private var terminalFocused = true
    private var enhancedKeysSupported = false

    private val _frameRequester: FrameRequester = FrameRequester.new(drawTx, scope)

    fun frameRequester(): FrameRequester = _frameRequester

    fun enhancedKeysSupported(): Boolean = enhancedKeysSupported

    fun notify(message: String): Boolean {
        if (!terminalFocused) {
            // execute!(stdout(), PostNotification(message.as_ref().to_string()));
            return true
        }
        return false
    }

    fun eventStream(): Flow<TuiEvent> = flow {
        // This is a complex flow that merges crossterm events and draw notifications.
        // In Kotlin, we can use merge().
        
        val kasuariEvents = flow {
            // Placeholder for kasuari event loop
            // while(true) emit(backend.nextEvent())
        }

        val drawEvents = drawTx.map { TuiEvent.Draw }

        merge(
            kasuariEvents.map { event ->
                when (event) {
                    is Event.Key -> TuiEvent.Key(event.keyEvent)
                    is Event.Paste -> TuiEvent.Paste(event.pasted)
                    is Event.FocusGained -> {
                        terminalFocused = true
                        TuiEvent.Draw
                    }
                    is Event.FocusLost -> {
                        terminalFocused = false
                        null
                    }
                    is Event.Resize -> TuiEvent.Draw
                    else -> null
                }
            }.filterNotNull(),
            drawEvents
        ).collect {
            emit(it)
        }
    }

    fun enterAltScreen() {
        // terminal.backend_mut().execute(EnterAlternateScreen)
        val size = terminal.size()
        altSavedViewport = terminal.viewportArea
        terminal.setViewportArea(Rect(0u, 0u, size.width, size.height))
        terminal.clear()
        altScreenActive = true
    }

    fun leaveAltScreen() {
        // terminal.backend_mut().execute(LeaveAlternateScreen)
        altSavedViewport?.let {
            terminal.setViewportArea(it)
        }
        altSavedViewport = null
        altScreenActive = false
    }

    fun insertHistoryLines(lines: List<Line>) {
        pendingHistoryLines.addAll(lines)
        frameRequester().scheduleFrame()
    }

    fun draw(height: UShort, drawFn: (Frame) -> Unit) {
        // Simplified version of sync_update logic
        val size = terminal.size()
        var area = terminal.viewportArea
        area = area.copy(
            height = minOf(height, size.height),
            width = size.width
        )

        if (area.y + area.height > size.height) {
            // Scroll up
            // terminal.backend_mut().scroll_region_up(...)
            area = area.copy(y = (size.height - area.height).toUShort())
        }

        if (area != terminal.viewportArea) {
            terminal.clear()
            terminal.setViewportArea(area)
        }

        if (pendingHistoryLines.isNotEmpty()) {
            // insert_history_lines(terminal, pendingHistoryLines)
            pendingHistoryLines.clear()
        }

        terminal.draw { frame ->
            drawFn(frame)
        }
    }
}
