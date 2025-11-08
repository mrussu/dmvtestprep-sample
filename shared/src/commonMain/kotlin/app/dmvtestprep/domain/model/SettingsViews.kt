package app.dmvtestprep.domain.model

sealed class SettingsViews(val title: String) {
    data object SettingsView : SettingsViews("Settings")
    data class FeedbackView(val uiState: FeedbackState) : SettingsViews(uiState.title)
    data class DocumentView(val uiState: DocumentState) : SettingsViews(uiState.title)
}