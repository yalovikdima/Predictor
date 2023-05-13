package com.origins.predictor.features.prediction

import com.origins.kmm.gaba.base.cmd.GabaCmd
import com.origins.kmm.gaba.base.cmd.ResultDispatcher
import com.origins.predictor.base.PredictorLogger
import com.origins.predictor.features.prediction.ui.TimerEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class CountDownTimer(
    private val milliseconds: Long,
    private val logger: PredictorLogger
) : GabaCmd<Input>() {
    override fun execute(scope: CoroutineScope, dispatcher: ResultDispatcher<Input>): Job {
        return scope.launch {
            try {
                logger.logD("startTimer duration=$milliseconds")
                (milliseconds downTo 0 step millisecondsInMinutes).forEach { milliseconds ->
                    logger.logD("startTimer tick = $milliseconds")
                    val totalSeconds = milliseconds / 1000
                    val totalDays = totalSeconds / (3600 * 24)
                    val totalHours = (totalSeconds - (totalDays * 3600 * 24)) / 3600
                    val totalMinutes = ((totalSeconds - (totalDays * 3600 * 24) - totalHours * 3600) % 3600) / 60
                    dispatcher(
                        PredictionInput.Internal.OnTimerTicked(
                            TimerEntity(
                                totalDays.toInt(),
                                totalHours.toInt(),
                                totalMinutes.toInt()
                            )
                        )
                    )
                    delay(millisecondsInMinutes)
                }
                dispatcher.invoke(PredictionInput.Internal.OnStopTimer)
            } catch (ignore: Exception) {
                ignore.printStackTrace()
            }
        }
    }

    companion object {
        private const val millisecondsInMinutes = 60 * 1000L
    }

}