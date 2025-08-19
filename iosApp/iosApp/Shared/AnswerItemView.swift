import SwiftUICore
import Shared

struct AnswerItemView: View {
    let answer: Question.Answer
    let englishMode: Bool
    let colorScheme: AnswerColorProvider.ColorScheme
    let onTap: () -> Void
    
    var body: some View {
        HStack(alignment: .top, spacing: 0) {
            if !answer.prefix.isEmpty {
                Text(answer.prefix)
            }
            Text(answer.getText(englishMode: englishMode))
        }
        .padding()
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(colorScheme.background.asColor())
        .foregroundColor(colorScheme.text.asColor())
        .cornerRadius(8)
        .overlay(
            RoundedRectangle(cornerRadius: 8)
                .inset(by: 1)
                .stroke(colorScheme.border.asColor(), lineWidth: 2)
        )
        .contentShape(Rectangle())
        .lineLimit(nil)
        .fixedSize(horizontal: false, vertical: true)
        .onTapGesture {
            onTap()
        }
    }
}
