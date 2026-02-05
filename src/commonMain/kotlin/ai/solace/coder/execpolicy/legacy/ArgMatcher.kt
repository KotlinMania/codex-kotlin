// port-lint: source execpolicy-legacy/src/arg_matcher.rs
package ai.solace.coder.execpolicy.legacy

import kotlinx.serialization.Serializable

/** Patterns that lists of arguments should be compared against. */
@Serializable
sealed class ArgMatcher {
    /** Literal string value. */
    @Serializable
    data class Literal(val value: String) : ArgMatcher()

    /** We cannot say what type of value this should match, but it is *not* a file path. */
    @Serializable
    data object OpaqueNonFile : ArgMatcher()

    /** Required readable file. */
    @Serializable
    data object ReadableFile : ArgMatcher()

    /** Required writeable file. */
    @Serializable
    data object WriteableFile : ArgMatcher()

    /** Non-empty list of readable files. */
    @Serializable
    data object ReadableFiles : ArgMatcher()

    /** Non-empty list of readable files, or empty list, implying readable cwd. */
    @Serializable
    data object ReadableFilesOrCwd : ArgMatcher()

    /** Positive integer, like one that is required for `head -n`. */
    @Serializable
    data object PositiveInteger : ArgMatcher()

    /** Bespoke matcher for safe sed commands. */
    @Serializable
    data object SedCommand : ArgMatcher()

    /**
     * Matches an arbitrary number of arguments without attributing any particular meaning to them.
     * Caller is responsible for interpreting them.
     */
    @Serializable
    data object UnverifiedVarargs : ArgMatcher()

    /**
     * Port of the Rust `AllocValue` impl used by starlark.
     *
     * The Kotlin port does not embed a starlark interpreter yet, so this is a placeholder that
     * preserves call sites and shape for future integration.
     */
    fun allocValue(heap: Any): Any {
        @Suppress("UNUSED_VARIABLE")
        val _unused = heap
        return this
    }

    fun cardinality(): ArgMatcherCardinality {
        return when (this) {
            is Literal,
            OpaqueNonFile,
            ReadableFile,
            WriteableFile,
            PositiveInteger,
            SedCommand,
            -> ArgMatcherCardinality.One

            ReadableFiles -> ArgMatcherCardinality.AtLeastOne

            ReadableFilesOrCwd, UnverifiedVarargs -> ArgMatcherCardinality.ZeroOrMore
        }
    }

    fun argType(): ArgType {
        return when (this) {
            is Literal -> ArgType.Literal(value)
            OpaqueNonFile -> ArgType.OpaqueNonFile
            ReadableFile -> ArgType.ReadableFile
            WriteableFile -> ArgType.WriteableFile
            ReadableFiles -> ArgType.ReadableFile
            ReadableFilesOrCwd -> ArgType.ReadableFile
            PositiveInteger -> ArgType.PositiveInteger
            SedCommand -> ArgType.SedCommand
            UnverifiedVarargs -> ArgType.Unknown
        }
    }

    companion object {
        /**
         * Port of the Rust `UnpackValue` impl used by starlark.
         *
         * Rust behavior:
         * - If the value is a string, it becomes a `Literal`.
         * - Otherwise, try to downcast to an `ArgMatcher`.
         */
        fun unpackValue(value: Any): ArgMatcher? {
            return when (value) {
                is String -> Literal(value)
                is ArgMatcher -> value
                else -> null
            }
        }
    }
}

@Serializable
enum class ArgMatcherCardinality {
    One,
    AtLeastOne,
    ZeroOrMore;

    fun isExact(): Int? {
        return when (this) {
            One -> 1
            AtLeastOne, ZeroOrMore -> null
        }
    }
}
