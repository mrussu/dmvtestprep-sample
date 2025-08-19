package app.dmvtestprep.model

import kotlinx.serialization.Serializable

@Serializable
data class QuestionData(
    val id: Int,
    val chapter: Int,
    val question: Int,
    val english: String,
    val native: String? = null,
    val image: String? = null,
    val answers: List<AnswerData>,
    val answer: Char
) {
    @Serializable
    data class AnswerData(
        val char: Char,
        val english: String,
        val native: String? = null
    )
}