// port-lint: source protocol/src/num_format.rs
package io.github.kotlinmania.codex.protocol

import io.github.kotlinmania.icudecimal.DecimalFormatter
import io.github.kotlinmania.icudecimal.input.Decimal
import io.github.kotlinmania.icudecimal.options.DecimalFormatterOptions
import kotlin.math.pow
import kotlin.math.round

private val FORMATTER: DecimalFormatter by lazy { makeEnUsFormatter() }

private fun makeEnUsFormatter(): DecimalFormatter =
    DecimalFormatter.tryNew("en-US", DecimalFormatterOptions())

private fun formatter(): DecimalFormatter = FORMATTER

/**
 * Format an i64 with locale-aware digit separators (e.g. "12345" -> "12,345"
 * for en-US).
 */
fun formatWithSeparators(n: Long): String =
    formatWithSeparatorsWithFormatter(n, formatter())

private fun formatWithSeparatorsWithFormatter(n: Long, formatter: DecimalFormatter): String =
    formatter.format(Decimal.from(n)).toString()

private fun formatSiSuffixWithFormatter(n: Long, formatter: DecimalFormatter): String {
    val value = n.coerceAtLeast(0)
    if (value < 1000) {
        return formatter.format(Decimal.from(value)).toString()
    }

    fun formatScaled(value: Long, scale: Long, fractionDigits: Int): String {
        val scaled = round((value.toDouble() / scale.toDouble()) * 10.0.pow(fractionDigits))
            .toLong()
        val decimal = Decimal.from(scaled)
        decimal.multiplyPow10(-fractionDigits)
        return formatter.format(decimal).toString()
    }

    val units = listOf(
        1_000L to "K",
        1_000_000L to "M",
        1_000_000_000L to "G",
    )
    val floating = value.toDouble()
    for ((scale, suffix) in units) {
        if (round(100.0 * floating / scale.toDouble()) < 1000.0) {
            return formatScaled(value, scale, 2) + suffix
        } else if (round(10.0 * floating / scale.toDouble()) < 1000.0) {
            return formatScaled(value, scale, 1) + suffix
        } else if (round(floating / scale.toDouble()) < 1000.0) {
            return formatScaled(value, scale, 0) + suffix
        }
    }

    // Above 1000G, keep whole-G precision.
    return formatWithSeparatorsWithFormatter(round(floating / 1e9).toLong(), formatter) + "G"
}

/**
 * Format token counts to 3 significant figures, using base-10 SI suffixes.
 *
 * Examples (en-US):
 *   - 999 -> "999"
 *   - 1200 -> "1.20K"
 *   - 123456789 -> "123M"
 */
fun formatSiSuffix(n: Long): String {
    return formatSiSuffixWithFormatter(n, formatter())
}
