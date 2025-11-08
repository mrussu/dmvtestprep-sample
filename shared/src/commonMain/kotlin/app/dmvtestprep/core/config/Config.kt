package app.dmvtestprep.core.config

import app.dmvtestprep.domain.model.BuildContext
import app.dmvtestprep.domain.model.Config
import app.dmvtestprep.domain.usecase.UseCaseProvider

expect fun getDebugConfig(platformName: String): Config

suspend fun initializeConfig(buildContext: BuildContext) {
    runCatching {
        UseCaseProvider.getConfigUseCase(
            isDebug = buildContext.isDebug,
            platformName = buildContext.platformName
        )
    }.onSuccess { config ->
        // Apply the retrieved configuration
    }
}