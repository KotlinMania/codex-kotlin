package io.github.kotlinmania.codex.core.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ModelProviderInfoTest {
    @Test
    fun testDeserializeOllamaModelProviderToml() {
        val expectedProvider =
            ModelProviderInfo(
                name = "Ollama",
                baseUrl = "http://localhost:11434/v1",
                envKey = null,
                envKeyInstructions = null,
                experimentalBearerToken = null,
                wireApi = WireApi.Chat,
                queryParams = null,
                httpHeaders = null,
                envHttpHeaders = null,
                requestMaxRetries = null,
                streamMaxRetries = null,
                streamIdleTimeoutMs = null,
                requiresOpenaiAuth = false,
            )

        val provider =
            ModelProviderInfo(
                name = "Ollama",
                baseUrl = "http://localhost:11434/v1",
            )
        assertEquals(expectedProvider, provider)
    }

    @Test
    fun testDeserializeAzureModelProviderToml() {
        val expectedProvider =
            ModelProviderInfo(
                name = "Azure",
                baseUrl = "https://xxxxx.openai.azure.com/openai",
                envKey = "AZURE_OPENAI_API_KEY",
                envKeyInstructions = null,
                experimentalBearerToken = null,
                wireApi = WireApi.Chat,
                queryParams = mapOf("api-version" to "2025-04-01-preview"),
                httpHeaders = null,
                envHttpHeaders = null,
                requestMaxRetries = null,
                streamMaxRetries = null,
                streamIdleTimeoutMs = null,
                requiresOpenaiAuth = false,
            )

        val provider =
            ModelProviderInfo(
                name = "Azure",
                baseUrl = "https://xxxxx.openai.azure.com/openai",
                envKey = "AZURE_OPENAI_API_KEY",
                queryParams = mapOf("api-version" to "2025-04-01-preview"),
            )
        assertEquals(expectedProvider, provider)
    }

    @Test
    fun testDeserializeExampleModelProviderToml() {
        val expectedProvider =
            ModelProviderInfo(
                name = "Example",
                baseUrl = "https://example.com",
                envKey = "API_KEY",
                envKeyInstructions = null,
                experimentalBearerToken = null,
                wireApi = WireApi.Chat,
                queryParams = null,
                httpHeaders = mapOf("X-Example-Header" to "example-value"),
                envHttpHeaders = mapOf("X-Example-Env-Header" to "EXAMPLE_ENV_VAR"),
                requestMaxRetries = null,
                streamMaxRetries = null,
                streamIdleTimeoutMs = null,
                requiresOpenaiAuth = false,
            )

        val provider =
            ModelProviderInfo(
                name = "Example",
                baseUrl = "https://example.com",
                envKey = "API_KEY",
                httpHeaders = mapOf("X-Example-Header" to "example-value"),
                envHttpHeaders = mapOf("X-Example-Env-Header" to "EXAMPLE_ENV_VAR"),
            )
        assertEquals(expectedProvider, provider)
    }

    @Test
    fun detectsAzureResponsesBaseUrls() {
        val positiveCases =
            listOf(
                "https://foo.openai.azure.com/openai",
                "https://foo.openai.azure.us/openai/deployments/bar",
                "https://foo.cognitiveservices.azure.cn/openai",
                "https://foo.aoai.azure.com/openai",
                "https://foo.openai.azure-api.net/openai",
                "https://foo.z01.azurefd.net/",
            )
        for (baseUrl in positiveCases) {
            val provider =
                ModelProviderInfo(
                    name = "test",
                    baseUrl = baseUrl,
                    envKey = null,
                    envKeyInstructions = null,
                    experimentalBearerToken = null,
                    wireApi = WireApi.Responses,
                    queryParams = null,
                    httpHeaders = null,
                    envHttpHeaders = null,
                    requestMaxRetries = null,
                    streamMaxRetries = null,
                    streamIdleTimeoutMs = null,
                    requiresOpenaiAuth = false,
                )
            val api = provider.toApiProvider(null)
            assertTrue(
                api.isAzureResponsesEndpoint(),
                "expected $baseUrl to be detected as Azure",
            )
        }

        val namedProvider =
            ModelProviderInfo(
                name = "Azure",
                baseUrl = "https://example.com",
                envKey = null,
                envKeyInstructions = null,
                experimentalBearerToken = null,
                wireApi = WireApi.Responses,
                queryParams = null,
                httpHeaders = null,
                envHttpHeaders = null,
                requestMaxRetries = null,
                streamMaxRetries = null,
                streamIdleTimeoutMs = null,
                requiresOpenaiAuth = false,
            )
        val namedApi = namedProvider.toApiProvider(null)
        assertTrue(namedApi.isAzureResponsesEndpoint())

        val negativeCases =
            listOf(
                "https://api.openai.com/v1",
                "https://example.com/openai",
                "https://myproxy.azurewebsites.net/openai",
            )
        for (baseUrl in negativeCases) {
            val provider =
                ModelProviderInfo(
                    name = "test",
                    baseUrl = baseUrl,
                    envKey = null,
                    envKeyInstructions = null,
                    experimentalBearerToken = null,
                    wireApi = WireApi.Responses,
                    queryParams = null,
                    httpHeaders = null,
                    envHttpHeaders = null,
                    requestMaxRetries = null,
                    streamMaxRetries = null,
                    streamIdleTimeoutMs = null,
                    requiresOpenaiAuth = false,
                )
            val api = provider.toApiProvider(null)
            assertFalse(
                api.isAzureResponsesEndpoint(),
                "expected $baseUrl not to be detected as Azure",
            )
        }
    }
}
