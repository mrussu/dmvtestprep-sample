import SwiftUI

struct MaxErrorsSliderView: View {
    let label: String
    let total: Int
    let colorFor: (Double) -> Color
    let textFor: (Double) -> String
    
    @Binding
    var value: Double
    var range: ClosedRange<Double> {
        0...max(Double(total), 1.0)
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            HStack {
                Text(label)
                    .frame(maxWidth: .infinity, alignment: .leading)
                Text("\(Int(value)) out of \(total)")
                    .frame(alignment: .trailing)
            }
            .padding(.bottom, 20)
            Text(dynamicText)
                .font(.system(size: 12, weight: .medium, design: .rounded))
                .frame(maxWidth: .infinity, alignment: .center)
                .foregroundColor(dynamicColor)
                .transition(.opacity)
                .animation(.easeInOut, value: value)
            Slider(value: $value, in: range, step: 1)
                .frame(height: 48)
                .accentColor(dynamicColor)
        }
    }
    
    private var progress: Double {
        (value - range.lowerBound) / (range.upperBound - range.lowerBound)
    }
    private var dynamicColor: Color { colorFor(progress) }
    private var dynamicText: String { textFor(progress) }
}
