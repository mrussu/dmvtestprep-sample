package app.dmvtestprep.domain.usecase

import app.dmvtestprep.domain.model.Feedback
import app.dmvtestprep.domain.model.FeedbackResponse
import app.dmvtestprep.domain.utils.AppInfo
import app.dmvtestprep.repository.RemoteRepository

class SendFeedbackUseCase(private val remoteRepository: RemoteRepository) {

    suspend operator fun invoke(message: String): FeedbackResponse {
        val feedback = Feedback(message)
        val usageContext = AppInfo.usageContext

        return remoteRepository.sendFeedback(feedback, usageContext)
    }

}