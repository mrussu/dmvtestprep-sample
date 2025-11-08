package app.dmvtestprep.core.platform

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android"
    override val version: String = Build.VERSION.SDK_INT.toString()
    override val manufacturer: String = Build.MANUFACTURER
    override val model: String = Build.MODEL
}

actual val platform: Platform = AndroidPlatform()