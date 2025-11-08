import UIKit
import SwiftUICore
import Shared

extension Question {
    var lsLabel: String {
        num.formatted(suffix: learningStage.type.value)
    }
    var lsTextColor: Color {
        learningStage.getTextColor().asColor()
    }
    var lsBackgroundColor: Color {
        learningStage.getBackgroundColor().asColor()
    }
    var backgroundColor: Color {
        learningStage.getBackgroundColor().asColor(alpha: 0.1)
    }
    var imageUrl: String? {
        image.toFullImageUrl()
    }
}

extension ThemeColor {
    func asColor(alpha: Float? = nil) -> Color {
        Color(uiColor: UIColor { trait in
            let colorModel = (trait.userInterfaceStyle == .dark ? self.dark : self.light)
            
            return UIColor(
                red: CGFloat(colorModel.r),
                green: CGFloat(colorModel.g),
                blue: CGFloat(colorModel.b),
                alpha: CGFloat(alpha ?? self.alpha)
            )
        })
    }
}

extension [MarkdownText] {
    func toAttributedString() -> AttributedString {
        var attributedString = AttributedString()

        for text in self {
            var attrText = AttributedString(text.text)

            switch text {
            case is MarkdownText.Bold:
                attrText.font = .boldSystemFont(ofSize: 16)
            case is MarkdownText.Italic:
                attrText.font = .italicSystemFont(ofSize: 16)
            case let link as MarkdownText.Link:
                attrText.font = .system(size: 16)
                attrText.foregroundColor = link.color.asColor()
                attrText.underlineStyle = Text.LineStyle.single
                attrText.link = URL(string: link.url)
            default:
                break
            }

            attributedString.append(attrText)
        }
        
        return attributedString
    }
}

extension Int {
    func toInt32() -> Int32 {
        return Int32(self)
    }
}

extension Int32 {
    func toInt() -> Int {
        return Int(self)
    }
    func toKotlinInt() -> KotlinInt {
        return KotlinInt(int: self)
    }
}

extension String? {
    func toFullImageUrl() -> String? {
        return ApiConfig.shared.getFullImageUrl(path: self)
    }
}

public extension View {
    func transparentScrolling() -> some View {
        modifier(TransparentScrollingModifier())
    }
}

private struct TransparentScrollingModifier: ViewModifier {
    func body(content: Content) -> some View {
        if #available(iOS 16.0, *) {
            content.scrollContentBackground(.hidden)
        } else {
            content.onAppear {
                UITextView.appearance().backgroundColor = .clear
            }
            .onDisappear {
                UITextView.appearance().backgroundColor = nil
            }
        }
    }
}