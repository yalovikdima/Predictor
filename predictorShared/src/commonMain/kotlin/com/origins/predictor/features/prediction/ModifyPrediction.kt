package com.origins.predictor.features.prediction

import com.origins.kmm.gaba.base.cmd.GabaCmd
import com.origins.kmm.gaba.base.cmd.ResultDispatcher
import com.origins.predictor.base.PredictorLogger
import com.origins.predictor.data.prediction.models.ScoreRequest
import com.origins.predictor.data.prediction.models.ScoreRequestModel
import com.origins.predictor.domain.game.models.ScoreEntity
import com.origins.predictor.domain.prediction.PredictionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class ModifyPrediction(
    private val score: ScoreEntity,
    private val predictionId: String,
    private val predictionRepository: PredictionRepository,
    private val logger: PredictorLogger
) : GabaCmd<Input>() {
    override fun execute(scope: CoroutineScope, dispatcher: ResultDispatcher<Input>): Job {
        return scope.launch {
            try {
                logger.logD(this, "putPrediction = start")
                val prediction = predictionRepository.updatePrediction(
                    predictionId = predictionId,
                    score = ScoreRequest(
                        ScoreRequestModel(
                            score.homeScore.toString(),
                            score.awayScore.toString()
                        )
                    )
                )
                logger.logD(this, "putPrediction = is ok")
                dispatcher.invoke(PredictionInput.Internal.Modified(prediction = prediction))
                dispatcher.invoke(PredictionInput.Internal.ShowModifiedButton)
            } catch (e: Exception) {
                logger.logD(this, "putPrediction = not ok", e)
                dispatcher.invoke(PredictionInput.Internal.LoadingFailed(e))
            }
        }
    }
}