package app.dmvtestprep.domain.model

data class ColorModel(
    val hex: Long,
    val r: Float,
    val g: Float,
    val b: Float,
    val a: Float = 1f
) {
    companion object {
        val limeGreen = ColorModel(0xFF32CD32, 0.2f, 0.8f, 0.2f)
        val softRed = ColorModel(0xFFE74C3C, 0.89f, 0.29f, 0.22f)
        val softYellow = ColorModel(0xFFE6CC4D, 0.94f, 0.76f, 0.03f)
        val skyBlue = ColorModel(0xFF4286F4, 0.26f, 0.52f, 0.96f)
        val gray = ColorModel(0xFF848484, 0.52f, 0.52f, 0.52f)
        val lightGray = ColorModel(0xFFB4B4B4, 0.71f, 0.71f, 0.71f)
        val alto = ColorModel(0x80DBDBDB, 0.86f, 0.86f, 0.86f, 0.5f)
        val mercury = ColorModel(0x80E5E5E5, 0.9f, 0.9f, 0.9f, 0.5f)
        val brightRed = ColorModel(0xFFFF3A30, 1.00f, 0.23f, 0.19f)
        val brightOrange = ColorModel(0xFFFF991C, 1.00f, 0.6f, 0.11f)
        val brightGreen = ColorModel(0xFF33CC33, 0.20f, 0.80f, 0.20f)
        val black = ColorModel(0xFF000000, 0f, 0f, 0f, 1f)
        val white = ColorModel(0xFFFFFFFF, 1f, 1f, 1f, 1f)
        val transparent = ColorModel(0x00FFFFFF, 1f, 1f, 1f, 0f)

        fun getBlendColor(factor: Double, colors: List<ColorModel>, isPercent: Boolean = false): ColorModel {
            val normalizedFactor = (if (isPercent) (factor / 100) else factor).coerceIn(0.0, 1.0)
            val position = normalizedFactor * (colors.size - 1)
            val index = position.toInt()
            val segmentFactor = position % 1.0
            val startColor = colors[index]
            val endColor = colors[minOf(index + 1, colors.size - 1)]

            return ColorModel(
                hex = startColor.hex,
                r = startColor.r + (endColor.r - startColor.r) * segmentFactor.toFloat(),
                g = startColor.g + (endColor.g - startColor.g) * segmentFactor.toFloat(),
                b = startColor.b + (endColor.b - startColor.b) * segmentFactor.toFloat(),
                a = startColor.a + (endColor.a - startColor.a) * segmentFactor.toFloat()
            )
        }
    }
}