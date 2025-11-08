import SwiftUI
import Shared

struct TestView: View {
    @ObservedObject var viewModel: TestViewModel
    @State private var showExitConfirmation = false
    let onDismiss: () -> Void
    
    var body: some View {
        NavigationView {
            ZStack {
                switch viewModel.uiState {
                case let ongoingState as TestState.TestOngoing:
                    TestOngoingView(
                        uiState: ongoingState,
                        onToggleSave: viewModel.onToggleSavedQuestion,
                        onSelectAnswer: viewModel.onSelectAnswer,
                        onActionButtonClick: viewModel.onActionButtonClick
                    )
                case let completedState as TestState.TestCompleted:
                    if completedState.isDetailView {
                        QuestionsDetailView(
                            answers: completedState.mistakes,
                            questions: completedState.questions,
                            showIncorrectAnswers: true,
                            englishMode: completedState.englishMode,
                            onToggleSave: viewModel.onToggleSavedQuestion,
                            onQuestionPageChanged: viewModel.onQuestionPageChanged
                        )
                    } else {
                        TestCompletedView(
                            uiState: completedState,
                            actions: TestCompletedActionsHandler(
                                onRetryCallback: viewModel.onRetryMistakes,
                                onReviewCallback: viewModel.onReviewMistakes,
                                onStartOverCallback: viewModel.onStartOver,
                                onNewTestCallback: {
                                    Task {
                                        await viewModel.onNewTest()
                                    }
                                },
                                onDismissCallback: onDismiss
                            )
                        )
                    }
                case let errorState as TestState.Error:
                    ErrorView(
                        errorSummary: errorState.errorSummary,
                        onRetry: errorState.onRetry
                    )
                default:
                    LoadingView()
                }
            }
            .navigationBarTitle(viewModel.uiState.title, displayMode: .inline)
            .navigationBarItems(
                leading: Button("Cancel") {
                    switch true {
                    case viewModel.uiState.isDetailView:
                        viewModel.onBackToTestResult()
                    case viewModel.uiState.isErrorOccurred:
                        onDismiss()
                    default:
                        showExitConfirmation = true
                    }
                },
                trailing: ToggleEnglishModeButton(
                    isNativeAvailable: viewModel.uiState.isNativeAvailable,
                    isEnglishMode: viewModel.uiState.englishMode,
                    onToggleEnglishMode: viewModel.onToggleEnglishMode
                )
            )
            .alert(isPresented: $showExitConfirmation) {
                Alert(
                    title: Text("Confirmation"),
                    message: Text("Are you sure you want to finish the test?"),
                    primaryButton: .destructive(Text("Exit")) {
                        onDismiss()
                    },
                    secondaryButton: .cancel()
                )
            }
        }
    }
}
