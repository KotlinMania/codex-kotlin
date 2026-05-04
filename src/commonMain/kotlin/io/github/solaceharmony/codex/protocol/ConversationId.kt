// port-lint: source protocol/src/conversation_id.rs
package io.github.solaceharmony.codex.protocol

import io.github.kotlinmania.schemars.Schema
import io.github.kotlinmania.schemars.SchemaGenerator
import io.github.kotlinmania.schemars.StringJsonSchema
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalUuidApi::class)
@Serializable(with = ConversationIdSerializer::class)
data class ConversationId(
    internal val uuid: Uuid,
) {
    companion object {
        fun new(): ConversationId {
            return ConversationId(
                uuid = Uuid.random(),
            )
        }

        fun fromString(s: String): kotlin.Result<ConversationId> {
            return runCatching {
                ConversationId(
                    uuid = Uuid.parse(s),
                )
            }
        }

        fun default(): ConversationId {
            return new()
        }

        fun schemaName(): String {
            return "ConversationId"
        }

        fun jsonSchema(generator: SchemaGenerator): Schema {
            return StringJsonSchema.jsonSchema(generator)
        }
    }

    override fun toString(): String {
        return "$uuid"
    }
}

@OptIn(ExperimentalUuidApi::class)
object ConversationIdSerializer : KSerializer<ConversationId> {
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
