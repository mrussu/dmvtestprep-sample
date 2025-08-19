package app.dmvtestprep.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestResultData(
    @SerialName("mode_name")
    val modeName: String,
    @SerialName("language_code")
    val languageCode: String,
    @SerialName("total_questions")
    val totalQuestions: Int,
    @SerialName("max_errors")
    val maxErrors: Int,
    @SerialName("start_time")
    val startTime: Long,
    @SerialName("end_time")
    val endTime: Long,
    @SerialName("total_time")
    val totalTime: Long,
    @SerialName("correct_answers")
    val correctAnswers: Int,
    @SerialName("incorrect_answers")
    val incorrectAnswers: Int,
    @SerialName("is_passed")
    val isPassed: Boolean,
    val accuracy: Double
)