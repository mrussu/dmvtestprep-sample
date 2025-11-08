package app.dmvtestprep.domain.usecase

import app.dmvtestprep.domain.model.Language
import app.dmvtestprep.domain.model.Settings
import app.dmvtestprep.domain.settings.DefaultSetting
import app.dmvtestprep.repository.SettingsRepository
import app.dmvtestprep.domain.settings.SettingType

class SettingsUseCase(private val settingsRepository: SettingsRepository) {

    fun getCurrentLanguageCode(): String {
        return settingsRepository.getSettingValue(DefaultSetting.LanguageCode)
    }

    fun saveSelectedLanguage(language: Language) {
        settingsRepository.saveSetting(DefaultSetting.LanguageCode, language.code)
    }

    fun toggleEnglishMode(): Boolean {
        return settingsRepository.toggleEnglishMode()
    }

    fun getSettings(): Settings {
        return settingsRepository.getSettings()
    }

    fun <T> saveSetting(settingType: SettingType, value: T) {
        when (settingType) {
            SettingType.EnglishMode -> settingsRepository.saveSetting(DefaultSetting.EnglishMode, value as Boolean)
            SettingType.AnswerPrefix -> settingsRepository.saveSetting(DefaultSetting.AnswerPrefix, value as Boolean)
        }
    }

}