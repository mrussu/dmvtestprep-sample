package app.dmvtestprep.domain.model

sealed class TestEvent {
    abstract suspend fun handle(handler: TestEventHandler)

    data class TestStarted(
        val test: Test,
        val correctAnswer: AnswerChar.Value
    ) : TestEvent() {
        override suspend fun handle(handler: TestEventHandler) {
            handler.onTestStarted(test, correctAnswer)
        }
    }

    data class AnswerSubmitted(
        val questionId: Int,
        val correctAnswer: Char,
        val selectedAnswer: Char
    ) : TestEvent() {
        override suspend fun handle(handler: TestEventHandler) {
            handler.onAnswerSubmitted(questionId, correctAnswer, selectedAnswer)
        }
    }

    data object NewQuestion : TestEvent() {
        override suspend fun handle(handler: TestEventHandler) {
            handler.onNewQuestion()
        }
    }

    data object TestCompleted : TestEvent() {
        override suspend fun handle(handler: TestEventHandler) {
            handler.onTestCompleted()
        }
    }
}