package com.origins.predictor.domain.prediction

import com.origins.predictor.data.prediction.models.PredictionRequestModel
import com.origins.predictor.data.prediction.models.ScoreRequest

interface PredictionRepository {
    @Throws(Exception::class)
    suspend fun sendPrediction(prediction: PredictionRequestModel): PredictionEntity

    @Throws(Exception::class)
    suspend fun getPrediction(externalId: String, gameId: String?): PredictionEntity?

    @Throws(Exception::class)
    suspend fun updatePrediction(predictionId: String?, score: ScoreRequest): PredictionEntity
}