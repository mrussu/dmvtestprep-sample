package app.dmvtestprep.domain.usecase

import app.dmvtestprep.domain.model.TestMode
import app.dmvtestprep.domain.settings.DefaultSetting.*
import app.dmvtestprep.domain.settings.TestSettingType
import app.dmvtestprep.domain.settings.TestSettingType.*
import app.dmvtestprep.repository.SettingsRepository

class TestSettingsUseCase(private val settingsRepository: SettingsRepository) {

    fun getTestMode(): TestMode {
        return settingsRepository.getTestMode()
    }

    fun saveTestMode(testMode: TestMode) {
        settingsRepository.saveTestMode(testMode)
    }

    fun saveTestSetting(mode: TestMode, type: TestSettingType) {
        when (mode) {
            TestMode.PREP -> when (type) {
                is SkipLearnedQuestions -> settingsRepository.saveSetting(PrepSkipLearnedQuestions, type.value)
                is ShowCorrectAnswer -> settingsRepository.saveSetting(PrepShowCorrectAnswer, type.value)
                is ShuffleQuestions -> settingsRepository.saveSetting(PrepShuffleQuestions, type.value)
                is ShuffleAnswers -> settingsRepository.saveSetting(PrepShuffleAnswers, type.value)
                is TotalQuestions -> settingsRepository.saveSetting(PrepTotalQuestions, type.value)
                is MaxErrors -> settingsRepository.saveSetting(PrepMaxErrors, type.value)
            }
            TestMode.EXAM -> when (type) {
                is SkipLearnedQuestions -> settingsRepository.saveSetting(ExamSkipLearnedQuestions, type.value)
                is ShowCorrectAnswer -> settingsRepository.saveSetting(ExamShowCorrectAnswer, type.value)
                is ShuffleQuestions -> settingsRepository.saveSetting(ExamShuffleQuestions, type.value)
                is ShuffleAnswers -> settingsRepository.saveSetting(ExamShuffleAnswers, type.value)
                is TotalQuestions -> settingsRepository.saveSetting(ExamTotalQuestions, type.value)
                is MaxErrors -> settingsRepository.saveSetting(ExamMaxErrors, type.value)
            }
        }
    }

}