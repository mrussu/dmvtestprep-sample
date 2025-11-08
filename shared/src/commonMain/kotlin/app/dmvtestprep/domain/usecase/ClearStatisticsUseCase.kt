package app.dmvtestprep.domain.usecase

import app.dmvtestprep.repository.DatabaseRepository

class ClearStatisticsUseCase(val databaseRepository: DatabaseRepository) {

    operator fun invoke() {
        databaseRepository.clearStatistics()
    }

}