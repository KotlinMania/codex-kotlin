// port-lint: source util.rs
package io.github.solaceharmony.codex.core

import kotlin.test.Test
import kotlin.test.assertEquals

class UtilTest {
    @Test
    fun testTryParseErrorMessage() {
        val text = """{
  "error": {
    "message": "Your refresh token has already been used to generate a new access token. Please try signing in again.",
    "type": "invalid_request_error",
    "param": null,
    "code": "refresh_token_reused"
  }
}"""
        val message = tryParseErrorMessage(text)
        assertEquals(
            "Your refresh token has already been used to generate a new access token. Please try signing in again.",
            message
        )
    }

    @Test
    fun testTryParseErrorMessageNoError() {
        val text = """{"message": "test"}"""
        val message = tryParseErrorMessage(text)
        assertEquals("""{"message": "test"}""", message)
    }
}
