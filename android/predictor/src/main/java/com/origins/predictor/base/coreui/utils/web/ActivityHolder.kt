package com.origins.predictor.base.coreui.utils.web

import android.app.Activity
import android.app.Application
import android.os.Bundle

internal class ActivityHolder : Application.ActivityLifecycleCallbacks {
    var currentActivity: Activity? = null
    override fun onActivityPaused(activity: Activity) {
        currentActivity = null
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
}