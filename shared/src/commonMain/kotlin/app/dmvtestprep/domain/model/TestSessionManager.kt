package app.dmvtestprep.domain.model

import app.dmvtestprep.repository.DatabaseRepository

class TestSessionManager(private val databaseRepository: DatabaseRepository) {

    private var testSession: TestSession? = null

    val testId: Int?
        get() = testSession?.testId

    fun addAnswer(answer: UserAnswer) {
        testSession?.addAnswer(answer)
    }

    suspend fun startSession(modeName: String, languageCode: String, totalQuestions: Int, maxErrors: Int): Int {
        return databaseRepository.startNewTest(
            modeName = modeName,
            languageCode = languageCode,
            totalQuestions = totalQuestions,
            maxErrors = maxErrors
        ).also { id ->
            testSession = TestSession(
                id = id,
                modeName = modeName,
                languageCode = languageCode,
                totalQuestions = totalQuestions,
                maxErrors = maxErrors
            )
        }
    }

    suspend fun completeSession(complete: suspend (TestResult, List<UserAnswer>) -> Unit) {
        testSession?.toTestResult()?.also { testResult ->
            databaseRepository.completeTest(
                id = testResult.id,
                correctAnswers = testResult.correctAnswers,
                incorrectAnswers = testResult.incorrectAnswers,
                isPassed = testResult.isPassed
            )
            complete(testResult, getAnswers())
        }
        testSession = null
    }

    private fun getAnswers(): List<UserAnswer> {
        return testSession?.getAnswers() ?: emptyList()
    }
}