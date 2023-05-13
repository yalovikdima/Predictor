package com.origins.predictor.features.routers.base

import android.os.Bundle
import android.view.View
import androidx.annotation.NavigationRes
import androidx.navigation.fragment.NavHostFragment
import com.origins.predictor.base.core.di.PredictorKoinComponent
import com.origins.predictor.base.coreui.fragments.PredictorBackPressObserver
import com.origins.predictor.base.navigation.routers.BaseRouter
import com.origins.predictor.base.navigation.routers.BaseRouterHolder
import com.origins.predictor.features.routers.di.PredictorNavGraphQualifier
import org.koin.core.component.get

internal class PredictorNavHostFragment : NavHostFragment(), PredictorKoinComponent, BaseRouterHolder,
    PredictorBackPressObserver {

    override val navigationRouter: BaseRouter get() = get(PredictorNavGraphQualifier(navController.graph.id))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationRouter.setNavigatorContainerAndOwner(view, viewLifecycleOwner)
    }

    override fun onBackPressed(): Boolean {
        val fragment =
            (childFragmentManager.fragments.find { it is PredictorBackPressObserver } as? PredictorBackPressObserver)
        return if (fragment?.onBackPressed() == true) {
            true
        } else {
            navigationRouter.back()
        }
    }

    companion object {
        fun newInstance(@NavigationRes graphResId: Int): PredictorNavHostFragment {
            return newInstance(graphResId, null)
        }

        fun newInstance(
            @NavigationRes graphResId: Int,
            startDestinationArgs: Bundle?
        ): PredictorNavHostFragment {
            val originalNavHost = create(graphResId, startDestinationArgs)
            return PredictorNavHostFragment().apply {
                arguments = originalNavHost.arguments
            }
        }
    }
}