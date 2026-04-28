// port-lint: source conversationId.rs
package ai.solace.coder.protocol

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = ConversationIdSerializer::class)
data class ConversationId(
    private val uuid: String,
) {
    companion object {
        fun new(): ConversationId = ConversationId(uuid = nowV7())

        @OptIn(ExperimentalUuidApi::class)
        fun fromString(s: String): kotlin.Result<ConversationId> = runCatching {
            val parsed = Uuid.parse(s)
            ConversationId(uuid = parsed.toString())
        }

        fun default(): ConversationId = new()

        @OptIn(ExperimentalUuidApi::class)
        private fun nowV7(): String = Uuid.random().toString()
    }

    override fun toString(): String = uuid
}

object ConversationIdSerializer : KSerializer<ConversationId> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ConversationId", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ConversationId) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): ConversationId {
        val value = decoder.decodeString()
        return ConversationId.fromString(value).getOrThrow()
    }
}
