package com.origins.predictor.features.prediction.views.tooLate

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.netcosports.kotlin.extensions.dpToPx
import com.origins.predictor.R
import com.origins.predictor.base.core.di.PredictorKoinComponent
import com.origins.predictor.base.coreui.utils.PredictorNumberItemDecoration
import com.origins.predictor.base.coreui.utils.extensions.setLabelKMM
import com.origins.predictor.databinding.PredictionTooLateViewBinding
import com.origins.predictor.features.prediction.ui.PredictionUi
import com.origins.predictor.features.prediction.views.*
import com.origins.predictor.features.prediction.views.utils.*

internal class PredictionTooLateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), PredictorKoinComponent {

    val binding: PredictionTooLateViewBinding =
        PredictionTooLateViewBinding.inflate(LayoutInflater.from(context), this, true)

    private val validateButton = createButton(binding.root.context)

    private val shareButton = createButton(binding.root.context)

    private val timerView = PredictionTimerView(binding.root.context)

    private val headerView = PredictionHeaderView(binding.root.context)

    private val sponsorView = PredictionSponsorView(binding.root.context)

    private val giftView = PredictionGiftView(binding.root.context)

    private val scoreView = PredictionScoreView(binding.root.context).apply {
        scoreSeparatorPadding = 20.dpToPx(context).toInt()
    }

    fun setData(data: PredictionUi.TooLateUi) {
        timerView.setTime(data.timer)
        val homeAdapter = PredictionScorePickerAdapter(true)
            .apply {
                range = data.range
            }
        val awayAdapter = PredictionScorePickerAdapter(false)
            .apply {
                range = data.range
            }
        binding.homeTeamNumberPicker.adapter = homeAdapter
        binding.awayTeamNumberPicker.adapter = awayAdapter

        binding.homeTeamNumberPicker.addOnItemTouchListener(RecyclerViewDisabler())
        binding.awayTeamNumberPicker.addOnItemTouchListener(RecyclerViewDisabler())

        binding.homeTeamNumberPicker.addItemDecoration(PredictorNumberItemDecoration(context, homeAdapter, true))
        binding.awayTeamNumberPicker.addItemDecoration(PredictorNumberItemDecoration(context, awayAdapter, true))

        validateButton.setBackgroundResource(R.drawable.predictor_validate_button_disable_bkg)
        validateButton.setLabelKMM(data.validateButton.label)

        shareButton.setBackgroundResource(R.drawable.predictor_share_button_disable_bkg)

        data.baseUi.let { data ->
            scoreView.score = data.score
            headerView.setTitle(data.title)
            headerView.setSubTitle(data.subTitle)
            sponsorView.setSponsor(data.sponsor)
            giftView.setGift(data.gift)
            data.shareButton?.let {
                shareButton.setLabelKMM(it.label)
            }
            binding.contentContainer.addView(0f, headerView)
            binding.contentContainer.addView(9f, timerView)
            if (data.sponsor != null && data.gift != null) {
                binding.contentContainer.addView(5f, scoreView)
                binding.contentContainer.addView(3f, sponsorView)
                binding.contentContainer.addView(0f, giftView)
                binding.contentContainer.addButtonView(
                    13f,
                    validateButton
                )
            }
            if (data.sponsor != null && data.gift == null) {
                binding.contentContainer.addView(24f, scoreView)
                binding.contentContainer.addView(3f, sponsorView)
                binding.contentContainer.addButtonView(
                    13f,
                    validateButton
                )
            }
            if (data.sponsor == null && data.gift != null) {
                binding.contentContainer.addView(5f, scoreView)
                binding.contentContainer.addView(5f, giftView)
                binding.contentContainer.addButtonView(
                    28f,
                    validateButton
                )
            }
            if (data.sponsor == null && data.gift == null) {
                binding.contentContainer.addView(24f, scoreView)
                binding.contentContainer.addButtonView(
                    75f,
                    validateButton
                )
            }

            binding.contentContainer.addButtonView(
                marginTop = 13f,
                paddingBottom = 13f,
                view = shareButton
            )
            data.terms?.let { binding.termsContainer.addTermsView(it) }
        }
    }
}
