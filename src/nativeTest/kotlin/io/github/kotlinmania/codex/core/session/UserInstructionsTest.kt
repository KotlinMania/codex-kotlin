// port-lint: source tests:core/src/userInstructions.rs
package io.github.kotlinmania.codex.core.session

import io.github.kotlinmania.codex.protocol.ContentItem
import io.github.kotlinmania.codex.protocol.ResponseItem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class UserInstructionsTest {
    @Test
    fun testUserInstructions() {
        val userInstructions =
            UserInstructions(
                directory = "test_directory",
                text = "test_text",
            )
        val responseItem: ResponseItem = userInstructions.toResponseItem()

        if (responseItem !is ResponseItem.Message) {
            fail("expected ResponseItem.Message")
        }

        assertEquals("user", responseItem.role)

        val content = responseItem.content
        if (content.size != 1) fail("expected one InputText content item")
        val only = content[0]
        if (only !is ContentItem.InputText) fail("expected one InputText content item")

        assertEquals(
            "# AGENTS.md instructions for test_directory\n\n<INSTRUCTIONS>\ntest_text\n</INSTRUCTIONS>",
            only.text,
        )
    }

    @Test
    fun testIsUserInstructions() {
        assertTrue(
            UserInstructions.isUserInstructions(
                listOf(
                    ContentItem.InputText(
                        text = "# AGENTS.md instructions for test_directory\n\n<INSTRUCTIONS>\ntest_text\n</INSTRUCTIONS>",
                    ),
                ),
            ),
        )
        assertTrue(
            UserInstructions.isUserInstructions(
                listOf(
                    ContentItem.InputText(
                        text = "<user_instructions>test_text</user_instructions>",
                    ),
                ),
            ),
        )
        assertFalse(
            UserInstructions.isUserInstructions(
                listOf(
                    ContentItem.InputText(text = "test_text"),
                ),
            ),
        )
    }
}
