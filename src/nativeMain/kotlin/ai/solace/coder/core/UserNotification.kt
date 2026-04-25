// port-lint: source codex-rs/core/src/user_notification.rs
package ai.solace.coder.core

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

/**
 * User can configure a program that will receive notifications. Each
 * notification is serialized as JSON and passed as an argument to the
 * program.
 */
@Serializable(with = UserNotificationSerializer::class)
sealed class UserNotification {
    data class AgentTurnComplete(
        val threadId: String,
        val turnId: String,
        val cwd: String,
        /** Messages that the user sent to the agent to initiate the turn. */
        val inputMessages: List<String>,
        /** The last message sent by the assistant in the turn. */
        val lastAssistantMessage: String?
    ) : UserNotification()
}

/**
 * Serializer matching Rust's
 * `#[serde(tag = "type", rename_all = "kebab-case")]` with variant
 * `#[serde(rename_all = "kebab-case")]` on fields.
 */
internal object UserNotificationSerializer : KSerializer<UserNotification> {
    @OptIn(ExperimentalSerializationApi::class)
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("UserNotification") {
            element<String>("type")
            element<String>("thread-id")
            element<String>("turn-id")
            element<String>("cwd")
            element("input-messages", listSerialDescriptor(String.serializer().descriptor))
            element<String>("last-assistant-message", isOptional = true)
        }

    override fun serialize(encoder: Encoder, value: UserNotification) {
        val out: CompositeEncoder = encoder.beginStructure(descriptor)
        when (value) {
            is UserNotification.AgentTurnComplete -> {
                out.encodeStringElement(descriptor, 0, "agent-turn-complete")
                out.encodeStringElement(descriptor, 1, value.threadId)
                out.encodeStringElement(descriptor, 2, value.turnId)
                out.encodeStringElement(descriptor, 3, value.cwd)
                out.encodeSerializableElement(
                    descriptor,
                    4,
                    ListSerializer(String.serializer()),
                    value.inputMessages
                )
                if (value.lastAssistantMessage != null) {
                    out.encodeStringElement(descriptor, 5, value.lastAssistantMessage)
                }
            }
        }
        out.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): UserNotification {
        throw UnsupportedOperationException("UserNotification deserialization is not supported")
    }
}

class UserNotifier(
    private val notifyCommand: List<String>? = null
) {
    fun notify(notification: UserNotification) {
        val cmd = notifyCommand
        if (cmd != null && cmd.isNotEmpty()) {
            invokeNotify(cmd, notification)
        }
    }

    private fun invokeNotify(notifyCommand: List<String>, notification: UserNotification) {
        val json = try {
            Json.encodeToString(UserNotificationSerializer, notification)
        } catch (_: Throwable) {
            println("ERROR: failed to serialise notification payload")
            return
        }

        val program = notifyCommand[0]
        val args: List<String> = buildList {
            if (notifyCommand.size > 1) addAll(notifyCommand.drop(1))
            add(json)
        }

        // Fire-and-forget – we do not wait for completion.
        try {
            val handle = createPlatformProcess(program, args, cwd = ".", env = emptyMap())
            handle.close()
        } catch (e: Throwable) {
            println("WARN: failed to spawn notifier '$program': ${e.message}")
        }
    }
}
