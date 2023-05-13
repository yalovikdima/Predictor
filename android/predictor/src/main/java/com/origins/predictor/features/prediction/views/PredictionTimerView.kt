package com.origins.predictor.features.prediction.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.origins.predictor.databinding.PredictorCoutDownTimerBinding
import com.origins.predictor.features.prediction.ui.TimerUi
import com.origins.resources.entity.extensions.setLabelKMM

internal class PredictionTimerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding = PredictorCoutDownTimerBinding.inflate(LayoutInflater.from(context), this)

    fun setTime(timer: TimerUi) {
        binding.days.setLabelKMM(timer.days.value)
        binding.daysText.setLabelKMM(timer.days.label)

        binding.hours.setLabelKMM(timer.hours.value)
        binding.housText.setLabelKMM(timer.hours.label)

        binding.minutes.setLabelKMM(timer.minutes.value)
        binding.minutesText.setLabelKMM(timer.minutes.label)
    }
}