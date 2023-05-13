package com.origins.predictor.base.coreui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.origins.predictor.base.core.di.PredictorKoinComponent
import com.origins.predictor.base.core.logger.predictorLog

abstract class PredictorBaseFragment<VIEW_BINDING : ViewBinding> : Fragment(), PredictorKoinComponent {

    protected open var viewBinding: VIEW_BINDING? = null
    protected fun requireViewBinding(): VIEW_BINDING = requireNotNull(viewBinding)
    protected abstract fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VIEW_BINDING

    override fun onAttach(context: Context) {
        logFragment("onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        logFragment("onCreate $savedInstanceState")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        logFragment("onCreateView $savedInstanceState")
        return createViewBinding(inflater, container).also {
            viewBinding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logFragment("onViewCreated $savedInstanceState")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        logFragment("onDestroyView")
        super.onDestroyView()
        viewBinding = null
    }

    override fun onDestroy() {
        logFragment("onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        logFragment("onDetach")
        super.onDetach()
    }

    private fun logFragment(message: String) {
        predictorLog(tag = this, text = "fragments = $message")
    }
}