package com.origins.predictor.features.prediction

import com.origins.kmm.gaba.base.cmd.GabaCmd
import com.origins.kmm.gaba.base.cmd.ResultDispatcher
import com.origins.predictor.base.PredictorLogger
import com.origins.predictor.domain.config.PredictorConfigRepository
import com.origins.predictor.domain.game.GameRepository
import com.origins.predictor.domain.game.models.CombineDataInfo
import com.origins.predictor.domain.game.models.GameState
import com.origins.predictor.domain.prediction.PredictionEntity
import com.origins.predictor.domain.prediction.PredictionRepository
import com.origins.predictor.domain.prediction.PredictorState
import com.soywiz.klock.DateTime
import kotlinx.coroutines.*
import kotlin.Exception as Exception1

internal class LoadData(
    private val matchId: String?,
    private val gameRepository: GameRepository,
    private val predictionRepository: PredictionRepository,
    private val predictorConfigRepository: PredictorConfigRepository,
    private val logger: PredictorLogger
) : GabaCmd<Input>() {
    override fun execute(scope: CoroutineScope, dispatcher: ResultDispatcher<Input>): Job {
        return scope.launch {
            var stopAutoRefresh = false
            logger.logD(this, "loadData = start")
            while (isActive && !stopAutoRefresh) {
                try {
                    if (matchId != null) {
                        val game = gameRepository.getGame(matchId)
                        if (game != null) {
                            var prediction: PredictionEntity? = null
                            predictorConfigRepository.getContestant()?.externalId?.let { id ->
                                if (id.isNotEmpty() && game.id.isNotEmpty()) {
                                    prediction = predictionRepository.getPrediction(
                                        externalId = id,
                                        gameId = game.id
                                    )
                                } else {
                                    logger.logD(this, "externalId = is null or empty")
                                }
                            }
                            val result =
                                CombineDataInfo(
                                    game = game,
                                    prediction = prediction
                                )
                            stopAutoRefresh = isStopAutoRefresh(result)
                            logger.logD(this, "loadData = is ok")
                            dispatcher.invoke(calculateScreen(result))
                        } else {
                            dispatcher.invoke(PredictionInput.Internal.NotAvailable)
                        }
                    } else {
                        logger.logD(this, "loadData = matchId is null")
                        throw  IllegalStateException("loadData = matchId is null")
                    }
                } catch (exception: Exception1) {
                    logger.logD(this, "loadData = not ok", exception)
                    dispatcher.invoke(PredictionInput.Internal.LoadingFailed(exception))
                }
                delay(REFRESH_INTERVAL)
            }
        }
    }

    private fun isStopAutoRefresh(screen: CombineDataInfo): Boolean {
        return screen.prediction?.state == PredictorState.LOST
                || screen.prediction?.state == PredictorState.WIN
                || screen.game?.state == GameState.ARCHIVED
    }

    private fun calculateScreen(data: CombineDataInfo): Input {
        if (data.game == null) {
            return PredictionInput.Internal.NotAvailable
        }
        val startTime = data.game.startDate - DateTime.now().unixMillis
        val endTime = data.game.endDate - DateTime.now().unixMillis

        return when (data.game.state) {
            GameState.ACTIVATED -> {
                if (startTime > 0L) {
                    PredictionInput.Internal.NotAvailable
                } else if (endTime > 0L && startTime <= 0L) {
                    PredictionInput.Internal.Game(data)
                } else {
                    if (data.prediction == null) {
                        PredictionInput.Internal.TooLate(data.game)
                    } else {
                        PredictionInput.Internal.Result(data)
                    }
                }
            }
            GameState.ARCHIVED -> {
                if (data.prediction != null) {
                    PredictionInput.Internal.Result(data)
                } else {
                    PredictionInput.Internal.TooLate(data.game)
                }
            }
            GameState.DRAFT -> {
                PredictionInput.Internal.NotAvailable
            }
        }
    }


    companion object {
        private const val REFRESH_INTERVAL = 30_000L
    }

}