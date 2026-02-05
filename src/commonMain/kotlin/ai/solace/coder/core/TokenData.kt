// port-lint: source core/src/token_data.rs
package ai.solace.coder.core

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class TokenData(
    /** Flat info parsed from the JWT in auth.json. */
    @SerialName("id_token") @Serializable(with = IdTokenInfoAsJwtSerializer::class)
    val idToken: IdTokenInfo = IdTokenInfo(),

    /** This is a JWT. */
    @SerialName("access_token") val accessToken: String = "",

    @SerialName("refresh_token") val refreshToken: String = "",

    @SerialName("account_id") val accountId: String? = null,
)

/** Flat subset of useful claims in id_token from auth.json. */
@Serializable
data class IdTokenInfo(
    val email: String? = null,
    /** The ChatGPT subscription plan type (values may vary by backend). */
    @SerialName("chatgpt_plan_type") val chatgptPlanType: PlanType? = null,
    /** Organization/workspace identifier associated with the token, if present. */
    @SerialName("chatgpt_account_id") val chatgptAccountId: String? = null,
    @SerialName("raw_jwt") val rawJwt: String = "",
) {
    fun getChatgptPlanType(): String? {
        val planType = chatgptPlanType ?: return null
        return when (planType) {
            is PlanType.Known -> planType.plan.name
            is PlanType.Unknown -> planType.value
        }
    }
}

@Serializable(with = PlanTypeSerializer::class)
sealed class PlanType {
    @Serializable
    data class Known(val plan: KnownPlan) : PlanType()

    @Serializable
    data class Unknown(val value: String) : PlanType()

    companion object {
        fun fromString(value: String): PlanType {
            val normalized = value.lowercase()
            return when (normalized) {
                "free" -> Known(KnownPlan.Free)
                "plus" -> Known(KnownPlan.Plus)
                "pro" -> Known(KnownPlan.Pro)
                "team" -> Known(KnownPlan.Team)
                "business" -> Known(KnownPlan.Business)
                "enterprise" -> Known(KnownPlan.Enterprise)
                "edu" -> Known(KnownPlan.Edu)
                else -> Unknown(value)
            }
        }
    }
}

@Serializable
enum class KnownPlan {
    Free,
    Plus,
    Pro,
    Team,
    Business,
    Enterprise,
    Edu,
}

private object PlanTypeSerializer : KSerializer<PlanType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("PlanType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: PlanType) {
        val str = when (value) {
            is PlanType.Known -> value.plan.name.lowercase()
            is PlanType.Unknown -> value.value
        }
        encoder.encodeString(str)
    }

    override fun deserialize(decoder: Decoder): PlanType {
        return PlanType.fromString(decoder.decodeString())
    }
}

@Serializable
private data class IdClaims(
    val email: String? = null,
    @SerialName("https://api.openai.com/auth") val auth: AuthClaims? = null,
)

@Serializable
private data class AuthClaims(
    @SerialName("chatgpt_plan_type") val chatgptPlanType: PlanType? = null,
    @SerialName("chatgpt_account_id") val chatgptAccountId: String? = null,
)

sealed class IdTokenInfoError(message: String) : Exception(message) {
    class InvalidFormat : IdTokenInfoError("invalid ID token format")
    class Base64Decode(cause: Throwable) : IdTokenInfoError(cause.message ?: "base64 decode error")
    class JsonDecode(cause: Throwable) : IdTokenInfoError(cause.message ?: "json decode error")
}

@OptIn(ExperimentalEncodingApi::class)
fun parseIdToken(idToken: String, json: Json = Json): Result<IdTokenInfo> {
    // JWT format: header.payload.signature
    val parts = idToken.split('.', limit = 3)
    if (parts.size != 3 || parts.any { it.isEmpty() }) {
        return Result.failure(IdTokenInfoError.InvalidFormat())
    }

    val payloadB64 = parts[1]

    val payloadBytes = try {
        Base64.UrlSafe.decode(padBase64Url(payloadB64))
    } catch (t: Throwable) {
        return Result.failure(IdTokenInfoError.Base64Decode(t))
    }

    val claims = try {
        json.decodeFromString(IdClaims.serializer(), payloadBytes.decodeToString())
    } catch (t: Throwable) {
        return Result.failure(IdTokenInfoError.JsonDecode(t))
    }

    val auth = claims.auth
    return Result.success(
        IdTokenInfo(
            email = claims.email,
            rawJwt = idToken,
            chatgptPlanType = auth?.chatgptPlanType,
            chatgptAccountId = auth?.chatgptAccountId,
        )
    )
}

private fun padBase64Url(input: String): String {
    // Kotlin's Base64 decoder expects padding.
    val rem = input.length % 4
    if (rem == 0) return input
    return input + "=".repeat(4 - rem)
}

/** Serializes an [IdTokenInfo] as its raw JWT string, and deserializes by parsing the JWT. */
private object IdTokenInfoAsJwtSerializer : KSerializer<IdTokenInfo> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("IdTokenInfo", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: IdTokenInfo) {
        encoder.encodeString(value.rawJwt)
    }

    override fun deserialize(decoder: Decoder): IdTokenInfo {
        val jwt = decoder.decodeString()
        return parseIdToken(jwt).getOrElse { throw SerializationException(it.message, it) }
    }
}
