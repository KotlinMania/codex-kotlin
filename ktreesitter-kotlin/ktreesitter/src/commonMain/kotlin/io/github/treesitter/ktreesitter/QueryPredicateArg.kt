package io.github.treesitter.ktreesitter

/**
 * An argument to a [QueryPredicate].
 *
 * @property value The value of the argument.
 */
sealed interface QueryPredicateArg {
    val value: String

    /** A capture argument (`@value`). */
    data class Capture(override val value: String) : QueryPredicateArg {
        override fun toString() = "@$value"
    }

    /** A literal string argument (`"value"`). */
    data class Literal(override val value: String) : QueryPredicateArg {
        override fun toString() = "\"$value\""
    }
}
