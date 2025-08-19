package app.dmvtestprep.domain.model

import app.dmvtestprep.repository.RepositoryProvider

object TestEventHandlerFactory {

    private val databaseRepository = RepositoryProvider.databaseRepository

    fun create(): TestEventHandler {
        return TestEventHandler(databaseRepository)
    }
}