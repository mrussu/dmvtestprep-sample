import SwiftUI
import Shared

struct StatisticsView: View {
    @ObservedObject var viewModel: StatisticsViewModel
    @State private var showClearConfirmation = false
    
    var body: some View {
        NavigationView {
            ZStack {
                AppColors.shared.background.asColor()
                    .ignoresSafeArea()
                
                switch viewModel.uiState {
                case let statisticsState as StatisticsState.Statistics:
                    SectionsView(
                        sections: statisticsState.sections,
                        onClearStatistics: {
                            showClearConfirmation.toggle()
                        }
                    )
                    .alert(isPresented: $showClearConfirmation) {
                        Alert(
                            title: Text("Confirmation"),
                            message: Text("Are you sure you want to clear all statistics?"),
                            primaryButton: .destructive(Text("Clear")) {
                                viewModel.onClearStatistics()
                                viewModel.loadStatistics()
                            },
                            secondaryButton: .cancel()
                        )
                    }
                case let errorState as StatisticsState.Error:
                    ErrorView(
                        errorSummary: errorState.errorSummary,
                        onRetry: errorState.onRetry
                    )
                default:
                    LoadingView()
                }
            }
            .navigationBarTitle(viewModel.uiState.title)
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
}

struct SectionsView: View {
    let sections: [StatisticsSection]
    let onClearStatistics: () -> Void
    
    var body: some View {
        ScrollView {
            VStack(spacing: 20) {
                ForEach(sections, id: \.header) { section in
                    StatisticsSectionView(header: section.header, rows: section.rows)
                }
                Button(action: {
                    onClearStatistics()
                }) {
                    Text("Clear statistics")
                }
                .buttonStyle(ActionButtonStyle())
            }
            .padding()
            .padding(.vertical, 8)
        }
    }
}

struct StatisticsSectionView: View {
    var header: String
    var rows: [StatisticsSection.StatisticRow]
    
    var body: some View {
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
            VStack(alignment: .leading, spacing: 8) {
                Text(header)
                    .font(.headline)
                    .frame(maxWidth: .infinity)
                Divider()
                    .frame(height: 1)
                    .padding(.vertical, 6)
                ForEach(rows, id: \.self) { row in
                    HStack(alignment: .bottom) {
                        Text(row.label)
                            .lineLimit(1)
                            .truncationMode(.tail)
                            .layoutPriority(1)
                        Spacer()
                            .frame(height: 1)
                            .frame(maxWidth: .infinity)
                            .background(AppColors.shared.onSurface.asColor(alpha: 0.1))
                        Text(row.value)
                            .font(.system(size: 14, weight: .medium))
                            .foregroundColor(row.color.asColor())
                            .layoutPriority(1)
                            .lineLimit(1)
                    }
                }
            }
            .padding()
        }
    }
}
