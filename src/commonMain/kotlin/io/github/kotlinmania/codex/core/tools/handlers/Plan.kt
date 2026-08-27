// port-lint: source core/src/tools/handlers/plan.rs
package io.github.kotlinmania.codex.core.tools.handlers

import io.github.kotlinmania.codex.core.CodexErr
import io.github.kotlinmania.codex.core.session.ResponsesApiTool
import io.github.kotlinmania.codex.core.session.ToolSpec
import io.github.kotlinmania.codex.core.tools.AdditionalProperties
import io.github.kotlinmania.codex.core.tools.JsonSchema
import io.github.kotlinmania.codex.core.tools.ToolHandler
import io.github.kotlinmania.codex.core.tools.ToolInvocation
import io.github.kotlinmania.codex.core.tools.ToolKind
import io.github.kotlinmania.codex.core.tools.ToolOutput
import io.github.kotlinmania.codex.core.tools.ToolPayload
import io.github.kotlinmania.codex.protocol.StepStatus
import io.github.kotlinmania.codex.protocol.UpdatePlanArgs
import kotlinx.serialization.json.Json

/**
 * Handler for the updatePlan tool. Allows the model to record and update its task plan.
 *
 * This tool does not do anything useful computationally. However, it gives the model a structured
 * way to record its plan that clients can read and render. The _inputs_ to this function are useful
 * to clients, not the outputs.
 *
 * Ported from Rust codex-rs/core/src/tools/handlers/plan.rs
 */
internal class PlanHandler : ToolHandler {

    override val kind: ToolKind = ToolKind.Function

    override suspend fun handle(invocation: ToolInvocation): Result<ToolOutput> {
        val payload = invocation.payload
        if (payload !is ToolPayload.Function) {
            return Result.failure(
                    io.github.kotlinmania.codex.core.tools.ToolError.Codex(
                            CodexErr.Fatal("update_plan handler received unsupported payload")
                    )
            )
        }

        val args =
                try {
                    json.decodeFromString<UpdatePlanArgs>(payload.arguments)
                } catch (e: Exception) {
                    return Result.failure(
                            io.github.kotlinmania.codex.core.tools.ToolError.Codex(
                                    CodexErr.Fatal(
                                            "failed to parse function arguments: ${e.message}"
                                    )
                            )
                    )
                }

        // Validate that at most one step is inProgress
        val inProgressCount = args.plan.count { it.status == StepStatus.InProgress }
        if (inProgressCount > 1) {
            return Result.failure(
                    io.github.kotlinmania.codex.core.tools.ToolError.Codex(
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

/** The updatePlan tool spec. Mirrors codex-rs/core/src/tools/handlers/plan.rs PLAN_TOOL. */
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
