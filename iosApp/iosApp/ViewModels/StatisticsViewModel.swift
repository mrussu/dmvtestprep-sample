import Foundation
import Shared

final class StatisticsViewModel: ObservableObject {
    @Published var uiState: StatisticsState = .Loading()
    
    private var sharedViewModel: Shared.StatisticsViewModel
    
    init() {
        self.sharedViewModel = Shared.StatisticsViewModel(
            statisticsFactory: UseCaseProvider().statisticsFactory,
            clearStatisticsUseCase: UseCaseProvider().clearStatisticsUseCase
        )
        Task { await startObservingState() }
        Task { await loadStatistics() }
    }
    
    @MainActor
    func startObservingState() async {
        for await state in sharedViewModel.uiState {
            self.uiState = state
        }
    }
    
    @MainActor
    func loadStatistics() {
        sharedViewModel.loadStatistics()
    }
    
    func onClearStatistics() {
        sharedViewModel.onClearStatistics()
    }
    
    deinit {
        sharedViewModel.clear()
    }
}
