import Foundation
import Shared

class TestConfigViewModel: ObservableObject {
    @Published var uiState: TestConfigState
    
    private var sharedViewModel: Shared.TestConfigViewModel
    
    init() {
        self.sharedViewModel = Shared.TestConfigViewModel(
            learnedQuestionsUseCase: UseCaseProvider().learnedQuestionsUseCase,
            testSettingsUseCase: UseCaseProvider().testSettingsUseCase,
            settingFactory: UseCaseProvider().settingFactory
        )
        self.uiState = sharedViewModel.uiState.value
        Task { await startObservingState() }
    }
    
    @MainActor
    func startObservingState() async {
        for await state in sharedViewModel.uiState {
            self.uiState = state
        }
    }
    
    func startTest() {
        sharedViewModel.startTest()
    }
    
    func finishTest() {
        sharedViewModel.finishTest()
    }
    
    func onTestModeChanged(testMode: TestMode) {
        sharedViewModel.onTestModeChanged(testMode: testMode)
    }
    
    func onSkipLearnedQuestionsChanged(isChecked: Bool) {
        sharedViewModel.onSkipLearnedQuestionsChanged(isChecked: isChecked)
    }
    
    func onShowCorrectAnswerChanged(isChecked: Bool) {
        sharedViewModel.onShowCorrectAnswerChanged(isChecked: isChecked)
    }
    
    func onShuffleQuestionsChanged(isChecked: Bool) {
        sharedViewModel.onShuffleQuestionsChanged(isChecked: isChecked)
    }
    
    func onShuffleAnswersChanged(isChecked: Bool) {
        sharedViewModel.onShuffleAnswersChanged(isChecked: isChecked)
    }
    
    func onMaxErrorsChanged(maxErrors: Int) {
        sharedViewModel.onMaxErrorsChanged(maxErrors: maxErrors.toInt32())
    }
    
    func updateLearnedQuestions() {
        sharedViewModel.updateLearnedQuestions()
    }
}
