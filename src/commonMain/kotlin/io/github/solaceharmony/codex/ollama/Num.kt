// port-lint: ignore
package io.github.solaceharmony.codex.ollama

import kotlin.math.pow
import kotlin.math.round

internal fun formatDouble(value: Double, decimals: Int): String {
    if (decimals == 0) {
        return value.toLong().toString()
    }
    val multiplier = when (decimals) {
        1 -> 10.0
        2 -> 100.0
        else -> 10.0.pow(decimals.toDouble())
    }
    val rounded = round(value * multiplier) / multiplier
    val str = rounded.toString()
    val dotIndex = str.indexOf('.')
    return if (dotIndex < 0) {
        str + "." + "0".repeat(decimals)
    } else {
        val decimalPart = str.substring(dotIndex + 1)
        if (decimalPart.length >= decimals) {
            str.substring(0, dotIndex + 1 + decimals)
        } else {
            str + "0".repeat(decimals - decimalPart.length)
        }
    }
}
