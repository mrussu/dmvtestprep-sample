package app.dmvtestprep.viewmodel

import app.dmvtestprep.domain.model.DocumentState
import app.dmvtestprep.domain.model.FeedbackState
import app.dmvtestprep.domain.model.ErrorSummary
import app.dmvtestprep.domain.model.ErrorTypes
import app.dmvtestprep.domain.model.FeedbackResponse
import app.dmvtestprep.domain.model.Language
import app.dmvtestprep.domain.model.SettingsState
import app.dmvtestprep.domain.model.SettingsViews
import app.dmvtestprep.domain.settings.SettingConfig
import app.dmvtestprep.domain.settings.SettingFactory
import app.dmvtestprep.domain.settings.SettingType.*
import app.dmvtestprep.domain.settings.Settings
import app.dmvtestprep.domain.usecase.GetDocumentUseCase
import app.dmvtestprep.domain.usecase.GetLanguagesUseCase
import app.dmvtestprep.domain.usecase.SendFeedbackUseCase
import app.dmvtestprep.domain.usecase.SettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    private val getLanguagesUseCase: GetLanguagesUseCase,
    private val getDocumentUseCase: GetDocumentUseCase,
    private val sendFeedbackUseCase: SendFeedbackUseCase,
    private val settingsUseCase: SettingsUseCase,
    private val settingFactory: SettingFactory
) : BaseViewModel() {

    private val _message = MutableStateFlow("")
    private val _uiState: MutableStateFlow<SettingsState> = MutableStateFlow(initializeUiState())

    val uiState = _uiState.asStateFlow()

    init {
        launch {
            val state = _uiState.value
            val languages = fetchLanguages()
            val language = findSelectedLanguage(languages)

            _uiState.value = state.copy(
                languages = languages,
                language = language,
                settings = updateSettings(state.settings, language)
            )
        }
    }

    fun onSelectLanguage(language: Language) {
        val state = _uiState.value

        if (state.language != language) {
            _uiState.value = state.copy(
                language = language,
                settings = updateSettings(state.settings, language)
            )
            settingsUseCase.saveSelectedLanguage(language)
        }
    }

    fun onToggleEnglishMode(isChecked: Boolean) {
        _uiState.value = _uiState.value.run {
            settingsUseCase.saveSetting(EnglishMode, isChecked)
            update(EnglishMode, isChecked)
        }
    }

    fun onAnswerPrefix(isChecked: Boolean) {
        _uiState.value = _uiState.value.run {
            settingsUseCase.saveSetting(AnswerPrefix, isChecked)
            update(AnswerPrefix, isChecked)
        }
    }

    fun onSettingsView() {
        _uiState.value = _uiState.value.copy(
            views = SettingsViews.SettingsView
        )
    }

    fun onFeedbackView() {
        _uiState.value = _uiState.value.copy(
            views = SettingsViews.FeedbackView(
                uiState = FeedbackState.Message(
                    value = _message.value
                )
            )
        )
    }

    fun onDocumentView(id: Int) {
        _uiState.value = _uiState.value.copy(
            views = SettingsViews.DocumentView(
                uiState = DocumentState.Loading
            )
        )
        loadDocument(id)
    }

    fun onMessageChange(newMessage: String) {
        _message.value = newMessage
        _uiState.value = _uiState.value.copy(
            views = SettingsViews.FeedbackView(
                uiState = FeedbackState.Message(
                    value = newMessage
                )
            )
        )
    }

    fun onSendFeedback() {
        _uiState.value = _uiState.value.copy(
            views = SettingsViews.FeedbackView(
                uiState = FeedbackState.Sending
            )
        )
        sendFeedback()
    }

    private fun initializeUiState(): SettingsState {
        return SettingsState(
            languages = emptyList(),
            language = Language.default(),
            settings = updateSettings(getSettings()),
            views = SettingsViews.SettingsView
        )
    }

    private fun getSettings(): Settings {
        return Settings(
            englishMode = settingFactory.createSetting(SettingConfig.EnglishMode),
            answerPrefix = settingFactory.createSetting(SettingConfig.AnswerPrefix)
        )
    }

    private fun updateSettings(settings: Settings, currentLanguage: Language? = null): Settings {
        val isEnglishModeEnabled = currentLanguage?.let { it != Language.default() } ?: false

        return settings.copy(
            englishMode = settings.englishMode.updateEnabled(isEnglishModeEnabled)
        )
    }

    private suspend fun fetchLanguages(): List<Language> {
        return runCatching { getLanguagesUseCase() }.getOrDefault(Language.defaultList())
    }

    private fun findSelectedLanguage(languages: List<Language>): Language {
        val languageCode = settingsUseCase.getCurrentLanguageCode()
        return languages.find { it.code == languageCode } ?: Language.default()
    }

    private fun sendFeedback() {
        launch {
            try {
                when (val response = sendFeedbackUseCase(_message.value.trim())) {
                    is FeedbackResponse.Success -> feedbackSent()
                    is FeedbackResponse.Failure -> feedbackError(toErrorSummary(response.message))
                }
            } catch (e: ErrorTypes) {
                feedbackError(e.toErrorSummary())
            } catch (e: Throwable) {
                feedbackError(toErrorSummary(e.message))
            }
        }
    }

    private fun feedbackSent() {
        _message.value = ""
        _uiState.value = _uiState.value.copy(
            views = SettingsViews.FeedbackView(
                uiState = FeedbackState.Sent
            )
        )
    }

    private fun feedbackError(errorSummary: ErrorSummary) {
        _uiState.value = _uiState.value.copy(
            views = SettingsViews.FeedbackView(
                uiState = FeedbackState.Error(
                    errorSummary = errorSummary,
                    onRetry = { sendFeedback() }
                )
            )
        )
    }

    private fun loadDocument(id: Int) {
        launch {
            try {
                _uiState.value = _uiState.value.copy(
                    views = SettingsViews.DocumentView(
                        uiState = getDocumentUseCase(id)
                    )
                )
            } catch (e: ErrorTypes) {
                documentError(id, e.toErrorSummary())
            } catch (e: Throwable) {
                documentError(id, toErrorSummary(e.message))
            }
        }
    }

    private fun documentError(id: Int, errorSummary: ErrorSummary) {
        _uiState.value = _uiState.value.copy(
            views = SettingsViews.DocumentView(
                uiState = DocumentState.Error(
                    errorSummary = errorSummary,
                    onRetry = { loadDocument(id) }
                )
            )
        )
    }
}