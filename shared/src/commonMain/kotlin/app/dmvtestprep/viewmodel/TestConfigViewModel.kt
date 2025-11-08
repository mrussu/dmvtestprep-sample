package app.dmvtestprep.viewmodel

import app.dmvtestprep.domain.model.TestConfigState
import app.dmvtestprep.domain.model.TestMode
import app.dmvtestprep.domain.model.TestMode.*
import app.dmvtestprep.domain.settings.SettingFactory
import app.dmvtestprep.domain.settings.ModeSettings
import app.dmvtestprep.domain.settings.SettingConfig.Exam
import app.dmvtestprep.domain.settings.SettingConfig.Prep
import app.dmvtestprep.domain.settings.TestSettingType.*
import app.dmvtestprep.domain.usecase.LearnedQuestionsUseCase
import app.dmvtestprep.domain.usecase.TestSettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TestConfigViewModel(
    private val learnedQuestionsUseCase: LearnedQuestionsUseCase,
    private val testSettingsUseCase: TestSettingsUseCase,
    private val settingFactory: SettingFactory
) {
    private val _uiState: MutableStateFlow<TestConfigState> = MutableStateFlow(initializeUiState())

    val uiState: StateFlow<TestConfigState> = _uiState.asStateFlow()

    private fun initializeUiState(): TestConfigState {
        return TestConfigState(
            learnedQuestions = getLearnedQuestions(),
            prepSettings = getModeSettings(PREP),
            examSettings = getModeSettings(EXAM),
            testMode = testSettingsUseCase.getTestMode(),
            showTest = false
        )
    }

    fun startTest() {
        _uiState.value = _uiState.value.copy(
            showTest = true
        )
    }

    fun finishTest() {
        _uiState.value = _uiState.value.copy(
            learnedQuestions = getLearnedQuestions(),
            showTest = false
        )
    }

    fun onTestModeChanged(testMode: TestMode) {
        _uiState.value = _uiState.value.copy(testMode = testMode)
        testSettingsUseCase.saveTestMode(testMode)
    }

    fun onSkipLearnedQuestionsChanged(isChecked: Boolean) {
        _uiState.value = _uiState.value.run {
            testSettingsUseCase.saveTestSetting(testMode, SkipLearnedQuestions(isChecked))
            update(SkipLearnedQuestions(isChecked))
        }
    }

    fun onShowCorrectAnswerChanged(isChecked: Boolean) {
        _uiState.value = _uiState.value.run {
            testSettingsUseCase.saveTestSetting(testMode, ShowCorrectAnswer(isChecked))
            update(ShowCorrectAnswer(isChecked))
        }
    }

    fun onShuffleQuestionsChanged(isChecked: Boolean) {
        _uiState.value = _uiState.value.run {
            testSettingsUseCase.saveTestSetting(testMode, ShuffleQuestions(isChecked))
            update(ShuffleQuestions(isChecked))
        }
    }

    fun onShuffleAnswersChanged(isChecked: Boolean) {
        _uiState.value = _uiState.value.run {
            testSettingsUseCase.saveTestSetting(testMode, ShuffleAnswers(isChecked))
            update(ShuffleAnswers(isChecked))
        }
    }

    fun onMaxErrorsChanged(maxErrors: Int) {
        _uiState.value = _uiState.value.run {
            testSettingsUseCase.saveTestSetting(testMode, MaxErrors(maxErrors))
            update(MaxErrors(maxErrors))
        }
    }

    fun updateLearnedQuestions() {
        _uiState.value = _uiState.value.copy(
            learnedQuestions = getLearnedQuestions()
        )
    }

    private fun getLearnedQuestions(): Int {
        return learnedQuestionsUseCase.count()
    }

    private fun getModeSettings(testMode: TestMode): ModeSettings {
        return when (testMode) {
            PREP -> ModeSettings(
                skipLearnedQuestions = settingFactory.createSetting(Prep.SkipLearnedQuestions),
                showCorrectAnswer = settingFactory.createSetting(Prep.ShowCorrectAnswer),
                shuffleQuestions = settingFactory.createSetting(Prep.ShuffleQuestions),
                shuffleAnswers = settingFactory.createSetting(Prep.ShuffleAnswers),
                totalQuestions = settingFactory.createSetting(Prep.TotalQuestions),
                maxErrors = settingFactory.createSetting(Prep.MaxErrors)
            )
            EXAM -> ModeSettings(
                skipLearnedQuestions = settingFactory.createSetting(Exam.SkipLearnedQuestions),
                showCorrectAnswer = settingFactory.createSetting(Exam.ShowCorrectAnswer),
                shuffleQuestions = settingFactory.createSetting(Exam.ShuffleQuestions),
                shuffleAnswers = settingFactory.createSetting(Exam.ShuffleAnswers),
                totalQuestions = settingFactory.createSetting(Exam.TotalQuestions),
                maxErrors = settingFactory.createSetting(Exam.MaxErrors)
            )
        }
    }
}