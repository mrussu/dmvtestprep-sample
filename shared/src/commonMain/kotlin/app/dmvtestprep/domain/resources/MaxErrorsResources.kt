package app.dmvtestprep.domain.resources

import app.dmvtestprep.domain.model.ColorModel

object MaxErrorsResources {

    private val colors = listOf(
        ColorModel.brightRed,
        ColorModel.brightOrange,
        ColorModel.brightGreen,
        ColorModel.brightGreen
    )

    private val texts: List<String> = listOf(
        "Extremely hard",
        "Hard",
        "Challenging",
        "Moderate",
        "Manageable",
        "Uncertain",
        "Likely pass",
        "Safe bet",
        "Almost there",
        "Guaranteed"
    )

    fun getText(progress: Double): String {
        return when {
            progress <= 0.0 -> "Impossible"
            progress >= 1.0 -> "Already passed"
            else -> {
                val lowerBound = 0.001
                val upperBound = 0.999
                val step = upperBound / texts.size
                val index = ((progress - lowerBound) / step).toInt()
                    .coerceIn(0, texts.size - 1)
                texts[index]
            }
        }
    }

    fun getColor(progress: Double) = ColorModel.getBlendColor(progress, colors)
}