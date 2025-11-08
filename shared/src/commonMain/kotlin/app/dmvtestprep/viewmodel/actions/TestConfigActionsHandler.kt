package app.dmvtestprep.viewmodel.actions

import app.dmvtestprep.domain.model.TestMode
import app.dmvtestprep.viewmodel.TestConfigViewModel

class TestConfigActionsHandler(
    private val viewModel: TestConfigViewModel,
    private val onStartTestCallback: () -> Unit
) : TestConfigActions {
    override fun onTestModeChanged(testMode: TestMode) {
        viewModel.onTestModeChanged(testMode)
    }

    override fun onSkipLearnedQuestionsChanged(isChecked: Boolean) {
        viewModel.onSkipLearnedQuestionsChanged(isChecked)
    }

    override fun onShowCorrectAnswerChanged(isChecked: Boolean) {
        viewModel.onShowCorrectAnswerChanged(isChecked)
    }

    override fun onShuffleQuestionsChanged(isChecked: Boolean) {
        viewModel.onShuffleQuestionsChanged(isChecked)
    }

    override fun onShuffleAnswersChanged(isChecked: Boolean) {
        viewModel.onShuffleAnswersChanged(isChecked)
    }

    override fun onMaxErrorsChanged(maxErrors: Int) {
        viewModel.onMaxErrorsChanged(maxErrors)
    }

    override fun onStartTest() {
        onStartTestCallback()
    }
}