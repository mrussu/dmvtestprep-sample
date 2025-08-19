import SwiftUI
import Shared

struct QuestionsDetailView: View {
    let answers: [KotlinInt: AnswerChar.Value]
    let questions: ListNavigator<Question>
    let showIncorrectAnswers: Bool
    let englishMode: Bool
    let onToggleSave: (Int) -> Void
    let onQuestionPageChanged: (Int) -> Void
    
    var body: some View {
        
        GeometryReader { geometry in
            ScrollViewReader { scrollProxy in
                VerticalPager(
                    totalPages: questions.size.toInt(),
                    initialPage: questions.currentIndex.toInt(),
                    onPageChanged: onQuestionPageChanged
                ) {
                    LazyVStack(spacing: 0) {
                        ForEach(0..<questions.size, id: \.self) { index in
                            let question = questions.get(index: index)
                            let selectedAnswerChar = answers[question.id.toKotlinInt()] ?? question.correctAnswerChar
                            
                            GeometryReader { pageGeometry in
                                let offset = pageGeometry.frame(in: .global).minY - geometry.frame(in: .global).minY
                                let normalizedOffset = abs(offset / geometry.size.height)
                                let scale = 0.85 + (1 - min(normalizedOffset, 1.0)) * 0.15
                                let cornerFraction = min(normalizedOffset / 0.1, 1.0)
                                let cornerRadius = CGFloat(8 * cornerFraction)
                                
                                QuestionView(
                                    question: question,
                                    selectedAnswerChar: selectedAnswerChar,
                                    showIncorrectAnswers: showIncorrectAnswers,
                                    englishMode: englishMode,
                                    onToggleSave: onToggleSave
                                )
                                .frame(width: geometry.size.width, height: geometry.size.height)
                                .clipShape(RoundedRectangle(cornerRadius: cornerRadius))
                                .background(
                                    RoundedRectangle(cornerRadius: cornerRadius)
                                        .fill(Color.white)
                                        .shadow(color: Color.black.opacity(0.25), radius: 4, x: 0, y: 2)
                                )
                                .overlay(
                                    RoundedRectangle(cornerRadius: cornerRadius)
                                        .stroke(Color.white.opacity(0.75), lineWidth: 1)
                                )
                                .scaleEffect(x: scale, y: scale, anchor: .center)
                            }
                            .frame(height: geometry.size.height)
                            .id(index)
                        }
                    }
                }
            }
        }
    }
}
