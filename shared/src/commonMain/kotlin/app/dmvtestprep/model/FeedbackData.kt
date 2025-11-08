package app.dmvtestprep.model

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackData(
    val message: String
)