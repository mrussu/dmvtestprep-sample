package app.dmvtestprep.domain.model

sealed class ViewMode {
    data object MainView : ViewMode()
    data object DetailView : ViewMode()
}