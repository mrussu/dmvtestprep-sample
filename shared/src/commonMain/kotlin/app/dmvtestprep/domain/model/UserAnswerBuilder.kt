package app.dmvtestprep.domain.model

class UserAnswerBuilder {
    private var testId: Int = 0
    private var questionId: Int = 0
    private var startTime: Long = 0
    private var endTime: Long = 0
    private var correctAnswer: Char = ' '
    private var selectedAnswer: Char = ' '

    fun setTestId(testId: Int): UserAnswerBuilder {
        this.testId = testId
        return this
    }

    fun setQuestionId(questionId: Int): UserAnswerBuilder {
        this.questionId = questionId
        return this
    }

    fun setStartTime(startTime: Long): UserAnswerBuilder {
        this.startTime = startTime
        return this
    }

    fun setEndTime(endTime: Long): UserAnswerBuilder {
        this.endTime = endTime
        return this
    }

    fun setCorrectAnswer(correctAnswer: Char): UserAnswerBuilder {
        this.correctAnswer = correctAnswer
        return this
    }

    fun setSelectedAnswer(selectedAnswer: Char): UserAnswerBuilder {
        this.selectedAnswer = selectedAnswer
        return this
    }

    fun reset(): UserAnswerBuilder {
        testId = 0
        questionId = 0
        startTime = 0
        endTime = 0
        correctAnswer = ' '
        selectedAnswer =  ' '
        return this
    }

    fun build(): UserAnswer {
        return UserAnswer(
            testId = testId,
            questionId = questionId,
            startTime = startTime,
            endTime = endTime,
            totalTime = endTime - startTime,
            correctAnswer = correctAnswer,
            selectedAnswer = selectedAnswer,
            isCorrect = correctAnswer == selectedAnswer
        )
    }
}