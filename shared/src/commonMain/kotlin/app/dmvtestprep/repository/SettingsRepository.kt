package app.dmvtestprep.repository

import app.dmvtestprep.datasource.storage.SettingsStorage
import app.dmvtestprep.domain.model.TestConfig
import app.dmvtestprep.domain.model.TestMode
import app.dmvtestprep.domain.model.NavTab
import app.dmvtestprep.domain.model.Settings
import app.dmvtestprep.domain.model.TestSettings
import app.dmvtestprep.domain.settings.DefaultSetting
import app.dmvtestprep.domain.settings.DefaultSetting.*

class SettingsRepository(private val settingsStorage: SettingsStorage) {

    private fun saveSetting(key: BooleanSetting, value: Boolean) {
        settingsStorage.putBoolean(key.key, value)
    }

    private fun saveSetting(key: StringSetting, value: String) {
        settingsStorage.putString(key.key, value)
    }

    private fun saveSetting(key: IntSetting, value: Int) {
        settingsStorage.putInt(key.key, value)
    }

    private fun toggleSetting(key: BooleanSetting): Boolean {
        return (!getSettingValue(key)).also {
            settingsStorage.putBoolean(key.key, it)
        }
    }

    fun <T> saveSetting(defaultSetting: DefaultSetting<T>, value: T) {
        when (defaultSetting) {
            is BooleanSetting -> saveSetting(defaultSetting, value as Boolean)
            is StringSetting -> saveSetting(defaultSetting, value as String)
            is IntSetting -> saveSetting(defaultSetting, value as Int)
        }
    }

    fun getSettingValue(key: DefaultSetting<Boolean>): Boolean {
        return settingsStorage.getBoolean(key.key, key.defaultValue)
    }

    fun getSettingValue(key: DefaultSetting<String>): String {
        return settingsStorage.getString(key.key, key.defaultValue)
    }

    fun getSettingValue(key: DefaultSetting<Int>): Int {
        return settingsStorage.getInt(key.key, key.defaultValue)
    }

    fun getNavTab(): NavTab {
        return NavTab.fromId(getSettingValue(NavTabId))
    }

    fun savaNavTab(navTab: NavTab) {
        saveSetting(NavTabId, navTab.id)
    }

    fun getTestMode(): TestMode {
        return TestMode.fromModeName(getSettingValue(TestModeName))
    }

    fun saveTestMode(testMode: TestMode) {
        saveSetting(TestModeName, testMode.modeName)
    }

    fun toggleEnglishMode(): Boolean {
        return toggleSetting(EnglishMode)
    }

    fun getTestConfig(): TestConfig {
        return TestConfig(
            modeName = getSettingValue(TestModeName),
            languageCode = getSettingValue(LanguageCode)
        )
    }

    fun getSettings(): Settings {
        return Settings(
            englishMode = getSettingValue(EnglishMode),
            answerPrefix = getSettingValue(AnswerPrefix)
        )
    }

    fun getTestSettings(testModeName: String): TestSettings {
        return when (TestMode.fromModeName(testModeName)) {
            TestMode.PREP -> TestSettings(
                skipLearnedQuestions = getSettingValue(PrepSkipLearnedQuestions),
                showCorrectAnswer = getSettingValue(PrepShowCorrectAnswer),
                shuffleQuestions = getSettingValue(PrepShuffleQuestions),
                shuffleAnswers = getSettingValue(PrepShuffleAnswers),
                totalQuestions = getSettingValue(PrepTotalQuestions),
                maxErrors = getSettingValue(PrepMaxErrors)
            )
            TestMode.EXAM -> TestSettings(
                skipLearnedQuestions = ExamSkipLearnedQuestions.defaultValue,
                showCorrectAnswer = getSettingValue(ExamShowCorrectAnswer),
                shuffleQuestions = ExamShuffleQuestions.defaultValue,
                shuffleAnswers = getSettingValue(ExamShuffleAnswers),
                totalQuestions = ExamTotalQuestions.defaultValue,
                maxErrors = ExamMaxErrors.defaultValue
            )
        }
    }
}