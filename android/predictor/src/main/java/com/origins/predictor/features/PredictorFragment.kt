package com.origins.predictor.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.netcosports.kotlin.extensions.withArguments
import com.origins.predictor.R
import com.origins.predictor.base.coreui.fragments.PredictorBaseFragment
import com.origins.predictor.databinding.PredictorFragmentBinding
import com.origins.predictor.features.prediction.ui.PredictionFragment
import com.origins.predictor.features.routers.base.PredictorNavHostFragment

internal class PredictorFragment : PredictorBaseFragment<PredictorFragmentBinding>() {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): PredictorFragmentBinding {
        return PredictorFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(requireViewBinding()) {
            val callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val navHostFragment =
                        childFragmentManager.findFragmentById(root.id) as? PredictorNavHostFragment

                    if (navHostFragment?.onBackPressed() != true) {
                        isEnabled = false
                        activity?.onBackPressed()
                    }
                }
            }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

            if (childFragmentManager.fragments.isEmpty()) {
                val host = PredictorNavHostFragment.newInstance(
                    R.navigation.predictor_app_navigation,
                    PredictionFragment.getArguments(requireArguments().getString(EXTRA_MATCH_ID))
                )
                childFragmentManager.beginTransaction()
                    .replace(container.id, host)
                    .commit()
            }
        }
    }

    companion object {
        private const val EXTRA_MATCH_ID = "EXTRA_MATCH_ID"

        fun newInstance(
            matchId: String
        ): PredictorFragment {
            return PredictorFragment().withArguments {
                putString(EXTRA_MATCH_ID, matchId)
            }
        }
    }

}