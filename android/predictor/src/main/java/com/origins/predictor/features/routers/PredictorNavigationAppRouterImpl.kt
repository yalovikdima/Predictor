package com.origins.predictor.features.routers

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.origins.predictor.features.routers.base.PredictorNavigationBaseRouter

internal class PredictorNavigationAppRouterImpl(
    private val baseRouter: PredictorNavigationBaseRouter
) : PredictorNavigationBaseRouter by baseRouter {

    override fun setNavigatorContainerAndOwner(view: View, lifecycleOwner: LifecycleOwner) {
        baseRouter.setNavigatorContainerAndOwner(view, lifecycleOwner)
    }

    override fun back(): Boolean = baseRouter.back()
}