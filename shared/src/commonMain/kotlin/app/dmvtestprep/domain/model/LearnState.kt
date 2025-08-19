package app.dmvtestprep.domain.model

sealed class LearnState(
    override val title: String,
    override val isProcessing: Boolean = false,
    override val isErrorOccurred: Boolean = false
) : BaseState, LanguageState {
    open val isDetailView = false

    override val englishMode: Boolean = false
    override val isNativeAvailable: Boolean = false

    override fun onToggleEnglishMode(): LearnState = this

    data object Loading : LearnState(title = "Questions loading...", isProcessing = true)

    data class Learn(
        private val allQuestions: ListNavigator<Question>,
        val questions: ListNavigator<Question>,
        val filter: QuestionFilter,
        val viewMode: ViewMode,
        val settings: Settings
    ) : LearnState(title = "Questions") {
        override val title: String
            get() = when (viewMode) {
                is ViewMode.MainView -> mainViewTitle
                is ViewMode.DetailView -> detailViewTitle
            }
        override val englishMode: Boolean
            get() = settings.englishMode
        override val isNativeAvailable
            get() = when (viewMode) {
                is ViewMode.MainView -> questions.any { it.text.hasNativeText() }
                is ViewMode.DetailView -> questions[questions.currentIndex].text.hasNativeText()
            }
        override val isDetailView: Boolean
            get() = viewMode is ViewMode.DetailView

        val mainViewTitle: String = super.title
        val detailViewTitle: String
            get() = "${filter.filterName} - Question ${questions.currentIndex + 1} of ${questions.size}"

        fun onSetFilter(filter: QuestionFilter): Learn {
            return copy(
                questions = filteredQuestions(filter, allQuestions),
                filter = filter
            )
        }

        override fun onToggleEnglishMode(): LearnState {
            return copy(
                settings = settings.copy(
                    englishMode = !settings.englishMode
                )
            )
        }

        fun onToggleSavedQuestion(questionId: Int, isSaved: Boolean): Learn {
            return copy(
                allQuestions = allQuestions.updateById(questionId) {
                    it.copy(isSaved = isSaved)
                },
                questions = questions.updateById(questionId) {
                    it.copy(isSaved = isSaved)
                }
            )
        }

        fun toQuestionList(): Learn {
            return copy(
                viewMode = ViewMode.MainView
            )
        }

        fun toQuestionDetail(index: Int): Learn {
            return copy(
                questions = questions.goTo(index),
                viewMode = ViewMode.DetailView
            )
        }

        companion object {
            fun create(
                allQuestions: ListNavigator<Question>,
                filter: QuestionFilter,
                settings: Settings
            ): Learn {
                return Learn(
                    allQuestions = allQuestions,
                    questions = filteredQuestions(filter, allQuestions),
                    filter = filter,
                    viewMode = ViewMode.MainView,
                    settings = settings
                )
            }

            private fun filteredQuestions(
                filter: QuestionFilter,
                questions: ListNavigator<Question>
            ): ListNavigator<Question> {
                return when (filter) {
                    QuestionFilter.ALL -> questions
                    QuestionFilter.LEARNING -> questions.filter { it.learningStage.type.isLearning() }
                    QuestionFilter.LEARNED -> questions.filter { it.learningStage.type.isLearned() }
                    QuestionFilter.SAVED -> questions.filter { it.isSaved }
                }
            }
        }
    }

    data class Error(
        val errorSummary: ErrorSummary,
        val onRetry: () -> Unit
    ) : LearnState(title = errorSummary.type, isErrorOccurred = true)
}