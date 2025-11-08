package app.dmvtestprep.domain.model

sealed class DocumentState(
    override val title: String,
    override val isProcessing: Boolean = false,
    override val isErrorOccurred: Boolean = false
) : BaseState {
    data object Loading : DocumentState(title = "Document loading...", isProcessing = true)
    data class Document(
        val header: String,
        val content: List<MarkdownLine>
    ) : DocumentState(title = header)
    data class Error(
        val errorSummary: ErrorSummary,
        val onRetry: () -> Unit
    ) : DocumentState(title = errorSummary.type, isErrorOccurred = true)
}