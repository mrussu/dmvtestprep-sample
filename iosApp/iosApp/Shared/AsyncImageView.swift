import SwiftUI

struct AsyncImageView: View {
    let url: String
    let size: CGFloat
    let maxWidth: CGFloat
    
    var body: some View {
        AsyncImage(url: URL(string: url)) { phase in
            switch phase {
            case .success(let image):
                image
                    .resizable()
                    .aspectRatio(contentMode: .fit)
            case .failure:
                Image(systemName: "photo.badge.exclamationmark")
                    .resizable()
                    .scaledToFit()
                    .frame(width: 48, height: 48)
                    .foregroundColor(.gray)
                    .accessibilityLabel("Image loading error")
            case .empty:
                ProgressView()
                    .frame(maxWidth: .infinity, maxHeight: size)
            @unknown default:
                EmptyView()
            }
        }
        .frame(maxWidth: maxWidth)
        .frame(height: size)
        .clipped()
    }
}
