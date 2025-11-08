package app.dmvtestprep.domain.model

interface LanguageState {
    val englishMode: Boolean
    val isNativeAvailable: Boolean

    fun onToggleEnglishMode(): LanguageState
}