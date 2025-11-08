package app.dmvtestprep.domain.model

sealed class TestResultResponse {
    class Success(val headerText: String?, val inspireText: String?) : TestResultResponse()
    data object Failure : TestResultResponse()
}