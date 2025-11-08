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
    fun update(type: TestSettingType): ModeSettings = when (type) {
        is SkipLearnedQuestions -> copy(skipLearnedQuestions = skipLearnedQuestions.copy(type.value))
        is ShowCorrectAnswer -> copy(showCorrectAnswer = showCorrectAnswer.copy(type.value))
        is ShuffleQuestions -> copy(shuffleQuestions = shuffleQuestions.copy(type.value))
        is ShuffleAnswers -> copy(shuffleAnswers = shuffleAnswers.copy(type.value))
        is TotalQuestions -> copy(totalQuestions = totalQuestions.copy(type.value))
        is MaxErrors -> copy(maxErrors = maxErrors.copy(type.value))
    }
}