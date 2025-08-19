package app.dmvtestprep.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsageContextData(
    @SerialName("app_version")
    val appVersion: String,
    @SerialName("system_info")
    val systemInfo: String,
    @SerialName("device_info")
    val deviceInfo: String
)