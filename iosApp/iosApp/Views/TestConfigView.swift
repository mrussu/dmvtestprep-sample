import SwiftUI
import Shared

struct TestConfigView: View {
    @ObservedObject var viewModel: TestConfigViewModel
    @State private var showSettings = false
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading, spacing: 12) {
                    SegmentedPicker(
                        selection: Binding(
                            get: { viewModel.uiState.testMode },
                            set: { testMode in viewModel.onTestModeChanged(testMode: testMode) }
                        ),
                        items: TestMode.allCases,
                        title: \.modeName,
                        fontSize: 16
                    )
                    .frame(height: 40)
                    .padding(.bottom, 10)
                    switch viewModel.uiState.testMode {
                    case .prep:
                        Text(TestMode.prep.modeText)
                            .font(.system(size: 16, weight: .medium, design: .default))
                            .foregroundColor(.gray)
                    case .exam:
                        Text(TestMode.exam.modeText)
                            .font(.system(size: 16, weight: .medium, design: .default))
                            .foregroundColor(.gray)
                    }
                    Divider().padding(.vertical, 10)
                    MaxErrorsSliderView(
                        label: viewModel.uiState.maxErrors.label,
                        total: viewModel.uiState.totalQuestions.value.intValue,
                        colorFor: { progress in MaxErrorsResources.shared.getColor(progress: progress).asColor() },
                        textFor: { progress in MaxErrorsResources.shared.getText(progress: progress) },
                        value: Binding(
                            get: { Double(viewModel.uiState.maxErrors.value.intValue) },
                            set: { newValue in viewModel.onMaxErrorsChanged(maxErrors: Int(newValue)) }
                        )
                    )
                    .disabled(!viewModel.uiState.maxErrors.isEnabled)
                    Toggle(viewModel.uiState.skipLearnedQuestions.label, isOn: Binding(
                        get: { viewModel.uiState.skipLearnedQuestions.value.boolValue },
                        set: { newValue in viewModel.onSkipLearnedQuestionsChanged(isChecked: newValue) }
                    ))
                    .disabled(!viewModel.uiState.skipLearnedQuestions.isEnabled)
                    Divider().padding(.vertical, 10)
                    Toggle(viewModel.uiState.showCorrectAnswer.label, isOn: Binding(
                        get: { viewModel.uiState.showCorrectAnswer.value.boolValue },
                        set: { newValue in viewModel.onShowCorrectAnswerChanged(isChecked: newValue) }
                    ))
                    .disabled(!viewModel.uiState.showCorrectAnswer.isEnabled)
                    Toggle(viewModel.uiState.shuffleQuestions.label, isOn: Binding(
                        get: { viewModel.uiState.shuffleQuestions.value.boolValue },
                        set: { newValue in viewModel.onShuffleQuestionsChanged(isChecked: newValue) }
                    ))
                    .disabled(!viewModel.uiState.shuffleQuestions.isEnabled)
                    Toggle(viewModel.uiState.shuffleAnswers.label, isOn: Binding(
                        get: { viewModel.uiState.shuffleAnswers.value.boolValue },
                        set: { newValue in viewModel.onShuffleAnswersChanged(isChecked: newValue) }
                    ))
                    .disabled(!viewModel.uiState.shuffleAnswers.isEnabled)
                    Spacer().frame(minHeight: 12)
                    Button(action: {
                        viewModel.startTest()
                    }) {
                        Text(viewModel.uiState.startButton.value as String)
                    }
                    .buttonStyle(ActionButtonStyle())
                }
                .padding()
                .padding(.vertical, 8)
                .onAppear {
                    viewModel.updateLearnedQuestions()
                }
            }
            .background(AppColors.shared.background.asColor())
            .navigationBarTitle(viewModel.uiState.title)
            .navigationBarItems(
                trailing: Button(action: {
                    showSettings = true
                }) {
                    Image(systemName: "gear")
                }
            )
            .fullScreenCover(isPresented: Binding(
                get: { viewModel.uiState.showTest },
                set: { _ in }
            )) {
                TestView(
                    viewModel: TestViewModel(),
                    onDismiss: { viewModel.finishTest() }
                )
            }
            .sheet(isPresented: $showSettings) {
                SettingsView()
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
}
