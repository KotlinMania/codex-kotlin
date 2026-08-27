// port-lint: source codex-rs/core/src/skills/loader.rs
package io.github.kotlinmania.codex.core.config

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString

private val configJson = Json { ignoreUnknownKeys = true }

/**
 * Loader for Codex configuration. This is a first-pass skeleton that will be
 * expanded to mirror Rust `mod.rs` behavior (defaults → file → profile → overrides → env).
 */
data class LoadedConfig(
    val model: String,
    val modelProvider: String?,
    val mcpServers: Map<String, McpServerConfig>,
    val otelEnvironment: String = DEFAULT_OTEL_ENVIRONMENT,
)

object ConfigLoader {
    /**
     * Build a minimal Config from provided inputs. For now, this does not read files.
     * Later iterations will add TOML parsing and full precedence rules.
     */
    fun load(
        base: ConfigToml? = null,
        selectedProfile: String? = null,
        overrides: ConfigOverrides? = null,
    ): Result<LoadedConfig> {
        // 1) Start from base (file) values
        val fileModel = base?.model
        val fileProvider = base?.modelProvider

        // 2) Determine active profile (by argument or from file)
        val profileName = overrides?.profile ?: selectedProfile ?: base?.profile
        val profile = base?.profiles?.get(profileName ?: "")

        // 3) Apply overrides (last)
        val model = overrides?.model ?: profile?.model ?: fileModel ?: "gpt-4o-mini"
        val modelProvider = overrides?.modelProvider ?: profile?.modelProvider ?: fileProvider

        // 4) Normalize MCP servers if present
        val mcp: Map<String, McpServerConfig> =
            base?.mcpServers?.mapValues { (_, raw) ->
                raw.normalize()
            } ?: emptyMap()

        return Result.success(
            LoadedConfig(
                model = model,
                modelProvider = modelProvider,
                mcpServers = mcp,
                otelEnvironment = DEFAULT_OTEL_ENVIRONMENT,
            ),
        )
    }

    /**
     * Load from conventional file locations, using Okio for file I/O.
     * Precedence: first found wins among workingDir → repoRoot → codexHome.
     * Only JSON is supported in this pass. TOML detection will return a descriptive error.
     */
    fun loadFromFilesystem(
        codexHome: String? = null,
        repoRoot: String? = null,
        workingDir: String? = null,
        selectedProfile: String? = null,
        overrides: ConfigOverrides? = null,
        env: Map<String, String>? = null,
    ): Result<LoadedConfig> {
        val fs = SystemFileSystem

        val codexHomeDir = env?.get("CODEX_HOME") ?: codexHome
        val candidates =
            buildList<Path> {
                fun addIfNotNull(base: String?, rel: String) {
                    if (!base.isNullOrBlank()) add(Path(base.trimEnd('/') + "/" + rel))
                }
                // Working directory .codex
                addIfNotNull(workingDir, ".codex/config.json")
                addIfNotNull(workingDir, ".codex/config.toml")
                // Repo root .codex
                addIfNotNull(repoRoot, ".codex/config.json")
                addIfNotNull(repoRoot, ".codex/config.toml")
                // CODEX_HOME or provided home
                addIfNotNull(codexHomeDir, "config.json")
                addIfNotNull(codexHomeDir, "config.toml")
                // XDG-style default
                val home = env?.get("HOME")
                if (home != null) {
                    addIfNotNull("$home/.config/codex", "config.json")
                    addIfNotNull("$home/.config/codex", "config.toml")
                }
            }

        var parsed: ConfigToml? = null
        var error: String? = null
        for (path in candidates) {
            if (!fs.exists(path)) continue
            if (path.name.endsWith(".json")) {
                val json = fs.source(path).buffered().use { it.readString() }
                parsed =
                    try {
                        configJson.decodeFromString<ConfigToml>(json)
                    } catch (e: Throwable) {
                        error = "Failed to parse $path: ${e.message}"
                        null
                    }
                break
            }
            if (path.name.endsWith(".toml")) {
                error = "TOML config detected at $path, but TOML parsing is not yet implemented in this port. Provide JSON or wait for TOML support."
                break
            }
        }

        val base = parsed

        // Compose overrides: CLI overrides take precedence over ENV overrides.
        val envOverrides = envOverridesFrom(env)
        val mergedOverrides = mergeOverrides(envOverrides, overrides)

        val result =
            load(
                base = base,
                selectedProfile = selectedProfile ?: envOverrides.profile,
                overrides = mergedOverrides,
            )
        return result
            .map { loaded ->
                // Apply minimal env overrides here (extensible later)
                val otelEnv = env?.get("OTEL_ENVIRONMENT") ?: loaded.otelEnvironment
                loaded.copy(otelEnvironment = otelEnv)
            }.also {
                if (base == null && error != null) {
                    // Surface parsing/detection error if nothing was loaded
                    // but do not fail hard; caller may rely on defaults from load()
                    // To fail instead, replace with: return Result.failure(IllegalStateException(error))
                }
            }
    }

    // Build overrides from environment variables. Naming mirrors Rust where possible.
    private fun envOverridesFrom(env: Map<String, String>?): ConfigOverrides {
        if (env == null) return ConfigOverrides()
        val profile = env["CODEX_PROFILE"] ?: env["PROFILE"]
        val model = env["CODEX_MODEL"] ?: env["MODEL"]
        val modelProvider = env["CODEX_MODEL_PROVIDER"] ?: env["MODEL_PROVIDER"]
        return ConfigOverrides(
            profile = profile,
            model = model,
            modelProvider = modelProvider,
        )
    }

    // Merge two override structures; b takes precedence over a.
    private fun mergeOverrides(a: ConfigOverrides, b: ConfigOverrides?): ConfigOverrides {
        if (b == null) return a
        return ConfigOverrides(
            profile = b.profile ?: a.profile,
            model = b.model ?: a.model,
            modelProvider = b.modelProvider ?: a.modelProvider,
        )
    }
}
