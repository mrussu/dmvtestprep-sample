package app.dmvtestprep.domain.model

import app.dmvtestprep.core.ui.model.ThemeColor
import app.dmvtestprep.core.ui.theme.AppColors

object AnswerColorProvider {
    enum class ColorScheme(val border: ThemeColor, val background: ThemeColor, val text: ThemeColor) {
        CORRECT(AppColors.success, AppColors.success.copy(alpha = 0.1f), AppColors.success),
        INCORRECT(AppColors.failure, AppColors.failure.copy(alpha = 0.1f), AppColors.failure),
        SELECTED(AppColors.primary, AppColors.primary.copy(alpha = 0.1f), AppColors.primary),
        HIGHLIGHT(AppColors.yellow, AppColors.yellow.copy(alpha = 0.1f), AppColors.yellow),
        UNSELECTED(AppColors.gray2, AppColors.gray2.copy(alpha = 0.1f), AppColors.gray2),
        DEFAULT(AppColors.onBackground, AppColors.none, AppColors.onBackground)
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