// port-lint: source protocol/src/conversation_id.rs
package io.github.kotlinmania.codex.protocol

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable(with = ConversationIdSerializer::class)
data class ConversationId(
    internal val uuid: Uuid,
) {
    companion object {
        fun new(): ConversationId =
            ConversationId(
                uuid = Uuid.random(),
            )

        fun fromString(s: String): kotlin.Result<ConversationId> =
            runCatching {
                ConversationId(
                    uuid = Uuid.parse(s),
                )
            }

        fun default(): ConversationId = new()

        fun schemaName(): String = "ConversationId"
    }

    override fun toString(): String = "$uuid"
}

@OptIn(ExperimentalUuidApi::class)
internal object ConversationIdSerializer : KSerializer<ConversationId> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ConversationId", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ConversationId) {
        encoder.encodeString(value.uuid.toString())
    }

    override fun deserialize(decoder: Decoder): ConversationId {
        val value = decoder.decodeString()
        val uuid = Uuid.parse(value)
        return ConversationId(uuid = uuid)
    }
}
