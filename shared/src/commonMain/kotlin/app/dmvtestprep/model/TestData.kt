package app.dmvtestprep.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestData(
    @SerialName("mode_name")
    val modeName: String,
    @SerialName("language_code")
    val languageCode: String,
    val questions: List<QuestionData>
)