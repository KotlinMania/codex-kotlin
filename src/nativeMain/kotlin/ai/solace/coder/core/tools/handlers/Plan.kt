// port-lint: source core/src/tools/handlers/plan.rs
package ai.solace.coder.core.tools.handlers

import ai.solace.coder.core.CodexErr
import ai.solace.coder.core.session.ResponsesApiTool
import ai.solace.coder.core.session.ToolSpec
import ai.solace.coder.core.tools.AdditionalProperties
import ai.solace.coder.core.tools.JsonSchema
import ai.solace.coder.core.tools.ToolHandler
import ai.solace.coder.core.tools.ToolInvocation
import ai.solace.coder.core.tools.ToolKind
import ai.solace.coder.core.tools.ToolOutput
import ai.solace.coder.core.tools.ToolPayload
import ai.solace.coder.protocol.StepStatus
import ai.solace.coder.protocol.UpdatePlanArgs
import kotlinx.serialization.json.Json

/**
 * Handler for the update_plan tool. Allows the model to record and update its task plan.
 *
 * This tool doesn't do anything useful computationally. However, it gives the model a structured
 * way to record its plan that clients can read and render. The _inputs_ to this function are useful
 * to clients, not the outputs.
 *
 * Ported from Rust codex-rs/core/src/tools/handlers/plan.rs
 */
class PlanHandler : ToolHandler {

    override val kind: ToolKind = ToolKind.Function

    override suspend fun handle(invocation: ToolInvocation): Result<ToolOutput> {
        val payload = invocation.payload
        if (payload !is ToolPayload.Function) {
            return Result.failure(
                    ai.solace.coder.core.tools.ToolError.Codex(
                            CodexErr.Fatal("update_plan handler received unsupported payload")
                    )
            )
        }

        val args =
                try {
                    json.decodeFromString<UpdatePlanArgs>(payload.arguments)
                } catch (e: Exception) {
                    return Result.failure(
                            ai.solace.coder.core.tools.ToolError.Codex(
                                    CodexErr.Fatal(
                                            "failed to parse function arguments: ${e.message}"
                                    )
                            )
                    )
                }

        // Validate that at most one step is in_progress
        val inProgressCount = args.plan.count { it.status == StepStatus.InProgress }
        if (inProgressCount > 1) {
            return Result.failure(
                    ai.solace.coder.core.tools.ToolError.Codex(
                            CodexErr.Fatal("at most one step can be in_progress at a time")
                    )
            )
        }

        // The actual plan update event would be sent through the session
        // For now, we return success - the session layer handles event emission
        return Result.success(
                ToolOutput.Function(content = "Plan updated", contentItems = null, success = true)
        )
    }

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
}

/** The update_plan tool spec. Mirrors codex-rs/core/src/tools/handlers/plan.rs PLAN_TOOL. */
val PLAN_TOOL: ToolSpec = run {
    val planItemProps = mutableMapOf<String, JsonSchema>()
    planItemProps["step"] = JsonSchema.String(description = null)
    planItemProps["status"] = JsonSchema.String(
        description = "One of: pending, in_progress, completed"
    )

    val planItemsSchema = JsonSchema.Array(
        description = "The list of steps",
        items = JsonSchema.Object(
            properties = planItemProps,
            required = listOf("step", "status"),
            additionalProperties = AdditionalProperties.from(false)
        )
    )

    val properties = mutableMapOf<String, JsonSchema>()
    properties["explanation"] = JsonSchema.String(description = null)
    properties["plan"] = planItemsSchema

    ToolSpec.Function(
        ResponsesApiTool(
            name = "update_plan",
            description = """Updates the task plan.
Provide an optional explanation and a list of plan items, each with a step and status.
At most one step can be in_progress at a time.
""",
            strict = false,
            parameters = JsonSchema.Object(
                properties = properties,
                required = listOf("plan"),
                additionalProperties = AdditionalProperties.from(false)
            )
        )
    )
}
