import SwiftUI
import Shared

struct AppNavigation: View {
    @State private var selectedNavTab: NavTab = .test
    private let learnViewModel = LearnViewModel()
    private let testConfigViewModel = TestConfigViewModel()
    private let statisticsViewModel = StatisticsViewModel()

    var body: some View {
        TabView(selection: $selectedNavTab) {
            ForEach(NavTab.allCases, id: \.self) { navTab in
                getView(for: navTab)
                    .tabItem {
                        getNavTabIcon(navTab: navTab)
                        Text(navTab.title)
                    }
                    .tag(navTab)
            }
        }
        .onAppear {
            selectedNavTab = UseCaseProvider().getSelectedNavTabUseCase.invoke()
        }
        .onChange(of: selectedNavTab) { newValue in
            UseCaseProvider().saveSelectedNavTabUseCase.invoke(navTab: newValue)
            if newValue == .learn {
                if !learnViewModel.uiState.isProcessing {
                    learnViewModel.loadQuestions()
                }
            }
            if newValue == .stats {
                if !statisticsViewModel.uiState.isProcessing {
                    statisticsViewModel.loadStatistics()
                }
            }
        }
    }

    private func getNavTabIcon(navTab: NavTab) -> Image {
        return switch navTab {
        case .learn: Image(systemName: "book.fill")
        case .test: Image(systemName: "square.text.square.fill")
        case .stats: Image(systemName: "chart.bar.horizontal.page.fill")
        }
    }

    @ViewBuilder
    private func getView(for navTab: NavTab) -> some View {
        switch navTab {
        case .learn: LearnView(viewModel: learnViewModel)
        case .test: TestConfigView(viewModel: testConfigViewModel)
        case .stats: StatisticsView(viewModel: statisticsViewModel)
        }
    }
}
