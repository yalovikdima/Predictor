package com.origins.predictor.domain.game.models

import com.origins.predictor.domain.prediction.PredictionEntity

data class CombineDataInfo(
    val game: GameEntity?,
    val prediction: PredictionEntity?
)
