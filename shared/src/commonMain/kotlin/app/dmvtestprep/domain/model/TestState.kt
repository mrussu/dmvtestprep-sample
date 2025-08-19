package app.dmvtestprep.domain.model

sealed class TestState(
    override val title: String = "Test",
    override val isProcessing: Boolean = false,
    override val isErrorOccurred: Boolean = false
) : BaseState, LanguageState {
    open val isDetailView = false

    override val englishMode = false
    override val isNativeAvailable = false

    override fun onToggleEnglishMode(): TestState = this

    open fun onToggleSavedQuestion(questionId: Int, isSaved: Boolean): TestState = this

    data object Loading : TestState(title = "Test loading...", isProcessing = true)
    data object Completing : TestState(title = "Test completing...", isProcessing = true)

    data class TestOngoing(
        val id: Int,
        val test: Test,
        val answerState: AnswerState,
        val questionIndex: Int,
        val settings: Settings
    ) : TestState() {
        val question: Question
            get() = test.questions[questionIndex]
        val actionButtonText: String
            get() = if (answerState.isSubmitted) "Next" else "Submit"
        val isLastQuestion: Boolean
            get() = questionIndex >= test.questions.size - 1

        override val title: String
            get() = "Question ${questionIndex + 1} of ${test.questions.size}"
        override val englishMode: Boolean
            get() = settings.englishMode
        override val isNativeAvailable
            get() = question.text.hasNativeText()

        override fun onToggleSavedQuestion(questionId: Int, isSaved: Boolean): TestOngoing {
            return copy(
                test = test.copy(
                    questions = test.questions.map {
                        if (it.id == questionId) it.copy(isSaved = isSaved) else it
                    }
                )
            )
        }

        override fun onToggleEnglishMode(): TestState {
            return copy(
                settings = settings.copy(
                    englishMode = !settings.englishMode
                )
            )
        }
    }

    data class TestCompleted(
        val mistakes: Map<Int, AnswerChar.Value>,
        val questions: ListNavigator<Question>,
        val testResult: TestResult,
        val testMode: TestMode,
        val viewMode: ViewMode,
        val settings: Settings
    ) : TestState() {
        override val title: String
            get() = when (viewMode) {
                is ViewMode.MainView -> testResult.title
                is ViewMode.DetailView -> "Review mistake ${questions.currentIndex + 1} of ${questions.size}"
            }
        override val englishMode: Boolean
            get() = settings.englishMode
        override val isNativeAvailable: Boolean
            get() = when (viewMode) {
                is ViewMode.MainView -> false
                is ViewMode.DetailView -> questions.currentOrNull?.text?.hasNativeText() ?: false
            }
        override val isDetailView: Boolean
            get() = viewMode is ViewMode.DetailView

        val maxErrors: Int
            get() = (testResult.maxErrors * questions.size / testResult.totalQuestions)
                .coerceIn(questions.indices)

        fun toTestResult(): TestCompleted {
            return copy(viewMode = ViewMode.MainView)
        }

        fun toReviewMistakes(): TestCompleted {
            return copy(viewMode = ViewMode.DetailView)
        }

        override fun onToggleSavedQuestion(questionId: Int, isSaved: Boolean): TestCompleted {
            return copy(
                questions = questions.updateById(questionId) {
                    it.copy(isSaved = isSaved)
                }
            )
        }

        override fun onToggleEnglishMode(): TestCompleted {
            return copy(
                settings = settings.copy(
                    englishMode = !settings.englishMode
                )
            )
        }
    }

    data class Error(
        val errorSummary: ErrorSummary,
        val onRetry: () -> Unit
    ) : TestState(title = errorSummary.type, isErrorOccurred = true)
}