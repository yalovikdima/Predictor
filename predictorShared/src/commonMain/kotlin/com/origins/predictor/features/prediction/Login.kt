package com.origins.predictor.features.prediction

import com.origins.kmm.gaba.base.cmd.GabaCmd
import com.origins.kmm.gaba.base.cmd.ResultDispatcher
import com.origins.predictor.base.PredictorLogger
import com.origins.predictor.domain.config.ContestantEntity
import com.origins.predictor.domain.config.PredictorConfigRepository
import com.origins.predictor.domain.game.models.ScoreEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class Login(
    private val contestant: ContestantEntity?,
    private val score: ScoreEntity?,
    private val predictorConfigRepository: PredictorConfigRepository,
    private val logger: PredictorLogger,
    private val isVisible: Boolean
) : GabaCmd<Input>() {
    override fun execute(scope: CoroutineScope, dispatcher: ResultDispatcher<Input>): Job {
        return scope.launch {
            predictorConfigRepository.storeContestant(contestant)
            score?.let {
                dispatcher(PredictionInput.Ui.ValidateAfterLogin(it, isVisible))
            }
            dispatcher(PredictionInput.Internal.HideLoginDialog)
            logger.logD(this, "login = is ok")
        }
    }
}
