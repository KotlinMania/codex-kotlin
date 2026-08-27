// port-lint: source models.rs
package io.github.kotlinmania.codex.protocol

import io.github.kotlinmania.codex.utils.Environment
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ModelsTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun serializesSuccessAsPlainString() {
        val item =
            ResponseInputItem.FunctionCallOutput(
                callId = "call1",
                output = FunctionCallOutputPayload(content = "ok", success = null, contentItems = null),
            )

        val element = json.encodeToJsonElement(ResponseInputItem.serializer(), item).jsonObject
        val output = element["output"]
        assertNotNull(output)
        assertEquals("ok", output.jsonPrimitive.content)
    }

    @Test
    fun serializesFailureAsString() {
        val item =
            ResponseInputItem.FunctionCallOutput(
                callId = "call1",
                output = FunctionCallOutputPayload(content = "bad", success = false, contentItems = null),
            )

        val element = json.encodeToJsonElement(ResponseInputItem.serializer(), item).jsonObject
        val output = element["output"]
        assertNotNull(output)
        assertEquals("bad", output.jsonPrimitive.content)
    }

    @Test
    fun serializesImageOutputsAsArray() {
        val callToolResult =
            CallToolResult(
                content =
                    listOf(
                        ContentBlock.TextContent(
                            text = "caption",
                            annotations = null,
                            typeField = "text",
                        ),
                        ContentBlock.ImageContent(
                            data = "BASE64",
                            mimeType = "image/png",
                            annotations = null,
                            typeField = "image",
                        ),
                    ),
                isError = null,
                structuredContent = null,
            )

        val payload = FunctionCallOutputPayload.from(callToolResult)
        assertEquals(true, payload.success)
        val items = payload.contentItems
        assertNotNull(items)
        assertEquals(
            listOf(
                FunctionCallOutputContentItem.InputText(text = "caption"),
                FunctionCallOutputContentItem.InputImage(imageUrl = "data:image/png;base64,BASE64"),
            ),
            items,
        )

        val item = ResponseInputItem.FunctionCallOutput(callId = "call1", output = payload)
        val element = json.encodeToJsonElement(ResponseInputItem.serializer(), item).jsonObject
        val output = element["output"]
        assertTrue(output is JsonArray, "expected array output")
    }

    @Test
    fun deserializesArrayPayloadIntoItems() {
        val jsonLiteral =
            """
            [
              {"type": "input_text", "text": "note"},
              {"type": "input_image", "image_url": "data:image/png;base64,XYZ"}
            ]
            """.trimIndent()

        val payload = json.decodeFromString(FunctionCallOutputPayload.serializer(), jsonLiteral)
        assertEquals(null, payload.success)
        val expectedItems =
            listOf(
                FunctionCallOutputContentItem.InputText(text = "note"),
                FunctionCallOutputContentItem.InputImage(imageUrl = "data:image/png;base64,XYZ"),
            )
        assertEquals(expectedItems, payload.contentItems)
        val expectedContent =
            json.encodeToString(
                kotlinx.serialization.builtins.ListSerializer(FunctionCallOutputContentItem.serializer()),
                expectedItems,
            )
        assertEquals(expectedContent, payload.content)
    }

    @Test
    fun roundtripsWebSearchCallActions() {
        val cases =
            listOf(
                Triple(
                    """
                    {
                      "type": "web_search_call",
                      "status": "completed",
                      "action": {
                        "type": "search",
                        "query": "weather seattle"
                      }
                    }
                    """.trimIndent(),
                    WebSearchAction.Search(query = "weather seattle"),
                    "completed",
                ),
                Triple(
                    """
                    {
                      "type": "web_search_call",
                      "status": "open",
                      "action": {
                        "type": "open_page",
                        "url": "https://example.com"
                      }
                    }
                    """.trimIndent(),
                    WebSearchAction.OpenPage(url = "https://example.com"),
                    "open",
                ),
                Triple(
                    """
                    {
                      "type": "web_search_call",
                      "status": "in_progress",
                      "action": {
                        "type": "find_in_page",
                        "url": "https://example.com/docs",
                        "pattern": "installation"
                      }
                    }
                    """.trimIndent(),
                    WebSearchAction.FindInPage(url = "https://example.com/docs", pattern = "installation"),
                    "in_progress",
                ),
            )

        for ((jsonLiteral, expectedAction, expectedStatus) in cases) {
            val parsed = json.decodeFromString(ResponseItem.serializer(), jsonLiteral)
            val expected =
                ResponseItem.WebSearchCall(
                    id = null,
                    status = expectedStatus,
                    action = expectedAction,
                )
            assertEquals(expected, parsed)

            val serialized = json.encodeToJsonElement(ResponseItem.serializer(), parsed)
            val original = json.parseToJsonElement(jsonLiteral)
            assertEquals(original, serialized)
        }
    }

    @Test
    fun deserializeShellToolCallParams() {
        val jsonLiteral =
            """
            {
              "command": ["ls", "-l"],
              "workdir": "/tmp",
              "timeout": 1000
            }
            """.trimIndent()

        val params = json.decodeFromString(ShellToolCallParams.serializer(), jsonLiteral)
        assertEquals(
            ShellToolCallParams(
                command = listOf("ls", "-l"),
                workdir = "/tmp",
                timeoutMs = 1000,
                withEscalatedPermissions = null,
                justification = null,
            ),
            params,
        )
    }

    @Test
    fun localImageReadErrorAddsPlaceholder() {
        val missingPath = makeTempPath("missing-image.png")
        val item = ResponseInputItem.from(listOf(UserInput.LocalImage(path = missingPath)))
        val message = item as ResponseInputItem.Message
        assertEquals(1, message.content.size)
        val content0 = message.content[0] as ContentItem.InputText
        assertTrue(content0.text.contains(missingPath), "placeholder should mention missing path")
    }

    @Test
    fun localImageNonImageAddsPlaceholder() {
        val path = makeTempPath("not-image.txt")
        SystemFileSystem.sink(Path(path)).buffered().use { it.writeString("not an image") }

        try {
            val item = ResponseInputItem.from(listOf(UserInput.LocalImage(path = path)))
            val message = item as ResponseInputItem.Message
            assertEquals(1, message.content.size)
            val content0 = message.content[0] as ContentItem.InputText
            assertTrue(content0.text.contains("unsupported MIME type"), "expected unsupported mime placeholder")
        } finally {
            if (SystemFileSystem.exists(Path(path))) {
                SystemFileSystem.delete(Path(path))
            }
        }
    }

    private fun makeTempPath(filename: String): String {
        val tmp =
            Environment.get("TMPDIR")
                ?: Environment.get("TEMP")
                ?: Environment.get("TMP")
                ?: "/tmp"
        val dir = "$tmp/codex-kotlin-${Random.nextInt(0, Int.MAX_VALUE)}"
        SystemFileSystem.createDirectories(Path(dir))
        return "$dir/$filename"
    }
}
