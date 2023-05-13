package com.origins.predictor.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.origins.predictor.Predictor
import com.origins.predictor.sample.databinding.SamplePredictorFragmentBinding
import kotlinx.coroutines.launch

class SamplePredictorFragment : Fragment() {

    private var _binding: SamplePredictorFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return SamplePredictorFragmentBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    private val optaId by lazy { requireArguments().getString(EXTRA_OPTA_ID).orEmpty() }
    private val isClearUser by lazy { requireArguments().getBoolean(EXTRA_IS_CLEAR_USER) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = UserActivity.getUser(requireContext())
        val api = UserActivity.getApi(requireContext())
        Predictor.init(
            accountKey = api.type.accountKey,
            api = when (api.type) {
                ApiEntity.Type.DEV -> Predictor.Api.DEV
                ApiEntity.Type.PROD -> Predictor.Api.PROD
                ApiEntity.Type.STAGING -> Predictor.Api.STAGING
            },
            isUseNativeAppLogin = false,
            isLogsEnabled = true,
            context = requireContext(),
            analytics = PredictorDummyAnalytics(),
            qrCode = "https://www.origins-digital.com/"
        )

        with(binding) {
            if (isClearUser) {
                Predictor.setUserData(null, null, null)
            } else if (user.deviceId != null) {
                Predictor.setUserData(user.deviceId, user.firstName, user.lastName)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    if (Predictor.checkExistGame(optaId)) {
                        error.isVisible = false
                        childFragmentManager
                            .beginTransaction()
                            .replace(
                                container.id, Predictor.getFragment(optaId)
                            )
                            .commit()
                    } else {
                        error.isVisible = true
                        error.text = "Predictor is not shown. MatchId is null or empty"
                    }
                } catch (e: Exception) {
                    error.isVisible = true
                    error.text = "Error"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val EXTRA_OPTA_ID = "EXTRA_OPTA_ID"
        private const val EXTRA_IS_CLEAR_USER = "EXTRA_IS_CLEAR_USER"

        fun newInstance(optaId: String, isClearUser: Boolean): SamplePredictorFragment {
            return SamplePredictorFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_OPTA_ID, optaId)
                    putBoolean(EXTRA_IS_CLEAR_USER, isClearUser)
                }
            }
        }

    }
}