package com.origins.predictor.features.prediction.views.result

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.netcosports.kotlin.extensions.dpToPx
import com.origins.predictor.R
import com.origins.predictor.base.core.di.PredictorKoinComponent
import com.origins.predictor.base.coreui.utils.extensions.setLabelKMM
import com.origins.predictor.databinding.PredictorResultViewBinding
import com.origins.predictor.domain.game.models.ScoreEntity
import com.origins.predictor.features.prediction.ui.PredictionUi
import com.origins.predictor.features.prediction.views.PredictionGiftView
import com.origins.predictor.features.prediction.views.PredictionHeaderView
import com.origins.predictor.features.prediction.views.PredictionScoreView
import com.origins.predictor.features.prediction.views.PredictionSponsorView
import com.origins.predictor.features.prediction.views.utils.*
import com.origins.resources.entity.extensions.setImageKMM
import com.origins.resources.entity.extensions.setLabelKMM


internal class PredictionResultView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), PredictorKoinComponent {

    val binding = PredictorResultViewBinding.inflate(LayoutInflater.from(context), this)
    var shareClickListener: () -> Unit = {}

    private val headerView = PredictionHeaderView(binding.root.context)
    private val sponsorView = PredictionSponsorView(binding.root.context)
    private val giftView = PredictionGiftView(binding.root.context)
    private val shareButton = createButton(binding.root.context)
    private val scoreView = PredictionScoreView(binding.root.context).apply {
        scoreSeparatorPadding = 20.dpToPx(context).toInt()
    }
    private val iconView = createIconResultView(binding.root.context)

    fun setData(data: PredictionUi.ResultUi?) {

        binding.contentContainer.removeAllViews()
        binding.termsContainer.removeAllViews()

        data?.image?.let { iconView.setImageKMM(it) }
        data?.baseUi?.let { data ->
            scoreView.score = data.score
            shareButton.setOnClickListener { shareClickListener() }
            data.shareButton?.let {
                shareButton.setLabelKMM(it.label)
                shareButton.setBackgroundResource(R.drawable.predictor_share_button_bkg)
            }
            headerView.binding.title.setLabelKMM(data.title)
            headerView.binding.subTitle.setLabelKMM(data.subTitle)
            scoreView.score = data.score
            sponsorView.setSponsor(data.sponsor)
            giftView.setGift(data.gift)

            binding.contentContainer.addView(0f, headerView)
            if (data.sponsor != null && data.gift != null) {
                binding.contentContainer.addView(38f, scoreView)
                binding.contentContainer.addView(3f, sponsorView)
                binding.contentContainer.addView(0f, giftView)
                binding.contentContainer.addView(26f, iconView)
                binding.contentContainer.addButtonView(marginTop = 24f, paddingBottom = 13f, view = shareButton)
            }
            if (data.sponsor != null && data.gift == null) {
                binding.contentContainer.addView(42f, scoreView)
                binding.contentContainer.addView(3f, sponsorView)
                binding.contentContainer.addView(29f, iconView)
                binding.contentContainer.addButtonView(marginTop = 35f, paddingBottom = 13f, view = shareButton)
            }

            if (data.sponsor == null && data.gift != null) {
                binding.contentContainer.addView(38f, scoreView)
                binding.contentContainer.addView(5f, giftView)
                binding.contentContainer.addView(40f, iconView)
                binding.contentContainer.addButtonView(marginTop = 32f, paddingBottom = 13f, view = shareButton)
            }
            if (data.sponsor == null && data.gift == null) {
                binding.contentContainer.addView(64f, scoreView)
                binding.contentContainer.addView(34f, iconView)
                binding.contentContainer.addButtonView(marginTop = 52f, paddingBottom = 13f, view = shareButton)
            }
            data.terms?.let { binding.termsContainer.addTermsView(it) }
        }
    }

}