package app.dmvtestprep.core.config

import app.dmvtestprep.shared.BuildConfig

actual val AppInfo.Build.isDebug: Boolean
    get() = BuildConfig.DEBUG
actual val AppInfo.Build.apiVersion: String
    get() = BuildConfig.API_VERSION
actual val AppInfo.Build.apiBaseUrl: String
    get() = BuildConfig.API_BASE_URL
actual val AppInfo.Build.mediaBaseUrl: String
    get() = BuildConfig.MEDIA_BASE_URL

actual val AppInfo.Version.major: Int
    get() = BuildConfig.VERSION_MAJOR
actual val AppInfo.Version.minor: Int
    get() = BuildConfig.VERSION_MINOR
actual val AppInfo.Version.patch: Int
    get() = BuildConfig.VERSION_PATCH