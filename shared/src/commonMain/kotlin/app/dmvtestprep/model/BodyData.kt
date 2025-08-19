package app.dmvtestprep.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BodyData<T>(
    val data: T,
    @SerialName("usage_context")
    val usageContext: UsageContextData
)