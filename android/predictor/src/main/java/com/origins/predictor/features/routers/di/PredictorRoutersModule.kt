package com.origins.predictor.features.routers.di

import androidx.annotation.IdRes
import com.origins.predictor.R
import com.origins.predictor.base.navigation.routers.BaseRouter
import com.origins.predictor.features.routers.PredictorNavigationAppRouterImpl
import com.origins.predictor.features.routers.base.PredictorNavigationBaseRouter
import com.origins.predictor.features.routers.base.PredictorNavigationBaseRouterImpl
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue
import org.koin.dsl.module

internal val predictorRoutersModule
    get() = module {
        factory<PredictorNavigationBaseRouter> { PredictorNavigationBaseRouterImpl() }

        single<BaseRouter>(PredictorNavGraphQualifier(R.id.predictor_app_navigation)) {
            val baseRouter: PredictorNavigationBaseRouter = get()
            PredictorNavigationAppRouterImpl(baseRouter)
        }
    }

internal data class PredictorNavGraphQualifier(@IdRes val graphId: Int) : Qualifier {
    override val value: QualifierValue get() = graphId.toString()
}