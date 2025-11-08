package app.dmvtestprep.core.platform

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName
    override val version: String = UIDevice.currentDevice.systemVersion
    override val manufacturer: String = "Apple"
    override val model: String = UIDevice.currentDevice.model
}

actual val platform: Platform = IOSPlatform()