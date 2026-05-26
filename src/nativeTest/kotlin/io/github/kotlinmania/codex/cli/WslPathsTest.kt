// port-lint: source cli/src/wsl_paths.rs
package io.github.kotlinmania.codex.cli

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WslPathsTest {
    @Test
    fun winToWslBasic() {
        assertEquals("/mnt/c/Temp/codex.zip", winPathToWsl("C:\\Temp\\codex.zip"))
        assertEquals("/mnt/d/Work/codex.tgz", winPathToWsl("D:/Work/codex.tgz"))
        assertNull(winPathToWsl("/home/user/codex"))
    }

    @Test
    fun normalizeIsNoopOnUnixPaths() {
        assertEquals("/home/u/x", normalizeForWsl("/home/u/x"))
    }
}
