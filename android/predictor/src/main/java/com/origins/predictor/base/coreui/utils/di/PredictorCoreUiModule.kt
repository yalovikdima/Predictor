package com.origins.predictor.base.coreui.utils.di


import com.origins.predictor.base.coreui.utils.PredictorNotificationHelper
import com.origins.predictor.base.coreui.utils.web.ActivityHolder
import org.koin.dsl.module

 internal val predictorCoreUiModule
    get() = module {
        single { ActivityHolder() }
        single { PredictorNotificationHelper(get()) }
    }