package app.dmvtestprep.domain.model

data class TestResult(
    val id: Int,
    val modeName: String,
    val languageCode: String,
    val totalQuestions: Int,
    val maxErrors: Int,
    val startTime: Long,
    val endTime: Long = 0,
    val totalTime: Long = 0,
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0,
    val isPassed: Boolean = false,
    private val headerText: String? = null,
    private val inspireText: String? = null
) {
    val accuracy = (correctAnswers.toDouble() / totalQuestions * 100)
        .coerceIn(0.0, 100.0)
        .let { (it * 100).toInt() / 100.0 }
    val title = if (isPassed) "Test passed" else "Test failed"
    val header get() = ColoredText(
        text = headerText ?: if (isPassed) "You’ve passed." else "Unfortunately, you didn’t pass.",
        color = if (isPassed) ColorModel.brightGreen else ColorModel.brightRed
    )
    val inspire get() = ColoredText(
        text = inspireText ?: if (isPassed) "Well done!" else "Try again!",
        color = ColorModel.skyBlue
    )
    val detail = ColoredText(
        text = "You got $correctAnswers out of $totalQuestions questions right,\nthat’s $accuracy% accuracy!",
        color = ColorModel.getBlendColor(
            factor = accuracy,
            colors = listOf(
                ColorModel.brightRed,
                ColorModel.brightOrange,
                ColorModel.brightGreen
            ),
            isPercent = true
        )
    )
    val backgroundColor = (if (isPassed) ColorModel.brightGreen else ColorModel.brightOrange).copy(a = 0.1f)
}