package app.dmvtestprep.domain.usecase

import app.dmvtestprep.repository.DatabaseRepository

class ToggleSavedQuestionUseCase(private val databaseRepository: DatabaseRepository) {

    fun toggle(questionId: Int): Boolean {
        return databaseRepository.toggleSavedQuestion(questionId)
    }

}