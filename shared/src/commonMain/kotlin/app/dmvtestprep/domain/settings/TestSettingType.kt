package app.dmvtestprep.domain.settings

sealed class TestSettingType {
    data class SkipLearnedQuestions(val value: Boolean) : TestSettingType()
    data class ShowCorrectAnswer(val value: Boolean) : TestSettingType()
    data class ShuffleQuestions(val value: Boolean) : TestSettingType()
    data class ShuffleAnswers(val value: Boolean) : TestSettingType()
    data class TotalQuestions(val value: Int) : TestSettingType()
    data class MaxErrors(val value: Int) : TestSettingType()
}