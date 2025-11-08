import SwiftUICore

struct QuestionImageView: View {
    let imageUrl: String?
    
    var body: some View {
        if let url = imageUrl {
            AsyncImageView(url: url, size: 160, maxWidth: .infinity)
                .padding(12)
            Spacer().frame(minHeight: 12)
        } else {
            Spacer().frame(height: 12)
        }
    }
}
