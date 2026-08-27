// port-lint: source core/src/tools/handlers/mcp_resource.rs
package io.github.kotlinmania.codex.core.tools.handlers

import io.github.kotlinmania.codex.core.FunctionCallError
import io.github.kotlinmania.codex.core.session.Session
import io.github.kotlinmania.codex.core.session.TurnContext
import io.github.kotlinmania.codex.core.tools.ToolHandler
import io.github.kotlinmania.codex.core.tools.ToolInvocation
import io.github.kotlinmania.codex.core.tools.ToolKind
import io.github.kotlinmania.codex.core.tools.ToolOutput
import io.github.kotlinmania.codex.core.tools.ToolPayload
import io.github.kotlinmania.codex.protocol.CallToolResult
import io.github.kotlinmania.codex.protocol.ContentBlock
import io.github.kotlinmania.codex.protocol.EventMsg
import io.github.kotlinmania.codex.protocol.ListResourceTemplatesRequestParams
import io.github.kotlinmania.codex.protocol.ListResourcesRequestParams
import io.github.kotlinmania.codex.protocol.McpInvocation
import io.github.kotlinmania.codex.protocol.McpResult
import io.github.kotlinmania.codex.protocol.McpToolCallBeginEvent
import io.github.kotlinmania.codex.protocol.McpToolCallEndEvent
import io.github.kotlinmania.codex.protocol.McpResource
import io.github.kotlinmania.codex.protocol.McpResourceTemplate
import io.github.kotlinmania.codex.protocol.ReadResourceRequestParams
import io.github.kotlinmania.codex.protocol.ReadResourceResult
import io.github.kotlinmania.codex.protocol.ResourceContent
import kotlin.time.TimeSource
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

internal class McpResourceHandler : ToolHandler {
        override val kind: ToolKind = ToolKind.Function

        override suspend fun handle(invocation: ToolInvocation): Result<ToolOutput> {
                val payload = invocation.payload
                val arguments =
                        when (payload) {
                                is ToolPayload.Function -> payload.arguments
                                else ->
                                        return Result.failure(
                                                FunctionCallError.RespondToModel(
                                                        "mcp_resource handler received unsupported payload"
                                                )
                                        )
                        }

                val argumentsValue =
                        parseArguments(arguments).getOrElse {
                                return Result.failure(it)
                        }

                return when (invocation.toolName) {
                        "list_mcp_resources" ->
                                handleListResources(
                                        invocation.session,
                                        invocation.turn,
                                        invocation.callId,
                                        argumentsValue
                                )
                        "list_mcp_resource_templates" ->
                                handleListResourceTemplates(
                                        invocation.session,
                                        invocation.turn,
                                        invocation.callId,
                                        argumentsValue
                                )
                        "read_mcp_resource" ->
                                handleReadResource(
                                        invocation.session,
                                        invocation.turn,
                                        invocation.callId,
                                        argumentsValue
                                )
                        else ->
                                Result.failure(
                                        FunctionCallError.RespondToModel(
                                                "unsupported MCP resource tool: ${invocation.toolName}"
                                        )
                                )
                }
        }
}

@Serializable data class ListResourcesArgs(val server: String? = null, val cursor: String? = null)

@Serializable
data class ListResourceTemplatesArgs(val server: String? = null, val cursor: String? = null)

@Serializable data class ReadResourceArgs(val server: String, val uri: String)

@Serializable
data class ResourceWithServer(
        val server: String,
        val uri: String,
        val name: String,
        val description: String? = null,
        val mimeType: String? = null
) {
        constructor(
                server: String,
                resource: McpResource
        ) : this(
                server = server,
                uri = resource.uri,
                name = resource.name,
                description = resource.description,
                mimeType = resource.mimeType
        )
}

@Serializable
data class ResourceTemplateWithServer(
        val server: String,
        val uriTemplate: String,
        val name: String,
        val description: String? = null,
        val mimeType: String? = null
) {
        constructor(
                server: String,
                template: McpResourceTemplate
        ) : this(
                server = server,
                uriTemplate = template.uriTemplate,
                name = template.name,
                description = template.description,
                mimeType = template.mimeType
        )
}

@Serializable
data class ListResourcesPayload(
        val server: String? = null,
        val resources: List<ResourceWithServer>,
        val nextCursor: String? = null
)

@Serializable
data class ListResourceTemplatesPayload(
        val server: String? = null,
        val resourceTemplates: List<ResourceTemplateWithServer>,
        val nextCursor: String? = null
)

@Serializable
data class ReadResourcePayload(
        val server: String,
        val uri: String,
        val contents: List<io.github.kotlinmania.codex.protocol.ResourceContent>,
        val mimeType: String? = null
) {
        constructor(
                server: String,
                uri: String,
                result: ReadResourceResult
        ) : this(server = server, uri = uri, contents = result.contents, mimeType = result.mimeType)
}

private suspend fun handleListResources(
        session: Session,
        turn: TurnContext,
        callId: String,
        arguments: JsonElement?
): Result<ToolOutput> {
        val args: ListResourcesArgs = parseArgsWithDefault(arguments)
        val server = normalizeOptionalString(args.server)
        val cursor = normalizeOptionalString(args.cursor)

        val invocation =
                McpInvocation(
                        server = server ?: "codex",
                        tool = "list_mcp_resources",
                        arguments = arguments
                )

        emitToolCallBegin(session, turn, callId, invocation)
        val start = TimeSource.Monotonic.markNow()

        val payloadResult =
                try {
                        if (server != null) {
                                val params = cursor?.let { ListResourcesRequestParams(cursor = it) }
                                val result = session.listResources(server, params)

                                val resources =
                                        result.resources.map { ResourceWithServer(server, it) }
                                ListResourcesPayload(server, resources, result.nextCursor)
                        } else {
                                if (cursor != null) {
                                        throw FunctionCallError.RespondToModel(
                                                "cursor can only be used when a server is specified"
                                        )
                                }
                                val resourcesByServer =
                                        session.services.mcpConnectionManager.listAllResources()
                                val allResources =
                                        resourcesByServer
                                                .flatMap { (srv, resList) ->
                                                        resList.map { ResourceWithServer(srv, it) }
                                                }
                                                .sortedBy { it.server }

                                ListResourcesPayload(null, allResources, null)
                        }
                } catch (e: Exception) {
                        val duration = start.elapsedNow().toString()
                        emitToolCallEnd(
                                session,
                                turn,
                                callId,
                                invocation,
                                duration,
                                Result.failure(e)
                        )
                        return Result.failure(
                                if (e is FunctionCallError) e
                                else FunctionCallError.RespondToModel(e.message ?: "Unknown error")
                        )
                }

        val output = serializeFunctionOutput(payloadResult)
        val duration = start.elapsedNow().toString()

        val toolResult =
                output.map {
                        callToolResultFromContent((it as ToolOutput.Function).content, it.success)
                }

        emitToolCallEnd(session, turn, callId, invocation, duration, toolResult)
        return output
}

private suspend fun handleListResourceTemplates(
        session: Session,
        turn: TurnContext,
        callId: String,
        arguments: JsonElement?
): Result<ToolOutput> {
        val args: ListResourceTemplatesArgs = parseArgsWithDefault(arguments)
        val server = normalizeOptionalString(args.server)
        val cursor = normalizeOptionalString(args.cursor)

        val invocation =
                McpInvocation(
                        server = server ?: "codex",
                        tool = "list_mcp_resource_templates",
                        arguments = arguments
                )

        emitToolCallBegin(session, turn, callId, invocation)
        val start = TimeSource.Monotonic.markNow()

        val payloadResult =
                try {
                        if (server != null) {
                                val params =
                                        cursor?.let {
                                                ListResourceTemplatesRequestParams(cursor = it)
                                        }
                                val result = session.listResourceTemplates(server, params)

                                val templates =
                                        result.resourceTemplates.map {
                                                ResourceTemplateWithServer(server, it)
                                        }
                                ListResourceTemplatesPayload(server, templates, result.nextCursor)
                        } else {
                                if (cursor != null) {
                                        throw FunctionCallError.RespondToModel(
                                                "cursor can only be used when a server is specified"
                                        )
                                }
                                val templatesByServer =
                                        session.services.mcpConnectionManager
                                                .listAllResourceTemplates()
                                val allTemplates =
                                        templatesByServer
                                                .flatMap { (srv, tmplList) ->
                                                        tmplList.map {
                                                                ResourceTemplateWithServer(srv, it)
                                                        }
                                                }
                                                .sortedBy { it.server }

                                ListResourceTemplatesPayload(null, allTemplates, null)
                        }
                } catch (e: Exception) {
                        val duration = start.elapsedNow().toString()
                        emitToolCallEnd(
                                session,
                                turn,
                                callId,
                                invocation,
                                duration,
                                Result.failure(e)
                        )
                        return Result.failure(
                                if (e is FunctionCallError) e
                                else FunctionCallError.RespondToModel(e.message ?: "Unknown error")
                        )
                }

        val output = serializeFunctionOutput(payloadResult)
        val duration = start.elapsedNow().toString()

        val toolResult =
                output.map {
                        callToolResultFromContent((it as ToolOutput.Function).content, it.success)
                }

        emitToolCallEnd(session, turn, callId, invocation, duration, toolResult)
        return output
}

private suspend fun handleReadResource(
        session: Session,
        turn: TurnContext,
        callId: String,
        arguments: JsonElement?
): Result<ToolOutput> {
        val args: ReadResourceArgs =
                parseArgs<ReadResourceArgs>(arguments).getOrElse {
                        return Result.failure(it)
                }
        val server = normalizeRequiredString("server", args.server)
        val uri = normalizeRequiredString("uri", args.uri)

        val invocation =
                McpInvocation(server = server, tool = "read_mcp_resource", arguments = arguments)

        emitToolCallBegin(session, turn, callId, invocation)
        val start = TimeSource.Monotonic.markNow()

        val payloadResult =
                try {
                        val result =
                                session.readResource(server, ReadResourceRequestParams(uri = uri))
                        ReadResourcePayload(server, uri, result)
                } catch (e: Exception) {
                        val duration = start.elapsedNow().toString()
                        emitToolCallEnd(
                                session,
                                turn,
                                callId,
                                invocation,
                                duration,
                                Result.failure(e)
                        )
                        return Result.failure(
                                if (e is FunctionCallError) e
                                else FunctionCallError.RespondToModel(e.message ?: "Unknown error")
                        )
                }

        val output = serializeFunctionOutput(payloadResult)
        val duration = start.elapsedNow().toString()

        val toolResult =
                output.map {
                        callToolResultFromContent((it as ToolOutput.Function).content, it.success)
                }

        emitToolCallEnd(session, turn, callId, invocation, duration, toolResult)
        return output
}

private fun callToolResultFromContent(content: String, success: Boolean?): CallToolResult {
        return CallToolResult(
                content = listOf(ContentBlock.TextContent(text = content)),
                isError = success?.let { !it } ?: false
        )
}

private suspend fun emitToolCallBegin(
        session: Session,
        turn: TurnContext,
        callId: String,
        invocation: McpInvocation
) {
        session.sendEvent(
                turn,
                EventMsg.McpToolCallBegin(McpToolCallBeginEvent(callId, invocation))
        )
}

private suspend fun emitToolCallEnd(
        session: Session,
        turn: TurnContext,
        callId: String,
        invocation: McpInvocation,
        duration: String,
        result: Result<CallToolResult>
) {
        val mcpResult =
                result.fold(
                        onSuccess = { McpResult<CallToolResult, String>(value = it) },
                        onFailure = { McpResult<CallToolResult, String>(error = it.message ?: "Unknown error") }
                )

        session.sendEvent(
                turn,
                EventMsg.McpToolCallEnd(
                        McpToolCallEndEvent(callId, invocation, duration, mcpResult)
                )
        )
}

private fun normalizeOptionalString(input: String?): String? {
        val trimmed = input?.trim()
        return if (trimmed.isNullOrEmpty()) null else trimmed
}

private fun normalizeRequiredString(field: String, value: String): String {
        return normalizeOptionalString(value)
                ?: throw FunctionCallError.RespondToModel("$field must be provided")
}

private inline fun <reified T> serializeFunctionOutput(payload: T): Result<ToolOutput> {
        return try {
                val content = Json.encodeToString(payload)
                Result.success(
                        ToolOutput.Function(content = content, contentItems = null, success = true)
                )
        } catch (e: Exception) {
                Result.failure(
                        FunctionCallError.RespondToModel(
                                "failed to serialize MCP resource response: ${e.message}"
                        )
                )
        }
}

private fun parseArguments(rawArgs: String): Result<JsonElement?> {
        if (rawArgs.trim().isEmpty()) return Result.success(null)
        return try {
                Result.success(Json.parseToJsonElement(rawArgs))
        } catch (e: Exception) {
                Result.failure(
                        FunctionCallError.RespondToModel(
                                "failed to parse function arguments: ${e.message}"
                        )
                )
        }
}

private inline fun <reified T> parseArgs(arguments: JsonElement?): Result<T> {
        if (arguments == null)
                return Result.failure(
                        FunctionCallError.RespondToModel(
                                "failed to parse function arguments: expected value"
                        )
                )
        return try {
                Result.success(Json.decodeFromJsonElement<T>(arguments))
        } catch (e: Exception) {
                Result.failure(
                        FunctionCallError.RespondToModel(
                                "failed to parse function arguments: ${e.message}"
                        )
                )
        }
}

private inline fun <reified T> parseArgsWithDefault(arguments: JsonElement?): T where T : Any {
        return if (arguments == null) {
                // This requires T to have a no-arg constructor or default values, which data
                // classes with
                // defaults have.
                // However, standard Json.decodeFromJsonElement does not support "default" from null
                // directly
                // for non-nullable.
                // We can try to decode an empty object if T allows it.
                try {
                        Json.decodeFromJsonElement<T>(JsonObject(emptyMap()))
                } catch (e: Exception) {
                        throw FunctionCallError.RespondToModel(
                                "failed to parse function arguments with default: ${e.message}"
                        )
                }
        } else {
                parseArgs<T>(arguments).getOrElse { throw it }
        }
}
