// port-lint: source otel/src/otel_event_manager.rs
package ai.solace.coder.otel

import ai.solace.coder.api.sse.SseEvent
import ai.solace.coder.core.auth.AuthMode
import ai.solace.coder.protocol.AskForApproval
import ai.solace.coder.protocol.ConversationId
import ai.solace.coder.protocol.ReasoningEffort
import ai.solace.coder.protocol.ReasoningSummary
import ai.solace.coder.protocol.ResponseItem
import ai.solace.coder.protocol.ReviewDecision
import ai.solace.coder.protocol.SandboxPolicy
import ai.solace.coder.protocol.SandboxRiskLevel
import ai.solace.coder.protocol.UserInput
import io.ktor.client.statement.HttpResponse
import kotlinx.datetime.Clock
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlin.time.Duration
import kotlin.time.TimeSource

enum class ToolDecisionSource {
    Config,
    User;

    override fun toString(): String =
        when (this) {
            Config -> "config"
            User -> "user"
        }
}

data class OtelEventMetadata(
    val conversationId: ConversationId,
    val authMode: String?,
    val accountId: String?,
    val accountEmail: String?,
    val model: String,
    val slug: String,
    val logUserPrompts: Boolean,
    val appVersion: String,
    val terminalType: String,
)

class OtelEventManager private constructor(
    private val metadata: OtelEventMetadata,
) {
    companion object {
        private val json: Json =
            Json {
                ignoreUnknownKeys = true
            }

        fun new(
            conversationId: ConversationId,
            model: String,
            slug: String,
            accountId: String?,
            accountEmail: String?,
            authMode: AuthMode?,
            logUserPrompts: Boolean,
            terminalType: String,
            appVersion: String = "<unknown>",
        ): OtelEventManager {
            return OtelEventManager(
                OtelEventMetadata(
                    conversationId = conversationId,
                    authMode = authMode?.toString(),
                    accountId = accountId,
                    accountEmail = accountEmail,
                    model = model,
                    slug = slug,
                    logUserPrompts = logUserPrompts,
                    appVersion = appVersion,
                    terminalType = terminalType,
                ),
            )
        }
    }

    fun withModel(model: String, slug: String): OtelEventManager =
        OtelEventManager(metadata.copy(model = model, slug = slug))

    @Suppress("LongParameterList")
    fun conversationStarts(
        providerName: String,
        reasoningEffort: ReasoningEffort?,
        reasoningSummary: ReasoningSummary,
        contextWindow: Long?,
        autoCompactTokenLimit: Long?,
        approvalPolicy: AskForApproval,
        sandboxPolicy: SandboxPolicy,
        mcpServers: List<String>,
        activeProfile: String?,
    ) {
        event("codex.conversation_starts") {
            eventTimestamp = timestamp()
            conversationId = metadata.conversationId.toString()
            appVersion = metadata.appVersion
            authMode = metadata.authMode
            userAccountId = metadata.accountId
            userEmail = metadata.accountEmail
            terminalType = metadata.terminalType
            model = metadata.model
            slug = metadata.slug
            provider = providerName
            reasoningEffortStr = reasoningEffort?.toString()
            reasoningSummaryStr = reasoningSummary.toString()
            contextWindowSize = contextWindow
            autoCompactTokenLimitSize = autoCompactTokenLimit
            approvalPolicyStr = askForApprovalAsStr(approvalPolicy)
            sandboxPolicyStr = sandboxPolicyAsStr(sandboxPolicy)
            mcpServersCsv = mcpServers.joinToString(", ")
            activeProfileName = activeProfile
        }
    }

    suspend fun logRequest(
        attempt: Long,
        f: suspend () -> Result<HttpResponse>,
    ): Result<HttpResponse> {
        val start = TimeSource.Monotonic.markNow()
        val response = f()
        val duration = start.elapsedNow()

        val status = response.getOrNull()?.status?.value
        val error = response.exceptionOrNull()?.toString()
        recordApiRequest(attempt = attempt, status = status, errorMessage = error, duration = duration)

        return response
    }

    fun recordApiRequest(
        attempt: Long,
        status: Int?,
        errorMessage: String?,
        duration: Duration,
    ) {
        event("codex.api_request") {
            eventTimestamp = timestamp()
            conversationId = metadata.conversationId.toString()
            appVersion = metadata.appVersion
            authMode = metadata.authMode
            userAccountId = metadata.accountId
            userEmail = metadata.accountEmail
            terminalType = metadata.terminalType
            model = metadata.model
            slug = metadata.slug
            durationMs = duration.inWholeMilliseconds
            httpResponseStatusCode = status
            errorMessageStr = errorMessage
            attemptNum = attempt
        }
    }

    fun logSseEvent(
        response: Result<SseEvent?>,
        duration: Duration,
    ) {
        val sse = response.getOrNull() ?: return

        if (sse.data.trim() == "[DONE]") {
            sseEvent(kind = sse.event ?: "", duration = duration)
            return
        }

        val eventName = sse.event
        val element =
            try {
                json.parseToJsonElement(sse.data)
            } catch (e: SerializationException) {
                sseEventFailed(kind = eventName, duration = duration, error = e.toString())
                return
            } catch (e: IllegalArgumentException) {
                sseEventFailed(kind = eventName, duration = duration, error = e.toString())
                return
            }

        when (eventName) {
            "response.failed" -> {
                sseEventFailed(kind = eventName, duration = duration, error = element)
            }
            "response.output_item.done" -> {
                val ok =
                    try {
                        json.decodeFromJsonElement(ResponseItem.serializer(), element)
                        true
                    } catch (_: SerializationException) {
                        false
                    } catch (_: IllegalArgumentException) {
                        false
                    }
                if (ok) {
                    sseEvent(kind = eventName, duration = duration)
                } else {
                    sseEventFailed(
                        kind = eventName,
                        duration = duration,
                        error = "failed to parse response.output_item.done",
                    )
                }
            }
            else -> sseEvent(kind = eventName ?: "", duration = duration)
        }
    }

    private fun sseEvent(kind: String, duration: Duration) {
        event("codex.sse_event") {
            eventTimestamp = timestamp()
            eventKind = kind
            conversationId = metadata.conversationId.toString()
            appVersion = metadata.appVersion
            authMode = metadata.authMode
            userAccountId = metadata.accountId
            userEmail = metadata.accountEmail
            terminalType = metadata.terminalType
            model = metadata.model
            slug = metadata.slug
            durationMs = duration.inWholeMilliseconds
        }
    }

    fun seeEventCompletedFailed(error: String) {
        event("codex.sse_event") {
            eventTimestamp = timestamp()
            eventKind = "response.completed"
            conversationId = metadata.conversationId.toString()
            appVersion = metadata.appVersion
            authMode = metadata.authMode
            userAccountId = metadata.accountId
            userEmail = metadata.accountEmail
            terminalType = metadata.terminalType
            model = metadata.model
            slug = metadata.slug
            errorMessageStr = error
        }
    }

    @Suppress("LongParameterList")
    fun sseEventCompleted(
        inputTokenCount: Long,
        outputTokenCount: Long,
        cachedTokenCount: Long?,
        reasoningTokenCount: Long?,
        toolTokenCount: Long,
    ) {
        event("codex.sse_event") {
            eventTimestamp = timestamp()
            eventKind = "response.completed"
            conversationId = metadata.conversationId.toString()
            appVersion = metadata.appVersion
            authMode = metadata.authMode
            userAccountId = metadata.accountId
            userEmail = metadata.accountEmail
            terminalType = metadata.terminalType
            model = metadata.model
            slug = metadata.slug
            inputTokenCountNum = inputTokenCount
            outputTokenCountNum = outputTokenCount
            cachedTokenCountNum = cachedTokenCount
            reasoningTokenCountNum = reasoningTokenCount
            toolTokenCountNum = toolTokenCount
        }
    }

    fun userPrompt(items: List<UserInput>) {
        val prompt =
            items
                .asSequence()
                .mapNotNull { item ->
                    when (item) {
                        is UserInput.Text -> item.text
                        else -> null
                    }
                }
                .joinToString(separator = "")

        val promptToLog = if (metadata.logUserPrompts) prompt else "[REDACTED]"

        event("codex.user_prompt") {
            eventTimestamp = timestamp()
            conversationId = metadata.conversationId.toString()
            appVersion = metadata.appVersion
            authMode = metadata.authMode
            userAccountId = metadata.accountId
            userEmail = metadata.accountEmail
            terminalType = metadata.terminalType
            model = metadata.model
            slug = metadata.slug
            promptLength = countCodePoints(prompt)
            promptText = promptToLog
        }
    }

    fun toolDecision(
        toolName: String,
        callId: String,
        decision: ReviewDecision,
        source: ToolDecisionSource,
    ) {
        event("codex.tool_decision") {
            eventTimestamp = timestamp()
            conversationId = metadata.conversationId.toString()
            appVersion = metadata.appVersion
            authMode = metadata.authMode
            userAccountId = metadata.accountId
            userEmail = metadata.accountEmail
            terminalType = metadata.terminalType
            model = metadata.model
            slug = metadata.slug
            toolNameStr = toolName
            callIdStr = callId
            decisionStr = reviewDecisionAsStr(decision)
            sourceStr = source.toString()
        }
    }

    fun sandboxAssessment(
        callId: String,
        status: String,
        riskLevel: SandboxRiskLevel?,
        duration: Duration,
    ) {
        event("codex.sandbox_assessment") {
            eventTimestamp = timestamp()
            conversationId = metadata.conversationId.toString()
            appVersion = metadata.appVersion
            authMode = metadata.authMode
            userAccountId = metadata.accountId
            userEmail = metadata.accountEmail
            terminalType = metadata.terminalType
            model = metadata.model
            slug = metadata.slug
            callIdStr = callId
            statusStr = status
            riskLevelStr = riskLevel?.asStr()
            durationMs = duration.inWholeMilliseconds
        }
    }

    fun sandboxAssessmentLatency(callId: String, duration: Duration) {
        event("codex.sandbox_assessment_latency") {
            eventTimestamp = timestamp()
            conversationId = metadata.conversationId.toString()
            appVersion = metadata.appVersion
            authMode = metadata.authMode
            userAccountId = metadata.accountId
            userEmail = metadata.accountEmail
            terminalType = metadata.terminalType
            model = metadata.model
            slug = metadata.slug
            callIdStr = callId
            durationMs = duration.inWholeMilliseconds
        }
    }

    suspend fun logToolResult(
        toolName: String,
        callId: String,
        arguments: String,
        f: suspend () -> Result<Pair<String, Boolean>>,
    ): Result<Pair<String, Boolean>> {
        val start = TimeSource.Monotonic.markNow()
        val result = f()
        val duration = start.elapsedNow()

        val (output, success) =
            if (result.isSuccess) {
                val (preview, ok) = result.getOrThrow()
                preview to ok
            } else {
                (result.exceptionOrNull()?.toString() ?: "unknown error") to false
            }

        toolResult(
            toolName = toolName,
            callId = callId,
            arguments = arguments,
            duration = duration,
            success = success,
            output = output,
        )

        return result
    }

    fun logToolFailed(toolName: String, error: String) {
        event("codex.tool_result") {
            eventTimestamp = timestamp()
            conversationId = metadata.conversationId.toString()
            appVersion = metadata.appVersion
            authMode = metadata.authMode
            userAccountId = metadata.accountId
            userEmail = metadata.accountEmail
            terminalType = metadata.terminalType
            model = metadata.model
            slug = metadata.slug
            toolNameStr = toolName
            durationMs = Duration.ZERO.inWholeMilliseconds
            successBool = false
            outputText = error
        }
    }

    @Suppress("LongParameterList")
    fun toolResult(
        toolName: String,
        callId: String,
        arguments: String,
        duration: Duration,
        success: Boolean,
        output: String,
    ) {
        event("codex.tool_result") {
            eventTimestamp = timestamp()
            conversationId = metadata.conversationId.toString()
            appVersion = metadata.appVersion
            authMode = metadata.authMode
            userAccountId = metadata.accountId
            userEmail = metadata.accountEmail
            terminalType = metadata.terminalType
            model = metadata.model
            slug = metadata.slug
            toolNameStr = toolName
            callIdStr = callId
            argumentsStr = arguments
            durationMs = duration.inWholeMilliseconds
            successBool = success
            outputText = output
        }
    }

    private fun sseEventFailed(
        kind: String?,
        duration: Duration,
        error: Any,
    ) {
        if (kind != null) {
            event("codex.sse_event") {
                eventTimestamp = timestamp()
                eventKind = kind
                conversationId = metadata.conversationId.toString()
                appVersion = metadata.appVersion
                authMode = metadata.authMode
                userAccountId = metadata.accountId
                userEmail = metadata.accountEmail
                terminalType = metadata.terminalType
                model = metadata.model
                slug = metadata.slug
                durationMs = duration.inWholeMilliseconds
                errorMessageStr = error.toString()
            }
        } else {
            event("codex.sse_event") {
                eventTimestamp = timestamp()
                conversationId = metadata.conversationId.toString()
                appVersion = metadata.appVersion
                authMode = metadata.authMode
                userAccountId = metadata.accountId
                userEmail = metadata.accountEmail
                terminalType = metadata.terminalType
                model = metadata.model
                slug = metadata.slug
                durationMs = duration.inWholeMilliseconds
                errorMessageStr = error.toString()
            }
        }
    }

    private fun event(name: String, f: Fields.() -> Unit) {
        // TODO: Wire this to an OpenTelemetry exporter. The Rust implementation uses `tracing::event!`.
        val fields = Fields().apply(f)
        @Suppress("UNUSED_VARIABLE")
        val ignored = name to fields
    }
}

private class Fields {
    var eventTimestamp: String? = null
    var eventKind: String? = null
    var conversationId: String? = null
    var appVersion: String? = null
    var authMode: String? = null
    var userAccountId: String? = null
    var userEmail: String? = null
    var terminalType: String? = null
    var model: String? = null
    var slug: String? = null

    var provider: String? = null
    var reasoningEffortStr: String? = null
    var reasoningSummaryStr: String? = null
    var contextWindowSize: Long? = null
    var autoCompactTokenLimitSize: Long? = null
    var approvalPolicyStr: String? = null
    var sandboxPolicyStr: String? = null
    var mcpServersCsv: String? = null
    var activeProfileName: String? = null

    var durationMs: Long? = null
    var httpResponseStatusCode: Int? = null
    var errorMessageStr: String? = null
    var attemptNum: Long? = null

    var inputTokenCountNum: Long? = null
    var outputTokenCountNum: Long? = null
    var cachedTokenCountNum: Long? = null
    var reasoningTokenCountNum: Long? = null
    var toolTokenCountNum: Long? = null

    var promptLength: Int? = null
    var promptText: String? = null

    var toolNameStr: String? = null
    var callIdStr: String? = null
    var argumentsStr: String? = null
    var decisionStr: String? = null
    var sourceStr: String? = null

    var statusStr: String? = null
    var riskLevelStr: String? = null

    var successBool: Boolean? = null
    var outputText: String? = null
}

private fun timestamp(): String {
    val raw = Clock.System.now().toString()
    // Ensure we always log a fractional portion (millis), matching Rust SecondsFormat::Millis.
    if (!raw.contains('.')) {
        return raw.replace("Z", ".000Z")
    }

    val dot = raw.indexOf('.')
    val z = raw.indexOf('Z', startIndex = dot + 1)
    if (z == -1) {
        return raw
    }

    val frac = raw.substring(dot + 1, z)
    val fixed =
        when {
            frac.length >= 3 -> frac.substring(0, 3)
            else -> frac.padEnd(3, '0')
        }
    return raw.substring(0, dot + 1) + fixed + "Z"
}

private fun askForApprovalAsStr(v: AskForApproval): String =
    when (v) {
        AskForApproval.UnlessTrusted -> "untrusted"
        AskForApproval.OnFailure -> "on-failure"
        AskForApproval.OnRequest -> "on-request"
        AskForApproval.Never -> "never"
    }

private fun sandboxPolicyAsStr(v: SandboxPolicy): String =
    when (v) {
        is SandboxPolicy.DangerFullAccess -> "danger-full-access"
        is SandboxPolicy.ReadOnly -> "read-only"
        is SandboxPolicy.WorkspaceWrite -> "workspace-write"
    }

private fun reviewDecisionAsStr(v: ReviewDecision): String =
    when (v) {
        ReviewDecision.Approved -> "approved"
        ReviewDecision.ApprovedForSession -> "approved_for_session"
        ReviewDecision.Denied -> "denied"
        ReviewDecision.Abort -> "abort"
    }

private fun countCodePoints(s: String): Int {
    var i = 0
    var count = 0
    while (i < s.length) {
        val c = s[i]
        if (c.isHighSurrogate() && i + 1 < s.length && s[i + 1].isLowSurrogate()) {
            i += 2
        } else {
            i += 1
        }
        count += 1
    }
    return count
}
