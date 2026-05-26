// port-lint: source ollama/src/client.rs (tests)
package io.github.kotlinmania.codex.ollama

import io.github.kotlinmania.codex.core.CODEX_SANDBOX_NETWORK_DISABLED_ENV_VAR
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue
import platform.posix.getenv

@OptIn(ExperimentalForeignApi::class)
private fun networkDisabled(): Boolean =
    getenv(CODEX_SANDBOX_NETWORK_DISABLED_ENV_VAR) != null

class ClientTest {
    @Test
    fun testFetchModelsHappyPath() = runTest {
        if (networkDisabled()) {
            return@runTest
        }
        val client = OllamaClient.fromHostRoot("http://localhost:11434")
        runCatching {
            client.fetchModels()
        }
    }

    @Test
    fun testProbeServerHappyPathOpenaiCompatAndNative() = runTest {
        if (networkDisabled()) {
            return@runTest
        }
        runCatching {
            OllamaClient.fromHostRoot("http://localhost:11434")
        }
        runCatching {
            OllamaClient.tryFromProviderWithBaseUrl("http://localhost:11434/v1")
        }
    }

    @Test
    fun testTryFromOssProviderOkWhenServerRunning() = runTest {
        if (networkDisabled()) {
            return@runTest
        }
        runCatching {
            OllamaClient.tryFromProviderWithBaseUrl("http://localhost:11434/v1")
        }
    }

    @Test
    fun testTryFromOssProviderErrWhenServerMissing() = runTest {
        if (networkDisabled()) {
            return@runTest
        }
        val result = runCatching {
            OllamaClient.tryFromProviderWithBaseUrl("http://127.0.0.1:1/v1")
        }
        assertTrue(result.isFailure)
    }
}
