package app.dmvtestprep.domain.model

enum class QuestionFilter(val filterName: String) {
    ALL("All"),
    LEARNING("Learning"),
    LEARNED("Learned"),
    SAVED("Saved")
}