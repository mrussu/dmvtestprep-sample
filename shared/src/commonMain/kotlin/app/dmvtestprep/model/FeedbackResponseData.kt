package app.dmvtestprep.model

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackResponseData(
    val status: String,
    val message: String
)