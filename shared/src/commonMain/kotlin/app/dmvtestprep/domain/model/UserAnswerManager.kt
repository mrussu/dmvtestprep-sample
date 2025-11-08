package app.dmvtestprep.domain.model

import app.dmvtestprep.core.utils.TimeUtils.currentTimestamp
import app.dmvtestprep.repository.DatabaseRepository

class UserAnswerManager(private val databaseRepository: DatabaseRepository) {

    private val userAnswerBuilder = UserAnswerBuilder()

    fun startNewAnswer(testId: Int) {
        userAnswerBuilder
            .reset()
            .setTestId(testId)
            .setStartTime(currentTimestamp())
    }

    suspend fun submitAnswer(questionId: Int, correctAnswer: Char, selectedAnswer: Char): UserAnswer {
        return buildUserAnswer(questionId, correctAnswer, selectedAnswer).also {
            databaseRepository.saveUserAnswer(it)
            databaseRepository.updateLearnedQuestions(questionId, it.isCorrect)
        }
    }

    private fun buildUserAnswer(questionId: Int, correctAnswer: Char, selectedAnswer: Char): UserAnswer {
        return userAnswerBuilder
            .setQuestionId(questionId)
            .setEndTime(currentTimestamp())
            .setCorrectAnswer(correctAnswer)
            .setSelectedAnswer(selectedAnswer)
            .build()
    }
}