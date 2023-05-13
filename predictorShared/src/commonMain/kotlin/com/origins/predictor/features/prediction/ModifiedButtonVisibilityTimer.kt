package com.origins.predictor.features.prediction

import com.origins.kmm.gaba.base.cmd.GabaCmd
import com.origins.kmm.gaba.base.cmd.ResultDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


internal class ModifiedButtonVisibilityTimer : GabaCmd<Input>() {
    override fun execute(scope: CoroutineScope, dispatcher: ResultDispatcher<Input>): Job {
        return scope.launch {
            try {
                dispatcher.invoke(PredictionInput.Internal.ModifiedButtonVisibilityResult(isVisible = true))
                delay(HINT_VISIBILITY_DURATION)
                dispatcher.invoke(PredictionInput.Internal.ModifiedButtonVisibilityResult(isVisible = false))
            } catch (e: Throwable) {
                dispatcher.invoke(PredictionInput.Internal.ModifiedButtonVisibilityResult(isVisible = false))
            }
        }
    }

    companion object {
        const val HINT_VISIBILITY_DURATION = 2000L
    }

}
