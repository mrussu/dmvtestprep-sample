package app.dmvtestprep.domain.settings

sealed class SettingType {
    data object EnglishMode : SettingType()
    data object AnswerPrefix : SettingType()
}