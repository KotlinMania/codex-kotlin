package io.github.kotlinmania.codex.utils

import io.github.kotlinmania.base64.alphabet.Alphabet
import io.github.kotlinmania.base64.encodedLen

internal object Base64Util {
    fun encodeUrlSafeNoPad(data: ByteArray): String = encodeWithAlphabet(data, Alphabet.URL_SAFE, padding = false)

    fun encodeStandard(data: ByteArray): String = encodeWithAlphabet(data, Alphabet.STANDARD, padding = true)

    fun decodeUrlSafeNoPad(str: String): ByteArray = decodeWithAlphabet(str, Alphabet.URL_SAFE)

    private fun encodeWithAlphabet(
        data: ByteArray,
        alphabet: Alphabet,
        padding: Boolean,
    ): String {
        val chars = alphabet.asString()
        val expectedLen = encodedLen(data.size, padding) ?: ((data.size * 4 + 2) / 3)
        val sb = StringBuilder(expectedLen)
        var i = 0
        while (i < data.size) {
            val b0 = data[i++].toInt() and 0xFF
            val b1 = if (i < data.size) data[i++].toInt() and 0xFF else -1
            val b2 = if (i < data.size) data[i++].toInt() and 0xFF else -1

            sb.append(chars[b0 ushr 2])
            if (b1 != -1) {
                sb.append(chars[((b0 and 0x03) shl 4) or (b1 ushr 4)])
                if (b2 != -1) {
                    sb.append(chars[((b1 and 0x0F) shl 2) or (b2 ushr 6)])
                    sb.append(chars[b2 and 0x3F])
                } else {
                    sb.append(chars[(b1 and 0x0F) shl 2])
                    if (padding) sb.append('=')
                }
            } else {
                sb.append(chars[(b0 and 0x03) shl 4])
                if (padding) sb.append("==")
            }
        }
        return sb.toString()
    }

    private fun decodeWithAlphabet(
        str: String,
        alphabet: Alphabet,
    ): ByteArray {
        val chars = alphabet.asString()
        val decodeTable = IntArray(256) { -1 }
        for (idx in 0 until 64) {
            decodeTable[chars[idx].code] = idx
        }

        val clean = str.trimEnd('=')
        val output = mutableListOf<Byte>()
        var buffer = 0
        var bitsCollected = 0

        for (ch in clean) {
            val code = ch.code
            if (code >= 256) continue
            val value = decodeTable[code]
            if (value < 0) continue
            buffer = (buffer shl 6) or value
            bitsCollected += 6
            if (bitsCollected >= 8) {
                bitsCollected -= 8
                output.add(((buffer ushr bitsCollected) and 0xFF).toByte())
            }
        }
        return output.toByteArray()
    }
}
