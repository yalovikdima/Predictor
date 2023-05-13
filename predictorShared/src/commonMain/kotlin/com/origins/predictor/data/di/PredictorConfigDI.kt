package com.origins.predictor.data.di

import com.origins.predictor.data.config.PredictorConfigRepositoryImpl
import com.origins.predictor.di.PredictorCommonDataDI
import com.origins.predictor.domain.config.PredictorConfigRepository

class PredictorConfigDI internal constructor(
    private val commonDataDI: PredictorCommonDataDI
) {
    fun providePredictorConfigRepository(): PredictorConfigRepository {
        return PredictorConfigRepositoryImpl(
            predictorDataStorage = commonDataDI.predictorDataStorage
        )
    }
}