// port-lint: source tui/src/history_cell.rs
package ai.solace.coder.tui

/**
 * History cell types for displaying conversation events in the TUI.
 *
 * This module defines the HistoryCell trait and various implementations
 * for different types of conversation elements (user messages, agent responses,
 * tool calls, reasoning summaries, etc.).
 *
 * Ported from Rust codex-rs/tui/src/history_cell.rs
 * 
 * ⚠️ PARTIAL PORT: ~20% complete (464 of 2375 lines)
 * 
 * Ported cell types:
 * ✅ HistoryCell (trait)
 * ✅ UserHistoryCell
 * ✅ ReasoningSummaryCell
 * ✅ AgentMessageCell
 * ✅ PlainHistoryCell
 * ✅ UpdateAvailableHistoryCell
 * ✅ PrefixedWrappedHistoryCell
 * ✅ CompositeHistoryCell
 * ✅ SessionHeaderHistoryCell
 * ✅ McpToolCallCell
 * ✅ ErrorHistoryCell
 * ✅ FeedbackHistoryCell
 * 
 * TODO: Port remaining cell types (see end of file)
 */

// TODO: Import dependencies as they are ported
// import ai.solace.coder.tui.render.Renderable
// import ai.solace.coder.protocol.*

/**
 * Represents an event to display in the conversation history.
 * 
 * Returns its List<Line> representation to make it easier to display
 * in a scrollable list.
 */
interface HistoryCell {
    /**
     * Get display lines for rendering at the given width.
     */
    fun displayLines(width: Int): List<Line>
    
    /**
     * Calculate desired height for this cell at the given width.
     */
    fun desiredHeight(width: Int): Int {
        // TODO: Port Paragraph.lineCount calculation
        return displayLines(width).size
    }
    
    /**
     * Get lines for transcript (may differ from display lines).
     */
    fun transcriptLines(width: Int): List<Line> {
        return displayLines(width)
    }
    
    /**
     * Calculate desired height for transcript at the given width.
     */
    fun desiredTranscriptHeight(width: Int): Int {
        val lines = transcriptLines(width)
        
        // Workaround for ratatui bug: if there's only one line and it's 
        // whitespace-only, ratatui gives 2 lines.
        if (lines.size == 1) {
            val line = lines[0]
            if (line.spans.all { span -> span.content.all { it.isWhitespace() } }) {
                return 1
            }
        }
        
        // TODO: Port Paragraph.lineCount calculation
        return lines.size
    }
    
    /**
     * Check if this cell is a continuation of a stream.
     */
    fun isStreamContinuation(): Boolean = false
}

// Placeholder for ratatui Line type
// TODO: Port full ratatui line rendering
data class Line(
    val spans: List<Span> = emptyList()
) {
    companion object {
        fun from(text: String): Line = Line(listOf(Span.from(text)))
    }
}

// Placeholder for ratatui Span type
data class Span(
    val content: String,
    val style: Style = Style.default()
) {
    companion object {
        fun from(text: String): Span = Span(text)
    }
    
    fun patchStyle(newStyle: Style): Span = copy(style = newStyle)
}

// Placeholder for ratatui Style type
data class Style(
    val dim: Boolean = false,
    val bold: Boolean = false,
    val italic: Boolean = false
) {
    companion object {
        fun default(): Style = Style()
    }
}

/**
 * User message history cell.
 */
data class UserHistoryCell(
    val message: String
) : HistoryCell {
    
    override fun displayLines(width: Int): List<Line> {
        val lines = mutableListOf<Line>()
        
        // TODO: Port LIVE_PREFIX_COLS constant
        val LIVE_PREFIX_COLS = 2
        val wrapWidth = (width - LIVE_PREFIX_COLS - 1).coerceAtLeast(1)
        
        // TODO: Port user_message_style()
        val style = Style.default()
        
        // TODO: Port word_wrap_lines with proper options
        val wrapped = message.lines().map { Line.from(it) }
        
        lines.add(Line.from(""))
        // TODO: Port prefix_lines with proper styling
        lines.addAll(wrapped.map { line ->
            Line(listOf(Span("› ", Style(bold = true, dim = true))) + line.spans)
        })
        lines.add(Line.from(""))
        
        return lines
    }
}

/**
 * Reasoning summary cell (may be transcript-only).
 */
data class ReasoningSummaryCell(
    private val header: String,
    val content: String,
    val transcriptOnly: Boolean
) : HistoryCell {
    
    private fun lines(width: Int): List<Line> {
        val lines = mutableListOf<Line>()
        
        // TODO: Port append_markdown
        // For now, just split content into lines
        val contentLines = content.lines().map { Line.from(it) }
        
        val summaryStyle = Style(dim = true, italic = true)
        val styledLines = contentLines.map { line ->
            Line(line.spans.map { span -> span.patchStyle(summaryStyle) })
        }
        
        // TODO: Port word_wrap_lines with bullet prefix
        return styledLines
    }
    
    override fun displayLines(width: Int): List<Line> {
        return if (transcriptOnly) emptyList() else lines(width)
    }
    
    override fun desiredHeight(width: Int): Int {
        return if (transcriptOnly) 0 else lines(width).size
    }
    
    override fun transcriptLines(width: Int): List<Line> {
        return lines(width)
    }
    
    override fun desiredTranscriptHeight(width: Int): Int {
        return lines(width).size
    }
}

/**
 * Agent message cell (streaming or complete).
 */
data class AgentMessageCell(
    val lines: List<Line>,
    val isFirstLine: Boolean
) : HistoryCell {
    
    override fun displayLines(width: Int): List<Line> {
        // TODO: Port word_wrap_lines with proper options
        return lines
    }
    
    override fun isStreamContinuation(): Boolean = !isFirstLine
}

/**
 * Plain history cell with pre-formatted lines.
 */
data class PlainHistoryCell(
    val lines: List<Line>
) : HistoryCell {
    override fun displayLines(width: Int): List<Line> = lines
}

/**
 * Update available notification cell.
 */
data class UpdateAvailableHistoryCell(
    val latestVersion: String,
    val updateAction: UpdateAction?
) : HistoryCell {
    
    override fun displayLines(width: Int): List<Line> {
        // TODO: Port full update notification with styling
        val currentVersion = "1.0.0" // TODO: Get from CODEX_CLI_VERSION
        
        val lines = mutableListOf<Line>()
        lines.add(Line.from("✨ Update available! $currentVersion -> $latestVersion"))
        
        if (updateAction != null) {
            lines.add(Line.from("Run ${updateAction.commandStr()} to update."))
        } else {
            lines.add(Line.from("See https://github.com/openai/codex for installation options."))
        }
        
        lines.add(Line.from(""))
        lines.add(Line.from("See full release notes:"))
        lines.add(Line.from("https://github.com/openai/codex/releases/latest"))
        
        return lines
    }
}

// Placeholder for UpdateAction
data class UpdateAction(val command: String) {
    fun commandStr(): String = command
}

/**
 * Prefixed wrapped history cell with custom indent.
 */
data class PrefixedWrappedHistoryCell(
    val text: List<Line>,
    val initialPrefix: Line,
    val subsequentPrefix: Line
) : HistoryCell {
    
    override fun displayLines(width: Int): List<Line> {
        if (width == 0) return emptyList()
        
        // TODO: Port word_wrap_lines with RtOptions
        // For now, just add prefix to first line
        val result = mutableListOf<Line>()
        text.forEachIndexed { index, line ->
            val prefix = if (index == 0) initialPrefix else subsequentPrefix
            result.add(Line(prefix.spans + line.spans))
        }
        return result
    }
    
    override fun desiredHeight(width: Int): Int {
        return displayLines(width).size
    }
}

/**
 * Helper to truncate exec command snippets.
 */
private fun truncateExecSnippet(fullCmd: String): String {
    var snippet = when (val firstNewline = fullCmd.indexOf('\n')) {
        -1 -> fullCmd
        else -> "${fullCmd.substring(0, firstNewline)} ..."
    }
    
    // TODO: Port truncate_text
    if (snippet.length > 80) {
        snippet = snippet.substring(0, 77) + "..."
    }
    
    return snippet
}

/**
 * Create exec command snippet from command array.
 */
private fun execSnippet(command: List<String>): String {
    // TODO: Port strip_bash_lc_and_escape
    val fullCmd = command.joinToString(" ")
    return truncateExecSnippet(fullCmd)
}

/**
 * Approval decision types.
 */
enum class ReviewDecision {
    Approved,
    Rejected,
    SkippedInteractive
}

/**
 * Create approval decision cell.
 */
fun newApprovalDecisionCell(
    command: List<String>,
    decision: ReviewDecision
): HistoryCell {
    val snippet = execSnippet(command)
    
    val (symbol, summary) = when (decision) {
        ReviewDecision.Approved -> {
            Pair("✓", listOf(Span("Approved: ", Style(dim = true)), Span(snippet, Style(dim = true))))
        }
        ReviewDecision.Rejected -> {
            Pair("✗", listOf(Span("Rejected: ", Style(dim = true)), Span(snippet, Style(dim = true))))
        }
        ReviewDecision.SkippedInteractive -> {
            Pair("→", listOf(Span("Skipped (interactive): ", Style(dim = true)), Span(snippet, Style(dim = true))))
        }
    }
    
    return PlainHistoryCell(listOf(
        Line(listOf(Span(symbol), Span(" ")) + summary)
    ))
}

/**
 * Composite history cell containing multiple sub-cells.
 */
data class CompositeHistoryCell(
    val parts: List<HistoryCell>
) : HistoryCell {
    
    override fun displayLines(width: Int): List<Line> {
        val out = mutableListOf<Line>()
        var first = true
        
        for (part in parts) {
            val lines = part.displayLines(width)
            if (lines.isNotEmpty()) {
                if (!first) {
                    out.add(Line.from(""))
                }
                out.addAll(lines)
                first = false
            }
        }
        
        return out
    }
}

/**
 * Session header cell showing model, directory, version info.
 */
data class SessionHeaderHistoryCell(
    val version: String,
    val model: String,
    val directory: String,
    val reasoningEffort: String?
) : HistoryCell {
    
    override fun displayLines(width: Int): List<Line> {
        // TODO: Port full session header with borders and formatting
        val lines = mutableListOf<Line>()
        
        lines.add(Line.from(">_ OpenAI Codex (v$version)"))
        lines.add(Line.from(""))
        lines.add(Line.from("model: $model ${reasoningEffort ?: ""}"))
        lines.add(Line.from("directory: $directory"))
        
        return lines
    }
}

/**
 * MCP tool call cell (Model Context Protocol).
 */
data class McpToolCallCell(
    val callId: String,
    val serverName: String,
    val toolName: String,
    var duration: Long? = null,
    var result: McpToolResult? = null,
    val animationsEnabled: Boolean = true
) : HistoryCell {
    
    override fun displayLines(width: Int): List<Line> {
        // TODO: Port full MCP tool call rendering with spinner
        val statusStr = when {
            result?.isError == true -> "✗"
            result != null -> "✓"
            else -> "..."
        }
        
        val durationStr = duration?.let { " (${it}ms)" } ?: ""
        
        return listOf(
            Line.from("$statusStr MCP: $serverName.$toolName$durationStr")
        )
    }
    
    fun complete(durationMs: Long, result: McpToolResult) {
        this.duration = durationMs
        this.result = result
    }
    
    fun callId(): String = callId
    
    fun success(): Boolean? = result?.let { !it.isError }
}

// Placeholder for MCP types
data class McpToolResult(
    val isError: Boolean,
    val content: String
)

/**
 * Error cell for displaying errors in conversation.
 */
data class ErrorHistoryCell(
    val message: String,
    val details: String? = null
) : HistoryCell {
    
    override fun displayLines(width: Int): List<Line> {
        val lines = mutableListOf<Line>()
        lines.add(Line(listOf(Span("✗ Error: ", Style(bold = true)), Span(message))))
        
        if (details != null) {
            lines.add(Line.from(""))
            details.lines().forEach { line ->
                lines.add(Line.from("  $line"))
            }
        }
        
        return lines
    }
}

/**
 * Feedback cell for user feedback prompts.
 */
data class FeedbackHistoryCell(
    val prompt: String
) : HistoryCell {
    
    override fun displayLines(width: Int): List<Line> {
        return listOf(
            Line.from(""),
            Line(listOf(Span("💬 ", Style(dim = true)), Span(prompt, Style(dim = true)))),
            Line.from("")
        )
    }
}

// TODO: Continue porting remaining complex cell types:
// - ThinkingCell (with spinner animation)
// - ToolCallCell (shell command execution)
// - ToolResultCell (command output with truncation)
// - PlanCell (multi-step plan rendering)
// - SessionConfiguredCell (session setup info)
// - ImageCell (image rendering in terminal)
// - FileChangesCell (git-style diff summary)
// etc.
