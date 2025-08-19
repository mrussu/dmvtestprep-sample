import SwiftUI
import Shared

struct TestConfigView: View {
    @ObservedObject var viewModel: TestConfigViewModel
    @State private var showSettings = false
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(alignment: .leading, spacing: 12) {
                    TestModePicker(items: TestMode.allCases, selection: Binding(
                        get: { viewModel.uiState.testMode },
                        set: { testMode in viewModel.onTestModeChanged(testMode: testMode) }
                    ))
                    .frame(height: 40)
                    .padding(.bottom, 10)
                    .pickerStyle(.segmented)
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

struct TestModePicker: UIViewRepresentable {
    var items: [TestMode]
    @Binding var selection: TestMode
    
    func makeUIView(context: Context) -> LargeSegmentedControl {
        let segmentedControl = LargeSegmentedControl(items: items.map(\.modeName))
        segmentedControl.selectedSegmentIndex = items.firstIndex(of: selection) ?? 0
        segmentedControl.addTarget(context.coordinator, action: #selector(Coordinator.valueChanged), for: .valueChanged)
        return segmentedControl
    }
    
    func updateUIView(_ uiView: LargeSegmentedControl, context: Context) {
        uiView.selectedSegmentIndex = items.firstIndex(of: selection) ?? 0
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    
    class Coordinator: NSObject {
        var parent: TestModePicker
        
        init(_ parent: TestModePicker) {
            self.parent = parent
        }
        
        @objc func valueChanged(_ sender: UISegmentedControl) {
            let index = sender.selectedSegmentIndex
            guard index >= 0, index < parent.items.count else { return }
            parent.selection = parent.items[index]
        }
    }
}

class LargeSegmentedControl: UISegmentedControl {
    override open func didMoveToSuperview() {
        super.didMoveToSuperview()
        self.setContentHuggingPriority(.defaultLow, for: .vertical)
        let attributes = [NSAttributedString.Key.font: UIFont.systemFont(ofSize: 16, weight: .medium)]
        self.setTitleTextAttributes(attributes, for: .normal)
        self.setTitleTextAttributes(attributes, for: .selected)
    }
}

struct MaxErrorsSliderView: View {
    let label: String
    let total: Int
    
    @Binding
    var value: Double
    var range: ClosedRange<Double> {
        0...max(Double(total), 1.0)
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack {
                Text(label)
                    .frame(maxWidth: .infinity, alignment: .leading)
                Text("\(Int(value)) out of \(total)")
                    .frame(alignment: .trailing)
            }
            .padding(.bottom, 20)
            Text(dynamicText)
                .font(.system(size: 12, weight: .medium, design: .rounded))
                .frame(maxWidth: .infinity, alignment: .center)
                .foregroundColor(dynamicColor)
                .transition(.opacity)
                .animation(.easeInOut, value: value)
            Slider(value: $value, in: range, step: 1)
                .frame(height: 48)
                .accentColor(dynamicColor)
        }
    }
    
    private var dynamicColor: Color {
        let progress = (value - range.lowerBound) / (range.upperBound - range.lowerBound)
        let color = MaxErrorsResources.shared.getColor(progress: progress)
        
        return color.asColor()
    }
    
    private var dynamicText: String {
        let progress = (value - range.lowerBound) / (range.upperBound - range.lowerBound)
        return MaxErrorsResources.shared.getText(progress: progress)
    }
}
