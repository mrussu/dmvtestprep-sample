import SwiftUI

struct ActionButtonStyle: ButtonStyle {
    var cornerRadius: CGFloat = 8
    var isDisabled: Bool = false
    
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .padding()
            .frame(maxWidth: .infinity)
            .background(isDisabled ? Color(UIColor.systemGray2) : (configuration.isPressed ? .blue.opacity(0.7) : .blue))
            .foregroundColor(.white)
            .cornerRadius(cornerRadius)
            .shadow(radius: isDisabled ? 0 : (configuration.isPressed ? 2 : 8))
            .opacity(isDisabled ? 0.5 : (configuration.isPressed ? 0.8 : 1.0))
            .font(.system(size: 16, weight: .medium))
    }
}

struct FilledButtonStyle: ButtonStyle {
    var cornerRadius: CGFloat = 6
    var isDisabled: Bool = false
    
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .padding()
            .background(isDisabled ? Color(UIColor.systemGray3) : (configuration.isPressed ? .blue.opacity(0.7) : .blue))
            .foregroundColor(.white)
            .cornerRadius(cornerRadius)
            .shadow(radius: isDisabled ? 0 : (configuration.isPressed ? 2 : 8))
            .opacity(isDisabled ? 0.5 : (configuration.isPressed ? 0.8 : 1.0))
    }
}
