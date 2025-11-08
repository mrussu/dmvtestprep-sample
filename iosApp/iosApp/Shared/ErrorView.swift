import SwiftUI
import Shared

struct ErrorView: View {
    let errorSummary: ErrorSummary
    let onRetry: () -> Void
    
    var body: some View {
        ZStack {
            VStack(spacing: 0) {
                Text(errorSummary.error)
                    .font(.system(size: 18, weight: .medium))
                    .multilineTextAlignment(.center)
                Spacer().frame(height: 12)
                Text(errorSummary.details)
                    .font(.system(size: 16))
                    .multilineTextAlignment(.center)
                Spacer().frame(height: 24)
                Button(action: onRetry) {
                    Text("Retry")
                        .font(.system(size: 14, weight: .medium))
                        .padding(.horizontal, 14)
                }
                .buttonStyle(FilledButtonStyle())
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .center)
        .padding()
    }
}
