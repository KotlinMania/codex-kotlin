// port-lint: source core/src/reviewFormat.rs
package ai.solace.coder.core

import ai.solace.coder.protocol.ReviewFinding

private fun formatLocation(item: ReviewFinding): String {
    val path = item.codeLocation.absoluteFilePath
    val start = item.codeLocation.lineRange.start
    val end = item.codeLocation.lineRange.end
    return "$path:$start-$end"
}

fun formatReviewFindingsBlock(
    findings: List<ReviewFinding>,
    selection: List<Boolean>?,
): String {
    val lines = mutableListOf<String>()
    lines.add("")

    if (findings.size > 1) {
        lines.add("Full review comments:")
    } else {
        lines.add("Review comment:")
    }

    findings.forEachIndexed { idx, item ->
        lines.add("")

        val title = item.title
        val location = formatLocation(item)

        if (selection != null) {
            val checked = selection.getOrElse(idx) { true }
            val marker = if (checked) "[x]" else "[ ]"
            lines.add("- $marker $title — $location")
        } else {
            lines.add("- $title — $location")
        }

        val bodyLines = item.body.lines()
        val trimmed = if (bodyLines.isNotEmpty() && bodyLines.last().isEmpty()) {
            bodyLines.dropLast(1)
        } else {
            bodyLines
        }
        for (bodyLine in trimmed) {
            lines.add("  $bodyLine")
        }
    }

    return lines.joinToString("\n")
}
