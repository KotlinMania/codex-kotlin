// port-lint: source core/src/userInstructions.rs
package io.github.solaceharmony.codex.core.session

import io.github.solaceharmony.codex.protocol.ContentItem
import io.github.solaceharmony.codex.protocol.ResponseItem

const val USER_INSTRUCTIONS_OPEN_TAG_LEGACY: String = "<user_instructions>"
const val USER_INSTRUCTIONS_PREFIX: String = "# AGENTS.md instructions for "

data class UserInstructions(
    val directory: String,
    val text: String,
) {
    fun toResponseItem(): ResponseItem = ResponseItem.Message(
        id = null,
        role = "user",
        content = listOf(
            ContentItem.InputText(
                text = "${USER_INSTRUCTIONS_PREFIX}$directory\n\n<INSTRUCTIONS>\n$text\n</INSTRUCTIONS>",
            ),
        ),
    )

    companion object {
        fun isUserInstructions(message: List<ContentItem>): Boolean {
            if (message.size != 1) return false
            val only = message[0]
            if (only !is ContentItem.InputText) return false
            return only.text.startsWith(USER_INSTRUCTIONS_PREFIX) ||
                only.text.startsWith(USER_INSTRUCTIONS_OPEN_TAG_LEGACY)
        }
    }
}

data class DeveloperInstructions(private val text: String) {
    fun intoText(): String = text

    fun toResponseItem(): ResponseItem = ResponseItem.Message(
        id = null,
        role = "developer",
        content = listOf(
            ContentItem.InputText(text = intoText()),
        ),
    )
}
