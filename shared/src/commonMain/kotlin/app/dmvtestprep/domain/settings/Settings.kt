package app.dmvtestprep.domain.settings

data class Settings(
    val englishMode: Setting<Boolean>,
    val answerPrefix: Setting<Boolean>
) {
    fun <T> update(settingType: SettingType, value: T): Settings {
        return when (settingType) {
            SettingType.EnglishMode -> copy(englishMode = englishMode.copy(value = value as Boolean))
            SettingType.AnswerPrefix -> copy(answerPrefix = answerPrefix.copy(value = value as Boolean))
        }
    }
}