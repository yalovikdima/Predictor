package com.origins.predictor.features.prediction.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.origins.predictor.databinding.PredictionHeaderViewBinding
import com.origins.resources.entity.extensions.setLabelKMM
import com.origins.resources.entity.ui.Label

internal class PredictionHeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val binding = PredictionHeaderViewBinding.inflate(LayoutInflater.from(context), this)

    init {
        orientation = VERTICAL
    }

    fun setTitle(title: Label) {
        binding.title.setLabelKMM(title)
    }

    fun setSubTitle(description: Label?) {
        binding.subTitle.setLabelKMM(description)
    }
}