package app.dmvtestprep.domain.model

sealed class FeedbackResponse(val message: String) {
    class Success(message: String) : FeedbackResponse(message)
    class Failure(message: String) : FeedbackResponse(message)
}