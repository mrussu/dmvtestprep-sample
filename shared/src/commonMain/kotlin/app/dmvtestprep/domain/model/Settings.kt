package app.dmvtestprep.domain.model

import app.dmvtestprep.domain.settings.DefaultSetting.*

data class Settings(
    val englishMode: Boolean,
    val answerPrefix: Boolean
) {
    companion object {
        fun default(): Settings {
            return Settings(
                englishMode = EnglishMode.defaultValue,
                answerPrefix = AnswerPrefix.defaultValue
            )
        }
    }
}