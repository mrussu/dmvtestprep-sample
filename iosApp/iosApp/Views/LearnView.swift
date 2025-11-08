import SwiftUI
import Shared

struct LearnView: View {
    @ObservedObject var viewModel: LearnViewModel
    
    var body: some View {
        switch viewModel.uiState {
        case let learnState as LearnState.Learn:
            refreshableNavigationView(title: learnState.mainViewTitle) {
                QuestionsView(
                    uiState: learnState,
                    onSetFilter: viewModel.onSetFilter,
                    onToggleSave: viewModel.onToggleSavedQuestion,
                    onQuestionItemClick: viewModel.onQuestionDetail
                )
            }
            .fullScreenCover(isPresented: Binding(
                get: { viewModel.uiState.isDetailView },
                set: { _ in }
            ), content: {
                detailNavigationView {
                    QuestionsDetailView(
                        answers: [:],
                        questions: learnState.questions,
                        showIncorrectAnswers: false,
                        englishMode: learnState.englishMode,
                        onToggleSave: viewModel.onToggleSavedQuestion,
                        onQuestionPageChanged: viewModel.onQuestionPageChanged
                    )
                }
            })
        case let errorState as LearnState.Error:
            navigationView {
                ErrorView(
                    errorSummary: errorState.errorSummary,
                    onRetry: errorState.onRetry
                )
            }
        default:
            navigationView {
                LoadingView()
            }
        }
    }
    
    @ViewBuilder
    private func navigationView<Content: View>(@ViewBuilder content: () -> Content) -> some View {
        NavigationView {
            ZStack {
                AppColors.shared.background.asColor()
                    .ignoresSafeArea()
                        
                content()
            }
            .navigationBarTitle(viewModel.uiState.title)
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
    
    @ViewBuilder
    private func refreshableNavigationView<Content: View>(
        title: String? = nil,
        @ViewBuilder content: () -> Content
    ) -> some View {
        NavigationView {
            ScrollView {
                content()
            }
            .background(AppColors.shared.background.asColor())
            .navigationBarTitle(title ?? viewModel.uiState.title)
            .navigationBarItems(
                trailing: ToggleEnglishModeButton(
                    isNativeAvailable: viewModel.uiState.isNativeAvailable,
                    isEnglishMode: viewModel.uiState.englishMode,
                    onToggleEnglishMode: viewModel.onToggleEnglishMode
                )
            )
            .refreshable {
                if !viewModel.uiState.isProcessing {
                    await viewModel.reloadQuestions()
                }
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
    
    @ViewBuilder
    private func detailNavigationView<Content: View>(@ViewBuilder content: () -> Content) -> some View {
        NavigationView {
            ZStack {
                content()
            }
            .navigationBarTitle(viewModel.uiState.title, displayMode: .inline)
            .navigationBarItems(
                leading: Button(action: {
                    viewModel.onBackToQuestionList()
                }) {
                    HStack {
                        Image(systemName: "chevron.left")
                        Text("Back")
                    }
                },
                trailing: ToggleEnglishModeButton(
                    isNativeAvailable: viewModel.uiState.isNativeAvailable,
                    isEnglishMode: viewModel.uiState.englishMode,
                    onToggleEnglishMode: viewModel.onToggleEnglishMode
                )
            )
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
}
