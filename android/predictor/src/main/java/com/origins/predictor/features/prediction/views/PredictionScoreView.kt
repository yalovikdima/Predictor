package com.origins.predictor.features.prediction.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import androidx.core.view.updatePadding
import com.netcosports.kotlin.extensions.dpToPx
import com.origins.predictor.R
import com.origins.predictor.databinding.PredictorScoreViewBinding
import com.origins.predictor.domain.game.models.ScoreEntity
import com.origins.predictor.features.prediction.ui.ScoreUi
import com.origins.resources.entity.extensions.setLabelKMM

internal class PredictionScoreView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val binding = PredictorScoreViewBinding.inflate(LayoutInflater.from(context), this)

    var currentScore: ScoreEntity? = null
        set(value) {
            field = value
            binding.homeTeamScore.text = value?.homeScore.toString()
            binding.awayTeamScore.text = value?.awayScore.toString()
        }

    var score: ScoreUi? = null
        set(value) {
            field = value
            setData(value)
        }

    var scoreSeparatorPadding: Int = 0
        get() {
            return binding.separator.paddingStart
        }
        set(value) {
            field = value
            binding.separator.updatePadding(left = value, right = value)
        }

    init {
        attrs?.let { handleAttrs(it) }
    }

    private fun handleAttrs(attrs: AttributeSet) {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.PredictionScoreView
        ).use { a ->
            val scoreSeparatorPadding =
                a.getDimensionPixelSize(
                    R.styleable.PredictionScoreView_scoreSeparatorPadding,
                    20.dpToPx(context).toInt()
                )
            this.scoreSeparatorPadding = scoreSeparatorPadding

        }
    }

    private fun setData(score: ScoreUi?) {
        score?.let {
            binding.separator.setLabelKMM(score.separator)
            binding.homeTeamScore.setLabelKMM(score.homeTeamScore)
            binding.awayTeamScore.setLabelKMM(score.awayTeamScore)
        }
    }
}