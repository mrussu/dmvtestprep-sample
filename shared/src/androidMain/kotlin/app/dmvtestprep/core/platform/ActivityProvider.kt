package app.dmvtestprep.core.platform

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

object ActivityProvider : Application.ActivityLifecycleCallbacks {
    private var current: WeakReference<Activity>? = null

    val currentActivity: Activity?
        get() = current?.get()

    fun init(app: Application) {
        app.registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityResumed(activity: Activity) {
        current = WeakReference(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (current?.get() === activity) {
            current = null
        }
    }

    override fun onActivityCreated(a: Activity, b: Bundle?) {}
    override fun onActivityStarted(a: Activity) {}
    override fun onActivityPaused(a: Activity) {}
    override fun onActivityStopped(a: Activity) {}
    override fun onActivitySaveInstanceState(a: Activity, outState: Bundle) {}
}