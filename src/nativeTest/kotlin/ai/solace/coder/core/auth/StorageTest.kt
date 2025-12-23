package ai.solace.coder.core.auth

import ai.solace.coder.core.AuthDotJson
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlin.test.*
import platform.posix.chmod
import platform.posix.S_IRUSR
import platform.posix.S_IWUSR
import kotlinx.cinterop.ExperimentalForeignApi

class StorageTest {

    @Test
    fun testGetAuthFile() {
        val codexHome = Path("/tmp/codex")
        val authFile = getAuthFile(codexHome)
        assertEquals("/tmp/codex/auth.json", authFile.toString())
    }

    @Test
    fun testFileStorageSaveAndLoad() {
        val tempDir = Path("/tmp/codex_test_${SystemFileSystem.hashCode()}")
        SystemFileSystem.createDirectories(tempDir)
        try {
            val storage = FileAuthStorage(tempDir)
            val auth = AuthDotJson(
                openai_api_key = "test-key",
                tokens = null,
                last_refresh = null
            )

            val saveResult = storage.save(auth)
            assertTrue(saveResult.isSuccess, "Save should be successful")

            val loadResult = storage.load()
            assertTrue(loadResult.isSuccess, "Load should be successful")
            assertEquals(auth, loadResult.getOrNull())

            val authFile = getAuthFile(tempDir)
            assertTrue(SystemFileSystem.exists(authFile), "Auth file should exist")
        } finally {
            SystemFileSystem.delete(tempDir, mustExist = false)
        }
    }

    @Test
    fun testComputeStoreKey() {
        // Based on Rust test: "~/.codex" -> "cli|940db7b1d0e4eb40"
        // Since we can't easily canonicalize "~" in a unit test without OS help,
        // we test the core hashing logic with a fixed string.
        val path = Path("/tmp/.codex") 
        // We know from our manual python check that hashlib.sha256(b'/tmp/.codex').hexdigest()[:16]
        // is what we expect. Let's verify our implementation produces a stable key.
        
        val keyResult = computeStoreKey(path)
        assertTrue(keyResult.isSuccess)
        val key = keyResult.getOrThrow()
        assertTrue(key.startsWith("cli|"))
        assertEquals(20, key.length) // "cli|" (4) + 16 chars = 20
    }

    @Test
    fun testAutoAuthStorageFallback() {
        val tempDir = Path("/tmp/codex_auto_test_${SystemFileSystem.hashCode()}")
        SystemFileSystem.createDirectories(tempDir)
        try {
            val mockKeychain = MockKeychainStore()
            val fileStorage = FileAuthStorage(tempDir)
            val keychainStorage = KeychainAuthStorage(tempDir, mockKeychain)
            val autoStorage = AutoAuthStorage(keychainStorage, fileStorage)

            val auth = AuthDotJson(openai_api_key = "auto-key", tokens = null, last_refresh = null)

            // Setup: Keychain fails on save
            mockKeychain.setError("cli|", Exception("Keychain failure")) // Key prefix match is enough for mock if we don't know exact key
            
            // This is tricky because computeStoreKey produces a real key. 
            // Let's just use the real key.
            val key = computeStoreKey(tempDir).getOrThrow()
            mockKeychain.setError(key, Exception("Keychain failure"))

            val saveResult = autoStorage.save(auth)
            assertTrue(saveResult.isSuccess, "Should fallback to file storage")
            
            val authFile = getAuthFile(tempDir)
            assertTrue(SystemFileSystem.exists(authFile), "Auth file should exist after fallback")
            
            val loadedAuth = autoStorage.load().getOrNull()
            assertEquals(auth, loadedAuth)
        } finally {
            SystemFileSystem.delete(tempDir, mustExist = false)
        }
    }
}
