// port-lint: source core/src/config/constraint.rs
package ai.solace.coder.core.config

/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * Copyright (c) 2025 Sydney Renee, The Solace Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

sealed class ConstraintError : Exception() {
    data class InvalidValue(
        val candidate: String,
        val allowed: String,
    ) : ConstraintError() {
        override val message: String
            get() = "value `$candidate` is not in the allowed set $allowed"
    }

    data class EmptyField(
        val fieldName: String,
    ) : ConstraintError() {
        override val message: String
            get() = "field `$fieldName` cannot be empty"
    }

    companion object {
        fun invalidValue(candidate: String, allowed: String): ConstraintError =
            InvalidValue(candidate = candidate, allowed = allowed)

        fun emptyField(fieldName: String): ConstraintError =
            EmptyField(fieldName = fieldName)
    }
}

/**
 * A value constrained by a validator function.
 *
 * The validator is checked on construction and on every [set] call.
 */
class Constrained<T>(
    private var value: T,
    private val validator: (T) -> Result<Unit>,
) {
    companion object {
        /**
         * Creates a [Constrained] that accepts the initial value and validates with the
         * provided [validator].
         *
         * Returns a [Result] that is a failure if the initial value does not pass validation.
         */
        fun <T> new(
            initialValue: T,
            validator: (T) -> Result<Unit>,
        ): Result<Constrained<T>> {
            validator(initialValue).getOrElse { return Result.failure(it) }
            return Result.success(Constrained(initialValue, validator))
        }

        /** Creates a [Constrained] that accepts any value. */
        fun <T> allowAny(initialValue: T): Constrained<T> =
            Constrained(initialValue) { Result.success(Unit) }

        /**
         * Creates a [Constrained] that only allows the given [value].
         */
        inline fun <reified T> allowOnly(value: T): Constrained<T> where T : Any {
            val result = new(value) { candidate ->
                if (candidate == value) {
                    Result.success(Unit)
                } else {
                    Result.failure(
                        ConstraintError.invalidValue(
                            candidate.toString(),
                            value.toString(),
                        )
                    )
                }
            }
            // Initial value should always be valid
            return result.getOrThrow()
        }

        /** Allow any value of [T], using a default as the initial value. */
        fun <T> allowAnyFromDefault(default: T): Constrained<T> =
            allowAny(default)

        /**
         * Creates a [Constrained] that only allows values in the given [allowed] list.
         */
        fun <T> allowValues(
            initialValue: T,
            allowed: List<T>,
        ): Result<Constrained<T>> = new(initialValue) { candidate ->
            if (candidate in allowed) {
                Result.success(Unit)
            } else {
                Result.failure(
                    ConstraintError.invalidValue(
                        candidate.toString(),
                        allowed.toString(),
                    )
                )
            }
        }
    }

    /** Returns the current value. */
    fun get(): T = value

    /**
     * Checks whether [candidate] would be accepted by the validator without
     * actually setting it.
     */
    fun canSet(candidate: T): Result<Unit> = validator(candidate)

    /**
     * Sets the value if it passes validation.
     *
     * Returns a [Result] that is a failure if the value does not pass validation.
     * On failure, the previous value is retained.
     */
    fun set(newValue: T): Result<Unit> {
        validator(newValue).getOrElse { return Result.failure(it) }
        value = newValue
        return Result.success(Unit)
    }

    override fun toString(): String = "Constrained(value=$value)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Constrained<*>) return false
        return value == other.value
    }

    override fun hashCode(): Int = value?.hashCode() ?: 0
}
