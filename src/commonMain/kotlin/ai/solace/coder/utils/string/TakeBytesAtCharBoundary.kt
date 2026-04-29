// port-lint: ignore
// transliterated from upstream module root (utils/string crate)
package ai.solace.coder.utils.string

fun takeBytesAtCharBoundary(content: String, maxBytes: Int): String {
    val s = content
    val maxb = maxBytes
    if (s.encodeToByteArray().size <= maxb) return s
    var lastOk = 0
    var i = 0
    while (i < s.length) {
        val chLen = utf16CharLen(s, i)
        val ch = s.substring(i, i + chLen)
        val lenUtf8 = ch.encodeToByteArray().size
        val nb = lastOk + lenUtf8
        if (nb > maxb) break
        lastOk = nb
        i += chLen
    }
    return s.encodeToByteArray().copyOfRange(0, lastOk).decodeToString()
}

fun takeLastBytesAtCharBoundary(content: String, maxBytes: Int): String {
    val s = content
    val maxb = maxBytes
    if (s.encodeToByteArray().size <= maxb) return s
    var start = s.length
    var used = 0
    var i = s.length
    while (i > 0) {
        val chLen =
            if (i >= 2 && s[i - 2].isHighSurrogate() && s[i - 1].isLowSurrogate()) 2 else 1
        val j = i - chLen
        val ch = s.substring(j, i)
        val nb = ch.encodeToByteArray().size
        if (used + nb > maxb) break
        start = j
        used += nb
        if (start == 0) break
        i = j
    }
    return s.substring(start, s.length)
}

private fun utf16CharLen(s: String, i: Int): Int {
    val c = s[i]
    return if (c.isHighSurrogate() && i + 1 < s.length && s[i + 1].isLowSurrogate()) 2 else 1
}
