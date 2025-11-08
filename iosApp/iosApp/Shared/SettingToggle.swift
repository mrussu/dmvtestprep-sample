import SwiftUI
import Shared

struct SettingToggle: View {
    let setting: Setting<KotlinBoolean>
    let onToggle: (KotlinBoolean) -> Void
    let labelIcon: String?
    
    init(
        setting: Setting<KotlinBoolean>,
        onToggle: @escaping (KotlinBoolean) -> Void,
        labelIcon: String? = nil
    ) {
        self.setting = setting
        self.onToggle = onToggle
        self.labelIcon = labelIcon
    }

    var body: some View {
        HStack(alignment: .top, spacing: 8) {
            VStack(alignment: .leading, spacing: 0) {
                HStack(alignment: .center, spacing: 8) {
                    Text(setting.label)
                        .frame(minHeight: 32)
                        .font(.headline)
                    if let labelIcon, UIImage(systemName: labelIcon) != nil {
                        Image(systemName: labelIcon)
                    }
                }
                if !setting.details.isEmpty {
                    Text(setting.details)
                        .font(.subheadline)
                        .foregroundColor(.gray)
                }
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            Toggle("", isOn: Binding(
                get: { setting.value.boolValue },
                set: { newValue in
                    onToggle(KotlinBoolean(value: newValue))
                }
            ))
            .labelsHidden()
            .disabled(!setting.isEnabled)
        }
        .frame(maxWidth: .infinity)
    }
}
