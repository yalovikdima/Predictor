package com.origins.predictor.features.dialog.congrats

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.netcosports.arch.extensions.getViewModel
import com.origins.predictor.R
import com.origins.predictor.base.coreui.utils.extensions.loadCenterImg
import com.origins.predictor.databinding.PredictorCongratsDialogFragmentBinding
import com.origins.predictor.features.prediction.PredictionViewModel
import com.origins.predictor.features.prediction.ui.PredictionUi
import com.origins.resources.entity.extensions.setImageKMM
import com.origins.resources.entity.extensions.setLabelKMM

internal class CongratsDialogFragment : DialogFragment() {

    private var _viewBinding: PredictorCongratsDialogFragmentBinding? = null
    private val binding: PredictorCongratsDialogFragmentBinding get() = requireNotNull(_viewBinding)

    val viewModel: PredictionViewModel by lazy {
        requireParentFragment().getViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return PredictorCongratsDialogFragmentBinding.bind(
            inflater.inflate(R.layout.predictor_congrats_dialog_fragment, container)

        ).also {
            _viewBinding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = viewModel.state.value.data as? PredictionUi.GameUi

        with(binding) {
            data?.congratulationDialog?.let { data ->
                title.setLabelKMM(data.title)
                subTitle.setLabelKMM(data.subTitle)
                confirmButton.setLabelKMM(data.confirm)
                score.score = data.score
                image.setImageKMM(data.image)
                close.setImageKMM(data.closeIcon)
                loadCenterImg(homeTeamLogo, data.homeTeamLogo)
                loadCenterImg(awayTeamLogo, data.awayTeamLogo)
            }

            confirmButton.setOnClickListener {
                viewModel.hideCongratsDialog()
            }
            close.setOnClickListener {
                viewModel.hideCongratsDialog()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.hideCongratsDialog()
    }

    companion object {
        internal const val TAG = "CongratsDialogFragment"
    }
}