package app.dmvtestprep.viewmodel.actions

import app.dmvtestprep.domain.model.MistakesAction
import app.dmvtestprep.domain.model.TestMode

class TestCompletedActionsHandler(
    private val onRetryCallback: () -> Unit,
    private val onReviewCallback: () -> Unit,
    private val onStartOverCallback: () -> Unit,
    private val onNewTestCallback: () -> Unit,
    private val onDismissCallback: () -> Unit
) : TestCompletedActions {

    private fun TestMode.toMistakesAction(
        hasMistakes: Boolean,
        correctAnswers: Int
    ): MistakesAction {
        return when (this) {
            TestMode.PREP -> MistakesAction.retry(hasMistakes, correctAnswers, onRetryCallback)
            TestMode.EXAM -> MistakesAction.review(hasMistakes, onReviewCallback)
        }
    }

    override fun createMistakesAction(
        testMode: TestMode,
        hasMistakes: Boolean,
        correctAnswers: Int
    ): MistakesAction {
        return testMode.toMistakesAction(
            hasMistakes = hasMistakes,
            correctAnswers = correctAnswers
        )
    }

    override fun onStartOver() = onStartOverCallback()

    override fun onNewTest() = onNewTestCallback()

    override fun onDismiss() = onDismissCallback()
}