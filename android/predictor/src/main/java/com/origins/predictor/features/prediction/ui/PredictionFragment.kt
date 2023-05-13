package com.origins.predictor.features.prediction.ui

import Scene
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.origins.predictor.Predictor
import com.origins.predictor.base.coreui.fragments.PredictorBaseFragment
import com.origins.predictor.base.coreui.utils.extensions.addSystemBarBottomPadding
import com.origins.predictor.base.coreui.utils.extensions.initColor
import com.origins.predictor.base.coreui.utils.extensions.observe
import com.origins.predictor.base.coreui.utils.sharing.PredictorShareManager
import com.origins.predictor.databinding.PredictionFragmentBinding
import com.origins.predictor.domain.config.ContestantEntity
import com.origins.predictor.domain.config.PredictorConfigRepository
import com.origins.predictor.features.dialog.congrats.CongratsDialogFragment
import com.origins.predictor.features.dialog.validate.ValidateDialogFragment
import com.origins.predictor.features.prediction.PredictionOutputEvent
import com.origins.predictor.features.prediction.PredictionViewModel
import com.origins.predictor.features.prediction.views.game.PredictionGameView
import com.origins.predictor.features.prediction.views.noGame.PredictionNoGameView
import com.origins.predictor.features.prediction.views.result.PredictionResultView
import com.origins.predictor.features.prediction.views.tooLate.PredictionTooLateView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf


internal class PredictionFragment : PredictorBaseFragment<PredictionFragmentBinding>() {

    private val matchId by lazy { requireArguments().getString(EXTRA_MATCH_ID).orEmpty() }
    private val predictorConfigRepository: PredictorConfigRepository by inject()
    private val predictorShareManager: PredictorShareManager by inject()

    private var currentView: View? = null
    private val contestantData: Flow<ContestantEntity?> = predictorConfigRepository.observeContestant()

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): PredictionFragmentBinding {
        return PredictionFragmentBinding.inflate(inflater, container, false)
    }

    private val viewModel: PredictionViewModel by lazy {
        getViewModel(
            parameters = { parametersOf(matchId) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = requireViewBinding()
        with(binding) {
            root.addSystemBarBottomPadding()
            swipeRefreshLayout.initColor()
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refresh()
            }
            viewModel.state.observe(viewLifecycleOwner) { state ->
                swipeRefreshLayout.isRefreshing = state.scene.isRefreshing
                when (state.scene) {
                    Scene.EMPTY_LOADING, Scene.REFRESHING -> renderLoadingState(binding)
                    Scene.EMPTY_ERROR -> renderErrorState(binding)
                    Scene.DATA -> renderDataState(binding, state)
                }
            }

            viewModel.outputEvent.observe(viewLifecycleOwner) { event ->
                when (event) {
                    is PredictionOutputEvent.SharePrediction -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            startActivity(predictorShareManager.share(event.share))
                        }
                    }
                    is PredictionOutputEvent.ShowLoginDialog -> {
                        if (Predictor.useNativeAppLogin) {
                            Predictor.setShowLogin(true)
                            viewModel.pendingScore.value = event.score
                        } else {
                            ValidateDialogFragment().show(childFragmentManager, ValidateDialogFragment.TAG)
                        }
                    }

                    is PredictionOutputEvent.HideLoginDialog -> {
                        (childFragmentManager.findFragmentByTag(ValidateDialogFragment.TAG) as? DialogFragment)?.dismiss()
                    }
                    else -> {}
                }

            }

            contestantData.observe(viewLifecycleOwner) {
                if (isResumed) {
                    if (it != null && viewModel.pendingScore.value != null) {
                        viewModel.onValidateClick(true)
                        viewModel.pendingScoreConsumed()
                    }
                }
            }
        }
    }

    private fun renderErrorState(binding: PredictionFragmentBinding) {
        binding.loaderProgress.isVisible = false
        binding.swipeRefreshLayout.isVisible = true
        binding.predictionContainer.isVisible = false
        binding.error.isVisible = true
    }

    private fun renderLoadingState(binding: PredictionFragmentBinding) {
        binding.loaderProgress.isVisible = true
        binding.swipeRefreshLayout.isVisible = false
    }

    private fun renderDataState(binding: PredictionFragmentBinding, state: PredictionUiState) {
        binding.loaderProgress.isVisible = false
        binding.swipeRefreshLayout.isVisible = true
        binding.error.isVisible = false
        binding.predictionContainer.isVisible = true
        val congratsDialog = CongratsDialogFragment()
        val newView: View? = when (val data = state.data) {
            is PredictionUi.GameUi -> {
                if (data.congratulationDialog != null) {
                    congratsDialog.show(childFragmentManager, CongratsDialogFragment.TAG)
                } else {
                    (childFragmentManager.findFragmentByTag(CongratsDialogFragment.TAG) as? DialogFragment)?.dismiss()
                }

                if (currentView is PredictionGameView) {
                    (currentView as PredictionGameView).data = data
                    currentView
                } else {
                    PredictionGameView(binding.root.context).apply {
                        this.data = data

                        validateClickListener = {
                            viewModel.onValidateClick(isResumed)
                        }

                        modifyClickListener = {
                            viewModel.onModifyClick()
                        }

                        shareClickListener = {
                            viewModel.onShareClick()
                        }

                        onScrollListener = {
                            it?.let { viewModel.onScroll(it) }
                        }
                    }
                }
            }
            is PredictionUi.TooLateUi -> {
                PredictionTooLateView(binding.root.context).apply {
                    setData(data)
                }
            }
            is PredictionUi.NotAvailableUi -> {
                PredictionNoGameView(binding.root.context).apply {
                    setData(data)
                }
            }

            is PredictionUi.ResultUi -> {
                PredictionResultView(binding.root.context).apply {
                    setData(data)
                    shareClickListener = {
                        viewModel.onShareClick()
                    }
                }
            }
            else -> {
                currentView
            }
        }
        newView?.let {
            currentView?.parent?.let { (it as ViewGroup).removeView(currentView) }
            newView.minimumHeight = getContainerHeight(binding.predictionContainer)
            binding.predictionContainer.addView(it)
            currentView = it
        }
    }

    private fun getContainerHeight(container: View): Int {
        return container.measuredHeight - container.paddingTop - container.paddingBottom
    }

    companion object {
        private const val EXTRA_MATCH_ID = "EXTRA_MATCH_ID"
        fun getArguments(matchId: String?): Bundle {
            return Bundle().apply {
                putString(EXTRA_MATCH_ID, matchId)
            }
        }
    }
}