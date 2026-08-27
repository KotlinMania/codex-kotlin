package io.github.kotlinmania.codex.core.tools.handlers

import io.github.kotlinmania.codex.utils.Environment
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ReadFileTest {
    private val handler = ReadFileHandler()
    private val testDir = Path("${Environment.TMPDIR}/codex-readfile-test-${Random.nextInt()}")

    init {
        SystemFileSystem.createDirectories(testDir)
    }

    @AfterTest
    fun cleanup() {
        if (SystemFileSystem.exists(testDir)) {
            SystemFileSystem.list(testDir).forEach { SystemFileSystem.delete(it) }
            SystemFileSystem.delete(testDir)
        }
    }

    private fun writeFile(name: String, contents: String): String {
        val path = Path(testDir, name)
        SystemFileSystem.sink(path).buffered().use { it.writeString(contents) }
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
        val error =
            assertFailsWith<IllegalArgumentException> {
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
