package com.origins.predictor.base.navigation.routers

import android.view.View
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

interface BaseRouter : LifecycleObserver {

    fun setNavigatorContainerAndOwner(view: View, lifecycleOwner: LifecycleOwner)

    fun back(): Boolean
}