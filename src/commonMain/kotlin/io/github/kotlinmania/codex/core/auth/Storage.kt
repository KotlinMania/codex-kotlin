// port-lint: source storage.rs
package io.github.kotlinmania.codex.core.auth

import io.github.kotlinmania.codex.core.AuthDotJson
import io.github.kotlinmania.codex.core.platformSetOwnerReadWritePermissions
import io.github.kotlinmania.codex.utils.canonicalizePath
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString
import kotlinx.serialization.json.Json

/**
 * Determine where Codex should store CLI auth credentials.
 * Mirrors the upstream AuthCredentialsStoreMode enum from auth/storage.rs
 */
enum class AuthCredentialsStoreMode {
    /** Persist credentials in CODEX_HOME/auth.json */
    File,

    /** Persist credentials in the keyring. Fail if unavailable. */
    Keychain,

    /** Use keyring when available; otherwise, fall back to a file in CODEX_HOME */
    Auto,
}

/**
 * Get the auth.json file path within codexHome.
 */
internal fun getAuthFile(codexHome: Path): Path = Path(codexHome.toString(), "auth.json")

/**
 * Delete the auth.json file if it exists.
 * Returns true if a file was removed, false if it did not exist.
 */
internal fun deleteFileIfExists(codexHome: Path): Result<Boolean> {
    val authFile = getAuthFile(codexHome)
    return try {
        SystemFileSystem.delete(authFile)
        Result.success(true)
    } catch (_: kotlinx.io.IOException) {
        // File does not exist
        Result.success(false)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

/**
 * Auth storage backend trait.
 * Mirrors the upstream AuthStorageBackend trait from auth/storage.rs
 */
interface AuthStorageBackend {
    fun load(): Result<AuthDotJson?>

    fun save(auth: AuthDotJson): Result<Unit>

    fun delete(): Result<Boolean>
}

/**
 * File-based auth storage implementation.
 * Stores credentials in CODEX_HOME/auth.json with 0600 permissions on Unix.
 */
class FileAuthStorage(
    private val codexHome: Path,
) : AuthStorageBackend {
    /**
     * Attempt to read and parse the auth.json file.
     */
    fun tryReadAuthJson(authFile: Path): Result<AuthDotJson> =
        try {
            val contents =
                SystemFileSystem.source(authFile).buffered().use { buffered ->
                    buffered.readString()
                }
            val authDotJson = Json.decodeFromString<AuthDotJson>(contents)
            Result.success(authDotJson)
        } catch (e: Exception) {
            Result.failure(e)
        }

    override fun load(): Result<AuthDotJson?> {
        val authFile = getAuthFile(codexHome)

        // Check if file exists
        if (!SystemFileSystem.exists(authFile)) {
            return Result.success(null)
        }

        return tryReadAuthJson(authFile).map { it }
    }

    override fun save(auth: AuthDotJson): Result<Unit> {
        val authFile = getAuthFile(codexHome)

        return try {
            // Create parent directory if it does not exist
            val parent = authFile.parent
            if (parent != null && !SystemFileSystem.exists(parent)) {
                SystemFileSystem.createDirectories(parent)
            }

            // Serialize to pretty JSON
            val jsonFormat = Json { prettyPrint = true }
            val jsonData = jsonFormat.encodeToString(AuthDotJson.serializer(), auth)

            // Write to file
            // Write to file
            SystemFileSystem.sink(authFile).buffered().use { buffered ->
                buffered.writeString(jsonData)
                buffered.flush()
            }

            // Set Unix file permissions to 0600 (owner read/write only)
            platformSetOwnerReadWritePermissions(authFile.toString())

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun delete(): Result<Boolean> = deleteFileIfExists(codexHome)

    companion object {
        fun new(codexHome: Path): FileAuthStorage = FileAuthStorage(codexHome)
    }
}

/**
 * Keychain/Keyring-based auth storage implementation.
 * Stores credentials securely in the system keychain.
 */
class KeychainAuthStorage(
    private val codexHome: Path,
    private val keychainStore: KeychainStore,
) : AuthStorageBackend {
    /**
     * Load auth from keychain using computed key.
     */
    private fun loadFromKeychain(key: String): Result<AuthDotJson?> =
        keychainStore.load(KEYCHAIN_SERVICE, key).mapCatching { serialized ->
            serialized?.let { Json.decodeFromString(AuthDotJson.serializer(), it) }
        }

    /**
     * Save auth to keychain using computed key.
     */
    private fun saveToKeychain(key: String, value: String): Result<Unit> =
        keychainStore.save(KEYCHAIN_SERVICE, key, value).onFailure { error ->
            println("Warning: failed to write OAuth tokens to keychain: ${error.message}")
        }

    override fun load(): Result<AuthDotJson?> {
        val key =
            computeStoreKey(codexHome).getOrElse {
                return Result.failure(it)
            }
        return loadFromKeychain(key)
    }

    override fun save(auth: AuthDotJson): Result<Unit> {
        val key =
            computeStoreKey(codexHome).getOrElse {
                return Result.failure(it)
            }

        val serialized =
            try {
                Json.encodeToString(AuthDotJson.serializer(), auth)
            } catch (e: Exception) {
                return Result.failure(e)
            }

        saveToKeychain(key, serialized).getOrElse {
            return Result.failure(it)
        }

        // Remove fallback file if it exists
        deleteFileIfExists(codexHome).onFailure { err ->
            println("Warning: failed to remove CLI auth fallback file: ${err.message}")
        }

        return Result.success(Unit)
    }

    override fun delete(): Result<Boolean> {
        val key =
            computeStoreKey(codexHome).getOrElse {
                return Result.failure(it)
            }

        val keychainRemoved =
            keychainStore
                .delete(KEYCHAIN_SERVICE, key)
                .getOrElse { return Result.failure(it) }

        val fileRemoved =
            deleteFileIfExists(codexHome)
                .getOrElse { return Result.failure(it) }

        return Result.success(keychainRemoved || fileRemoved)
    }

    companion object {
        fun new(codexHome: Path, keychainStore: KeychainStore): KeychainAuthStorage = KeychainAuthStorage(codexHome, keychainStore)
    }
}

/**
 * Auto auth storage - tries keychain first, falls back to file.
 * Mirrors the upstream AutoAuthStorage from auth/storage.rs
 */
class AutoAuthStorage(
    private val keychainStorage: KeychainAuthStorage,
    private val fileStorage: FileAuthStorage,
) : AuthStorageBackend {
    override fun load(): Result<AuthDotJson?> {
        // Try keychain first
        val result = keychainStorage.load()
        return if (result.isSuccess) {
            result.getOrNull()?.let {
                Result.success(it)
            } ?: fileStorage.load()
        } else {
            println("Warning: failed to load CLI auth from keychain, falling back to file storage: ${result.exceptionOrNull()?.message}")
            fileStorage.load()
        }
    }

    override fun save(auth: AuthDotJson): Result<Unit> {
        // Try keychain first
        val result = keychainStorage.save(auth)
        return if (result.isSuccess) {
            Result.success(Unit)
        } else {
            println("Warning: failed to save auth to keychain, falling back to file storage: ${result.exceptionOrNull()?.message}")
            fileStorage.save(auth)
        }
    }

    override fun delete(): Result<Boolean> {
        // Keychain storage will delete from disk as well
        return keychainStorage.delete()
    }

    companion object {
        fun new(codexHome: Path, keychainStore: KeychainStore): AutoAuthStorage =
            AutoAuthStorage(
                KeychainAuthStorage.new(codexHome, keychainStore),
                FileAuthStorage.new(codexHome),
            )
    }
}

// ============================================================================
// Helper Functions
// ============================================================================

private const val KEYCHAIN_SERVICE = "Codex Auth"

/**
 * Compute a stable, short key string from the codexHome path.
 * Uses SHA-256 hash truncated to 16 characters.
 *
 * Resolves symlinks via `realpath` and prefixes the truncated hex digest
 * with `cli|`.
 */
internal fun computeStoreKey(codexHome: Path): Result<String> =
    try {
        // Implement proper path canonicalization (resolve symlinks, make absolute)
        val pathStr = codexHome.toString()
        val canonical = canonicalizePath(pathStr)

        // Hash the path string with SHA-256
        val sha256 = Sha256MessageDigest()
        val digest = sha256.digest(canonical)

        // Convert digest bytes to hex string (lowercase)
        val hex =
            buildString(digest.size * 2) {
                digest.forEach { byte ->
                    val value = byte.toInt() and 0xFF
                    append(HEX_CHARS[value shr 4])
                    append(HEX_CHARS[value and 0x0F])
                }
            }

        // Truncate to first 16 characters and prefix with "cli|"
        val truncated = hex.take(16)
        Result.success("cli|$truncated")
    } catch (e: Exception) {
        Result.failure(e)
    }

private val HEX_CHARS = "0123456789abcdef".toCharArray()

/**
 * Create auth storage backend based on the specified mode.
 */
internal fun createAuthStorage(
    codexHome: Path,
    mode: AuthCredentialsStoreMode,
): AuthStorageBackend {
    val keychainStore = DefaultKeychainStore()
    return createAuthStorageWithKeychainStore(codexHome, mode, keychainStore)
}

/**
 * Create auth storage with a specific keychain store (useful for testing).
 */
internal fun createAuthStorageWithKeychainStore(
    codexHome: Path,
    mode: AuthCredentialsStoreMode,
    keychainStore: KeychainStore,
): AuthStorageBackend =
    when (mode) {
        AuthCredentialsStoreMode.File -> FileAuthStorage.new(codexHome)
        AuthCredentialsStoreMode.Keychain -> KeychainAuthStorage.new(codexHome, keychainStore)
        AuthCredentialsStoreMode.Auto -> AutoAuthStorage.new(codexHome, keychainStore)
    }

// ============================================================================
// Keychain Store Interface
// ============================================================================

/**
 * Keychain/Keyring store interface.
 */
interface KeychainStore {
    /**
     * Load a value from the keychain.
     * Returns null if the key does not exist.
     */
    fun load(service: String, key: String): Result<String?>

    /**
     * Save a value to the keychain.
     */
    fun save(service: String, key: String, value: String): Result<Unit>

    /**
     * Delete a value from the keychain.
     * Returns true if something was deleted, false if the key did not exist.
     */
    fun delete(service: String, key: String): Result<Boolean>
}

/**
 * Default keychain store implementation.
 *
 * The native target does not yet bind to a system keychain; load returns
 * empty so [AutoAuthStorage] falls back to file storage, save returns
 * failure for the same reason, and delete reports nothing removed.
 */
class DefaultKeychainStore : KeychainStore {
    override fun load(service: String, key: String): Result<String?> = Result.success(null)

    override fun save(service: String, key: String, value: String): Result<Unit> = Result.failure(Exception("Keychain storage not implemented"))

    override fun delete(service: String, key: String): Result<Boolean> = Result.success(false)
}

// ============================================================================
// Test Support (MockKeyringStore equivalent)
// ============================================================================

/**
 * Mock keychain store for testing — stores values in memory and supports
 * setting per-key errors via [setError].
 */
class MockKeychainStore : KeychainStore {
    private val storage = mutableMapOf<String, String>()
    private val errors = mutableMapOf<String, Exception>()

    override fun load(service: String, key: String): Result<String?> {
        errors[key]?.let { return Result.failure(it) }
        return Result.success(storage[key])
    }

    override fun save(service: String, key: String, value: String): Result<Unit> {
        errors[key]?.let { return Result.failure(it) }
        storage[key] = value
        return Result.success(Unit)
    }

    override fun delete(service: String, key: String): Result<Boolean> {
        errors[key]?.let { return Result.failure(it) }
        val existed = storage.remove(key) != null
        return Result.success(existed)
    }

    // Test helpers
    fun contains(key: String): Boolean = storage.containsKey(key)

    fun savedValue(key: String): String? = storage[key]

    fun setError(key: String, error: Exception) {
        errors[key] = error
    }

    fun clear() {
        storage.clear()
        errors.clear()
    }
}
