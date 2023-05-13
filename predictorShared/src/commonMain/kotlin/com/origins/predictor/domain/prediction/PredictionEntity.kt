package com.origins.predictor.domain.prediction

import com.origins.predictor.domain.game.models.ScoreEntity

data class PredictionEntity(
    val predictionId: String?,
    val gameId: String,
    val score: ScoreEntity,
    val state: PredictorState
)

enum class PredictorState {
    PENDING,
    PENDING_LOST,
    PENDING_WIN,
    WIN,
    LOST
}