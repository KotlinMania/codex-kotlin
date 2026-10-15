// port-lint: tests auth.rs
package io.github.kotlinmania.codex.protocol

import kotlin.test.Test
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun planTypeDeserializesRawAliases() {
        assertEquals(
            AuthPlan.known(KnownPlan.Enterprise),
            AuthPlan.fromRaw("hc"),
        )
        assertEquals(
            AuthPlan.known(KnownPlan.Edu),
            AuthPlan.fromRaw("education"),
        )
    }
}
