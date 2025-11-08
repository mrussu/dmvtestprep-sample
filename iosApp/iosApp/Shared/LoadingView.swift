import SwiftUI
import Shared

struct LoadingView: View {
    var body: some View {
        ProgressView()
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .background(AppColors.shared.background.asColor())
            .ignoresSafeArea()
    }
}
