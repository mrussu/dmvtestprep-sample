package app.dmvtestprep.domain.settings

import app.dmvtestprep.domain.settings.TestSettingType.*

data class ModeSettings(
    val skipLearnedQuestions: Setting<Boolean>,
    val showCorrectAnswer: Setting<Boolean>,
    val shuffleQuestions: Setting<Boolean>,
    val shuffleAnswers: Setting<Boolean>,
    val totalQuestions: Setting<Int>,
    val maxErrors: Setting<Int>
) {
    fun <T> update(testSettingType: TestSettingType, value: T): ModeSettings {
        return when (testSettingType) {
            SkipLearnedQuestions -> copy(skipLearnedQuestions = skipLearnedQuestions.copy(value = value as Boolean))
            ShowCorrectAnswer -> copy(showCorrectAnswer = showCorrectAnswer.copy(value = value as Boolean))
            ShuffleQuestions -> copy(shuffleQuestions = shuffleQuestions.copy(value = value as Boolean))
            ShuffleAnswers -> copy(shuffleAnswers = shuffleAnswers.copy(value = value as Boolean))
            TotalQuestions -> copy(totalQuestions = totalQuestions.copy(value = value as Int))
            MaxErrors -> copy(maxErrors = maxErrors.copy(value = value as Int))
        }
    }
}