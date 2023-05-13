package com.origins.predictor.base.core.di

import org.koin.core.Koin
import org.koin.core.component.KoinComponent

interface PredictorKoinComponent : KoinComponent {

    override fun getKoin(): Koin {
        return PredictorKoin.koin
    }
}