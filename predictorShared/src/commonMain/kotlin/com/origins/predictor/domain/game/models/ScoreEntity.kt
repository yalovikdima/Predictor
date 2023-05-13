package com.origins.predictor.domain.game.models

import com.origins.predictor.base.PredictorSerializable

data class ScoreEntity(
    val homeScore: Int,
    val awayScore: Int
) : PredictorSerializable