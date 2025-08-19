import SwiftUI

struct ToggleEnglishModeButton: View {
    let isNativeAvailable: Bool
    let isEnglishMode: Bool
    let onToggleEnglishMode: () -> Void
    
    var body: some View {
        if isNativeAvailable {
            Button(action: {
                onToggleEnglishMode()
            }) {
                Image(systemName: isEnglishMode ? "character.bubble.fill" : "character.bubble")
            }
        } else {
            EmptyView()
        }
    }
}
