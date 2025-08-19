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

    fun <T> saveTestSetting(testMode: TestMode, testSettingType: TestSettingType, value: T) {
        when (testMode) {
            TestMode.PREP -> savePrepTestSetting(testSettingType, value)
            TestMode.EXAM -> saveExamTestSetting(testSettingType, value)
        }
    }

    private fun <T> savePrepTestSetting(testSettingType: TestSettingType, value: T) {
        when (testSettingType) {
            SkipLearnedQuestions -> settingsRepository.saveSetting(PrepSkipLearnedQuestions, value as Boolean)
            ShowCorrectAnswer -> settingsRepository.saveSetting(PrepShowCorrectAnswer, value as Boolean)
            ShuffleQuestions -> settingsRepository.saveSetting(PrepShuffleQuestions, value as Boolean)
            ShuffleAnswers -> settingsRepository.saveSetting(PrepShuffleAnswers, value as Boolean)
            TotalQuestions -> settingsRepository.saveSetting(PrepTotalQuestions, value as Int)
            MaxErrors -> settingsRepository.saveSetting(PrepMaxErrors, value as Int)
        }
    }

    private fun <T> saveExamTestSetting(testSettingType: TestSettingType, value: T) {
        when (testSettingType) {
            SkipLearnedQuestions -> settingsRepository.saveSetting(ExamSkipLearnedQuestions, value as Boolean)
            ShowCorrectAnswer -> settingsRepository.saveSetting(ExamShowCorrectAnswer, value as Boolean)
            ShuffleQuestions -> settingsRepository.saveSetting(ExamShuffleQuestions, value as Boolean)
            ShuffleAnswers -> settingsRepository.saveSetting(ExamShuffleAnswers, value as Boolean)
            TotalQuestions -> settingsRepository.saveSetting(ExamTotalQuestions, value as Int)
            MaxErrors -> settingsRepository.saveSetting(ExamMaxErrors, value as Int)
        }
    }

}