package app.dmvtestprep.domain.model

data class QuestionNumber(
    val chapter: Int,
    val question: Int
) {
    fun formatted(suffix: String? = null): String = buildString {
        append("$chapter.$question")
        suffix.takeIf { !it.isNullOrBlank() }?.let {
            append(" $it")
        }
    }
}