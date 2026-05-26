package io.github.kotlinmania.codex.core.tools.handlers

import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ReadFileTest {

    private val fileSystem = FileSystem.SYSTEM
    private val handler = ReadFileHandler(fileSystem)
    private val testDir = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "codex-readfile-test-${Random.nextInt()}"

    init {
        fileSystem.createDirectories(testDir)
    }

    @AfterTest
    fun cleanup() {
        if (fileSystem.exists(testDir)) {
            fileSystem.listOrNull(testDir)?.forEach { fileSystem.delete(it) }
            fileSystem.delete(testDir)
        }
    }

    private fun writeFile(name: String, contents: String): String {
        val path = testDir / name
        fileSystem.sink(path).buffer().use { it.writeUtf8(contents) }
        return path.toString()
    }

    @Test
    fun readsRequestedRange() {
        val path = writeFile("range.txt", "alpha\nbeta\ngamma\n")
        val lines = handler.readSlice(path, offset = 2, limit = 2)
        assertEquals(listOf("L2: beta", "L3: gamma"), lines)
    }

    @Test
    fun errorsWhenOffsetExceedsLength() {
        val path = writeFile("short.txt", "only\n")
        val error = assertFailsWith<IllegalArgumentException> {
            handler.readSlice(path, offset = 3, limit = 1)
        }
        assertEquals("offset exceeds file length", error.message)
    }

    @Test
    fun trimsCrlfEndings() {
        val path = writeFile("crlf.txt", "one\r\ntwo\r\n")
        val lines = handler.readSlice(path, offset = 1, limit = 2)
        assertEquals(listOf("L1: one", "L2: two"), lines)
    }

    @Test
    fun respectsLimitEvenWithMoreLines() {
        val path = writeFile("multi.txt", "first\nsecond\nthird\n")
        val lines = handler.readSlice(path, offset = 1, limit = 2)
        assertEquals(listOf("L1: first", "L2: second"), lines)
    }
}
