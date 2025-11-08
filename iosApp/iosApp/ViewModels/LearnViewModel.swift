import Foundation
import Shared

final class LearnViewModel: ObservableObject {
    @Published var uiState: LearnState = .Loading()
    
    private var sharedViewModel: Shared.LearnViewModel
    
    init() {
        self.sharedViewModel = Shared.LearnViewModel(
            getQuestionsUseCase: UseCaseProvider().getQuestionsUseCase,
            toggleSavedQuestionUseCase: UseCaseProvider().toggleSavedQuestionUseCase,
            settingsUseCase: UseCaseProvider().settingsUseCase
        )
        Task { await startObservingState() }
        Task { await loadQuestions() }
    }
    
    @MainActor
    func startObservingState() async {
        for await state in sharedViewModel.uiState {
            self.uiState = state
        }
    }
    
    @MainActor
    func loadQuestions() {
        sharedViewModel.loadQuestions()
    }
    
    @MainActor
    func reloadQuestions() async {
        sharedViewModel.reloadQuestions()
    }
    
    func onToggleEnglishMode() {
        sharedViewModel.onToggleEnglishMode()
    }
    
    func onToggleSavedQuestion(questionId: Int) {
        sharedViewModel.onToggleSavedQuestion(questionId: questionId.toInt32())
    }
    
    func onSetFilter(filter: QuestionFilter) {
        sharedViewModel.onSetFilter(filter: filter)
    }
    
    func onQuestionPageChanged(newIndex: Int) {
        sharedViewModel.onQuestionPageChanged(newIndex: newIndex.toInt32())
    }
    
    func onQuestionDetail(initialIndex: Int) {
        sharedViewModel.onQuestionDetail(initialIndex: initialIndex.toInt32())
    }
    
    func onBackToQuestionList() {
        sharedViewModel.onBackToQuestionList()
    }
    
    deinit {
        sharedViewModel.clear()
    }
}
