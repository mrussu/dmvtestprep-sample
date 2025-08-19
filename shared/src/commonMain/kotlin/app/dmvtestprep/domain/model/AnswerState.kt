package app.dmvtestprep.domain.model

data class AnswerState(
    val correctAnswer: AnswerChar.Value,
    val selectedAnswer: AnswerChar = AnswerChar.None,
    val isSubmitted: Boolean = false
) {
    val isSelected: Boolean
        get() = selectedAnswer !is AnswerChar.None

    fun getColorScheme(char: Char): AnswerColorProvider.ColorScheme {
        return AnswerColorProvider.getColorScheme(
            char = char,
            selectedAnswerChar = selectedAnswer,
            correctAnswerChar = correctAnswer,
            isSubmitted = isSubmitted
        )
    }
}