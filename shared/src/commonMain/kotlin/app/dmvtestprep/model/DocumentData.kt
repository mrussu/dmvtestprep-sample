package app.dmvtestprep.model

import kotlinx.serialization.Serializable

@Serializable
data class DocumentData(
    val title: String,
    val text: String
)