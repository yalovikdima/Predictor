package com.origins.predictor.features.prediction.views.noGame

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.origins.predictor.base.coreui.utils.PredictorNumberItemDecoration
import com.origins.predictor.databinding.PredictionNoGameViewBinding
import com.origins.predictor.features.prediction.ui.PredictionUi
import com.origins.predictor.features.prediction.views.PredictionScorePickerAdapter
import com.origins.predictor.features.prediction.views.utils.RecyclerViewDisabler
import com.origins.resources.entity.extensions.setImageKMM
import com.origins.resources.entity.extensions.setLabelKMM

internal class PredictionNoGameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val binding: PredictionNoGameViewBinding =
        PredictionNoGameViewBinding.inflate(LayoutInflater.from(context), this)

    fun setData(data: PredictionUi.NotAvailableUi) {
        val homeAdapter = PredictionScorePickerAdapter(true).apply { range = data.range }
        val awayAdapter = PredictionScorePickerAdapter(false).apply { range = data.range }

        binding.homeTeamNumberPicker.adapter = homeAdapter
        binding.awayTeamNumberPicker.adapter = awayAdapter
        binding.header.binding.title.setLabelKMM(data.title)
        binding.header.binding.subTitle.setLabelKMM(data.subTitle)
        binding.image.setImageKMM(data.image)
        binding.homeTeamNumberPicker.addOnItemTouchListener(RecyclerViewDisabler())
        binding.awayTeamNumberPicker.addOnItemTouchListener(RecyclerViewDisabler())
        binding.homeTeamNumberPicker.addItemDecoration(PredictorNumberItemDecoration(context, homeAdapter, true))
        binding.awayTeamNumberPicker.addItemDecoration(PredictorNumberItemDecoration(context, awayAdapter, true))

    }

}