import SwiftUI

struct ToggleSaveButton: View {
    let questionId: Int
    let isSaved: Bool
    let onToggleSave: (Int) -> Void
    
    var body: some View {
        Button(action: {
            onToggleSave(questionId)
        }) {
            Image(systemName: isSaved ? "bookmark.fill" : "bookmark")
        }
    }
}
