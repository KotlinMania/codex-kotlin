package ai.solace.coder.core.auth

import io.github.kotlinmania.jwt.JWT
import io.github.kotlinmania.jwt.exceptions.JWTDecodeException

/**
 * Platform-specific ID Token parser using jwt-kotlin.
 */
actual fun parseIdToken(jwt: String): Result<IdTokenInfo> {
    return try {
        // Decode JWT without verification
        val decoded = JWT.decode(jwt)

        // Extract email
        val email = decoded.getClaim("email").asString()

        // Extract OpenAI auth claims
        val authClaim = decoded.getClaim("https://api.openai.com/auth")
        val authMap = authClaim.asMap()

        val planTypeStr = authMap?.get("chatgpt_plan_type") as? String
        val planType = planTypeStr?.let { PlanType.fromString(it) }

        val accountId = authMap?.get("chatgpt_account_id") as? String

        Result.success(
            IdTokenInfo(
                email = email,
                chatgptPlanType = planType,
                chatgptAccountId = accountId,
                rawJwt = jwt
            )
        )
    } catch (e: JWTDecodeException) {
        Result.failure(Exception("Failed to decode JWT: ${e.message}", e))
    } catch (e: Exception) {
        Result.failure(Exception("JWT parsing failed: ${e.message}", e))
    }
}
