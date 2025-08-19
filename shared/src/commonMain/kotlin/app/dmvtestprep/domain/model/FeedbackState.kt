package app.dmvtestprep.domain.model

sealed class FeedbackState(
    override val title: String,
    override val isProcessing: Boolean = false,
    override val isErrorOccurred: Boolean = false
) : BaseState {
    data object Sending : FeedbackState(title = "Feedback sending...", isProcessing = true)
    data object Sent : FeedbackState(title = "Feedback sent")
    data class Message(
        val value: String,
        val minLength: Int = 20
    ) : FeedbackState(title = "Feedback") {
        val isAllowToSend: Boolean
            get() = value.isNotBlank() && value.count { !it.isWhitespace() } >= minLength
    }
    data class Error(
        val errorSummary: ErrorSummary,
        val onRetry: () -> Unit
    ) : FeedbackState(title = errorSummary.type, isErrorOccurred = true)
}