package com.origins.predictor.features.di

import android.app.Application
import com.origins.predictor.BuildConfig
import com.origins.predictor.PredictorAnalytics
import com.origins.predictor.base.core.di.PREDICTOR_DEV_VERSION_QUALIFIER
import com.origins.predictor.base.core.di.PredictorKoin
import com.origins.predictor.base.core.di.getPredictorCoreModule
import com.origins.predictor.features.prediction.di.predictionUiModule
import com.origins.predictor.features.routers.di.predictorRoutersModule
import com.origins.predictor.utils.PredictorConfig
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val predictorModule
    get() = module {
        factory(PREDICTOR_DEV_VERSION_QUALIFIER) {
            BuildConfig.BUILD_TYPE != "release"
        }
    }

internal fun predictorInitKoin(
    application: Application,
    config: PredictorConfig,
    analytics: PredictorAnalytics,
    isLogsEnabled: Boolean,
    qrCode: String?
) {
    PredictorKoin.init(config) {
        androidContext(application)
        modules(
            getPredictorCoreModule(
                config = config,
                analytics = analytics,
                isLogsEnabled = isLogsEnabled,
                qrCode = qrCode
            ),

            predictorModule,
            predictorRoutersModule,
            predictionUiModule
        )
    }
}