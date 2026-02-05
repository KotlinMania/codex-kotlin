// port-lint: source protocol/src/conversation_id.rs
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

/**
 * Conversation ID wrapper.
 *
 * Ported from Rust codex-rs/protocol/src/conversation_id.rs
 */
@Serializable(with = ConversationId.Serializer::class)
@OptIn(ExperimentalUuidApi::class)
data class ConversationId(
    private val uuid: Uuid
) {
    companion object {
        fun new(): ConversationId {
            return ConversationId(Uuid.random())
        }

        fun default(): ConversationId = new()

        fun fromString(s: String): Result<ConversationId> {
            return runCatching { ConversationId(Uuid.parse(s)) }
        }
    }

    override fun toString(): String = uuid.toString()

    object Serializer : KSerializer<ConversationId> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("ConversationId", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: ConversationId) {
            encoder.encodeString(value.toString())
        }

        override fun deserialize(decoder: Decoder): ConversationId {
            return fromString(decoder.decodeString()).getOrThrow()
        }
    }
}
