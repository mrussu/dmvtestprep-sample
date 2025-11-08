package app.dmvtestprep.domain.model

import app.dmvtestprep.domain.settings.SettingType
import app.dmvtestprep.domain.settings.Settings

data class SettingsState(
    val languages: List<Language>,
    val language: Language,
    val settings: Settings,
    val views: SettingsViews
) {
    val languageLabel: String
        get() = if (languages.isNotEmpty()) language.getText(settings.englishMode.value) else "Loading..."
    val legalDocs = Documents.legal

    fun <T> update(settingType: SettingType, value: T): SettingsState {
        return copy(settings = settings.update(settingType, value))
    }
}