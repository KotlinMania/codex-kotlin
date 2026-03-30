// port-lint: source core/src/config/constraint.rs
package ai.solace.coder.core.config

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertIs

class ConstraintTest {

    @Test
    fun constrainedAllowAnyAcceptsAnyValue() {
        val constrained = Constrained.allowAny(5)
        constrained.set(-10).getOrThrow()
        assertEquals(-10, constrained.get())
    }

    @Test
    fun constrainedAllowAnyDefaultUsesDefaultValue() {
        val constrained = Constrained.allowAnyFromDefault(0)
        assertEquals(0, constrained.get())
    }

    @Test
    fun constrainedNewRejectsInvalidInitialValue() {
        val result = Constrained.new(0) { value ->
            if (value > 0) {
                Result.success(Unit)
            } else {
                Result.failure(
                    ConstraintError.invalidValue(
                        value.toString(),
                        "positive values",
                    )
                )
            }
        }

        assertTrue(result.isFailure)
        val error = result.exceptionOrNull()
        assertIs<ConstraintError.InvalidValue>(error)
        assertEquals("0", error.candidate)
        assertEquals("positive values", error.allowed)
    }

    @Test
    fun constrainedSetRejectsInvalidValueAndLeavesPrevious() {
        val constrained = Constrained.new(1) { value ->
            if (value > 0) {
                Result.success(Unit)
            } else {
                Result.failure(
                    ConstraintError.invalidValue(
                        value.toString(),
                        "positive values",
                    )
                )
            }
        }.getOrThrow()

        val result = constrained.set(-5)
        assertTrue(result.isFailure)
        val error = result.exceptionOrNull()
        assertIs<ConstraintError.InvalidValue>(error)
        assertEquals("-5", error.candidate)
        assertEquals("positive values", error.allowed)
        assertEquals(1, constrained.get())
    }

    @Test
    fun constrainedCanSetAllowsProbeWithoutSetting() {
        val constrained = Constrained.new(1) { value ->
            if (value > 0) {
                Result.success(Unit)
            } else {
                Result.failure(
                    ConstraintError.invalidValue(
                        value.toString(),
                        "positive values",
                    )
                )
            }
        }.getOrThrow()

        assertTrue(constrained.canSet(2).isSuccess)

        val result = constrained.canSet(-1)
        assertTrue(result.isFailure)
        val error = result.exceptionOrNull()
        assertIs<ConstraintError.InvalidValue>(error)
        assertEquals("-1", error.candidate)
        assertEquals("positive values", error.allowed)
        assertEquals(1, constrained.get())
    }
}
