package io.github.kotlinmania.codex.core.bash

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BashParserTest {
    private fun parseSeq(src: String): List<List<String>>? = parseShellLcPlainCommands(listOf("bash", "-lc", src))

    @Test
    fun acceptsSingleSimpleCommand() {
        val cmds = parseSeq("ls -1")
        assertEquals(listOf(listOf("ls", "-1")), cmds)
    }

    @Test
    fun acceptsMultipleCommandsWithAllowedOperators() {
        val src = "ls && pwd; echo 'hi there' | wc -l"
        val cmds = parseSeq(src)
        val expected =
            listOf(
                listOf("ls"),
                listOf("pwd"),
                listOf("echo", "hi there"),
                listOf("wc", "-l"),
            )
        assertEquals(expected, cmds)
    }

    @Test
    fun extractsDoubleAndSingleQuotedStrings() {
        assertEquals(listOf(listOf("echo", "hello world")), parseSeq("echo \"hello world\""))
        assertEquals(listOf(listOf("echo", "hi there")), parseSeq("echo 'hi there'"))
    }

    @Test
    fun acceptsNumbersAsWords() {
        assertEquals(listOf(listOf("echo", "123", "456")), parseSeq("echo 123 456"))
    }

    @Test
    fun rejectsParenthesesAndSubshells() {
        assertNull(parseSeq("(ls)"))
        assertNull(parseSeq("ls || (pwd && echo hi)"))
    }

    @Test
    fun rejectsRedirectionsAndUnsupportedOperators() {
        assertNull(parseSeq("ls > out.txt"))
        assertNull(parseSeq("echo hi & echo bye"))
    }

    @Test
    fun rejectsCommandAndProcessSubstitutionsAndExpansions() {
        assertNull(parseSeq("echo $(pwd)"))
        assertNull(parseSeq("echo `pwd`"))
        assertNull(parseSeq("echo $HOME"))
        assertNull(parseSeq("echo \"hi $USER\""))
    }

    @Test
    fun rejectsVariableAssignmentPrefix() {
        assertNull(parseSeq("FOO=bar ls"))
    }

    @Test
    fun rejectsTrailingOperatorParseError() {
        assertNull(parseSeq("ls &&"))
    }

    @Test
    fun parseZshLcPlainCommands() {
        val command = listOf("zsh", "-lc", "ls")
        val parsed = parseShellLcPlainCommands(command)
        assertEquals(listOf(listOf("ls")), parsed)
    }
}
