package app.dmvtestprep.domain.model

import app.dmvtestprep.repository.DatabaseRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class TestEventHandler(databaseRepository: DatabaseRepository) : TestEventListener {

    private val sessionManager = TestSessionManager(databaseRepository)
    private val answerManager = UserAnswerManager(databaseRepository)

    private val _events = MutableSharedFlow<TestEventResult>()
    val events: SharedFlow<TestEventResult> = _events

    override suspend fun handle(event: TestEvent) {
        event.handle(this)
    }

    internal suspend fun onTestStarted(test: Test, correctAnswer: AnswerChar.Value) {
        sessionManager.startSession(
            modeName = test.modeName,
            languageCode = test.languageCode,
            totalQuestions = test.questions.size,
            maxErrors = test.settings.maxErrors
        ).also {
            _events.emit(TestEventResult.TestStarted(it, test, correctAnswer))
        }
    }

    internal suspend fun onTestCompleted() {
        sessionManager.completeSession { testResult, testAnswers ->
            _events.emit(TestEventResult.TestCompleted(
                testResult = testResult,
                testAnswers = testAnswers
            ))
        }
    }

    internal suspend fun onAnswerSubmitted(questionId: Int, correctAnswer: Char, selectedAnswer: Char) {
        answerManager.submitAnswer(questionId, correctAnswer, selectedAnswer).also {
            sessionManager.addAnswer(it)
        }
    }

    internal fun onNewQuestion() {
        sessionManager.testId?.let { testId ->
            answerManager.startNewAnswer(testId)
        }
    }

}