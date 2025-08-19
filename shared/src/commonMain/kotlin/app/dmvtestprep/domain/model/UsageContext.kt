package app.dmvtestprep.domain.model

data class UsageContext(
    val appVersion: String,
    val systemInfo: String,
    val deviceInfo: String
)