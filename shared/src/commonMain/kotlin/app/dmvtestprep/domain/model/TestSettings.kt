package app.dmvtestprep.domain.model

import app.dmvtestprep.domain.settings.DefaultSetting.*

data class TestSettings(
    val skipLearnedQuestions: Boolean,
    val showCorrectAnswer: Boolean,
    val shuffleQuestions: Boolean,
    val shuffleAnswers: Boolean,
    val totalQuestions: Int,
    val maxErrors: Int
) {
    companion object {
        fun default(testModeName: String): TestSettings {
            return when (TestMode.fromModeName(testModeName)) {
                TestMode.PREP -> TestSettings(
                    skipLearnedQuestions = PrepSkipLearnedQuestions.defaultValue,
                    showCorrectAnswer = PrepShowCorrectAnswer.defaultValue,
                    shuffleQuestions = PrepShuffleQuestions.defaultValue,
                    shuffleAnswers = PrepShuffleAnswers.defaultValue,
                    totalQuestions = PrepTotalQuestions.defaultValue,
                    maxErrors = PrepMaxErrors.defaultValue
                )
                TestMode.EXAM -> TestSettings(
                    skipLearnedQuestions = ExamSkipLearnedQuestions.defaultValue,
                    showCorrectAnswer = ExamShowCorrectAnswer.defaultValue,
                    shuffleQuestions = ExamShuffleQuestions.defaultValue,
                    shuffleAnswers = ExamShuffleAnswers.defaultValue,
                    totalQuestions = ExamTotalQuestions.defaultValue,
                    maxErrors = ExamMaxErrors.defaultValue
                )
            }
        }
    }
}