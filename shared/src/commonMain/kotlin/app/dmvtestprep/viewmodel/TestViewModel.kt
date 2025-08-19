package app.dmvtestprep.viewmodel

import app.dmvtestprep.domain.model.AnswerChar
import app.dmvtestprep.domain.model.AnswerState
import app.dmvtestprep.domain.model.ErrorSummary
import app.dmvtestprep.domain.model.ErrorTypes
import app.dmvtestprep.domain.model.Test
import app.dmvtestprep.domain.model.TestEvent
import app.dmvtestprep.domain.model.TestEventHandler
import app.dmvtestprep.domain.model.TestEventResult
import app.dmvtestprep.domain.model.TestMode
import app.dmvtestprep.domain.model.TestResult
import app.dmvtestprep.domain.model.TestResultResponse
import app.dmvtestprep.domain.model.TestState
import app.dmvtestprep.domain.model.UserAnswer
import app.dmvtestprep.domain.model.ViewMode
import app.dmvtestprep.domain.model.toAnswerChar
import app.dmvtestprep.domain.model.toListNavigator
import app.dmvtestprep.domain.usecase.GetTestUseCase
import app.dmvtestprep.domain.usecase.SettingsUseCase
import app.dmvtestprep.domain.usecase.ToggleSavedQuestionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TestViewModel(
    private val testEventHandler: TestEventHandler,
    private val getTestUseCase: GetTestUseCase,
    private val toggleSavedQuestionUseCase: ToggleSavedQuestionUseCase,
    private val settingsUseCase: SettingsUseCase
) : BaseViewModel() {
    private val _test = MutableStateFlow<Test?>(null)
    private val _uiState = MutableStateFlow<TestState>(TestState.Loading)

    val uiState = _uiState.asStateFlow()

    init {
        observeTestEventResult()
    }

    private fun observeTestEventResult() {
        launch {
            testEventHandler.events.collect { event ->
                when (event) {
                    is TestEventResult.TestStarted -> {
                        testOngoing(event.id, event.test, event.correctAnswer)
                    }
                    is TestEventResult.TestCompleted -> {
                        testComplete(event.testResult, event.testAnswers)
                    }
                }
            }
        }
    }

    private fun processEvent(event: TestEvent) {
        launch {
            testEventHandler.handle(event)
        }
    }

    fun onNewTest() {
        launch {
            loadTest()
        }
    }

    private suspend fun loadTest() {
        _test.value = null
        _uiState.value = TestState.Loading
        try {
            _test.value = getTestUseCase()
            testStart()
        } catch (e: ErrorTypes) {
            testError(e.toErrorSummary())
        } catch (e: Throwable) {
            testError(toErrorSummary(e.message))
        }
    }

    private fun testStart() {
        val test = _test.value ?: return testError(toErrorSummary("No test available"))
        val question = test.questions.firstOrNull() ?: return testError(toErrorSummary("No questions available"))

        processEvent(
            TestEvent.TestStarted(
                test = test,
                correctAnswer = question.correctAnswerChar
            )
        )
    }

    private fun testOngoing(id: Int, test: Test, correctAnswer: AnswerChar.Value) {
        _uiState.value = TestState.TestOngoing(
            id = id,
            test = test,
            answerState = AnswerState(
                correctAnswer = correctAnswer
            ),
            questionIndex = 0,
            settings = settingsUseCase.getSettings()
        )
        processEvent(TestEvent.NewQuestion)
    }

    private fun testComplete(testResult: TestResult, testAnswers: List<UserAnswer>) {
        val mistakes = testAnswers.filterNot { it.isCorrect }.associate {
            it.questionId to it.selectedAnswer.toAnswerChar()
        }
        val questions = _test.value?.questions?.run {
            filter { it.id in mistakes.keys }
        } ?: emptyList()

        _uiState.value = TestState.Completing
        launch {
            _uiState.value = TestState.TestCompleted(
                mistakes = mistakes,
                questions = questions.toListNavigator(),
                testResult = updateTestResult(testResult),
                testMode = TestMode.fromModeName(testResult.modeName),
                viewMode = ViewMode.MainView,
                settings = settingsUseCase.getSettings()
            )
        }
    }

    private suspend fun updateTestResult(testResult: TestResult): TestResult {
        return runCatching {
            when (val response = getTestUseCase.sendTestResult(testResult)) {
                is TestResultResponse.Success -> testResult.copy(
                    headerText = response.headerText,
                    inspireText = response.inspireText
                )
                is TestResultResponse.Failure -> testResult
            }
        }.getOrDefault(testResult)
    }

    private fun testError(errorSummary: ErrorSummary) {
        _test.value = null
        _uiState.value = TestState.Error(
            errorSummary = errorSummary,
            onRetry = { onNewTest() }
        )
    }

    private fun handleShowCorrectAnswer(state: TestState.TestOngoing) {
        _uiState.value = state.copy(
            answerState = state.answerState.copy(isSubmitted = true)
        )
        submitAnswer(state.question.id, state.answerState)
    }

    private fun handleSubmitAndNext(state: TestState.TestOngoing) {
        if (!state.answerState.isSubmitted) {
            submitAnswer(state.question.id, state.answerState)
        }
        nextQuestion(state)
    }

    private fun submitAnswer(questionId: Int, answerState: AnswerState) {
        processEvent(
            TestEvent.AnswerSubmitted(
                questionId = questionId,
                correctAnswer = answerState.correctAnswer.char,
                selectedAnswer = answerState.selectedAnswer.char
            )
        )
    }

    private fun nextQuestion(state: TestState.TestOngoing) {
        if (state.isLastQuestion) {
            processEvent(TestEvent.TestCompleted)
        } else {
            val nextIndex = state.questionIndex + 1
            val nextQuestion = state.test.questions[nextIndex]

            _uiState.value = state.copy(
                questionIndex = nextIndex,
                answerState = AnswerState(
                    correctAnswer = nextQuestion.correctAnswerChar
                )
            )
            processEvent(TestEvent.NewQuestion)
        }
    }

    fun onToggleEnglishMode() {
        _uiState.value = _uiState.value.onToggleEnglishMode()
        settingsUseCase.toggleEnglishMode()
    }

    fun onToggleSavedQuestion(questionId: Int) {
        val isSaved = toggleSavedQuestionUseCase.toggle(questionId)

        _uiState.value = _uiState.value.onToggleSavedQuestion(questionId, isSaved)
    }

    fun onSelectAnswer(questionId: Int, selectedAnswer: AnswerChar.Value) {
        val state = _uiState.value

        if (state is TestState.TestOngoing) with(state) {
            if (questionId == question.id && selectedAnswer != answerState.selectedAnswer) {
                _uiState.value = copy(
                    answerState = answerState.copy(
                        selectedAnswer = selectedAnswer
                    )
                )
            }
        }
    }

    fun onActionButtonClick() {
        val state = _uiState.value

        if (state is TestState.TestOngoing) {
            if (state.test.settings.showCorrectAnswer && !state.answerState.isSubmitted) {
                handleShowCorrectAnswer(state)
            } else {
                handleSubmitAndNext(state)
            }
        }
    }

    fun onQuestionPageChanged(newIndex: Int) {
        val state = _uiState.value

        if (state is TestState.TestCompleted) {
            _uiState.value = state.copy(
                questions = state.questions.goTo(index = newIndex)
            )
        }
    }

    fun onRetryMistakes() {
        val state = _uiState.value

        if (state is TestState.TestCompleted) {
            _test.value?.let { test ->
                _test.value = test.copy(
                    questions = state.questions.toList(),
                    settings = test.settings.copy(
                        maxErrors = state.maxErrors
                    )
                )
                testStart()
            }
        }
    }

    fun onReviewMistakes() {
        val state = _uiState.value

        if (state is TestState.TestCompleted) {
            _uiState.value = state.toReviewMistakes()
        }
    }

    fun onBackToTestResult() {
        val state = _uiState.value

        if (state is TestState.TestCompleted) {
            _uiState.value = state.toTestResult()
        }
    }

    fun onStartOver() {
        testStart()
    }
}