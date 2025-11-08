import SwiftUI
import Shared

struct TestCompletedView: View {
    let uiState: TestState.TestCompleted
    let actions: TestCompletedActions
    
    var mistakesAction: MistakesAction {
        actions.createMistakesAction(
            testMode: uiState.testMode,
            hasMistakes: !uiState.mistakes.isEmpty,
            correctAnswers: uiState.testResult.correctAnswers
        )
    }
    
    var body: some View {
        uiState.testResult.backgroundColor.asColor()
            .ignoresSafeArea()
        
        GeometryReader { geometry in
            ScrollView {
                VStack(alignment: .center, spacing: 16) {
                    Spacer()
                    Text(uiState.testResult.header.text)
                        .font(.system(size: 18, weight: .semibold))
                        .foregroundColor(uiState.testResult.header.color.asColor())
                        .multilineTextAlignment(.center)
                    Text(uiState.testResult.detail.text)
                        .font(.system(size: 16))
                        .foregroundColor(uiState.testResult.detail.color.asColor())
                        .multilineTextAlignment(.center)
                    Spacer()
                    Text(uiState.testResult.inspire.text)
                        .font(.system(size: 24, weight: .medium))
                        .foregroundColor(uiState.testResult.inspire.color.asColor())
                        .multilineTextAlignment(.center)
                        .lineSpacing(6)
                        .frame(maxWidth: .infinity)
                    if mistakesAction.isVisible {
                        Spacer()
                        Button(action: mistakesAction.onClick) {
                            Text(mistakesAction.text)
                                .font(.system(size: 14, weight: .medium))
                                .padding(.horizontal, 14)
                        }
                        .buttonStyle(FilledButtonStyle())
                    }
                    Spacer()
                    Button(action: actions.onStartOver) {
                        Text("Start over")
                            .font(.system(size: 14, weight: .medium))
                            .padding(.horizontal, 14)
                    }
                    .buttonStyle(FilledButtonStyle())
                    Button(action: actions.onNewTest) {
                        Text("New test")
                            .font(.system(size: 18, weight: .medium))
                            .padding(.horizontal, 18)
                    }
                    .buttonStyle(FilledButtonStyle())
                    Button(action: actions.onDismiss) {
                        Text("Next time")
                            .font(.system(size: 14))
                    }
                    .padding(.top, 8)
                    Spacer()
                }
                .padding()
                .padding(.horizontal, 36)
                .frame(maxWidth: .infinity, minHeight: geometry.size.height)
            }
        }
    }
}
