// port-lint: source tui/src/history_cell.rs
package ai.solace.coder.tui

import ai.solace.coder.protocol.FileChange

/**
 * Represents an event to display in the conversation history.
 * Returns its display lines representation to make it easier to display
 * in a scrollable list.
 *
 * Ported from Rust codex-rs/tui/src/history_cell.rs
 */
interface HistoryCell {
    /**
     * Returns the display lines for the given terminal width.
     */
    fun displayLines(width: Int): List<StyledLine>

    /**
     * Returns the desired height in rows for the given terminal width.
     */
    fun desiredHeight(width: Int): Int {
        return displayLines(width).size
    }

    /**
     * Returns the transcript lines for the given terminal width.
     * By default returns the same as displayLines.
     */
    fun transcriptLines(width: Int): List<StyledLine> {
        return displayLines(width)
    }

    /**
     * Returns the desired transcript height for the given terminal width.
     */
    fun desiredTranscriptHeight(width: Int): Int {
        val lines = transcriptLines(width)
        // Workaround: if there's only one line and it's whitespace-only, return 1
        if (lines.size == 1 && lines[0].spans.all { it.content.all { ch -> ch.isWhitespace() } }) {
            return 1
        }
        return lines.size
    }

    /**
     * Returns true if this cell is a continuation of a stream.
     */
    fun isStreamContinuation(): Boolean = false
}

/**
 * A styled span of text with optional style attributes.
 */
data class StyledSpan(
    val content: String,
    val style: SpanStyle = SpanStyle.DEFAULT
) {
    companion object {
        fun plain(text: String) = StyledSpan(text)
        fun dim(text: String) = StyledSpan(text, SpanStyle(dim = true))
        fun bold(text: String) = StyledSpan(text, SpanStyle(bold = true))
        fun italic(text: String) = StyledSpan(text, SpanStyle(italic = true))
        fun cyan(text: String) = StyledSpan(text, SpanStyle(color = SpanColor.CYAN))
        fun green(text: String) = StyledSpan(text, SpanStyle(color = SpanColor.GREEN))
        fun red(text: String) = StyledSpan(text, SpanStyle(color = SpanColor.RED))
        fun yellow(text: String) = StyledSpan(text, SpanStyle(color = SpanColor.YELLOW))
        fun magenta(text: String) = StyledSpan(text, SpanStyle(color = SpanColor.MAGENTA))
    }
}

/**
 * Style attributes for a span.
 */
data class SpanStyle(
    val bold: Boolean = false,
    val dim: Boolean = false,
    val italic: Boolean = false,
    val underlined: Boolean = false,
    val crossedOut: Boolean = false,
    val color: SpanColor? = null
) {
    companion object {
        val DEFAULT = SpanStyle()
    }

    fun merge(other: SpanStyle): SpanStyle = SpanStyle(
        bold = bold || other.bold,
        dim = dim || other.dim,
        italic = italic || other.italic,
        underlined = underlined || other.underlined,
        crossedOut = crossedOut || other.crossedOut,
        color = other.color ?: color
    )
}

/**
 * Terminal colors for spans.
 */
enum class SpanColor {
    RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE
}

/**
 * A line of styled spans.
 */
data class StyledLine(
    val spans: List<StyledSpan> = emptyList()
) {
    constructor(vararg spans: StyledSpan) : this(spans.toList())

    companion object {
        fun plain(text: String) = StyledLine(listOf(StyledSpan.plain(text)))
        fun empty() = StyledLine(emptyList())
        fun of(vararg spans: StyledSpan) = StyledLine(spans.toList())
    }

    val width: Int
        get() = spans.sumOf { it.content.length }

    fun withStyle(style: SpanStyle): StyledLine =
        StyledLine(spans.map { it.copy(style = it.style.merge(style)) })
}

// ─── Constants ──────────────────────────────────────────────────────────

const val SESSION_HEADER_MAX_INNER_WIDTH = 56
const val LIVE_PREFIX_COLS = 2

// ─── Helper Functions ───────────────────────────────────────────────────

/**
 * Compute the inner width for a bordered card.
 */
fun cardInnerWidth(width: Int, maxInnerWidth: Int): Int? {
    if (width < 4) return null
    return minOf(width - 4, maxInnerWidth)
}

/**
 * Return the emoji followed by a hair space (U+200A).
 */
fun paddedEmoji(emoji: String): String = "$emoji\u200A"

/**
 * Render lines inside a Unicode box border.
 */
fun withBorder(lines: List<StyledLine>): List<StyledLine> =
    withBorderInternal(lines, null)

/**
 * Render lines inside a border with a specific inner width.
 */
fun withBorderWithInnerWidth(lines: List<StyledLine>, innerWidth: Int): List<StyledLine> =
    withBorderInternal(lines, innerWidth)

private fun withBorderInternal(
    lines: List<StyledLine>,
    forcedInnerWidth: Int?
): List<StyledLine> {
    val maxLineWidth = lines.maxOfOrNull { line ->
        line.spans.sumOf { it.content.length }
    } ?: 0
    val contentWidth = maxOf(forcedInnerWidth ?: maxLineWidth, maxLineWidth)

    val out = mutableListOf<StyledLine>()
    val borderInnerWidth = contentWidth + 2
    out.add(StyledLine(StyledSpan.dim("╭${"─".repeat(borderInnerWidth)}╮")))

    for (line in lines) {
        val usedWidth = line.spans.sumOf { it.content.length }
        val spans = mutableListOf<StyledSpan>()
        spans.add(StyledSpan.dim("│ "))
        spans.addAll(line.spans)
        if (usedWidth < contentWidth) {
            spans.add(StyledSpan.dim(" ".repeat(contentWidth - usedWidth)))
        }
        spans.add(StyledSpan.dim(" │"))
        out.add(StyledLine(spans))
    }

    out.add(StyledLine(StyledSpan.dim("╰${"─".repeat(borderInnerWidth)}╯")))
    return out
}

/**
 * Truncate text to a max number of grapheme clusters.
 */
fun truncateText(text: String, maxChars: Int): String {
    if (text.length <= maxChars) return text
    return text.take(maxChars - 3) + "..."
}

/**
 * Truncate an exec command snippet for display.
 */
private fun truncateExecSnippet(fullCmd: String): String {
    val snippet = fullCmd.split('\n', limit = 2).let { parts ->
        if (parts.size > 1) "${parts[0]} ..." else parts[0]
    }
    return truncateText(snippet, 80)
}

/**
 * Get a display snippet from a command array.
 */
private fun execSnippet(command: List<String>): String {
    val fullCmd = command.joinToString(" ")
    return truncateExecSnippet(fullCmd)
}

/**
 * Prefix lines with an initial prefix on the first line and a subsequent
 * prefix on remaining lines.
 */
fun prefixLines(
    lines: List<StyledLine>,
    initialPrefix: StyledSpan,
    subsequentPrefix: StyledSpan
): List<StyledLine> {
    return lines.mapIndexed { index, line ->
        val prefix = if (index == 0) initialPrefix else subsequentPrefix
        StyledLine(listOf(prefix) + line.spans)
    }
}

// ─── Cell Implementations ───────────────────────────────────────────────

/**
 * Displays a user's message in the conversation history.
 */
data class UserHistoryCell(
    val message: String
) : HistoryCell {
    override fun displayLines(width: Int): List<StyledLine> {
        val wrapWidth = maxOf(width - (LIVE_PREFIX_COLS + 1), 1)
        val lines = mutableListOf<StyledLine>()

        lines.add(StyledLine.empty())

        val messageLines = message.lines().flatMap { line ->
            wrapText(line, wrapWidth).map { wrapped ->
                StyledLine.plain(wrapped)
            }
        }
        lines.addAll(
            prefixLines(
                messageLines,
                StyledSpan("› ", SpanStyle(bold = true, dim = true)),
                StyledSpan.plain("  ")
            )
        )
        lines.add(StyledLine.empty())
        return lines
    }
}

/**
 * Displays a reasoning summary block.
 */
data class ReasoningSummaryCell(
    val header: String,
    val content: String,
    val transcriptOnly: Boolean
) : HistoryCell {
    private fun lines(width: Int): List<StyledLine> {
        val wrapWidth = maxOf(width - 2, 1)
        val summaryLines = wrapText(content, wrapWidth).map { line ->
            StyledLine(StyledSpan(line, SpanStyle(dim = true, italic = true)))
        }
        return prefixLines(
            summaryLines,
            StyledSpan.dim("• "),
            StyledSpan.plain("  ")
        )
    }

    override fun displayLines(width: Int): List<StyledLine> =
        if (transcriptOnly) emptyList() else lines(width)

    override fun desiredHeight(width: Int): Int =
        if (transcriptOnly) 0 else lines(width).size

    override fun transcriptLines(width: Int): List<StyledLine> = lines(width)

    override fun desiredTranscriptHeight(width: Int): Int = lines(width).size
}

/**
 * Displays an agent's message in the conversation history.
 */
data class AgentMessageCell(
    val lines: List<StyledLine>,
    val isFirstLine: Boolean
) : HistoryCell {
    override fun displayLines(width: Int): List<StyledLine> {
        return prefixLines(
            lines,
            if (isFirstLine) StyledSpan.dim("• ") else StyledSpan.plain("  "),
            StyledSpan.plain("  ")
        )
    }

    override fun isStreamContinuation(): Boolean = !isFirstLine
}

/**
 * Displays plain lines without any special formatting.
 */
data class PlainHistoryCell(
    val lines: List<StyledLine>
) : HistoryCell {
    override fun displayLines(width: Int): List<StyledLine> = lines
}

/**
 * Displays a notification that a new version is available.
 */
data class UpdateAvailableHistoryCell(
    val latestVersion: String,
    val updateAction: UpdateAction? = null
) : HistoryCell {
    override fun displayLines(width: Int): List<StyledLine> {
        val updateInstruction = if (updateAction != null) {
            StyledLine.of(
                StyledSpan.plain("Run "),
                StyledSpan.cyan(updateAction.commandStr()),
                StyledSpan.plain(" to update.")
            )
        } else {
            StyledLine.of(
                StyledSpan.plain("See "),
                StyledSpan("https://github.com/openai/codex", SpanStyle(color = SpanColor.CYAN, underlined = true)),
                StyledSpan.plain(" for installation options.")
            )
        }

        val content = listOf(
            StyledLine.of(
                StyledSpan(paddedEmoji("✨"), SpanStyle(bold = true, color = SpanColor.CYAN)),
                StyledSpan("Update available!", SpanStyle(bold = true, color = SpanColor.CYAN)),
                StyledSpan.plain(" "),
                StyledSpan.bold("$CLI_VERSION -> $latestVersion")
            ),
            updateInstruction,
            StyledLine.empty(),
            StyledLine.plain("See full release notes:"),
            StyledLine(StyledSpan("https://github.com/openai/codex/releases/latest",
                SpanStyle(color = SpanColor.CYAN, underlined = true)))
        )

        val innerWidth = minOf(
            content.maxOfOrNull { it.width } ?: 0,
            maxOf(width - 4, 1)
        ).coerceAtLeast(1)

        return withBorderWithInnerWidth(content, innerWidth)
    }
}

/**
 * A cell with prefixed and word-wrapped text.
 */
data class PrefixedWrappedHistoryCell(
    val text: List<StyledLine>,
    val initialPrefix: StyledSpan,
    val subsequentPrefix: StyledSpan
) : HistoryCell {
    constructor(
        text: StyledLine,
        initialPrefix: StyledSpan,
        subsequentPrefix: StyledSpan
    ) : this(listOf(text), initialPrefix, subsequentPrefix)

    override fun displayLines(width: Int): List<StyledLine> {
        if (width == 0) return emptyList()
        val wrappedLines = text.flatMap { line ->
            val lineText = line.spans.joinToString("") { it.content }
            wrapText(lineText, maxOf(width, 1)).map { wrapped ->
                StyledLine(line.spans.map { it.copy(content = wrapped) }.take(1))
            }
        }
        return prefixLines(wrappedLines, initialPrefix, subsequentPrefix)
    }

    override fun desiredHeight(width: Int): Int = displayLines(width).size
}

/**
 * Displays an exec interaction with background terminal.
 */
data class UnifiedExecInteractionCell(
    val commandDisplay: String?,
    val stdin: String
) : HistoryCell {
    override fun displayLines(width: Int): List<StyledLine> {
        if (width == 0) return emptyList()

        val headerSpans = mutableListOf(
            StyledSpan.dim("↳ "),
            StyledSpan.bold("Interacted with background terminal")
        )
        if (!commandDisplay.isNullOrEmpty()) {
            headerSpans.add(StyledSpan.dim(" · "))
            headerSpans.add(StyledSpan.dim(commandDisplay))
        }
        val out = mutableListOf(StyledLine(headerSpans))

        val inputLines = if (stdin.isEmpty()) {
            listOf(StyledLine(StyledSpan.dim("(waited)")))
        } else {
            stdin.lines().map { StyledLine.plain(it) }
        }

        out.addAll(prefixLines(
            inputLines,
            StyledSpan.dim("  └ "),
            StyledSpan.dim("    ")
        ))
        return out
    }

    override fun desiredHeight(width: Int): Int = displayLines(width).size
}

/**
 * Wait cell that shows while polling a background terminal.
 */
data class UnifiedExecWaitCell(
    private var commandDisplayVal: String?,
    val animationsEnabled: Boolean
) : HistoryCell {
    init {
        commandDisplayVal = commandDisplayVal?.takeIf { it.isNotEmpty() }
    }

    fun matches(commandDisplay: String?): Boolean {
        val filtered = commandDisplay?.takeIf { it.isNotEmpty() }
        return when {
            commandDisplayVal != null && filtered != null -> commandDisplayVal == filtered
            else -> true
        }
    }

    fun updateCommandDisplay(commandDisplay: String?) {
        if (commandDisplayVal == null) {
            commandDisplayVal = commandDisplay?.takeIf { it.isNotEmpty() }
        }
    }

    fun commandDisplay(): String? = commandDisplayVal

    override fun displayLines(width: Int): List<StyledLine> {
        if (width == 0) return emptyList()

        val headerSpans = mutableListOf(StyledSpan.dim("• "))
        headerSpans.add(StyledSpan.bold("Waiting for background terminal"))

        if (!commandDisplayVal.isNullOrEmpty()) {
            headerSpans.add(StyledSpan.dim(" · "))
            headerSpans.add(StyledSpan.dim(commandDisplayVal!!))
        }
        return listOf(StyledLine(headerSpans))
    }

    override fun desiredHeight(width: Int): Int = displayLines(width).size
}

/**
 * Displays a patch/diff summary.
 */
data class PatchHistoryCell(
    val changes: Map<String, FileChange>,
    val cwd: String
) : HistoryCell {
    override fun displayLines(width: Int): List<StyledLine> {
        val lines = mutableListOf<StyledLine>()
        for ((path, change) in changes.entries.sortedBy { it.key }) {
            val (marker, color) = when (change) {
                is FileChange.Add -> "A" to SpanColor.GREEN
                is FileChange.Delete -> "D" to SpanColor.RED
                is FileChange.Update -> {
                    if (change.movePath != null) "R" to SpanColor.YELLOW
                    else "M" to SpanColor.YELLOW
                }
            }
            val displayPath = if (path.startsWith(cwd)) {
                path.removePrefix(cwd).trimStart('/')
            } else {
                path
            }
            lines.add(StyledLine.of(
                StyledSpan("  └ ", SpanStyle(dim = true)),
                StyledSpan(marker, SpanStyle(color = color)),
                StyledSpan.dim(" "),
                StyledSpan.dim(displayPath)
            ))
        }
        return lines
    }
}

/**
 * Displays a composite of multiple history cells.
 */
data class CompositeHistoryCell(
    val parts: List<HistoryCell>
) : HistoryCell {
    override fun displayLines(width: Int): List<StyledLine> {
        val out = mutableListOf<StyledLine>()
        var first = true
        for (part in parts) {
            val lines = part.displayLines(width)
            if (lines.isNotEmpty()) {
                if (!first) out.add(StyledLine.empty())
                out.addAll(lines)
                first = false
            }
        }
        return out
    }
}

/**
 * Displays a session header with model info and directory.
 */
data class SessionHeaderHistoryCell(
    val model: String,
    val reasoningEffort: String?,
    val directory: String,
    val version: String
) : HistoryCell {
    private fun formatDirectory(maxWidth: Int?): String {
        val homedir = getHomeDir()
        val formatted = if (homedir != null && directory.startsWith(homedir)) {
            val rel = directory.removePrefix(homedir)
            if (rel.isEmpty()) "~" else "~$rel"
        } else {
            directory
        }
        if (maxWidth != null && maxWidth > 0 && formatted.length > maxWidth) {
            return formatted.take(maxWidth - 3) + "..."
        }
        return formatted
    }

    private fun reasoningLabel(): String? = reasoningEffort

    override fun displayLines(width: Int): List<StyledLine> {
        val innerWidth = cardInnerWidth(width, SESSION_HEADER_MAX_INNER_WIDTH) ?: return emptyList()

        val titleSpans = listOf(
            StyledSpan.dim(">_ "),
            StyledSpan.bold("OpenAI Codex"),
            StyledSpan.dim(" "),
            StyledSpan.dim("(v$version)")
        )

        val dirLabel = "directory:"
        val labelWidth = dirLabel.length
        val modelLabel = "model:".padEnd(labelWidth)

        val modelSpans = mutableListOf<StyledSpan>(
            StyledSpan.dim("$modelLabel "),
            StyledSpan.plain(model)
        )
        reasoningLabel()?.let { reasoning ->
            modelSpans.add(StyledSpan.plain(" "))
            modelSpans.add(StyledSpan.plain(reasoning))
        }
        modelSpans.add(StyledSpan.dim("   "))
        modelSpans.add(StyledSpan.cyan("/model"))
        modelSpans.add(StyledSpan.dim(" to change"))

        val dirPrefix = "$dirLabel "
        val dirMaxWidth = innerWidth - dirPrefix.length
        val dir = formatDirectory(if (dirMaxWidth > 0) dirMaxWidth else null)
        val dirSpans = listOf(StyledSpan.dim(dirPrefix), StyledSpan.plain(dir))

        val lines = listOf(
            StyledLine(titleSpans),
            StyledLine.empty(),
            StyledLine(modelSpans),
            StyledLine(dirSpans)
        )
        return withBorder(lines)
    }
}

/**
 * Displays session info including header and help lines.
 */
data class SessionInfoCell(
    private val composite: CompositeHistoryCell
) : HistoryCell {
    override fun displayLines(width: Int) = composite.displayLines(width)
    override fun desiredHeight(width: Int) = composite.desiredHeight(width)
    override fun transcriptLines(width: Int) = composite.transcriptLines(width)
}

/**
 * Displays a tooltip.
 */
data class TooltipHistoryCell(
    val tip: String
) : HistoryCell {
    override fun displayLines(width: Int): List<StyledLine> {
        val indent = "  "
        val wrapWidth = maxOf(width - indent.length, 1)
        val tipLines = wrapText("**Tip:** $tip", wrapWidth).map { line ->
            StyledLine.plain(line)
        }
        return prefixLines(tipLines, StyledSpan.plain(indent), StyledSpan.plain(indent))
    }
}

/**
 * Displays an MCP tool call with status and result.
 */
data class McpToolCallCell(
    val callId: String,
    val serverName: String,
    val toolName: String,
    val arguments: String?,
    val animationsEnabled: Boolean,
    private var duration: Long? = null,
    private var result: Result<String>? = null,
    private var failed: Boolean = false
) : HistoryCell {

    fun complete(durationMs: Long, callResult: Result<String>): HistoryCell? {
        duration = durationMs
        result = callResult
        return null
    }

    fun markFailed() {
        failed = true
        result = Result.failure(RuntimeException("interrupted"))
    }

    private fun success(): Boolean? = when {
        result != null -> result!!.isSuccess
        failed -> false
        else -> null
    }

    override fun displayLines(width: Int): List<StyledLine> {
        val lines = mutableListOf<StyledLine>()
        val status = success()
        val bullet = when (status) {
            true -> StyledSpan("•", SpanStyle(color = SpanColor.GREEN, bold = true))
            false -> StyledSpan("•", SpanStyle(color = SpanColor.RED, bold = true))
            null -> StyledSpan.dim("○")
        }
        val headerText = if (status != null) "Called" else "Calling"

        val argsStr = arguments ?: ""
        val invocation = "$serverName.$toolName($argsStr)"

        val headerSpans = mutableListOf(bullet, StyledSpan.plain(" "), StyledSpan.bold(headerText), StyledSpan.plain(" "))
        val reserved = headerSpans.sumOf { it.content.length }

        if (invocation.length <= width - reserved) {
            headerSpans.add(StyledSpan.cyan("$serverName."))
            headerSpans.add(StyledSpan.cyan(toolName))
            headerSpans.add(StyledSpan.plain("("))
            headerSpans.add(StyledSpan.dim(argsStr))
            headerSpans.add(StyledSpan.plain(")"))
            lines.add(StyledLine(headerSpans))
        } else {
            lines.add(StyledLine(headerSpans.dropLast(1))) // drop trailing space
            val invocationLine = StyledLine.of(
                StyledSpan.cyan("$serverName."),
                StyledSpan.cyan(toolName),
                StyledSpan.plain("("),
                StyledSpan.dim(argsStr),
                StyledSpan.plain(")")
            )
            lines.addAll(prefixLines(listOf(invocationLine), StyledSpan.dim("  └ "), StyledSpan.plain("    ")))
        }

        // Result details
        result?.let { r ->
            val detailLines = mutableListOf<StyledLine>()
            r.fold(
                onSuccess = { text ->
                    if (text.isNotEmpty()) {
                        val truncated = truncateText(text, 500)
                        truncated.lines().forEach { line ->
                            detailLines.add(StyledLine(StyledSpan.dim(line)))
                        }
                    }
                },
                onFailure = { err ->
                    detailLines.add(StyledLine(StyledSpan.dim("Error: ${err.message}")))
                }
            )
            if (detailLines.isNotEmpty()) {
                lines.addAll(prefixLines(detailLines, StyledSpan.dim("  └ "), StyledSpan.plain("    ")))
            }
        }
        return lines
    }
}

/**
 * Displays a deprecation notice.
 */
data class DeprecationNoticeCell(
    val summary: String,
    val details: String? = null
) : HistoryCell {
    override fun displayLines(width: Int): List<StyledLine> {
        val lines = mutableListOf<StyledLine>()
        lines.add(StyledLine.of(
            StyledSpan("⚠ ", SpanStyle(color = SpanColor.RED, bold = true)),
            StyledSpan(summary, SpanStyle(color = SpanColor.RED))
        ))
        if (!details.isNullOrBlank()) {
            val wrapWidth = maxOf(width - 4, 1)
            wrapText(details, wrapWidth).forEach { wrapped ->
                lines.add(StyledLine(StyledSpan.dim(wrapped)))
            }
        }
        return lines
    }
}

/**
 * Displays a plan update styled like a checkbox todo list.
 */
data class PlanUpdateCell(
    val explanation: String?,
    val plan: List<PlanItem>
) : HistoryCell {
    override fun displayLines(width: Int): List<StyledLine> {
        val lines = mutableListOf<StyledLine>()
        lines.add(StyledLine.of(StyledSpan.dim("• "), StyledSpan.bold("Updated Plan")))

        val indentedLines = mutableListOf<StyledLine>()
        val note = explanation?.trim()?.takeIf { it.isNotEmpty() }
        if (note != null) {
            val wrapWidth = maxOf(width - 4, 1)
            wrapText(note, wrapWidth).forEach { wrapped ->
                indentedLines.add(StyledLine(StyledSpan(wrapped, SpanStyle(dim = true, italic = true))))
            }
        }

        if (plan.isEmpty()) {
            indentedLines.add(StyledLine(StyledSpan("(no steps provided)", SpanStyle(dim = true, italic = true))))
        } else {
            for (item in plan) {
                val (boxStr, stepStyle) = when (item.status) {
                    StepStatus.COMPLETED -> "✔ " to SpanStyle(crossedOut = true, dim = true)
                    StepStatus.IN_PROGRESS -> "□ " to SpanStyle(color = SpanColor.CYAN, bold = true)
                    StepStatus.PENDING -> "□ " to SpanStyle(dim = true)
                }
                val wrapWidth = maxOf(width - 4 - boxStr.length, 1)
                val stepLines = wrapText(item.step, wrapWidth).map { wrapped ->
                    StyledLine(StyledSpan(wrapped, stepStyle))
                }
                indentedLines.addAll(prefixLines(stepLines, StyledSpan.plain(boxStr), StyledSpan.plain("  ")))
            }
        }
        lines.addAll(prefixLines(indentedLines, StyledSpan.dim("  └ "), StyledSpan.plain("    ")))
        return lines
    }
}

/**
 * A plan item with step text and status.
 */
data class PlanItem(
    val step: String,
    val status: StepStatus
)

/**
 * Status of a plan step.
 */
enum class StepStatus {
    COMPLETED,
    IN_PROGRESS,
    PENDING
}

/**
 * Separator line after the final message.
 */
data class FinalMessageSeparator(
    val elapsedSeconds: Long? = null
) : HistoryCell {
    override fun displayLines(width: Int): List<StyledLine> {
        if (elapsedSeconds != null) {
            val elapsed = formatElapsedCompact(elapsedSeconds)
            val workedFor = "─ Worked for $elapsed ─"
            val remaining = maxOf(width - workedFor.length, 0)
            return listOf(StyledLine(StyledSpan(
                "$workedFor${"─".repeat(remaining)}",
                SpanStyle(dim = true)
            )))
        }
        return listOf(StyledLine(StyledSpan("─".repeat(width), SpanStyle(dim = true))))
    }
}

// ─── Update Action ──────────────────────────────────────────────────────


// ─── Factory Functions ──────────────────────────────────────────────────

fun newUserPrompt(message: String) = UserHistoryCell(message)

fun newUnifiedExecInteraction(commandDisplay: String?, stdin: String) =
    UnifiedExecInteractionCell(commandDisplay, stdin)

fun newUnifiedExecWaitLive(commandDisplay: String?, animationsEnabled: Boolean) =
    UnifiedExecWaitCell(commandDisplay, animationsEnabled)

fun newReviewStatusLine(message: String) =
    PlainHistoryCell(listOf(StyledLine(StyledSpan.cyan(message))))

fun newWarningEvent(message: String) =
    PrefixedWrappedHistoryCell(
        StyledLine(StyledSpan.yellow(message)),
        StyledSpan.yellow("⚠ "),
        StyledSpan.plain("  ")
    )

fun newDeprecationNotice(summary: String, details: String?) =
    DeprecationNoticeCell(summary, details)

fun newWebSearchCall(query: String) =
    PrefixedWrappedHistoryCell(
        StyledLine.of(StyledSpan.bold("Searched"), StyledSpan.plain(" "), StyledSpan.plain(query)),
        StyledSpan.dim("• "),
        StyledSpan.plain("  ")
    )

fun newInfoEvent(message: String, hint: String?): PlainHistoryCell {
    val lines = mutableListOf<StyledLine>()
    lines.add(StyledLine.of(StyledSpan.dim("ℹ "), StyledSpan.plain(message)))
    if (!hint.isNullOrEmpty()) {
        lines.add(StyledLine(StyledSpan.dim("  $hint")))
    }
    return PlainHistoryCell(lines)
}

fun newErrorEvent(message: String): PlainHistoryCell {
    val lines = listOf(StyledLine(StyledSpan("■ $message", SpanStyle(color = SpanColor.RED))))
    return PlainHistoryCell(lines)
}

fun newPlanUpdate(explanation: String?, plan: List<PlanItem>) =
    PlanUpdateCell(explanation, plan)

fun newPatchEvent(changes: Map<String, FileChange>, cwd: String) =
    PatchHistoryCell(changes, cwd)

fun newPatchApplyFailure(stderr: String): PlainHistoryCell {
    val lines = mutableListOf<StyledLine>()
    lines.add(StyledLine(StyledSpan("✘ Failed to apply patch", SpanStyle(color = SpanColor.MAGENTA, bold = true))))
    if (stderr.isNotBlank()) {
        stderr.lines().take(20).forEach { line ->
            lines.add(StyledLine(StyledSpan.dim(line)))
        }
    }
    return PlainHistoryCell(lines)
}

fun newViewImageToolCall(path: String, cwd: String): PlainHistoryCell {
    val displayPath = if (path.startsWith(cwd)) path.removePrefix(cwd).trimStart('/') else path
    return PlainHistoryCell(listOf(
        StyledLine.of(StyledSpan.dim("• "), StyledSpan.bold("Viewed Image")),
        StyledLine.of(StyledSpan.dim("  └ "), StyledSpan.dim(displayPath))
    ))
}

fun newReasoningSummaryBlock(fullReasoningBuffer: String): HistoryCell {
    val trimmed = fullReasoningBuffer.trim()
    val openIdx = trimmed.indexOf("**")
    if (openIdx >= 0) {
        val afterOpen = trimmed.substring(openIdx + 2)
        val closeIdx = afterOpen.indexOf("**")
        if (closeIdx >= 0) {
            val afterCloseIdx = openIdx + 2 + closeIdx + 2
            if (afterCloseIdx < trimmed.length) {
                val header = trimmed.substring(0, afterCloseIdx)
                val summary = trimmed.substring(afterCloseIdx)
                return ReasoningSummaryCell(header, summary, transcriptOnly = false)
            }
        }
    }
    return ReasoningSummaryCell("", trimmed, transcriptOnly = true)
}

fun newApprovalDecisionCell(command: List<String>, decision: ReviewDecision): HistoryCell {
    val snippet = execSnippet(command)
    val (symbol, summary) = when (decision) {
        ReviewDecision.APPROVED -> StyledSpan.green("✔ ") to listOf(
            StyledSpan.plain("You "), StyledSpan.bold("approved"), StyledSpan.plain(" codex to run "),
            StyledSpan.dim(snippet), StyledSpan.bold(" this time")
        )
        ReviewDecision.APPROVED_FOR_SESSION -> StyledSpan.green("✔ ") to listOf(
            StyledSpan.plain("You "), StyledSpan.bold("approved"), StyledSpan.plain(" codex to run "),
            StyledSpan.dim(snippet), StyledSpan.bold(" every time this session")
        )
        ReviewDecision.DENIED -> StyledSpan.red("✗ ") to listOf(
            StyledSpan.plain("You "), StyledSpan.bold("did not approve"), StyledSpan.plain(" codex to run "),
            StyledSpan.dim(snippet)
        )
        ReviewDecision.ABORT -> StyledSpan.red("✗ ") to listOf(
            StyledSpan.plain("You "), StyledSpan.bold("canceled"), StyledSpan.plain(" the request to run "),
            StyledSpan.dim(snippet)
        )
    }
    return PrefixedWrappedHistoryCell(
        StyledLine(summary),
        symbol,
        StyledSpan.plain("  ")
    )
}

/**
 * Review decision types.
 */
enum class ReviewDecision {
    APPROVED,
    APPROVED_FOR_SESSION,
    DENIED,
    ABORT
}

// ─── Utility ────────────────────────────────────────────────────────────

/** CLI version placeholder - should be set by the application */
var CLI_VERSION: String = "0.0.0"

/** Simple word wrapping */
fun wrapText(text: String, maxWidth: Int): List<String> {
    if (maxWidth <= 0 || text.isEmpty()) return listOf(text)
    val lines = mutableListOf<String>()
    val words = text.split(' ')
    val currentLine = StringBuilder()

    for (word in words) {
        if (currentLine.isEmpty()) {
            currentLine.append(word)
        } else if (currentLine.length + 1 + word.length <= maxWidth) {
            currentLine.append(' ').append(word)
        } else {
            lines.add(currentLine.toString())
            currentLine.clear()
            currentLine.append(word)
        }
    }
    if (currentLine.isNotEmpty()) {
        lines.add(currentLine.toString())
    }
    return lines.ifEmpty { listOf("") }
}

/** Format elapsed seconds compactly */
fun formatElapsedCompact(seconds: Long): String {
    return when {
        seconds < 60 -> "${seconds}s"
        seconds < 3600 -> "${seconds / 60}m ${seconds % 60}s"
        else -> "${seconds / 3600}h ${(seconds % 3600) / 60}m"
    }
}

/** Get home directory - platform specific */
expect fun getHomeDir(): String?
