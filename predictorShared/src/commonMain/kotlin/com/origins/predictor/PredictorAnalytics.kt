package com.origins.predictor

import com.origins.predictor.domain.game.models.GameEntity
import com.origins.predictor.domain.game.models.ScoreEntity

interface PredictorAnalytics {

    fun onPredictorOpened(game: GameEntity)
    fun onValidated(score: ScoreEntity, game: GameEntity)
    fun onModified(score: ScoreEntity, game: GameEntity)
}