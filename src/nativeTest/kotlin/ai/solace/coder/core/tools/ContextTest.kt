package ai.solace.coder.core.tools

import ai.solace.coder.protocol.ResponseInputItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

class ContextTest {
    @Test
    fun customToolCallsShouldRoundtripAsCustomOutputs() {
        val payload = ToolPayload.Custom(input = "patch")
        val response = ToolOutput.Function(
            content = "patched",
            contentItems = null,
            success = true,
        ).intoResponse("call-42", payload)

        when (response) {
            is ResponseInputItem.CustomToolCallOutput -> {
                assertEquals("call-42", response.callId)
                assertEquals("patched", response.output)
            }
            else -> fail("expected CustomToolCallOutput, got $response")
        }
    }

    @Test
    fun functionPayloadsRemainFunctionOutputs() {
        val payload = ToolPayload.Function(arguments = "{}")
        val response = ToolOutput.Function(
            content = "ok",
            contentItems = null,
            success = true,
        ).intoResponse("fn-1", payload)

        when (response) {
            is ResponseInputItem.FunctionCallOutput -> {
                assertEquals("fn-1", response.callId)
                assertEquals("ok", response.output.content)
                assertNull(response.output.contentItems)
                assertEquals(true, response.output.success)
            }
            else -> fail("expected FunctionCallOutput, got $response")
        }
    }

    @Test
    fun telemetryPreviewReturnsOriginalWithinLimits() {
        val content = "short output"
        assertEquals(content, telemetryPreview(content))
    }

    @Test
    fun telemetryPreviewTruncatesByBytes() {
        val content = "x".repeat(TELEMETRY_PREVIEW_MAX_BYTES + 8)
        val preview = telemetryPreview(content)

        assertTrue(preview.contains(TELEMETRY_PREVIEW_TRUNCATION_NOTICE))
        assertTrue(
            preview.length <=
                TELEMETRY_PREVIEW_MAX_BYTES + TELEMETRY_PREVIEW_TRUNCATION_NOTICE.length + 1,
        )
    }

    @Test
    fun telemetryPreviewTruncatesByLines() {
        val content = (0 until (TELEMETRY_PREVIEW_MAX_LINES + 5))
            .joinToString("\n") { idx -> "line $idx" }

        val preview = telemetryPreview(content)
        val lines = preview.split('\n')

        assertTrue(lines.size <= TELEMETRY_PREVIEW_MAX_LINES + 1)
        assertEquals(TELEMETRY_PREVIEW_TRUNCATION_NOTICE, lines.last())
    }
}
