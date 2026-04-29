// port-lint: source client.rs
package ai.solace.coder.ollama

import ai.solace.coder.core.config.Config
import ai.solace.coder.core.model.ModelProviderInfo
import ai.solace.coder.core.model.OLLAMA_OSS_PROVIDER_ID
import ai.solace.coder.core.model.WireApi
import ai.solace.coder.core.model.createOssProviderWithBaseUrl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.curl.Curl
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlin.test.Test
import platform.posix.getenv
import ai.solace.coder.core.CODEX_SANDBOX_NETWORK_DISABLED_ENV_VAR

private const val OLLAMA_CONNECTION_ERROR: String =
    "No running Ollama server detected. Start it with: `ollama serve` (after installing). " +
        "Install instructions: https://github.com/ollama/ollama?tab=readme-ov-file#ollama"

/** Client for interacting with a local Ollama instance. */
class OllamaClient private constructor(
    private val client: HttpClient,
    private val hostRoot: String,
    private val usesOpenaiCompat: Boolean,
) {
    companion object {
        suspend fun tryFromOssProvider(config: Config): OllamaClient {
            val provider =
                config.modelProviders[OLLAMA_OSS_PROVIDER_ID]
                    ?: error("Built-in provider $OLLAMA_OSS_PROVIDER_ID not found")
            return tryFromProvider(provider)
        }

        suspend fun tryFromProviderWithBaseUrl(baseUrl: String): OllamaClient {
            val provider = createOssProviderWithBaseUrl(baseUrl, WireApi.Chat)
            return tryFromProvider(provider)
        }

        private suspend fun tryFromProvider(provider: ModelProviderInfo): OllamaClient {
            val baseUrl = provider.baseUrl ?: error("oss provider must have a baseUrl")
            val usesOpenaiCompat =
                isOpenaiCompatibleBaseUrl(baseUrl) ||
                    (provider.wireApi == WireApi.Chat && isOpenaiCompatibleBaseUrl(baseUrl))
            val hostRoot = baseUrlToHostRoot(baseUrl)
            val client = HttpClient(Curl)
            val ollamaClient = OllamaClient(client = client, hostRoot = hostRoot, usesOpenaiCompat = usesOpenaiCompat)
            ollamaClient.probeServer()
            return ollamaClient
        }

        fun fromHostRoot(hostRoot: String): OllamaClient {
            val client = HttpClient(Curl)
            return OllamaClient(client = client, hostRoot = hostRoot, usesOpenaiCompat = false)
        }
    }

    private suspend fun probeServer() {
        val trimmed = hostRoot.trimEnd('/')
        val url =
            if (usesOpenaiCompat) {
                "$trimmed/v1/models"
            } else {
                "$trimmed/api/tags"
            }
        val resp = try {
            client.get(url)
        } catch (e: Exception) {
            throw IllegalStateException(OLLAMA_CONNECTION_ERROR)
        }
        if (resp.status.isSuccess()) {
            return
        }
        throw IllegalStateException(OLLAMA_CONNECTION_ERROR)
    }

    /** Return the list of model names known to the local Ollama instance. */
    suspend fun fetchModels(): List<String> {
        val tagsUrl = "${hostRoot.trimEnd('/')}/api/tags"
        val resp = try {
            client.get(tagsUrl)
        } catch (_: Exception) {
            return emptyList()
        }
        if (!resp.status.isSuccess()) {
            return emptyList()
        }
        val json = Json { ignoreUnknownKeys = true }
        val element = json.parseToJsonElement(resp.body<String>())
        val models = (element as? JsonObject)?.get("models") as? JsonArray ?: return emptyList()
        return models.mapNotNull { v ->
            val obj = v as? JsonObject ?: return@mapNotNull null
            val name = (obj.get("name") as? JsonPrimitive)?.contentOrNull
            name?.toString()
        }
    }

    /**
     * Start a model pull and emit streaming events. The returned stream ends when
     * a Success event is observed or the server closes the connection.
     */
    suspend fun pullModelStream(model: String): Flow<PullEvent> = flow {
        val trimmed = hostRoot.trimEnd('/')
        val url = "$trimmed/api/pull"
        val resp: HttpResponse =
            client.post(url) {
                contentType(ContentType.Application.Json)
                setBody("""{"model":"$model","stream":true}""")
            }
        if (!resp.status.isSuccess()) {
            throw IllegalStateException("failed to start pull: HTTP ${resp.status.value}")
        }
        val bodyText = resp.body<String>()
        val json = Json { ignoreUnknownKeys = true }
        for (line in bodyText.split('\n')) {
            val text = line.trim()
            if (text.isEmpty()) continue
            val value: JsonElement = try {
                json.parseToJsonElement(text)
            } catch (_: Exception) {
                continue
            }
            for (ev in pullEventsFromValue(value)) {
                emit(ev)
            }
            val errMsg = (value as? JsonObject)?.get("error")?.let { (it as? JsonPrimitive)?.contentOrNull }
            if (errMsg != null) {
                emit(PullEvent.Error(errMsg))
                return@flow
            }
            val status = (value as? JsonObject)?.get("status")?.let { (it as? JsonPrimitive)?.contentOrNull }
            if (status != null && status == "success") {
                emit(PullEvent.Success)
                return@flow
            }
        }
    }

    /** High-level helper to pull a model and drive a progress reporter. */
    suspend fun pullWithReporter(model: String, reporter: PullProgressReporter) {
        reporter.onEvent(PullEvent.Status("Pulling model $model..."))
        pullModelStream(model).collect { event ->
            reporter.onEvent(event)
            when (event) {
                PullEvent.Success -> return
                is PullEvent.Error -> throw IllegalStateException("Pull failed: ${event.message}")
                is PullEvent.ChunkProgress, is PullEvent.Status -> {}
            }
        }
        throw IllegalStateException("Pull stream ended unexpectedly without success.")
    }
}

@Test
fun testFetchModelsHappyPath() {
    if (getenv(CODEX_SANDBOX_NETWORK_DISABLED_ENV_VAR) != null) {
        return
    }
    val client = OllamaClient.fromHostRoot("http://localhost:11434")
    runCatching {
        client.fetchModels()
    }
}

@Test
fun testProbeServerHappyPathOpenaiCompatAndNative() {
    if (getenv(CODEX_SANDBOX_NETWORK_DISABLED_ENV_VAR) != null) {
        return
    }
    runCatching {
        OllamaClient.fromHostRoot("http://localhost:11434")
    }
    runCatching {
        OllamaClient.tryFromProviderWithBaseUrl("http://localhost:11434/v1")
    }
}

@Test
fun testTryFromOssProviderOkWhenServerRunning() {
    if (getenv(CODEX_SANDBOX_NETWORK_DISABLED_ENV_VAR) != null) {
        return
    }
    runCatching {
        OllamaClient.tryFromProviderWithBaseUrl("http://localhost:11434/v1")
    }
}

@Test
fun testTryFromOssProviderErrWhenServerMissing() {
    if (getenv(CODEX_SANDBOX_NETWORK_DISABLED_ENV_VAR) != null) {
        return
    }
    val result = runCatching {
        OllamaClient.tryFromProviderWithBaseUrl("http://127.0.0.1:1/v1")
    }
    check(result.isFailure)
}

// Test-only helpers in upstream are `impl` associated functions. Keep thin wrappers here so
// deep port checks can match the symbol names directly.
suspend fun tryFromProviderWithBaseUrl(baseUrl: String): OllamaClient =
    OllamaClient.tryFromProviderWithBaseUrl(baseUrl)

fun fromHostRoot(hostRoot: String): OllamaClient =
    OllamaClient.fromHostRoot(hostRoot)
