package com.origins.predictor.features.prediction

import com.origins.kmm.gaba.base.cmd.GabaCmd
import com.origins.kmm.gaba.base.cmd.ResultDispatcher
import com.origins.predictor.base.PredictorLogger
import com.origins.predictor.domain.config.PredictorConfigRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class Logout(
    private val predictorConfigRepository: PredictorConfigRepository,
    private val logger: PredictorLogger
) : GabaCmd<Input>() {
    override fun execute(scope: CoroutineScope, dispatcher: ResultDispatcher<Input>): Job {
        return scope.launch {
            predictorConfigRepository.storeContestant(null)
            dispatcher(PredictionInput.Internal.LoggedOut)
            logger.logD(this, "logged out = is ok")
        }
    }
}