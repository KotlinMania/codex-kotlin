// port-lint: source protocol/src/num_format.rs
package io.github.kotlinmania.codex.protocol

import kotlin.math.pow
import kotlin.math.round

/**
 * Format an i64 with locale-aware digit separators (e.g. "12345" -> "12,345"
 * for en-US).
 */
fun formatWithSeparators(n: Long): String {
    val isNeg = n < 0
    val s = if (isNeg) (-n).toString() else n.toString()
    val sb = StringBuilder()
    val len = s.length
    for (i in 0 until len) {
        if (i > 0 && (len - i) % 3 == 0) {
            sb.append(',')
        }
        sb.append(s[i])
    }
    return if (isNeg) "-$sb" else sb.toString()
}

private fun formatScaled(value: Long, scale: Long, fractionDigits: Int): String {
    val scaled = round((value.toDouble() / scale.toDouble()) * 10.0.pow(fractionDigits)).toLong()
    if (fractionDigits == 0) {
        return scaled.toString()
    }
    val divisor = 10.0.pow(fractionDigits).toLong()
    val whole = scaled / divisor
    val frac = scaled % divisor
    val fracStr = frac.toString().padStart(fractionDigits, '0')
    return "$whole.$fracStr"
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
    val value = n.coerceAtLeast(0)
    if (value < 1000) {
        return value.toString()
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
    return formatWithSeparators(round(floating / 1e9).toLong()) + "G"
}
