package app.dmvtestprep.core.config

import app.dmvtestprep.domain.model.Config

actual fun getDebugConfig(platformName: String): Config {
    return Config(
        platformName = platformName
    )
}