package app.dmvtestprep.domain.model

object AnswerColorProvider {
    enum class ColorScheme(val border: ColorModel, val background: ColorModel, val text: ColorModel = ColorModel.gray) {
        CORRECT(ColorModel.limeGreen, ColorModel.limeGreen.copy(a = 0.1f), ColorModel.limeGreen),
        INCORRECT(ColorModel.softRed, ColorModel.softRed.copy(a = 0.1f), ColorModel.softRed),
        SELECTED(ColorModel.skyBlue, ColorModel.skyBlue.copy(a = 0.1f), ColorModel.skyBlue),
        HIGHLIGHT(ColorModel.softYellow, ColorModel.softYellow.copy(a = 0.1f), ColorModel.softYellow),
        UNSELECTED(ColorModel.mercury, ColorModel.mercury.copy(a = 0.25f), ColorModel.lightGray.copy(a = 0.5f)),
        DEFAULT(ColorModel.gray, ColorModel.transparent)
    }

    fun getColorScheme(
        char: Char,
        selectedAnswerChar: AnswerChar,
        correctAnswerChar: AnswerChar.Value,
        isSubmitted: Boolean
    ): ColorScheme {
        val isCorrect = selectedAnswerChar == correctAnswerChar

        return when (selectedAnswerChar) {
            is AnswerChar.Value -> when {
                isSubmitted && char == selectedAnswerChar.char && isCorrect -> ColorScheme.CORRECT
                isSubmitted && char == selectedAnswerChar.char && !isCorrect -> ColorScheme.INCORRECT
                isSubmitted && char == correctAnswerChar.char -> ColorScheme.HIGHLIGHT
                isSubmitted -> ColorScheme.UNSELECTED
                char == selectedAnswerChar.char -> ColorScheme.SELECTED
                else -> ColorScheme.DEFAULT
            }
            is AnswerChar.None -> ColorScheme.DEFAULT
        }
    }
}