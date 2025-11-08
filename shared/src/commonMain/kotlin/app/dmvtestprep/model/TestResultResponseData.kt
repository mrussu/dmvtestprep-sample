package app.dmvtestprep.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestResultResponseData(
    val status: String,
    @SerialName("header_text")
    val headerText: String? = null,
    @SerialName("inspire_text")
    val inspireText: String? = null
)