import SwiftUI
import Shared

struct QuestionsView: View {
    let uiState: LearnState.Learn
    let onSetFilter: (QuestionFilter) -> Void
    let onToggleSave: (Int) -> Void
    let onQuestionItemClick: (Int) -> Void
    
    var questions: [Question] {
        uiState.questions.toList()
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            SegmentedPicker(
                selection: Binding(
                    get: { uiState.filter },
                    set: { filter in onSetFilter(filter) }
                ),
                items: QuestionFilter.allCases,
                title: \.filterName,
                fontSize: 14
            )
            .frame(height: 30)
            .padding(.bottom, 10)
            LazyVStack(alignment: .leading, spacing: 20) {
                ForEach(questions.indices, id: \.self) { index in
                    QuestionItem(
                        question: questions[index],
                        englishMode: uiState.englishMode,
                        onToggleSave: onToggleSave
                    )
                    .onTapGesture {
                        onQuestionItemClick(index)
                    }
                }
                .frame(maxWidth: .infinity)
            }
        }
        .frame(maxWidth: .infinity)
        .padding()
    }
}

struct QuestionItem: View {
    let question: Question
    let englishMode: Bool
    let onToggleSave: (Int) -> Void
    
    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 8)
                .fill(question.backgroundColor)
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .fill(AppColors.shared.surface.asColor())
                        .shadow(color: .black.opacity(0.25), radius: 4, x: 0, y: 2)
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(AppColors.shared.surface.asColor(alpha: 0.75), lineWidth: 1)
                )
            VStack(alignment: .center, spacing: 12) {
                HStack(alignment: .center) {
                    Text(question.lsLabel)
                        .font(.system(size: 14, weight: .medium))
                        .foregroundColor(question.lsTextColor)
                        .padding(.vertical, 4)
                        .padding(.horizontal, 6)
                        .background(question.lsBackgroundColor)
                        .cornerRadius(6)
                    Spacer()
                    ToggleSaveButton(
                        questionId: question.id.toInt(),
                        isSaved: question.isSaved,
                        onToggleSave: onToggleSave
                    )
                }
                HStack(spacing: 12) {
                    Text(question.getText(englishMode: englishMode))
                        .font(.system(size: 16))
                        .lineLimit(3)
                        .multilineTextAlignment(.leading)
                        .truncationMode(.tail)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    
                    if let url = question.imageUrl {
                        AsyncImageView(url: url, size: 60, maxWidth: 60)
                    }
                }
                .frame(height: 60)
            }
            .padding()
        }
    }
}
