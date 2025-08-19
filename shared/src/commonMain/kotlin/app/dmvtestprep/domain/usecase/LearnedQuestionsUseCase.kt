package app.dmvtestprep.domain.usecase

import app.dmvtestprep.repository.DatabaseRepository

class LearnedQuestionsUseCase(val databaseRepository: DatabaseRepository)  {

    fun count(): Int {
        return databaseRepository.getLearnedQuestionsCount()
    }

    fun getIds(): Set<Int> {
        return databaseRepository.getLearnedQuestionsIds()
    }

}