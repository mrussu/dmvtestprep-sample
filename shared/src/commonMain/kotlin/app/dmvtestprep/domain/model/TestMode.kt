package app.dmvtestprep.domain.model

enum class TestMode(
    val modeName: String,
    val modeText: String
) {
    PREP(
        modeName = "Prep",
        modeText = "Work through all available questions for the most thorough practice."
    ),
    EXAM(
        modeName = "Exam",
        modeText = "Official test-based with 18 questions, allowing up to 3 errors to pass."
    );

    companion object {
        fun fromModeName(modeName: String): TestMode {
            return entries.firstOrNull { it.modeName.equals(modeName, ignoreCase = true) } ?: PREP
        }
    }
}