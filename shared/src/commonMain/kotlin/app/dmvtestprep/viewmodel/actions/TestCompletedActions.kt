package app.dmvtestprep.viewmodel.actions

import app.dmvtestprep.domain.model.MistakesAction
import app.dmvtestprep.domain.model.TestMode

interface TestCompletedActions {
    fun createMistakesAction(
        testMode: TestMode,
        hasMistakes: Boolean,
        correctAnswers: Int
    ): MistakesAction
    fun onStartOver()
    fun onNewTest()
    fun onDismiss()
}