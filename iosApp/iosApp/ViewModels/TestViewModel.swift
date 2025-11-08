import Foundation
import Shared

final class TestViewModel: ObservableObject {
    @Published var uiState: TestState = .Loading()
    
    private var sharedViewModel: Shared.TestViewModel
    
    init() {
        self.sharedViewModel = Shared.TestViewModel(
            testEventHandler: TestEventHandlerFactory().create(),
            getTestUseCase: UseCaseProvider().getTestUseCase,
            toggleSavedQuestionUseCase: UseCaseProvider().toggleSavedQuestionUseCase,
            settingsUseCase: UseCaseProvider().settingsUseCase
        )
        Task { await startObservingState() }
        Task { await onNewTest() }
    }
    
    @MainActor
    func startObservingState() async {
        for await state in sharedViewModel.uiState {
            self.uiState = state
        }
    }
    
    @MainActor
    func onNewTest() async {
        sharedViewModel.onNewTest()
    }
    
    func onToggleEnglishMode() {
        sharedViewModel.onToggleEnglishMode()
    }
    
    func onToggleSavedQuestion(questionId: Int) {
        sharedViewModel.onToggleSavedQuestion(questionId: questionId.toInt32())
    }
    
    func onSelectAnswer(questionId: Int, selectedAnswer: AnswerChar.Value) {
        sharedViewModel.onSelectAnswer(questionId: questionId.toInt32(), selectedAnswer: selectedAnswer)
    }
    
    func onActionButtonClick() {
        sharedViewModel.onActionButtonClick()
    }
    
    func onQuestionPageChanged(newIndex: Int) {
        sharedViewModel.onQuestionPageChanged(newIndex: newIndex.toInt32())
    }
    
    func onRetryMistakes() {
        sharedViewModel.onRetryMistakes()
    }
    
    func onReviewMistakes() {
        sharedViewModel.onReviewMistakes()
    }
    
    func onBackToTestResult() {
        sharedViewModel.onBackToTestResult()
    }
    
    func onStartOver() {
        sharedViewModel.onStartOver()
    }
    
    deinit {
        sharedViewModel.clear()
    }
}
