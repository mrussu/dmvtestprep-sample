package app.dmvtestprep.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.dmvtestprep.domain.model.Language
import app.dmvtestprep.domain.model.SettingsState
import app.dmvtestprep.domain.model.SettingsViews
import app.dmvtestprep.domain.settings.SettingConfig.AnswerPrefix
import app.dmvtestprep.domain.settings.SettingConfig.EnglishMode
import app.dmvtestprep.domain.settings.SettingFactory
import app.dmvtestprep.domain.settings.Settings
import app.dmvtestprep.ui.screen.SettingsContent
import app.dmvtestprep.viewmodel.actions.SettingsActionsHandler

@Preview
@Composable
fun PreviewSettingsContent() {
    SettingsContent(
        uiState = SettingsState(
            languages = emptyList(),
            language = Language.default(),
            settings = Settings(
                englishMode = SettingFactory.defaultSetting(EnglishMode),
                answerPrefix = SettingFactory.defaultSetting(AnswerPrefix)
            ),
            views = SettingsViews.SettingsView
        ),
        actions = SettingsActionsHandler(
            onSelectLanguageCallback = {},
            onEnglishModeCallback = {},
            onAnswerPrefixCallback = {},
            onFeedbackViewCallback = {},
            onDocumentViewCallback = {}
        ),
    )
}