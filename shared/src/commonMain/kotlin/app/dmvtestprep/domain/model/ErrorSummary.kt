package app.dmvtestprep.domain.model

data class ErrorSummary(
    val type: String,
    val error: String,
    val details: String
)