package ai.solace.coder.core.sandboxing

import ai.solace.coder.core.auth.CodexAuth
import ai.solace.coder.core.client.ModelClient
import ai.solace.coder.core.client.OtelEventManager
import ai.solace.coder.core.config.Config
import ai.solace.coder.core.model.ModelProviderInfo
import ai.solace.coder.core.prompt.Prompt
import ai.solace.coder.protocol.*
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import kotlin.time.Duration.Companion.seconds

/**
 * Logic for assessing sandbox commands for safety.
 *
 * Ported from codex-rs/core/src/sandboxing/assessment.rs
 */
object Assessment {
    private val SANDBOX_ASSESSMENT_TIMEOUT = 15.seconds
    private val SANDBOX_ASSESSMENT_REASONING_EFFORT = ReasoningEffortConfig.Medium

    /**
     * Assess a command for safety by querying a model.
     */
    suspend fun assessCommand(
        config: Config,
        provider: ModelProviderInfo,
        authManager: Any?, // Placeholder for AuthManager
        otel: OtelEventManager,
        conversationId: ConversationId,
        sessionSource: SessionSource,
        callId: String,
        command: List<String>,
        sandboxPolicy: SandboxPolicy,
        cwd: String,
        failureMessage: String?
    ): SandboxCommandAssessment? {
        if (!config.experimentalSandboxCommandAssessment || command.isEmpty()) {
            return null
        }

        val commandJson = Json.encodeToString(command)
        val commandJoined = shlexJoin(command)
        val failure = failureMessage?.trim()?.takeIf { it.isNotEmpty() }
        val sandboxSummary = summarizeSandboxPolicy(sandboxPolicy)
        val roots = sandboxRootsForPrompt(sandboxPolicy, cwd).distinct().sorted()
        val filesystemRoots = if (roots.isEmpty()) null else roots.joinToString(", ")

        val systemPrompt = renderSystemPrompt(
            platform = "macos", // TODO: Get actual platform
            sandboxPolicy = sandboxSummary,
            filesystemRoots = filesystemRoots,
            workingDirectory = cwd,
            commandArgv = commandJson,
            commandJoined = commandJoined,
            sandboxFailureMessage = failure
        )

        val prompt = Prompt(
            input = listOf(ResponseItem.Message(role = Role.User, content = listOf(ContentItem.InputText(text = "Assess this command.")))),
            tools = emptyList(),
            parallelToolCalls = false,
            baseInstructionsOverride = systemPrompt,
            outputSchema = sandboxAssessmentSchema()
        )

        // Create a model client for assessment
        // Note: In Kotlin we might need to properly initialize this
        val client = ModelClient(
            config = config,
            auth = null, // TODO: Pass proper auth
            otelEventManager = otel,
            provider = provider,
            effort = SANDBOX_ASSESSMENT_REASONING_EFFORT,
            verbosity = config.modelReasoningSummary,
            conversationId = conversationId,
            sessionSource = sessionSource
        )

        return withTimeoutOrNull(SANDBOX_ASSESSMENT_TIMEOUT) {
            try {
                val streamResult = client.stream(prompt)
                if (streamResult.isFailure) return@withTimeoutOrNull null
                val stream = streamResult.getOrNull() ?: return@withTimeoutOrNull null

                var lastJson: String? = null
                while (true) {
                    val eventResult = stream.next() ?: break
                    if (eventResult.isFailure) break
                    val event = eventResult.getOrNull() ?: break

                    when (event) {
                        is ResponseEvent.OutputItemDone -> {
                            val text = responseItemText(event.item)
                            if (text != null) {
                                lastJson = text
                            }
                        }
                        is ResponseEvent.Completed -> break
                        else -> continue
                    }
                }

                lastJson?.let {
                    try {
                        Json { ignoreUnknownKeys = true }.decodeFromString<SandboxCommandAssessment>(it)
                    } catch (e: Exception) {
                        null
                    }
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun summarizeSandboxPolicy(policy: SandboxPolicy): String {
        return when (policy) {
            is SandboxPolicy.DangerFullAccess -> "None (Full host access)"
            is SandboxPolicy.ReadOnly -> "Read-only access"
            is SandboxPolicy.WorkspaceWrite -> "Workspace write access"
        }
    }

    private fun sandboxRootsForPrompt(policy: SandboxPolicy, cwd: String): List<String> {
        val roots = mutableListOf(cwd)
        if (policy is SandboxPolicy.WorkspaceWrite) {
            roots.addAll(policy.writableRoots)
        }
        return roots
    }

    private fun shlexJoin(args: List<String>): String {
        return args.joinToString(" ") { arg ->
            if (arg.isEmpty()) "''"
            else if (arg.contains(Regex("[\\s\"']"))) {
                "'" + arg.replace("'", "'\\''") + "'"
            } else {
                arg
            }
        }
    }

    private fun sandboxAssessmentSchema(): JsonObject {
        return buildJsonObject {
            put("type", "object")
            putJsonArray("required") {
                add("description")
                add("risk_level")
            }
            putJsonObject("properties") {
                putJsonObject("description") {
                    put("type", "string")
                    put("minLength", 1)
                    put("maxLength", 500)
                }
                putJsonObject("risk_level") {
                    put("type", "string")
                    putJsonArray("enum") {
                        add("low")
                        add("medium")
                        add("high")
                    }
                }
            }
            put("additionalProperties", false)
        }
    }

    private fun responseItemText(item: ResponseItem): String? {
        return when (item) {
            is ResponseItem.Message -> {
                val buffers = mutableListOf<String>()
                for (segment in item.content) {
                    when (segment) {
                        is ContentItem.InputText -> if (segment.text.isNotEmpty()) buffers.add(segment.text)
                        is ContentItem.OutputText -> if (segment.text.isNotEmpty()) buffers.add(segment.text)
                        else -> {}
                    }
                }
                if (buffers.isEmpty()) null else buffers.joinToString("\n")
            }
            is ResponseItem.FunctionCallOutput -> item.output.content
            else -> null
        }
    }

    private fun renderSystemPrompt(
        platform: String,
        sandboxPolicy: String,
        filesystemRoots: String?,
        workingDirectory: String,
        commandArgv: String,
        commandJoined: String,
        sandboxFailureMessage: String?
    ): String {
        return buildString {
            appendLine("You are a security analyst evaluating shell commands that were blocked by a sandbox. Given the provided metadata, summarize the command's likely intent and assess the risk to help the user decide whether to approve command execution. Return strictly valid JSON with the keys:")
            appendLine("- description (concise summary of command intent and potential effects, no more than one sentence, use present tense)")
            appendLine("- risk_level (\"low\", \"medium\", or \"high\")")
            appendLine("Risk level examples:")
            appendLine("- low: read-only inspections, listing files, printing configuration, fetching artifacts from trusted sources")
            appendLine("- medium: modifying project files, installing dependencies")
            appendLine("- high: deleting or overwriting data, exfiltrating secrets, escalating privileges, or disabling security controls")
            appendLine("If information is insufficient, choose the most cautious risk level supported by the evidence.")
            appendLine("Respond with JSON only, without markdown code fences or extra commentary.")
            appendLine("---")
            appendLine("Command metadata:")
            appendLine("Platform: $platform")
            appendLine("Sandbox policy: $sandboxPolicy")
            if (filesystemRoots != null) {
                appendLine("Filesystem roots: $filesystemRoots")
            }
            appendLine("Working directory: $workingDirectory")
            appendLine("Command argv: $commandArgv")
            appendLine("Command (joined): $commandJoined")
            if (sandboxFailureMessage != null) {
                appendLine("Sandbox failure message: $sandboxFailureMessage")
            }
        }
    }
}
