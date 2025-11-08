package app.dmvtestprep.core.ui.model

data class ColorModel(
    val hex: Long,
    val r: Float,
    val g: Float,
    val b: Float,
    val a: Float = 1f
) {
    companion object {
        fun rgba(red: Int, green: Int, blue: Int, alpha: Int = 0xFF): ColorModel {
            val r = red.coerceIn(0, 255)
            val g = green.coerceIn(0, 255)
            val b = blue.coerceIn(0, 255)
            val a = alpha.coerceIn(0, 255)
            val hex = ((a and 0xFF) shl 24 or (r and 0xFF) shl 16 or (g and 0xFF) shl 8  or (b and 0xFF)).toLong()

            return ColorModel(
                hex = hex,
                r = r / 255f,
                g = g / 255f,
                b = b / 255f,
                a = a / 255f
            )
        }

        fun getBlendColor(factor: Double, colors: List<ThemeColor>, isPercent: Boolean = false): ThemeColor {
            val (startColor, endColor, t) = pickBlendSegment(factor, colors, isPercent)

            return ThemeColor(
                light = lerp(startColor.light.copy(a = startColor.alpha), endColor.light.copy(a = startColor.alpha), t),
                dark = lerp(startColor.dark.copy(a = startColor.alpha), endColor.dark.copy(a = startColor.alpha), t)
            )
        }

        private fun <T> pickBlendSegment(factor: Double, colors: List<T>, isPercent: Boolean = false): Triple<T, T, Float> {
            val normalizedFactor = (if (isPercent) factor / 100 else factor).coerceIn(0.0, 1.0)
            val position = normalizedFactor * (colors.size - 1)
            val index = position.toInt()
            val segmentFactor = (position % 1.0).toFloat()
            val startColor = colors[index]
            val endColor = colors[minOf(index + 1, colors.size - 1)]

            return Triple(startColor, endColor, segmentFactor)
        }

        private fun lerp(s: ColorModel, e: ColorModel, t: Float): ColorModel {
            return ColorModel(
                hex = s.hex,
                r = s.r + (e.r - s.r) * t,
                g = s.g + (e.g - s.g) * t,
                b = s.b + (e.b - s.b) * t,
                a = s.a + (e.a - s.a) * t
            )
        }
    }
}