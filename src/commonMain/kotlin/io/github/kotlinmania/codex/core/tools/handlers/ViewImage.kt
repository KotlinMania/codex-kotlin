// port-lint: source core/src/tools/handlers/view_image.rs
package io.github.kotlinmania.codex.core.tools.handlers

import io.github.kotlinmania.codex.core.CodexErr
import io.github.kotlinmania.codex.core.tools.ToolError
import io.github.kotlinmania.codex.core.tools.ToolHandler
import io.github.kotlinmania.codex.core.tools.ToolInvocation
import io.github.kotlinmania.codex.core.tools.ToolKind
import io.github.kotlinmania.codex.core.tools.ToolOutput
import io.github.kotlinmania.codex.core.tools.ToolPayload
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path.Companion.toPath

/**
 * Handler for the viewImage tool. Attaches a local image file to the conversation.
 *
 * Ported from Rust codex-rs/core/src/tools/handlers/viewImage.rs
 */
class ViewImageHandler : ToolHandler {

    override val kind: ToolKind = ToolKind.Function

    override suspend fun handle(invocation: ToolInvocation): Result<ToolOutput> {
        val payload = invocation.payload
        if (payload !is ToolPayload.Function) {
            return Result.failure(
                    ToolError.Codex(
                            CodexErr.Fatal("view_image handler received unsupported payload")
                    )
            )
        }

        val args =
                try {
                    VIEW_IMAGE_JSON.decodeFromString<ViewImageArgs>(payload.arguments)
                } catch (e: Exception) {
                    return Result.failure(
                            ToolError.Codex(
                                    CodexErr.Fatal(
                                            "failed to parse function arguments: ${e.message}"
                                    )
                            )
                    )
                }

        val absPath = invocation.turn.resolvePath(args.path)
        val path = absPath.toPath()

        // Check if file exists
        if (!FileSystem.SYSTEM.exists(path)) {
            return Result.failure(
                    ToolError.Codex(
                            CodexErr.Fatal(
                                    "unable to locate image at `$absPath`: path does not exist"
                            )
                    )
            )
        }

        // Check if it a file (not a directory)
        val metadata = FileSystem.SYSTEM.metadataOrNull(path)
        if (metadata == null || !metadata.isRegularFile) {
            return Result.failure(
                    ToolError.Codex(CodexErr.Fatal("image path `$absPath` is not a file"))
            )
        }

        // Verify it a supported image format
        val extension = path.name.substringAfterLast('.', "").lowercase()
        if (!VIEW_IMAGE_SUPPORTED_EXTENSIONS.contains(extension)) {
            return Result.failure(
                    ToolError.Codex(CodexErr.Fatal("unsupported image format: .$extension"))
            )
        }

        return Result.success(
                ToolOutput.Function(
                        content = "attached local image path: $absPath",
                        success = true
                )
        )
    }
}

/** Arguments for the viewImage tool. */
@Serializable private data class ViewImageArgs(val path: String)

private val VIEW_IMAGE_JSON = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

private val VIEW_IMAGE_SUPPORTED_EXTENSIONS =
        setOf("jpg", "jpeg", "png", "gif", "webp", "bmp", "tiff", "tif")
