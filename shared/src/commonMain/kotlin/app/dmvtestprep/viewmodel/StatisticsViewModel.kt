package app.dmvtestprep.viewmodel

import app.dmvtestprep.domain.model.ErrorSummary
import app.dmvtestprep.domain.model.StatisticsState
import app.dmvtestprep.domain.model.StatisticsFactory
import app.dmvtestprep.domain.usecase.ClearStatisticsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StatisticsViewModel(
    private val statisticsFactory: StatisticsFactory,
    private val clearStatisticsUseCase: ClearStatisticsUseCase
) : BaseViewModel() {
    private val _uiState = MutableStateFlow<StatisticsState>(StatisticsState.Loading)

    val uiState: StateFlow<StatisticsState> = _uiState.asStateFlow()

    fun loadStatistics() {
        launch {
            getStatistics()
        }
    }

    private fun getStatistics() {
        try {
            _uiState.value = StatisticsState.Statistics(
                sections = statisticsFactory.getStatistics()
            )
        } catch (e: Throwable) {
            statisticsError(toErrorSummary(e.message))
        }
    }

    fun onClearStatistics() {
        clearStatisticsUseCase()
    }

    private fun statisticsError(errorSummary: ErrorSummary) {
        _uiState.value = StatisticsState.Error(
            errorSummary = errorSummary,
            onRetry = { loadStatistics() }
        )
    }
}