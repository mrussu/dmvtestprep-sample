package app.dmvtestprep

import android.app.Application
import app.dmvtestprep.core.config.AppInfo
import app.dmvtestprep.core.config.initializeConfig
import app.dmvtestprep.core.platform.ActivityProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MainApplication : Application() {
    private val job = SupervisorJob()
    val applicationScope = CoroutineScope(job + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        ActivityProvider.init(this)

        applicationScope.launch {
            initializeConfig(AppInfo.buildContext)
        }
    }
}