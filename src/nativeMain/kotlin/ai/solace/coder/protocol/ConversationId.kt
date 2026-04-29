// port-lint: source conversation_id.rs
package ai.solace.coder.protocol

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

@Serializable(with = ConversationIdSerializer::class)
data class ConversationId(
    private val uuid: Uuid,
) {
    companion object {
        fun new(): ConversationId {
            val uuid = nowV7()
            return ConversationId(uuid = uuid)
        }

        @OptIn(ExperimentalUuidApi::class)
        fun fromString(s: String): kotlin.Result<ConversationId> = runCatching {
            val uuid = Uuid.parse(s)
            ConversationId(uuid = uuid)
        }

        fun default(): ConversationId {
            return new()
        }

        @OptIn(ExperimentalUuidApi::class)
        private fun nowV7(): Uuid = Uuid.random()

        fun schemaName(): String {
            return "ConversationId"
        }

        fun jsonSchema(generator: SchemaGenerator): Schema {
            return StringJsonSchema.jsonSchema(generator)
        }
    }

    override fun toString(): String {
        return fmt()
    }

    fun fmt(): String {
        return uuid.toString()
    }
}

object ConversationIdSerializer : KSerializer<ConversationId> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("ConversationId", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ConversationId) {
        encoder.collectStr(value.uuid.toString())
    }

    override fun deserialize(decoder: Decoder): ConversationId {
        val value = decoder.decodeString()
        val uuid = Uuid.parse(value)
        return ConversationId(uuid = uuid)
    }
}

private fun Encoder.collectStr(value: String) {
    encodeString(value)
}
