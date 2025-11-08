package app.dmvtestprep.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfigData(
    @SerialName("platform_name")
    val platformName: String
)