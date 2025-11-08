import SwiftUI
import Shared

struct FeedbackView: View {
    let uiState: FeedbackState
    let onChange: (String) -> Void
    let onSend: () -> Void
    let onClose: () -> Void
    
    var body: some View {
        NavigationView {
            ZStack {
                AppColors.shared.background.asColor()
                    .ignoresSafeArea()
                
                switch uiState {
                case let messageState as FeedbackState.Message:
                    MessageContent(
                        message: messageState.value,
                        isAllowToSend: messageState.isAllowToSend,
                        onChange: onChange,
                        onSend: onSend
                    )
                case is FeedbackState.Sent:
                    FeedbackSent(
                        onClose: onClose
                    )
                case let errorState as FeedbackState.Error:
                    ErrorView(
                        errorSummary: errorState.errorSummary,
                        onRetry: errorState.onRetry
                    )
                default:
                    LoadingView()
                }
            }
            .navigationBarTitle(uiState.title, displayMode: .inline)
            .navigationBarItems(
                leading: Button(action: onClose) {
                    Image(systemName: "arrow.backward")
                }
            )
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
}

struct MessageContent: View {
    @State var message: String
    let isAllowToSend: Bool
    let onChange: (String) -> Void
    let onSend: () -> Void
    
    var body: some View {
        VStack(spacing: 16) {
            TextEditor(text: $message)
                .transparentScrolling()
                .padding(4)
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .fill(AppColors.shared.surface.asColor())
                        .overlay(
                            RoundedRectangle(cornerRadius: 8)
                                .stroke(Color(UIColor.separator), lineWidth: 1)
                        )
                )
                .cornerRadius(8)
                .onChange(of: message) { newValue in
                    onChange(newValue)
                }
            Button(action: onSend) {
                Text("Send message")
            }
            .buttonStyle(ActionButtonStyle(isDisabled: !isAllowToSend))
            .disabled(!isAllowToSend)
        }
        .padding()
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

struct FeedbackSent: View {
    let onClose: () -> Void
    
    var body: some View {
        let texts = Texts.FeedbackSent.shared
        
        VStack(alignment: .center, spacing: 12) {
            Text(texts.HEADER)
                .font(.system(size: 18, weight: .semibold))
                .multilineTextAlignment(.center)
                .frame(maxWidth: .infinity, alignment: .center)
            Text(texts.BODY)
                .font(.system(size: 16))
                .multilineTextAlignment(.center)
                .lineSpacing(6)
                .padding(.top, 16)
                .padding(.bottom, 32)
                .frame(maxWidth: .infinity)
            Button(action: onClose) {
                Text(texts.BUTTON)
                    .font(.system(size: 14, weight: .medium))
                    .padding(.horizontal, 12)
            }
            .buttonStyle(FilledButtonStyle())
        }
        .padding()
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}
