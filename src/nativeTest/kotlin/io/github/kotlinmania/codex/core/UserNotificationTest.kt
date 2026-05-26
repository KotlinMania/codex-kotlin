// port-lint: source user_notification.rs
package io.github.kotlinmania.codex.core

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class UserNotificationTest {
    @Test
    fun testUserNotification() {
        val notification = UserNotification.AgentTurnComplete(
            threadId = "b5f6c1c2-1111-2222-3333-444455556666",
            turnId = "12345",
            cwd = "/Users/example/project",
            inputMessages = listOf("Rename `foo` to `bar` and update the callsites."),
            lastAssistantMessage = "Rename complete and verified `cargo build` succeeds."
        )
        val serialized = Json.encodeToString(UserNotificationSerializer, notification)
        assertEquals(
            """{"type":"agent-turn-complete","thread-id":"b5f6c1c2-1111-2222-3333-444455556666","turn-id":"12345","cwd":"/Users/example/project","input-messages":["Rename `foo` to `bar` and update the callsites."],"last-assistant-message":"Rename complete and verified `cargo build` succeeds."}""",
            serialized
        )
    }
}
