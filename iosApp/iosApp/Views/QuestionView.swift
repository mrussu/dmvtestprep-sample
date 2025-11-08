import SwiftUI
import Shared

struct QuestionView: View {
    let question: Question
    let selectedAnswerChar: AnswerChar.Value
    let showIncorrectAnswers: Bool
    let englishMode: Bool
    let onToggleSave: (Int) -> Void
    
    @State private var showIncorrectAnswersState: Bool
    
    init(
        question: Question,
        selectedAnswerChar: AnswerChar.Value,
        showIncorrectAnswers: Bool,
        englishMode: Bool,
        onToggleSave: @escaping (Int) -> Void
    ) {
        self.question = question
        self.selectedAnswerChar = selectedAnswerChar
        self.showIncorrectAnswers = showIncorrectAnswers
        self.englishMode = englishMode
        self.onToggleSave = onToggleSave
        
        self._showIncorrectAnswersState = State(initialValue: showIncorrectAnswers)
    }
    
    var body: some View {
        ZStack {
            question.backgroundColor
                .ignoresSafeArea()
            
            ScrollView {
                VStack(alignment: .leading, spacing: 0) {
                    HStack {
                        Text(question.lsLabel)
                            .font(.system(size: 16, weight: .medium))
                            .foregroundColor(question.lsTextColor)
                            .padding(.vertical, 6)
                            .padding(.horizontal, 8)
                            .background(question.lsBackgroundColor)
                            .cornerRadius(6)
                        Spacer()
                        ToggleSaveButton(
                            questionId: question.id.toInt(),
                            isSaved: question.isSaved,
                            onToggleSave: onToggleSave
                        )
                        .frame(width: 32, height: 32)
                    }
                    .padding(.bottom, 12)
                    Spacer().frame(minHeight: 12)
                    Text(question.getText(englishMode: englishMode))
                        .font(.system(size: 18, weight: .medium))
                        .multilineTextAlignment(.center)
                        .lineLimit(nil)
                        .minimumScaleFactor(0.8)
                        .fixedSize(horizontal: false, vertical: true)
                        .frame(maxWidth: .infinity)
                        .padding(.horizontal, 36)
                    Spacer().frame(minHeight: 12)
                    QuestionImageView(imageUrl: question.image.toFullImageUrl())
                    Button(action: {
                        showIncorrectAnswersState.toggle()
                    }) {
                        Text(showIncorrectAnswersState ? "Hide incorrect answers" : "Show incorrect answers")
                            .font(.system(size: 14, weight: .medium))
                            .padding(8)
                            .frame(maxWidth: .infinity)
                            .multilineTextAlignment(.center)
                    }
                    .padding(.bottom, 24)
                    VStack(spacing: showIncorrectAnswersState ? 8 : 0) {
                        ForEach(question.answers, id: \.self) { answer in
                            let isCorrect = answer.char == question.correctAnswerChar.char
                            let colorScheme = AnswerColorProvider.shared.getColorScheme(
                                char: answer.char,
                                selectedAnswerChar: selectedAnswerChar,
                                correctAnswerChar: question.correctAnswerChar,
                                isSubmitted: true
                            )

                            if showIncorrectAnswersState || isCorrect {
                                AnswerItemView(
                                    answer: answer,
                                    englishMode: englishMode,
                                    colorScheme: colorScheme,
                                    onTap: {}
                                )
                            }
                        }
                    }
                }
                .padding(12)
            }
        }
    }
}
