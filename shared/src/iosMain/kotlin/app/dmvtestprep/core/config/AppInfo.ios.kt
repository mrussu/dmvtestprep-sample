package app.dmvtestprep.core.config

import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.boolValue
import platform.Foundation.intValue

actual val AppInfo.Build.isDebug: Boolean
    get() = with(NSBundle.mainBundle.objectForInfoDictionaryKey("IS_DEBUG")) {
        if (this is NSString) boolValue else false
    }
actual val AppInfo.Build.apiVersion: String
    get() = with(NSBundle.mainBundle.objectForInfoDictionaryKey("API_VERSION")) {
        if (this is NSString) toString() else ""
    }
actual val AppInfo.Build.apiBaseUrl: String
    get() = with(NSBundle.mainBundle.objectForInfoDictionaryKey("API_BASE_URL")) {
        if (this is NSString) toString() else ""
    }
actual val AppInfo.Build.mediaBaseUrl: String
    get() = with(NSBundle.mainBundle.objectForInfoDictionaryKey("MEDIA_BASE_URL")) {
        if (this is NSString) toString() else ""
    }

actual val AppInfo.Version.major: Int
    get() = with(NSBundle.mainBundle.objectForInfoDictionaryKey("VERSION_MAJOR")) {
        if (this is NSString) intValue else 0
    }
actual val AppInfo.Version.minor: Int
    get() = with(NSBundle.mainBundle.objectForInfoDictionaryKey("VERSION_MINOR")) {
        if (this is NSString) intValue else 0
    }
actual val AppInfo.Version.patch: Int
    get() = with(NSBundle.mainBundle.objectForInfoDictionaryKey("VERSION_PATCH")) {
        if (this is NSString) intValue else 0
    }