package app.dmvtestprep.domain.model

sealed class TestEventResult {
    data class TestStarted(
        val id: Int,
        val test: Test,
        val correctAnswer: AnswerChar.Value
    ) : TestEventResult()
    data class TestCompleted(
        val testResult: TestResult,
        val testAnswers: List<UserAnswer>
    ) : TestEventResult()
}