package app.dmvtestprep.domain.model

interface TextProvider {
    val english: String
    val native: String?

    fun getText(englishMode: Boolean): String {
        return if (englishMode || native.isNullOrBlank()) english else native ?: english
    }

    fun hasNativeText(): Boolean {
        return !native.isNullOrBlank()
    }
}