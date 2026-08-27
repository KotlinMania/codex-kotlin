// port-lint: source ollama/src/client.rs (tests)
package io.github.kotlinmania.codex.ollama

import io.github.kotlinmania.codex.core.CODEX_SANDBOX_NETWORK_DISABLED_ENV_VAR
import io.github.kotlinmania.codex.utils.Environment
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

private fun networkDisabled(): Boolean =
    Environment.get(CODEX_SANDBOX_NETWORK_DISABLED_ENV_VAR) != null

class ClientTest {
    @Test
    fun testFetchModelsHappyPath() =
        runTest {
            if (networkDisabled()) {
                return@runTest
            }
            val mockEngine =
                MockEngine { request ->
                    when (request.url.encodedPath) {
                        "/api/tags" ->
                            respond(
                                content = """{"models":[{"name":"llama3.2:3b"},{"name":"mistral"}]}""",
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json"),
                            )
                        else -> respond("Not found", HttpStatusCode.NotFound)
                    }
                }
            val client = OllamaClient.fromHostRoot("http://localhost:11434", HttpClient(mockEngine))
            val models = client.fetchModels()
            assertTrue(models.contains("llama3.2:3b"))
            assertTrue(models.contains("mistral"))
        }

    @Test
    fun testProbeServerHappyPathOpenaiCompatAndNative() =
        runTest {
            if (networkDisabled()) {
                return@runTest
            }
            val mockEngine =
                MockEngine { request ->
                    when (request.url.encodedPath) {
                        "/api/tags", "/v1/models" ->
                            respond(
                                content = """{"models":[]}""",
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json"),
                            )
                        else -> respond("Not found", HttpStatusCode.NotFound)
                    }
                }
            val client1 = OllamaClient.fromHostRoot("http://localhost:11434", HttpClient(mockEngine))
            val client2 = OllamaClient.tryFromProviderWithBaseUrl("http://localhost:11434/v1", HttpClient(mockEngine))
            assertTrue(client1.fetchModels().isEmpty())
            assertTrue(client2.fetchModels().isEmpty())
        }

    @Test
    fun testTryFromOssProviderOkWhenServerRunning() =
        runTest {
            if (networkDisabled()) {
                return@runTest
            }
            val mockEngine =
                MockEngine { request ->
                    when (request.url.encodedPath) {
                        "/v1/models", "/api/tags" ->
                            respond(
                                content = """{"models":[]}""",
                                status = HttpStatusCode.OK,
                                headers = headersOf(HttpHeaders.ContentType, "application/json"),
                            )
                        else -> respond("Not found", HttpStatusCode.NotFound)
                    }
                }
            val client = OllamaClient.tryFromProviderWithBaseUrl("http://localhost:11434/v1", HttpClient(mockEngine))
            assertTrue(client.fetchModels().isEmpty())
        }

    @Test
    fun testTryFromOssProviderErrWhenServerMissing() =
        runTest {
            if (networkDisabled()) {
                return@runTest
            }
            val mockEngine =
                MockEngine { _ ->
                    respond("Internal Server Error", HttpStatusCode.InternalServerError)
                }
            val result =
                runCatching {
                    OllamaClient.tryFromProviderWithBaseUrl("http://127.0.0.1:1/v1", HttpClient(mockEngine))
                }
            assertTrue(result.isFailure)
        }
}
