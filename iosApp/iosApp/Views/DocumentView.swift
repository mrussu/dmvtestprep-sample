import SwiftUI
import Shared

struct DocumentView: View {
    let uiState: DocumentState
    let onClose: () -> Void
    
    var body: some View {
        NavigationView {
            ZStack {
                switch uiState {
                case let document as DocumentState.Document:
                    DocumentContent(document: document)
                case let errorState as DocumentState.Error:
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

struct DocumentContent: View {
    let document: DocumentState.Document
    
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                ForEach(document.content.indices, id: \.self) { index in
                    let line = document.content[index]
                    
                    switch line {
                    case let header as MarkdownLine.Header:
                        Text(header.text)
                            .font(.system(size: CGFloat(22 - header.level * 2), weight: .medium))
                            .frame(maxWidth: .infinity, alignment: .leading)
                            .padding(.vertical, 8)
                    case let listItem as MarkdownLine.ListItem:
                        HStack(alignment: .top, spacing: 6) {
                            Text(listItem.marker)
                                .fontWeight(.medium)
                            Text(listItem.segments.toAttributedString())
                        }
                        .padding(.leading, 8)
                    case let paragraph as MarkdownLine.Paragraph:
                        Text(paragraph.segments.toAttributedString())
                            .padding(.bottom, 8)
                    default:
                        EmptyView()
                    }
                }
            }
            .textSelection(.enabled)
            .padding()
        }
    }
}
