package app.dmvtestprep.domain.model

sealed class StatisticsState(
    override val title: String,
    override val isProcessing: Boolean = false,
    override val isErrorOccurred: Boolean = false
) : BaseState {
    data object Loading: StatisticsState(title = "Statistics loading...", isProcessing = true)
    data class Statistics(
        val sections: List<StatisticsSection>
    ): StatisticsState(title = "Statistics")
    data class Error(
        val errorSummary: ErrorSummary,
        val onRetry: () -> Unit
    ) : StatisticsState(title = errorSummary.type, isErrorOccurred = true)
}