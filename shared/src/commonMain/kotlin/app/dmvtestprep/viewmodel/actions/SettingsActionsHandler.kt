package app.dmvtestprep.viewmodel.actions

import app.dmvtestprep.domain.model.Language

class SettingsActionsHandler(
    onSelectLanguageCallback: (Language) -> Unit,
    onEnglishModeCallback: (Boolean) -> Unit,
    onAnswerPrefixCallback: (Boolean) -> Unit,
    onFeedbackViewCallback: () -> Unit,
    onDocumentViewCallback: (Int) -> Unit
) : SettingsActions {
    override val onSelectLanguage = onSelectLanguageCallback
    override val onToggleEnglishMode = onEnglishModeCallback
    override val onAnswerPrefix = onAnswerPrefixCallback
    override val onFeedbackView = onFeedbackViewCallback
    override val onDocumentView = onDocumentViewCallback
}