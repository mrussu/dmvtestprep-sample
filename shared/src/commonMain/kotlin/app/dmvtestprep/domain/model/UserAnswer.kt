package app.dmvtestprep.domain.model

data class UserAnswer(
    val testId: Int,
    val questionId: Int,
    val startTime: Long,
    val endTime: Long,
    val totalTime: Long,
    val correctAnswer: Char,
    val selectedAnswer: Char,
    val isCorrect: Boolean
)