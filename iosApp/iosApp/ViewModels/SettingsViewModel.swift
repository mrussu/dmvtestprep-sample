import Foundation
import Shared

class SettingsViewModel: ObservableObject {
    @Published var uiState: SettingsState
    
    private var sharedViewModel: Shared.SettingsViewModel
    
    init() {
        self.sharedViewModel = Shared.SettingsViewModel(
            getLanguagesUseCase: UseCaseProvider().getLanguagesUseCase,
            getDocumentUseCase: UseCaseProvider().getDocumentUseCase,
            sendFeedbackUseCase: UseCaseProvider().sendFeedbackUseCase,
            settingsUseCase: UseCaseProvider().settingsUseCase,
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
    
    func onToggleEnglishMode(isChecked: KotlinBoolean) {
        sharedViewModel.onToggleEnglishMode(isChecked: isChecked.boolValue)
    }
    
    func onAnswerPrefix(isChecked: KotlinBoolean) {
        sharedViewModel.onAnswerPrefix(isChecked: isChecked.boolValue)
    }
    
    func onSelectLanguage(language: Language) {
        sharedViewModel.onSelectLanguage(language: language)
    }
    
    func onSettingsView() {
        sharedViewModel.onSettingsView()
    }
    
    func onFeedbackView() {
        sharedViewModel.onFeedbackView()
    }
    
    func onDocumentView(id: KotlinInt) {
        sharedViewModel.onDocumentView(id: id.int32Value)
    }
    
    func onMessageChange(newMessage: String) {
        sharedViewModel.onMessageChange(newMessage: newMessage)
    }
    
    func onSendFeedback() {
        sharedViewModel.onSendFeedback()
    }
    
    deinit {
        sharedViewModel.clear()
    }
}
