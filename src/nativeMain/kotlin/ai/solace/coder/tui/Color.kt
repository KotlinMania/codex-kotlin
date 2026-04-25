// port-lint: source tui/src/color.rs
package ai.solace.coder.tui

import kotlin.math.pow
import kotlin.math.sqrt

internal fun isLight(bg: Triple<UByte, UByte, UByte>): Boolean {
    val (r, g, b) = bg
    val y = 0.299f * r.toFloat() + 0.587f * g.toFloat() + 0.114f * b.toFloat()
    return y > 128.0f
}

internal fun blend(fg: Triple<UByte, UByte, UByte>, bg: Triple<UByte, UByte, UByte>, alpha: Float): Triple<UByte, UByte, UByte> {
    val r = (fg.first.toFloat() * alpha + bg.first.toFloat() * (1.0f - alpha)).toInt().toUByte()
    val g = (fg.second.toFloat() * alpha + bg.second.toFloat() * (1.0f - alpha)).toInt().toUByte()
    val b = (fg.third.toFloat() * alpha + bg.third.toFloat() * (1.0f - alpha)).toInt().toUByte()
    return Triple(r, g, b)
}

/**
 * Returns the perceptual color distance between two RGB colors.
 * Uses the CIE76 formula (Euclidean distance in Lab space approximation).
 */
internal fun perceptualDistance(a: Triple<UByte, UByte, UByte>, b: Triple<UByte, UByte, UByte>): Float {
    // Convert sRGB to linear RGB
    fun srgbToLinear(c: UByte): Float {
        val cv = c.toFloat() / 255.0f
        return if (cv <= 0.04045f) {
            cv / 12.92f
        } else {
            ((cv + 0.055f) / 1.055f).pow(2.4f)
        }
    }

    // Convert RGB to XYZ
    fun rgbToXyz(r: UByte, g: UByte, b: UByte): Triple<Float, Float, Float> {
        val rl = srgbToLinear(r)
        val gl = srgbToLinear(g)
        val bl = srgbToLinear(b)

        val x = rl * 0.4124f + gl * 0.3576f + bl * 0.1805f
        val y = rl * 0.2126f + gl * 0.7152f + bl * 0.0722f
        val z = rl * 0.0193f + gl * 0.1192f + bl * 0.9505f
        return Triple(x, y, z)
    }

    // Convert XYZ to Lab
    fun xyzToLab(x: Float, y: Float, z: Float): Triple<Float, Float, Float> {
        // D65 reference white
        val xr = x / 0.95047f
        val yr = y / 1.00000f
        val zr = z / 1.08883f

        fun f(t: Float): Float {
            return if (t > 0.008856f) {
                t.pow(1.0f / 3.0f)
            } else {
                7.787f * t + 16.0f / 116.0f
            }
        }

        val fx = f(xr)
        val fy = f(yr)
        val fz = f(zr)

        val l = 116.0f * fy - 16.0f
        val la = 500.0f * (fx - fy)
        val lb = 200.0f * (fy - fz)
        return Triple(l, la, lb)
    }

    val (x1, y1, z1) = rgbToXyz(a.first, a.second, a.third)
    val (x2, y2, z2) = rgbToXyz(b.first, b.second, b.third)

    val (l1, a1, b1) = xyzToLab(x1, y1, z1)
    val (l2, a2, b2) = xyzToLab(x2, y2, z2)

    val dl = l1 - l2
    val da = a1 - a2
    val db = b1 - b2

    return sqrt(dl * dl + da * da + db * db)
}
