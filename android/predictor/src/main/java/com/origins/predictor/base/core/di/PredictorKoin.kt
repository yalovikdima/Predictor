package com.origins.predictor.base.core.di

import com.origins.predictor.di.PredictorDI
import com.origins.predictor.utils.PredictorConfig
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication

internal object PredictorKoin {
    private lateinit var koinApplication: KoinApplication

    val koin: Koin get() = koinApplication.koin

    fun init(config: PredictorConfig, delegate: KoinApplication.() -> Unit) {
        if (this::koinApplication.isInitialized) {
            val currentConfig = koinApplication.koin.get<PredictorDI>().commonDataDI.config
            if (currentConfig == config) {
                return
            }
        }

        koinApplication = koinApplication {
            delegate(this)
        }
    }
}