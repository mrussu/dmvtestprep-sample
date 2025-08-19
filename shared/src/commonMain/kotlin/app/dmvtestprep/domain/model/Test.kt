package app.dmvtestprep.domain.model

data class Test(
    val modeName: String,
    val languageCode: String,
    val questions: List<Question>,
    val settings: TestSettings
)