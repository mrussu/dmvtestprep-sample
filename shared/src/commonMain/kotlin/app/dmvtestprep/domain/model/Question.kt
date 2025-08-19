package app.dmvtestprep.domain.model

data class Question(
    override val id: Int,
    val num: QuestionNumber,
    val text: BilingualText,
    val answers: List<Answer>,
    val correctAnswerChar: AnswerChar.Value,
    val image: String?,
    val isSaved: Boolean = false,
    val learningStage: LearningStage = LearningStage()
) : Entity, TextProvider by text {

    data class Answer(
        val prefix: String,
        val char: Char,
        val text: BilingualText
    ) : TextProvider by text

}