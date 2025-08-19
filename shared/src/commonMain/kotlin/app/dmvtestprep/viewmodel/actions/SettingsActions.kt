package app.dmvtestprep.viewmodel.actions

import app.dmvtestprep.domain.model.Language

interface SettingsActions {
    val onSelectLanguage: (Language) -> Unit
    val onToggleEnglishMode: (Boolean) -> Unit
    val onAnswerPrefix: (Boolean) -> Unit
    val onFeedbackView: () -> Unit
    val onDocumentView: (Int) -> Unit
}