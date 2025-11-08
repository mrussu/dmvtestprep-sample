import CoreGraphics
import SwiftUI

final class SegmentedControl: UISegmentedControl {
    var fontSize: CGFloat = 14 {
        didSet { applyFont() }
    }

    override init(items: [Any]?) {
        super.init(items: items)
        configure()
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        configure()
    }
    
    private func configure() {
        setContentHuggingPriority(.defaultLow, for: .vertical)
        selectedSegmentTintColor = .systemBlue
        backgroundColor = .secondarySystemBackground
        tintColor = .clear
        applyFont()
    }
    
    private func applyFont() {
        let normal: [NSAttributedString.Key: Any] = [
            .foregroundColor: UIColor.label,
            .font: UIFont.systemFont(ofSize: fontSize, weight: .regular)
        ]
        let selected: [NSAttributedString.Key: Any] = [
            .foregroundColor: UIColor.white,
            .font: UIFont.systemFont(ofSize: fontSize, weight: .medium)
        ]
        setTitleTextAttributes(normal, for: .normal)
        setTitleTextAttributes(selected, for: .selected)
    }
}

struct SegmentedPicker<Selection: Hashable>: UIViewRepresentable {
    @Binding var selection: Selection
    let items: [Selection]
    let title: (Selection) -> String
    let fontSize: CGFloat

    func makeUIView(context: Context) -> SegmentedControl {
        let control = SegmentedControl(items: items.map(title))
        control.fontSize = fontSize
        control.addTarget(context.coordinator, action: #selector(Coordinator.changed(_:)), for: .valueChanged)
        control.selectedSegmentIndex = index(of: selection)
        return control
    }

    func updateUIView(_ uiView: SegmentedControl, context: Context) {
        for (i, item) in items.enumerated() {
            uiView.setTitle(title(item), forSegmentAt: i)
        }
        uiView.fontSize = fontSize
        uiView.selectedSegmentIndex = index(of: selection)
    }

    func makeCoordinator() -> Coordinator { Coordinator(self) }

    private func index(of value: Selection) -> Int {
        items.firstIndex(of: value) ?? 0
    }

    final class Coordinator: NSObject {
        let parent: SegmentedPicker
        init(_ parent: SegmentedPicker) { self.parent = parent }
        @objc func changed(_ sender: UISegmentedControl) {
            let i = max(0, sender.selectedSegmentIndex)
            parent.selection = parent.items[i]
        }
    }
}

struct SegmentedContainer<Content: View>: View {
    let cornerRadius: CGFloat
    let content: () -> Content

    init(cornerRadius: CGFloat = 8, @ViewBuilder content: @escaping () -> Content) {
        self.cornerRadius = cornerRadius
        self.content = content
    }

    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: cornerRadius)
                .fill(Color(.systemBackground))
                .shadow(color: .black.opacity(0.25), radius: 4, x: 0, y: 2)
            content()
        }
    }
}
