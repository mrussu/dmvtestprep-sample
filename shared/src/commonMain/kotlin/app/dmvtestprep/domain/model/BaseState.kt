package app.dmvtestprep.domain.model

interface BaseState {
    val title: String
    val isProcessing: Boolean
    val isErrorOccurred: Boolean
}