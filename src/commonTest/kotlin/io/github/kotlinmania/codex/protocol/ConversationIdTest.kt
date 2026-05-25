// port-lint: source protocol/src/conversation_id.rs
package io.github.kotlinmania.codex.protocol

import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ConversationIdTest {
    @Test
    fun testConversationIdDefaultIsNotZeroes() {
        val id = ConversationId.default()
        val nil = Uuid.parse("00000000-0000-0000-0000-000000000000")
        assertNotEquals(nil, id.uuid)
    }
}
