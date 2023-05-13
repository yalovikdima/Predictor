package com.origins.predictor.features.prediction

import com.origins.kmm.gaba.base.cmd.GabaCmd
import com.origins.kmm.gaba.base.cmd.ResultDispatcher
import com.origins.predictor.base.PredictorLogger
import com.origins.predictor.data.prediction.models.ContestantRequestModel
import com.origins.predictor.data.prediction.models.PredictionRequestModel
import com.origins.predictor.data.prediction.models.ScoreRequestModel
import com.origins.predictor.domain.config.PredictorConfigRepository
import com.origins.predictor.domain.game.models.ScoreEntity
import com.origins.predictor.domain.prediction.PredictionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class SendPrediction(
    private val gameId: String,
    private val score: ScoreEntity,
    private val predictionRepository: PredictionRepository,
    private val predictorConfigRepository: PredictorConfigRepository,
    private val logger: PredictorLogger
) : GabaCmd<Input>() {
    override fun execute(scope: CoroutineScope, dispatcher: ResultDispatcher<Input>): Job {
        return scope.launch {
            try {
                val contestant = predictorConfigRepository.getContestant()
                if (contestant?.externalId != null) {
                    logger.logD(this, "externalId = ${contestant.externalId}")
                    val prediction = predictionRepository.sendPrediction(
                        PredictionRequestModel(
                            gameId = gameId,
                            contestant = ContestantRequestModel(
                                externalId = contestant.externalId,
                                firstName = contestant.firstName,
                                lastName = contestant.lastName
                            ),
                            score = ScoreRequestModel(
                                homeTeam = score.homeScore.toString(),
                                awayTeam = score.awayScore.toString()
                            )
                        )
                    )
                    logger.logD(this, "postPrediction = is ok")
                    dispatcher.invoke(PredictionInput.Internal.Validated(prediction))
                } else {
                    logger.logD(this, "postPrediction = redirect to login")
                    dispatcher.invoke(PredictionInput.Internal.ShowLogin(score))
                }
            } catch (e: Exception) {
                logger.logD(this, "postPrediction = not ok", e)
                dispatcher.invoke(PredictionInput.Internal.LoadingFailed(e))
            }
        }
    }
}