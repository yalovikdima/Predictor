package com.origins.predictor.features.prediction.views.game

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.netcosports.kotlin.extensions.dpToPx
import com.origins.predictor.R
import com.origins.predictor.base.core.di.PredictorKoinComponent
import com.origins.predictor.base.coreui.utils.PredictorNumberItemDecoration
import com.origins.predictor.base.coreui.utils.extensions.setLabelKMM
import com.origins.predictor.databinding.PredictionHomeViewBinding
import com.origins.predictor.domain.game.models.ScoreEntity
import com.origins.predictor.features.prediction.ui.PredictionUi
import com.origins.predictor.features.prediction.ui.ScoreUi
import com.origins.predictor.features.prediction.views.*
import com.origins.predictor.features.prediction.views.utils.addButtonView
import com.origins.predictor.features.prediction.views.utils.addTermsView
import com.origins.predictor.features.prediction.views.utils.addView
import com.origins.predictor.features.prediction.views.utils.createButton

internal class PredictionGameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), PredictorKoinComponent {

    var validateClickListener: () -> Unit = {}
    var modifyClickListener: () -> Unit = {}
    var shareClickListener: () -> Unit = {}

    var onScrollListener: (ScoreEntity?) -> Unit = {}

    val binding: PredictionHomeViewBinding =
        PredictionHomeViewBinding.inflate(LayoutInflater.from(context), this)

    private var homeTeamAdapter: PredictionScorePickerAdapter? = null
    private var awayTeamAdapter: PredictionScorePickerAdapter? = null
    private val homeSnapHelper: PredictionLinearSnapHelper
    private val awaySnapHelper: PredictionLinearSnapHelper

    private var currentScore: ScoreEntity? = null

    private var validateButton = createButton(
        binding.root.context
    )
    private var shareButton = createButton(
        binding.root.context
    )

    private val scoreView = PredictionScoreView(binding.root.context).apply {
        scoreSeparatorPadding = 20.dpToPx(context).toInt()
    }
    private val timerView = PredictionTimerView(binding.root.context)
    private val sponsorView = PredictionSponsorView(binding.root.context)
    private val giftView = PredictionGiftView(binding.root.context)
    private val headerView = PredictionHeaderView(binding.root.context)

    private var firstButton: View? = null

    init {
        homeTeamAdapter =
            PredictionScorePickerAdapter(true) { position, isHomeTeam ->
                onClick(position, isHomeTeam)
            }

        awayTeamAdapter =
            PredictionScorePickerAdapter(false) { position, isHomeTeam ->
                onClick(position, isHomeTeam)
            }

        homeSnapHelper = PredictionLinearSnapHelper(true,
            itemPositionChangeListener = { item, isHomeTeam ->
                onSnapPositionChange(item, isHomeTeam)
            }, onScrollListener = { onScrollListener.invoke(currentScore) }
        ).apply {
            attachToRecyclerView(binding.homeTeamNumberPicker)
        }
        awaySnapHelper = PredictionLinearSnapHelper(false,
            itemPositionChangeListener = { item, isHomeTeam ->
                onSnapPositionChange(item, isHomeTeam)
            }, onScrollListener = { onScrollListener.invoke(currentScore) }
        ).apply {
            attachToRecyclerView(binding.awayTeamNumberPicker)
        }
        binding.homeTeamNumberPicker.addItemDecoration(
            PredictorNumberItemDecoration(
                context,
                homeTeamAdapter!!
            )
        )
        binding.awayTeamNumberPicker.addItemDecoration(
            PredictorNumberItemDecoration(
                context,
                awayTeamAdapter!!
            )
        )

    }

    var score: ScoreUi? = null
        set(value) {
            if (value != null) {
                homeTeamAdapter?.let {
                    homeSnapHelper.scrollTo(
                        it.numbers.indexOf(
                            value.homeTeamScore.text.getString(context).toInt()
                        )
                    )
                }
                awayTeamAdapter?.let {
                    awaySnapHelper.scrollTo(
                        it.numbers.indexOf(
                            value.awayTeamScore.text.getString(context).toInt()
                        )
                    )
                }
                scoreView.score = value
                currentScore = ScoreEntity(
                    value.homeTeamScore.text.getString(context).toInt(),
                    value.awayTeamScore.text.getString(context).toInt()
                )
                field = value
            }
        }

    var data: PredictionUi.GameUi? = null
        set(value) {
            val currentRange = field?.range
            field = value
            value?.let { data ->
                if (binding.homeTeamNumberPicker.adapter == null && currentRange != data.range) {
                    homeTeamAdapter?.range = data.range
                    binding.homeTeamNumberPicker.adapter = homeTeamAdapter
                }
                if (binding.awayTeamNumberPicker.adapter == null && currentRange != data.range) {
                    awayTeamAdapter?.range = data.range
                    binding.awayTeamNumberPicker.adapter = awayTeamAdapter
                }

                validateButton.setLabelKMM(data.validateButton.label)
                validateButton.setBackgroundResource(
                    if (data.predictionId != null) {
                        if (data.validateButton.isModifiedButton) {
                            R.drawable.predictor_modified_button_bkg
                        } else {
                            R.drawable.predictor_modify_button_bkg
                        }
                    } else {
                        R.drawable.predictor_validate_button_bkg
                    }
                )
                firstButton = validateButton

                data.baseUi.shareButton?.let { shareButton.setLabelKMM(it.label) }
                shareButton.setBackgroundResource(R.drawable.predictor_share_button_bkg)
                shareButton.setOnClickListener { shareClickListener() }

                score = data.baseUi.score
                if (data.predictionId != null) {
                    firstButton?.setOnClickListener {
                        modifyClickListener()
                    }
                } else {
                    firstButton?.setOnClickListener { validateClickListener() }
                }
                timerView.setTime(data.timer)

                sponsorView.setSponsor(data.baseUi.sponsor)
                giftView.setGift(data.baseUi.gift)
                headerView.setTitle(data.baseUi.title)
                headerView.setSubTitle(data.baseUi.subTitle)
                updateUi()
            }
        }

    private fun updateUi() {
        data?.baseUi?.let { data ->
            binding.contentContainer.removeAllViews()
            binding.termsContainer.removeAllViews()
            binding.contentContainer.addView(0f, headerView)
            binding.contentContainer.addView(9f, timerView)
            if (data.sponsor != null && data.gift != null) {
                binding.contentContainer.addView(5f, scoreView)
                binding.contentContainer.addView(3f, sponsorView)
                binding.contentContainer.addView(0f, giftView)

                firstButton?.let {
                    binding.contentContainer.addButtonView(
                        13f,
                        it
                    )
                }
            }

            if (data.sponsor != null && data.gift == null) {
                binding.contentContainer.addView(24f, scoreView)
                binding.contentContainer.addView(3f, sponsorView)
                firstButton?.let {
                    binding.contentContainer.addButtonView(
                        13f,
                        it
                    )
                }
            }

            if (data.sponsor == null && data.gift != null) {
                binding.contentContainer.addView(5f, scoreView)
                binding.contentContainer.addView(5f, giftView)
                firstButton?.let {
                    binding.contentContainer.addButtonView(
                        28f,
                        it
                    )
                }
            }

            if (data.sponsor == null && data.gift == null) {
                binding.contentContainer.addView(24f, scoreView)
                firstButton?.let {
                    binding.contentContainer.addButtonView(
                        75f,
                        it
                    )
                }
            }

            binding.contentContainer.addButtonView(
                marginTop = 13f,
                paddingBottom = 13f,
                view = shareButton
            )
            data.terms?.let { binding.termsContainer.addTermsView(it) }
        }
    }


    private fun onSnapPositionChange(position: Int, isHomePicker: Boolean) {
        currentScore = if (isHomePicker) {
            currentScore?.copy(homeScore = (binding.homeTeamNumberPicker.adapter as PredictionScorePickerAdapter).numbers[position])
        } else {
            currentScore?.copy(awayScore = (binding.awayTeamNumberPicker.adapter as PredictionScorePickerAdapter).numbers[position])
        }
        scoreView.currentScore = currentScore
    }

    private fun onClick(position: Int, isHomePicker: Boolean) {
        val snapHelper = if (isHomePicker) homeSnapHelper else awaySnapHelper
        snapHelper.scrollTo(position)
    }

}