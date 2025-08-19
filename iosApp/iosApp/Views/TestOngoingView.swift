import SwiftUI
import Shared

struct TestOngoingView: View {
    let uiState: TestState.TestOngoing
    let onToggleSave: (Int) -> Void
    let onSelectAnswer: (Int, AnswerChar.Value) -> Void
    let onActionButtonClick: () -> Void
    
    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .bottom) {
                ScrollView {
                    VStack(alignment: .leading, spacing: 0) {
                        Spacer().frame(minHeight: 12)
                        Text(uiState.question.getText(englishMode: uiState.englishMode))
                            .font(.system(size: 18, weight: .medium))
                            .multilineTextAlignment(.center)
                            .lineLimit(nil)
                            .minimumScaleFactor(0.8)
                            .fixedSize(horizontal: false, vertical: true)
                            .frame(maxWidth: .infinity)
                            .padding(.horizontal, 36)
                        Spacer().frame(minHeight: 12)
                        QuestionImageView(imageUrl: uiState.question.image.toFullImageUrl())
                        VStack(spacing: 8) {
                            ForEach(uiState.question.answers, id: \.self) { answer in
                                AnswerItemView(
                                    answer: answer,
                                    englishMode: uiState.englishMode,
                                    colorScheme: uiState.answerState.getColorScheme(char: answer.char),
                                    onTap: {
                                        if !uiState.answerState.isSubmitted {
                                            onSelectAnswer(uiState.question.id.toInt(), answer.char.toAnswerChar())
                                        }
                                    }
                                )
                            }
                        }
                    }
                    .overlay(
                        ToggleSaveButton(
                            questionId: uiState.question.id.toInt(),
                            isSaved: uiState.question.isSaved,
                            onToggleSave: onToggleSave
                        )
                        .frame(width: 32, height: 32),
                        alignment: .topTrailing
                    )
                    .padding(12)
                    .padding(.bottom, 84 + 12)
                    .frame(maxWidth: .infinity, minHeight: geometry.size.height)
                }
                
                ZStack {
                    VisualEffectBlurView(style: .systemMaterial)
                        .ignoresSafeArea(edges: [.horizontal, .bottom])
                        .shadow(color: .black.opacity(1), radius: 1, x: 0, y: 1)
                    Button(action: {
                        onActionButtonClick()
                    }) {
                        Text(uiState.actionButtonText)
                    }
                    .buttonStyle(ActionButtonStyle(isDisabled: !uiState.answerState.isSelected))
                    .disabled(!uiState.answerState.isSelected)
                    .padding()
                }
                .fixedSize(horizontal: false, vertical: true)
            }
        }
    }
}
