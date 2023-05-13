package com.origins.predictor.data.di

import com.origins.predictor.data.prediction.PredictionMapper
import com.origins.predictor.data.prediction.PredictionRepositoryImpl
import com.origins.predictor.di.PredictorCommonDataDI
import com.origins.predictor.domain.prediction.PredictionRepository

class PredictionDataDI internal constructor(
    private val commonDataDI: PredictorCommonDataDI
) {

    private fun providePredictionDataMapper(): PredictionMapper = PredictionMapper()

    fun providePredictionRepository(): PredictionRepository {
        return PredictionRepositoryImpl(
            httpClient = commonDataDI.provideHttpClient(),
            predictionMapper = providePredictionDataMapper()
        )
    }
}