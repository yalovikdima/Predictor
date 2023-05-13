package com.origins.predictor.data.prediction

import com.origins.predictor.data.prediction.models.PredictionResponse
import com.origins.predictor.domain.game.models.ScoreEntity
import com.origins.predictor.domain.prediction.PredictionEntity
import com.origins.predictor.domain.prediction.PredictorState

internal class PredictionMapper {

   internal fun mapFrom(response: PredictionResponse): PredictionEntity {
        return PredictionEntity(
            predictionId = response.predictionId,
            gameId = response.gameId.orEmpty(),
            score = ScoreEntity(response.score?.homeTeamScore ?: 0, response.score?.awayTeamScore ?: 0),
            state = when (response.state) {
                PENDING_LOST -> PredictorState.PENDING_LOST
                PENDING_WIN -> PredictorState.PENDING_WIN
                WIN -> PredictorState.WIN
                LOST -> PredictorState.LOST
                else -> PredictorState.PENDING
            }
        )
    }

    companion object {
        const val PENDING_LOST = "PENDING_LOST"
        const val PENDING_WIN = "PENDING_WIN"
        const val WIN = "WIN"
        const val LOST = "LOST"
    }
}