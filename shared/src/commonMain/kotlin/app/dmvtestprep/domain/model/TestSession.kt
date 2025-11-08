package app.dmvtestprep.domain.model

import app.dmvtestprep.core.utils.TimeUtils.currentTimestamp

class TestSession(
    private val id: Int,
    private val modeName: String,
    private val languageCode: String,
    private val totalQuestions: Int,
    private val maxErrors: Int
) {
    private val startTime: Long = currentTimestamp()
    private val answers = mutableListOf<UserAnswer>()

    val testId = id

    private fun correctAnswersCount(): Int {
        return answers.count { it.isCorrect }
    }

    private fun incorrectAnswersCount(): Int {
        return answers.size - correctAnswersCount()
    }

    private fun isPassed(): Boolean {
        val incorrectCount = totalQuestions - correctAnswersCount()

        return incorrectCount <= maxErrors
    }

    fun addAnswer(userAnswer: UserAnswer): TestSession {
        answers.add(userAnswer)

        return this
    }

    fun getAnswers(): List<UserAnswer> {
        return answers
    }

    fun toTestResult(): TestResult {
        val endTime = currentTimestamp()

        return TestResult(
            id = id,
            modeName = modeName,
            languageCode = languageCode,
            totalQuestions = totalQuestions,
            maxErrors = maxErrors,
            startTime = startTime,
            endTime = endTime,
            totalTime = endTime - startTime,
            correctAnswers = correctAnswersCount(),
            incorrectAnswers = incorrectAnswersCount(),
            isPassed = isPassed()
        )
    }
}