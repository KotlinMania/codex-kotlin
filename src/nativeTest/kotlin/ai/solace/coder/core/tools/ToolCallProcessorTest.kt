package ai.solace.coder.core.tools

import ai.solace.coder.core.ProcessItemsResult
import ai.solace.coder.core.ProcessedResponseItem
import ai.solace.coder.core.ToolCallProcessorConfig
import ai.solace.coder.protocol.ContentBlock
import ai.solace.coder.protocol.ContentItem
import ai.solace.coder.protocol.FunctionCallOutputPayload
import ai.solace.coder.protocol.ResponseInputItem
import ai.solace.coder.protocol.ResponseItem
import ai.solace.coder.protocol.CallToolResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ToolCallProcessorTest {

    @Test
    fun testDefaultConfig() {
        val config = ToolCallProcessorConfig()
        assertEquals(false, config.enableParallelExecution)
        assertEquals(1, config.maxConcurrentCalls)
        assertEquals(60000L, config.defaultTimeoutMs)
    }

    @Test
    fun testCustomConfig() {
        val config = ToolCallProcessorConfig(
            enableParallelExecution = true,
            maxConcurrentCalls = 4,
            defaultTimeoutMs = 120000L
        )
        assertEquals(true, config.enableParallelExecution)
        assertEquals(4, config.maxConcurrentCalls)
        assertEquals(120000L, config.defaultTimeoutMs)
    }
}

class ProcessedResponseItemTest {

    @Test
    fun testProcessedResponseItemWithResponse() {
        val item = ResponseItem.Message(
            role = "assistant",
            content = listOf(ContentItem.OutputText(text = "Result"))
        )
        val response = ResponseInputItem.FunctionCallOutput(
            callId = "call_1",
            output = FunctionCallOutputPayload(content = "done")
        )

        val processed = ProcessedResponseItem(item = item, response = response)
        assertEquals(item, processed.item)
        assertEquals(response, processed.response)
    }

    @Test
    fun testProcessedResponseItemWithoutResponse() {
        val item = ResponseItem.Message(
            role = "assistant",
            content = listOf(ContentItem.OutputText(text = "Hello"))
        )

        val processed = ProcessedResponseItem(item = item, response = null)
        assertEquals(item, processed.item)
        assertNull(processed.response)
    }
}

class FunctionCallOutputPayloadFromCallToolResultTest {

    @Test
    fun testFromCallToolResultWithError() {
        val result = CallToolResult(
            content = listOf(ContentBlock.TextContent(text = "Error occurred")),
            isError = true
        )

        val payload = FunctionCallOutputPayload.from(result)
        // isError=true should result in success=false
        assertEquals(false, payload.success)
    }

    @Test
    fun testFromCallToolResultIsErrorNull() {
        val result = CallToolResult(
            content = emptyList(),
            isError = null
        )

        val payload = FunctionCallOutputPayload.from(result)
        // isError=null (not true) should result in success=true
        assertEquals(true, payload.success)
    }

    @Test
    fun testFromCallToolResultEmptyContent() {
        val result = CallToolResult(
            content = emptyList(),
            isError = false
        )

        val payload = FunctionCallOutputPayload.from(result)
        // Empty content list should still succeed
        assertEquals(true, payload.success)
        assertNull(payload.contentItems) // No images
    }
}

class ProcessItemsResultTest {

    @Test
    fun testProcessItemsResultCreation() {
        val responses = listOf(
            ResponseInputItem.FunctionCallOutput(
                callId = "call_1",
                output = FunctionCallOutputPayload(content = "output1")
            )
        )
        val items = listOf<ResponseItem>(
            ResponseItem.FunctionCallOutput(
                callId = "call_1",
                output = FunctionCallOutputPayload(content = "output1")
            )
        )

        val result = ProcessItemsResult(
            responses = responses,
            itemsToRecord = items
        )

        assertEquals(1, result.responses.size)
        assertEquals(1, result.itemsToRecord.size)
    }
}
