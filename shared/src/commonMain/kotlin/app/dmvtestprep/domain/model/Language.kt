package app.dmvtestprep.domain.model

data class Language(
    val code: String,
    val name: BilingualText
) : TextProvider by name {
    companion object {
        const val DEFAULT_LANGUAGE_CODE = "eng"
        const val DEFAULT_LANGUAGE_NAME = "English"

        fun default(): Language {
            return Language(
                code = DEFAULT_LANGUAGE_CODE,
                name = BilingualText(
                    english = DEFAULT_LANGUAGE_NAME
                )
            )
        }

        fun defaultList() = listOf(default())
    }
}