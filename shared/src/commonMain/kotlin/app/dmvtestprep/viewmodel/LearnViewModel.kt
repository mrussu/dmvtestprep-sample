package app.dmvtestprep.viewmodel

import app.dmvtestprep.domain.model.LearnState
import app.dmvtestprep.domain.model.ErrorSummary
import app.dmvtestprep.domain.model.ErrorTypes
import app.dmvtestprep.domain.model.Question
import app.dmvtestprep.domain.model.QuestionFilter
import app.dmvtestprep.domain.model.toListNavigator
import app.dmvtestprep.domain.usecase.GetQuestionsUseCase
import app.dmvtestprep.domain.usecase.SettingsUseCase
import app.dmvtestprep.domain.usecase.ToggleSavedQuestionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LearnViewModel(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val toggleSavedQuestionUseCase: ToggleSavedQuestionUseCase,
    private val settingsUseCase: SettingsUseCase
) : BaseViewModel() {
    private val _uiState = MutableStateFlow<LearnState>(LearnState.Loading)

    private var reloadNeed = true

    val uiState = _uiState.asStateFlow()

    fun loadQuestions() {
        executeLoadQuestions(ignoreCache = false)
    }

    fun reloadQuestions() {
        executeLoadQuestions(ignoreCache = true)
    }

    fun onToggleEnglishMode() {
        _uiState.value = _uiState.value.onToggleEnglishMode()
        settingsUseCase.toggleEnglishMode()
    }

    fun onToggleSavedQuestion(questionId: Int) {
        val state = _uiState.value

        if (state is LearnState.Learn) {
            val isSaved = toggleSavedQuestionUseCase.toggle(questionId)

            _uiState.value = state.onToggleSavedQuestion(questionId, isSaved)
        }
    }

    fun onSetFilter(filter: QuestionFilter) {
        val state = _uiState.value

        if (state is LearnState.Learn) {
            _uiState.value = state.onSetFilter(filter)
        }
    }

    fun onQuestionPageChanged(newIndex: Int) {
        val state = _uiState.value

        if (state is LearnState.Learn) {
            _uiState.value = state.copy(
                questions = state.questions.goTo(index = newIndex)
            )
        }
    }

    fun onQuestionDetail(initialIndex: Int) {
        val state = _uiState.value

        if (state is LearnState.Learn) {
            _uiState.value = state.toQuestionDetail(index = initialIndex)
        }
    }

    fun onBackToQuestionList() {
        val state = _uiState.value

        if (state is LearnState.Learn) {
            _uiState.value = state.toQuestionList()
        }
    }

    private fun executeLoadQuestions(ignoreCache: Boolean) {
        if (reloadNeed || !_uiState.value.isProcessing) {
            launch {
                _uiState.value = LearnState.Loading
                try {
                    learnQuestions(getQuestionsUseCase(ignoreCache))
                } catch (e: ErrorTypes) {
                    learnError(e.toErrorSummary())
                } catch (e: Throwable) {
                    learnError(toErrorSummary(e.message))
                }
            }
            reloadNeed = false
        }
    }

    private fun learnQuestions(questions: List<Question>) {
        _uiState.value = LearnState.Learn.create(
            allQuestions = questions.toListNavigator(),
            settings = settingsUseCase.getSettings(),
            filter = QuestionFilter.ALL
        )
    }

    private fun learnError(errorSummary: ErrorSummary) {
        reloadNeed = true
        _uiState.value = LearnState.Error(
            errorSummary = errorSummary,
            onRetry = { reloadQuestions() }
        )
    }
}