package com.origins.predictor.features.dialog.validate

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.netcosports.arch.extensions.getViewModel
import com.origins.predictor.R
import com.origins.predictor.databinding.PredictorValidateDialogFragmentBinding
import com.origins.predictor.domain.config.ContestantEntity
import com.origins.predictor.features.prediction.PredictionViewModel
import com.origins.predictor.features.prediction.ui.PredictionUi
import com.origins.resources.entity.extensions.setHintKmm
import com.origins.resources.entity.extensions.setLabelKMM
import com.origins.resources.entity.extensions.setTextStyleKMM


internal class ValidateDialogFragment : DialogFragment() {

    private lateinit var defaultDeviceID: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#B3000000")))
        defaultDeviceID = Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID)
        return inflater.inflate(R.layout.predictor_validate_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: PredictionViewModel = requireParentFragment().getViewModel()

        PredictorValidateDialogFragmentBinding.bind(view).apply {
            val data = (viewModel.state.value.data as PredictionUi.GameUi).validateDialog
            title.setLabelKMM(data.title)
            description.setLabelKMM(data.subTitle)

            firstName.setHintKmm(data.firstName.hint)
            firstName.setTextStyleKMM(data.firstName.labelTextStyle)

            lastName.setHintKmm(data.lastName.hint)
            lastName.setTextStyleKMM(data.lastName.labelTextStyle)

            sendButton.setLabelKMM(data.buttonLabel)
            sendButton.setOnClickListener {
                val currentFirstName = firstName.text.toString()
                val currentLastName = lastName.text.toString()

                if (currentFirstName.isEmpty()) {
                    firstName.error = data.errorText.getString(requireContext())
                } else {
                    viewModel.loginAndValidate(
                        contestant = ContestantEntity(
                            firstName = currentFirstName,
                            lastName = currentLastName,
                            externalId = defaultDeviceID
                        ),
                        isVisible = parentFragment?.isResumed == true
                    )
                }
            }
        }
    }

    companion object {
        const val TAG = "ValidateDialogFragment"
    }

}