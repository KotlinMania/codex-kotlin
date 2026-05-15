// port-lint: source tui/src/terminal_palette.rs
package io.github.solaceharmony.codex.tui

import ratatui.style.Color

/**
 * Returns the closest color to the target color that the terminal can display.
 *
 * For now, assumes truecolor support and returns an RGB color directly.
 * A more complete implementation would query terminal capabilities.
 */
fun bestColor(target: Triple<UByte, UByte, UByte>): Color {
    // Assume truecolor support on modern terminals.
    // A full implementation would check supportsColor equivalent.
    return Color.Rgb(target.first, target.second, target.third)
}

fun requeryDefaultColors() {
    defaultColorsCache = null
    defaultColorsAttempted = false
}

data class DefaultColors(
    val fg: Triple<UByte, UByte, UByte>,
    val bg: Triple<UByte, UByte, UByte>,
)

private var defaultColorsCache: DefaultColors? = null
private var defaultColorsAttempted: Boolean = false

fun defaultColors(): DefaultColors? {
    if (!defaultColorsAttempted) {
        defaultColorsCache = queryDefaultColors()
        defaultColorsAttempted = true
    }
    return defaultColorsCache
}

fun defaultFg(): Triple<UByte, UByte, UByte>? {
    return defaultColors()?.fg
}

fun defaultBg(): Triple<UByte, UByte, UByte>? {
    return defaultColors()?.bg
}

private fun queryDefaultColors(): DefaultColors? {
    // Terminal color querying is platform-specific.
    // On Unix, this would import crossterm queryForegroundColor/queryBackgroundColor.
    // For now, return null (consistent with the non-Unix fallback in Rust).
    return null
}

/**
 * The subset of Xterm colors that are usually consistent across terminals.
 * Skips the first 16 system colors which vary by terminal theme.
 */
internal fun xtermFixedColors(): Sequence<Pair<Int, Triple<UByte, UByte, UByte>>> {
    return XTERM_COLORS.asSequence().withIndex()
        .drop(16)
        .map { (i, c) -> Pair(i, c) }
}

// Xterm colors; derived from https://ss64.com/bash/syntax-colors.html
@Suppress("MagicNumber")
val XTERM_COLORS: Array<Triple<UByte, UByte, UByte>> = arrayOf(
    // The first 16 colors vary based on terminal theme, so these are likely not the actual colors
    // that are displayed when using these indices.
    Triple(0u, 0u, 0u),       //   0 Black (SYSTEM)
    Triple(128u, 0u, 0u),     //   1 Maroon (SYSTEM)
    Triple(0u, 128u, 0u),     //   2 Green (SYSTEM)
    Triple(128u, 128u, 0u),   //   3 Olive (SYSTEM)
    Triple(0u, 0u, 128u),     //   4 Navy (SYSTEM)
    Triple(128u, 0u, 128u),   //   5 Purple (SYSTEM)
    Triple(0u, 128u, 128u),   //   6 Teal (SYSTEM)
    Triple(192u, 192u, 192u), //   7 Silver (SYSTEM)
    Triple(128u, 128u, 128u), //   8 Grey (SYSTEM)
    Triple(255u, 0u, 0u),     //   9 Red (SYSTEM)
    Triple(0u, 255u, 0u),     //  10 Lime (SYSTEM)
    Triple(255u, 255u, 0u),   //  11 Yellow (SYSTEM)
    Triple(0u, 0u, 255u),     //  12 Blue (SYSTEM)
    Triple(255u, 0u, 255u),   //  13 Fuchsia (SYSTEM)
    Triple(0u, 255u, 255u),   //  14 Aqua (SYSTEM)
    Triple(255u, 255u, 255u), //  15 White (SYSTEM)
    // The rest of the colors are consistent in most terminals.
    Triple(0u, 0u, 0u),       //  16 Grey0
    Triple(0u, 0u, 95u),      //  17 NavyBlue
    Triple(0u, 0u, 135u),     //  18 DarkBlue
    Triple(0u, 0u, 175u),     //  19 Blue3
    Triple(0u, 0u, 215u),     //  20 Blue3
    Triple(0u, 0u, 255u),     //  21 Blue1
    Triple(0u, 95u, 0u),      //  22 DarkGreen
    Triple(0u, 95u, 95u),     //  23 DeepSkyBlue4
    Triple(0u, 95u, 135u),    //  24 DeepSkyBlue4
    Triple(0u, 95u, 175u),    //  25 DeepSkyBlue4
    Triple(0u, 95u, 215u),    //  26 DodgerBlue3
    Triple(0u, 95u, 255u),    //  27 DodgerBlue2
    Triple(0u, 135u, 0u),     //  28 Green4
    Triple(0u, 135u, 95u),    //  29 SpringGreen4
    Triple(0u, 135u, 135u),   //  30 Turquoise4
    Triple(0u, 135u, 175u),   //  31 DeepSkyBlue3
    Triple(0u, 135u, 215u),   //  32 DeepSkyBlue3
    Triple(0u, 135u, 255u),   //  33 DodgerBlue1
    Triple(0u, 175u, 0u),     //  34 Green3
    Triple(0u, 175u, 95u),    //  35 SpringGreen3
    Triple(0u, 175u, 135u),   //  36 DarkCyan
    Triple(0u, 175u, 175u),   //  37 LightSeaGreen
    Triple(0u, 175u, 215u),   //  38 DeepSkyBlue2
    Triple(0u, 175u, 255u),   //  39 DeepSkyBlue1
    Triple(0u, 215u, 0u),     //  40 Green3
    Triple(0u, 215u, 95u),    //  41 SpringGreen3
    Triple(0u, 215u, 135u),   //  42 SpringGreen2
    Triple(0u, 215u, 175u),   //  43 Cyan3
    Triple(0u, 215u, 215u),   //  44 DarkTurquoise
    Triple(0u, 215u, 255u),   //  45 Turquoise2
    Triple(0u, 255u, 0u),     //  46 Green1
    Triple(0u, 255u, 95u),    //  47 SpringGreen2
    Triple(0u, 255u, 135u),   //  48 SpringGreen1
    Triple(0u, 255u, 175u),   //  49 MediumSpringGreen
    Triple(0u, 255u, 215u),   //  50 Cyan2
    Triple(0u, 255u, 255u),   //  51 Cyan1
    Triple(95u, 0u, 0u),      //  52 DarkRed
    Triple(95u, 0u, 95u),     //  53 DeepPink4
    Triple(95u, 0u, 135u),    //  54 Purple4
    Triple(95u, 0u, 175u),    //  55 Purple4
    Triple(95u, 0u, 215u),    //  56 Purple3
    Triple(95u, 0u, 255u),    //  57 BlueViolet
    Triple(95u, 95u, 0u),     //  58 Orange4
    Triple(95u, 95u, 95u),    //  59 Grey37
    Triple(95u, 95u, 135u),   //  60 MediumPurple4
    Triple(95u, 95u, 175u),   //  61 SlateBlue3
    Triple(95u, 95u, 215u),   //  62 SlateBlue3
    Triple(95u, 95u, 255u),   //  63 RoyalBlue1
    Triple(95u, 135u, 0u),    //  64 Chartreuse4
    Triple(95u, 135u, 95u),   //  65 DarkSeaGreen4
    Triple(95u, 135u, 135u),  //  66 PaleTurquoise4
    Triple(95u, 135u, 175u),  //  67 SteelBlue
    Triple(95u, 135u, 215u),  //  68 SteelBlue3
    Triple(95u, 135u, 255u),  //  69 CornflowerBlue
    Triple(95u, 175u, 0u),    //  70 Chartreuse3
    Triple(95u, 175u, 95u),   //  71 DarkSeaGreen4
    Triple(95u, 175u, 135u),  //  72 CadetBlue
    Triple(95u, 175u, 175u),  //  73 CadetBlue
    Triple(95u, 175u, 215u),  //  74 SkyBlue3
    Triple(95u, 175u, 255u),  //  75 SteelBlue1
    Triple(95u, 215u, 0u),    //  76 Chartreuse3
    Triple(95u, 215u, 95u),   //  77 PaleGreen3
    Triple(95u, 215u, 135u),  //  78 SeaGreen3
    Triple(95u, 215u, 175u),  //  79 Aquamarine3
    Triple(95u, 215u, 215u),  //  80 MediumTurquoise
    Triple(95u, 215u, 255u),  //  81 SteelBlue1
    Triple(95u, 255u, 0u),    //  82 Chartreuse2
    Triple(95u, 255u, 95u),   //  83 SeaGreen2
    Triple(95u, 255u, 135u),  //  84 SeaGreen1
    Triple(95u, 255u, 175u),  //  85 SeaGreen1
    Triple(95u, 255u, 215u),  //  86 Aquamarine1
    Triple(95u, 255u, 255u),  //  87 DarkSlateGray2
    Triple(135u, 0u, 0u),     //  88 DarkRed
    Triple(135u, 0u, 95u),    //  89 DeepPink4
    Triple(135u, 0u, 135u),   //  90 DarkMagenta
    Triple(135u, 0u, 175u),   //  91 DarkMagenta
    Triple(135u, 0u, 215u),   //  92 DarkViolet
    Triple(135u, 0u, 255u),   //  93 Purple
    Triple(135u, 95u, 0u),    //  94 Orange4
    Triple(135u, 95u, 95u),   //  95 LightPink4
    Triple(135u, 95u, 135u),  //  96 Plum4
    Triple(135u, 95u, 175u),  //  97 MediumPurple3
    Triple(135u, 95u, 215u),  //  98 MediumPurple3
    Triple(135u, 95u, 255u),  //  99 SlateBlue1
    Triple(135u, 135u, 0u),   // 100 Yellow4
    Triple(135u, 135u, 95u),  // 101 Wheat4
    Triple(135u, 135u, 135u), // 102 Grey53
    Triple(135u, 135u, 175u), // 103 LightSlateGrey
    Triple(135u, 135u, 215u), // 104 MediumPurple
    Triple(135u, 135u, 255u), // 105 LightSlateBlue
    Triple(135u, 175u, 0u),   // 106 Yellow4
    Triple(135u, 175u, 95u),  // 107 DarkOliveGreen3
    Triple(135u, 175u, 135u), // 108 DarkSeaGreen
    Triple(135u, 175u, 175u), // 109 LightSkyBlue3
    Triple(135u, 175u, 215u), // 110 LightSkyBlue3
    Triple(135u, 175u, 255u), // 111 SkyBlue2
    Triple(135u, 215u, 0u),   // 112 Chartreuse2
    Triple(135u, 215u, 95u),  // 113 DarkOliveGreen3
    Triple(135u, 215u, 135u), // 114 PaleGreen3
    Triple(135u, 215u, 175u), // 115 DarkSeaGreen3
    Triple(135u, 215u, 215u), // 116 DarkSlateGray3
    Triple(135u, 215u, 255u), // 117 SkyBlue1
    Triple(135u, 255u, 0u),   // 118 Chartreuse1
    Triple(135u, 255u, 95u),  // 119 LightGreen
    Triple(135u, 255u, 135u), // 120 LightGreen
    Triple(135u, 255u, 175u), // 121 PaleGreen1
    Triple(135u, 255u, 215u), // 122 Aquamarine1
    Triple(135u, 255u, 255u), // 123 DarkSlateGray1
    Triple(175u, 0u, 0u),     // 124 Red3
    Triple(175u, 0u, 95u),    // 125 DeepPink4
    Triple(175u, 0u, 135u),   // 126 MediumVioletRed
    Triple(175u, 0u, 175u),   // 127 Magenta3
    Triple(175u, 0u, 215u),   // 128 DarkViolet
    Triple(175u, 0u, 255u),   // 129 Purple
    Triple(175u, 95u, 0u),    // 130 DarkOrange3
    Triple(175u, 95u, 95u),   // 131 IndianRed
    Triple(175u, 95u, 135u),  // 132 HotPink3
    Triple(175u, 95u, 175u),  // 133 MediumOrchid3
    Triple(175u, 95u, 215u),  // 134 MediumOrchid
    Triple(175u, 95u, 255u),  // 135 MediumPurple2
    Triple(175u, 135u, 0u),   // 136 DarkGoldenrod
    Triple(175u, 135u, 95u),  // 137 LightSalmon3
    Triple(175u, 135u, 135u), // 138 RosyBrown
    Triple(175u, 135u, 175u), // 139 Grey63
    Triple(175u, 135u, 215u), // 140 MediumPurple2
    Triple(175u, 135u, 255u), // 141 MediumPurple1
    Triple(175u, 175u, 0u),   // 142 Gold3
    Triple(175u, 175u, 95u),  // 143 DarkKhaki
    Triple(175u, 175u, 135u), // 144 NavajoWhite3
    Triple(175u, 175u, 175u), // 145 Grey69
    Triple(175u, 175u, 215u), // 146 LightSteelBlue3
    Triple(175u, 175u, 255u), // 147 LightSteelBlue
    Triple(175u, 215u, 0u),   // 148 Yellow3
    Triple(175u, 215u, 95u),  // 149 DarkOliveGreen3
    Triple(175u, 215u, 135u), // 150 DarkSeaGreen3
    Triple(175u, 215u, 175u), // 151 DarkSeaGreen2
    Triple(175u, 215u, 215u), // 152 LightCyan3
    Triple(175u, 215u, 255u), // 153 LightSkyBlue1
    Triple(175u, 255u, 0u),   // 154 GreenYellow
    Triple(175u, 255u, 95u),  // 155 DarkOliveGreen2
    Triple(175u, 255u, 135u), // 156 PaleGreen1
    Triple(175u, 255u, 175u), // 157 DarkSeaGreen2
    Triple(175u, 255u, 215u), // 158 DarkSeaGreen1
    Triple(175u, 255u, 255u), // 159 PaleTurquoise1
    Triple(215u, 0u, 0u),     // 160 Red3
    Triple(215u, 0u, 95u),    // 161 DeepPink3
    Triple(215u, 0u, 135u),   // 162 DeepPink3
    Triple(215u, 0u, 175u),   // 163 Magenta3
    Triple(215u, 0u, 215u),   // 164 Magenta3
    Triple(215u, 0u, 255u),   // 165 Magenta2
    Triple(215u, 95u, 0u),    // 166 DarkOrange3
    Triple(215u, 95u, 95u),   // 167 IndianRed
    Triple(215u, 95u, 135u),  // 168 HotPink3
    Triple(215u, 95u, 175u),  // 169 HotPink2
    Triple(215u, 95u, 215u),  // 170 Orchid
    Triple(215u, 95u, 255u),  // 171 MediumOrchid1
    Triple(215u, 135u, 0u),   // 172 Orange3
    Triple(215u, 135u, 95u),  // 173 LightSalmon3
    Triple(215u, 135u, 135u), // 174 LightPink3
    Triple(215u, 135u, 175u), // 175 Pink3
    Triple(215u, 135u, 215u), // 176 Plum3
    Triple(215u, 135u, 255u), // 177 Violet
    Triple(215u, 175u, 0u),   // 178 Gold3
    Triple(215u, 175u, 95u),  // 179 LightGoldenrod3
    Triple(215u, 175u, 135u), // 180 Tan
    Triple(215u, 175u, 175u), // 181 MistyRose3
    Triple(215u, 175u, 215u), // 182 Thistle3
    Triple(215u, 175u, 255u), // 183 Plum2
    Triple(215u, 215u, 0u),   // 184 Yellow3
    Triple(215u, 215u, 95u),  // 185 Khaki3
    Triple(215u, 215u, 135u), // 186 LightGoldenrod2
    Triple(215u, 215u, 175u), // 187 LightYellow3
    Triple(215u, 215u, 215u), // 188 Grey84
    Triple(215u, 215u, 255u), // 189 LightSteelBlue1
    Triple(215u, 255u, 0u),   // 190 Yellow2
    Triple(215u, 255u, 95u),  // 191 DarkOliveGreen1
    Triple(215u, 255u, 135u), // 192 DarkOliveGreen1
    Triple(215u, 255u, 175u), // 193 DarkSeaGreen1
    Triple(215u, 255u, 215u), // 194 Honeydew2
    Triple(215u, 255u, 255u), // 195 LightCyan1
    Triple(255u, 0u, 0u),     // 196 Red1
    Triple(255u, 0u, 95u),    // 197 DeepPink2
    Triple(255u, 0u, 135u),   // 198 DeepPink1
    Triple(255u, 0u, 175u),   // 199 DeepPink1
    Triple(255u, 0u, 215u),   // 200 Magenta2
    Triple(255u, 0u, 255u),   // 201 Magenta1
    Triple(255u, 95u, 0u),    // 202 OrangeRed1
    Triple(255u, 95u, 95u),   // 203 IndianRed1
    Triple(255u, 95u, 135u),  // 204 IndianRed1
    Triple(255u, 95u, 175u),  // 205 HotPink
    Triple(255u, 95u, 215u),  // 206 HotPink
    Triple(255u, 95u, 255u),  // 207 MediumOrchid1
    Triple(255u, 135u, 0u),   // 208 DarkOrange
    Triple(255u, 135u, 95u),  // 209 Salmon1
    Triple(255u, 135u, 135u), // 210 LightCoral
    Triple(255u, 135u, 175u), // 211 PaleVioletRed1
    Triple(255u, 135u, 215u), // 212 Orchid2
    Triple(255u, 135u, 255u), // 213 Orchid1
    Triple(255u, 175u, 0u),   // 214 Orange1
    Triple(255u, 175u, 95u),  // 215 SandyBrown
    Triple(255u, 175u, 135u), // 216 LightSalmon1
    Triple(255u, 175u, 175u), // 217 LightPink1
    Triple(255u, 175u, 215u), // 218 Pink1
    Triple(255u, 175u, 255u), // 219 Plum1
    Triple(255u, 215u, 0u),   // 220 Gold1
    Triple(255u, 215u, 95u),  // 221 LightGoldenrod2
    Triple(255u, 215u, 135u), // 222 LightGoldenrod2
    Triple(255u, 215u, 175u), // 223 NavajoWhite1
    Triple(255u, 215u, 215u), // 224 MistyRose1
    Triple(255u, 215u, 255u), // 225 Thistle1
    Triple(255u, 255u, 0u),   // 226 Yellow1
    Triple(255u, 255u, 95u),  // 227 LightGoldenrod1
    Triple(255u, 255u, 135u), // 228 Khaki1
    Triple(255u, 255u, 175u), // 229 Wheat1
    Triple(255u, 255u, 215u), // 230 Cornsilk1
    Triple(255u, 255u, 255u), // 231 Grey100
    Triple(8u, 8u, 8u),       // 232 Grey3
    Triple(18u, 18u, 18u),    // 233 Grey7
    Triple(28u, 28u, 28u),    // 234 Grey11
    Triple(38u, 38u, 38u),    // 235 Grey15
    Triple(48u, 48u, 48u),    // 236 Grey19
    Triple(58u, 58u, 58u),    // 237 Grey23
    Triple(68u, 68u, 68u),    // 238 Grey27
    Triple(78u, 78u, 78u),    // 239 Grey30
    Triple(88u, 88u, 88u),    // 240 Grey35
    Triple(98u, 98u, 98u),    // 241 Grey39
    Triple(108u, 108u, 108u), // 242 Grey42
    Triple(118u, 118u, 118u), // 243 Grey46
    Triple(128u, 128u, 128u), // 244 Grey50
    Triple(138u, 138u, 138u), // 245 Grey54
    Triple(148u, 148u, 148u), // 246 Grey58
    Triple(158u, 158u, 158u), // 247 Grey62
    Triple(168u, 168u, 168u), // 248 Grey66
    Triple(178u, 178u, 178u), // 249 Grey70
    Triple(188u, 188u, 188u), // 250 Grey74
    Triple(198u, 198u, 198u), // 251 Grey78
    Triple(208u, 208u, 208u), // 252 Grey82
    Triple(218u, 218u, 218u), // 253 Grey85
    Triple(228u, 228u, 228u), // 254 Grey89
    Triple(238u, 238u, 238u), // 255 Grey93
)
