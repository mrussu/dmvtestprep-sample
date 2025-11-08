package app.dmvtestprep.repository

import app.dmvtestprep.datasource.api.ApiProvider
import app.dmvtestprep.datasource.storage.createDatabase
import app.dmvtestprep.datasource.storage.settingsStorage

object RepositoryProvider {

    private val database by lazy { createDatabase() }

    val remoteRepository by lazy { RemoteRepository(ApiProvider.apiService) }
    val databaseRepository by lazy { DatabaseRepository(database) }
    val settingsRepository by lazy { SettingsRepository(settingsStorage) }

}