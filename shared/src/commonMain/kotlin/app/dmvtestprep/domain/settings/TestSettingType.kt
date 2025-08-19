package app.dmvtestprep.domain.settings

sealed class TestSettingType {
    data object SkipLearnedQuestions : TestSettingType()
    data object ShowCorrectAnswer : TestSettingType()
    data object ShuffleQuestions : TestSettingType()
    data object ShuffleAnswers : TestSettingType()
    data object TotalQuestions : TestSettingType()
    data object MaxErrors : TestSettingType()
}