package app.dmvtestprep.viewmodel.actions

import app.dmvtestprep.domain.model.TestMode

interface TestConfigActions {
    fun onTestModeChanged(testMode: TestMode)
    fun onSkipLearnedQuestionsChanged(isChecked: Boolean)
    fun onShowCorrectAnswerChanged(isChecked: Boolean)
    fun onShuffleQuestionsChanged(isChecked: Boolean)
    fun onShuffleAnswersChanged(isChecked: Boolean)
    fun onMaxErrorsChanged(maxErrors: Int)
    fun onStartTest()
}