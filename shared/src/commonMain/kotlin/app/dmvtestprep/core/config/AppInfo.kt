package app.dmvtestprep.core.config

import app.dmvtestprep.core.platform.platform
import app.dmvtestprep.domain.model.BuildContext
import app.dmvtestprep.domain.model.UsageContext

object AppInfo {
    val usageContext by lazy {
        UsageContext(
            appVersion = Version.name,
            systemInfo = platform.systemInfo(),
            deviceInfo = platform.deviceInfo()
        )
    }
    val buildContext by lazy {
        BuildContext(
            isDebug = Build.isDebug,
            platformName = platform.name
        )
    }

    object Build

    object Version {
        val name get() = "$major.$minor.$patch"
        val displayName get() = if (Build.isDebug) "Debug version $name" else "Version $name"
    }
}

expect val AppInfo.Build.isDebug: Boolean
expect val AppInfo.Build.apiVersion: String
expect val AppInfo.Build.apiBaseUrl: String
expect val AppInfo.Build.mediaBaseUrl: String

expect val AppInfo.Version.major: Int
expect val AppInfo.Version.minor: Int
expect val AppInfo.Version.patch: Int