import SwiftUI
import Shared

struct SettingsView: View {
    @StateObject var viewModel = SettingsViewModel()
    @Environment(\.presentationMode) var presentationMode
    
    var body: some View {
        NavigationView {
            SettingsContent(
                uiState: viewModel.uiState,
                actions: SettingsActionsHandler(
                    onSelectLanguageCallback: viewModel.onSelectLanguage,
                    onEnglishModeCallback: viewModel.onToggleEnglishMode,
                    onAnswerPrefixCallback: viewModel.onAnswerPrefix,
                    onFeedbackViewCallback: viewModel.onFeedbackView,
                    onDocumentViewCallback: viewModel.onDocumentView
                )
            )
            .background(AppColors.shared.background.asColor())
            .navigationBarTitle(SettingsViews.SettingsView.shared.title, displayMode: .inline)
            .navigationBarItems(
                leading: Button(action: {
                    presentationMode.wrappedValue.dismiss()
                }) {
                    Image(systemName: "arrow.backward")
                }
            )
            .sheet(isPresented: Binding(
                get: { viewModel.uiState.views is SettingsViews.FeedbackView },
                set: { _ in viewModel.onSettingsView() }
            )) {
                if let feedbackView = viewModel.uiState.views as? SettingsViews.FeedbackView {
                    FeedbackView(
                        uiState: feedbackView.uiState,
                        onChange: viewModel.onMessageChange,
                        onSend: viewModel.onSendFeedback,
                        onClose: viewModel.onSettingsView
                    )
                }
            }
            .sheet(isPresented: Binding(
                get: { viewModel.uiState.views is SettingsViews.DocumentView },
                set: { _ in viewModel.onSettingsView() }
            )) {
                if let documentView = viewModel.uiState.views as? SettingsViews.DocumentView {
                    DocumentView(
                        uiState: documentView.uiState,
                        onClose: viewModel.onSettingsView
                    )
                }
            }
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
}

struct FeedbackSection: View {
    let onFeedbackView: () -> Void
    
    var body: some View {
        let texts = Texts.FeedbackSection.shared
        
        ZStack {
            RoundedRectangle(cornerRadius: 8)
                .fill(AppColors.shared.surface.asColor())
                .background(
                    RoundedRectangle(cornerRadius: 8)
                        .shadow(color: .black.opacity(0.25), radius: 4, x: 0, y: 2)
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 8)
                        .stroke(AppColors.shared.surface.asColor(alpha: 0.75), lineWidth: 1)
                )
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
                Button(action: onFeedbackView) {
                    Text(texts.BUTTON)
                        .font(.system(size: 14, weight: .medium))
                        .padding(.horizontal, 12)
                }
                .buttonStyle(FilledButtonStyle())
            }
            .padding()
        }
    }
}

struct SettingsContent: View {
    let uiState: SettingsState
    let actions: SettingsActionsHandler
    
    var body: some View {
        let englishMode = uiState.settings.englishMode.value.boolValue
        
        ScrollView {
            VStack(spacing: 20) {
                HStack {
                    Text("Test language")
                        .font(.headline)
                    Picker("Select test language", selection: Binding(
                        get: { uiState.language },
                        set: { language in actions.onSelectLanguage(language) }
                    )) {
                        if uiState.languages.isEmpty {
                            Text(uiState.languageLabel)
                                .tag(uiState.language)
                                .foregroundColor(.gray)
                        } else {
                            ForEach(uiState.languages, id: \.self) { language in
                                Text(language.getText(englishMode: englishMode))
                                    .tag(language)
                            }
                        }
                    }
                    .pickerStyle(WheelPickerStyle())
                    .frame(height: 100)
                    .disabled(uiState.languages.isEmpty)
                }
                SettingToggle(
                    setting: uiState.settings.englishMode,
                    onToggle: actions.onToggleEnglishMode,
                    labelIcon: englishMode ? "character.bubble.fill" : "character.bubble"
                )
                SettingToggle(
                    setting: uiState.settings.answerPrefix,
                    onToggle: actions.onAnswerPrefix
                )
                FeedbackSection(
                    onFeedbackView: actions.onFeedbackView
                )
                VStack {
                    ForEach(uiState.legalDocs, id: \.id) { legalDoc in
                        Button(action: {
                            actions.onDocumentView(legalDoc.id.toKotlinInt())
                        }) {
                            Text(legalDoc.title)
                                .font(.system(size: 14, weight: .medium))
                                .padding(8)
                                .frame(maxWidth: .infinity)
                                .multilineTextAlignment(.center)
                        }
                    }
                }
                Divider()
                Text(AppInfo.Version.shared.displayName)
                    .font(.subheadline)
                    .foregroundColor(.gray)
                    .frame(maxWidth: .infinity, alignment: .center)
                    .padding(.bottom, 4)
            }
            .padding()
        }
    }
}
