package app.dmvtestprep.domain.usecase

import app.dmvtestprep.core.config.getDebugConfig
import app.dmvtestprep.domain.model.Config
import app.dmvtestprep.repository.ApiRepository

class GetConfigUseCase(private val remoteRepository: ApiRepository) {

    suspend operator fun invoke(isDebug: Boolean, platformName: String): Config {
        return if (isDebug) fetchDebugConfig(platformName) else fetchReleaseConfig(platformName)
    }

    private suspend fun fetchReleaseConfig(platformName: String): Config {
        return remoteRepository.getConfig(platformName)
    }

    private fun fetchDebugConfig(platformName: String): Config = getDebugConfig(platformName)
}