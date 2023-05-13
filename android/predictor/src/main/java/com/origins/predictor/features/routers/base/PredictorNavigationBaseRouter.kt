package com.origins.predictor.features.routers.base

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.origins.predictor.base.core.logger.predictorLog
import com.origins.predictor.base.navigation.routers.BaseRouter

internal interface PredictorNavigationBaseRouter : BaseRouter {

    var view: View?

    fun <T> requireNavigate(delegate: NavController.() -> T): T? {
        return view?.findNavController()?.let { delegate(it) }
    }

    fun <T> safeNavigate(delegate: NavController.() -> T): T? {
        return view?.findNavController()?.predictorSafeNavigate(delegate)
    }

    override fun setNavigatorContainerAndOwner(view: View, lifecycleOwner: LifecycleOwner) {
        this.view = view
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        view = null
    }

    override fun back(): Boolean {
        return safeNavigate { popBackStack() } ?: false
    }
}

fun <T> NavController.predictorSafeNavigate(delegate: NavController.() -> T): T? {
    return try {
        delegate(this)
    } catch (ignore: Exception) {
        predictorLog(tag = PredictorNavigationBaseRouter::class.java.toString(), error = ignore)
        null
    }
}
