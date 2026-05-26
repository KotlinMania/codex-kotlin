// port-lint: tests protocol/src/num_format.rs
package io.github.kotlinmania.codex.protocol

import kotlin.test.Test
import kotlin.test.assertEquals

class NumFormatTest {
    @Test
    fun kmg() {
        assertEquals("0", formatSiSuffix(0))
        assertEquals("999", formatSiSuffix(999))
        assertEquals("1.00K", formatSiSuffix(1_000))
        assertEquals("1.20K", formatSiSuffix(1_200))
        assertEquals("10.0K", formatSiSuffix(10_000))
        assertEquals("100K", formatSiSuffix(100_000))
        assertEquals("1.00M", formatSiSuffix(999_500))
        assertEquals("1.00M", formatSiSuffix(1_000_000))
        assertEquals("1.23M", formatSiSuffix(1_234_000))
        assertEquals("12.3M", formatSiSuffix(12_345_678))
        assertEquals("1.00G", formatSiSuffix(999_950_000))
        assertEquals("1.00G", formatSiSuffix(1_000_000_000))
        assertEquals("1.23G", formatSiSuffix(1_234_000_000))
        assertEquals("1,234G", formatSiSuffix(1_234_000_000_000))
    }
}
