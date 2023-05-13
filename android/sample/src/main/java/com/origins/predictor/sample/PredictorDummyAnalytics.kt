package com.origins.predictor.sample

import android.util.Log
import com.origins.predictor.PredictorAnalytics
import com.origins.predictor.domain.game.models.GameEntity
import com.origins.predictor.domain.game.models.ScoreEntity

class PredictorDummyAnalytics : PredictorAnalytics {
    override fun onPredictorOpened(game: GameEntity) {
        Log.i("onPredictorOpened", getMathName(game))
    }

    override fun onValidated(score: ScoreEntity, game: GameEntity) {
        Log.i("onValidated", "${getMathName(game)} === ${getScoreString(score)}", null)
    }

    override fun onModified(score: ScoreEntity, game: GameEntity) {
        Log.i("onModified", "${getMathName(game)} === ${getScoreString(score)}", null)
    }

    private fun getMathName(game: GameEntity): String {
        return "${game.homeTeamName} - ${game.awayTeamName}"
    }

    private fun getScoreString(score: ScoreEntity): String {
        return "${score.homeScore} : ${score.awayScore}"
    }

}