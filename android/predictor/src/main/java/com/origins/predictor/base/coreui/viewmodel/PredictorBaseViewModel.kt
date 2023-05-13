package com.origins.predictor.base.coreui.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.origins.predictor.base.core.di.PREDICTOR_BG_DISPATCHER_QUALIFIER
import com.origins.predictor.base.core.di.PREDICTOR_UI_DISPATCHER_QUALIFIER
import com.origins.predictor.base.core.di.PredictorKoinComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class PredictorBaseViewModel<State : Any, OutputEvent : Any> : ViewModel(), LifecycleObserver,
    PredictorKoinComponent {
    protected val bgDispatcher: CoroutineDispatcher by lazy {
        getKoin().get(PREDICTOR_BG_DISPATCHER_QUALIFIER)
    }
    protected val uiDispatcher: CoroutineDispatcher by lazy {
        getKoin().get(PREDICTOR_UI_DISPATCHER_QUALIFIER)
    }


    abstract val state: StateFlow<State>
    abstract val outputEvent: Flow<OutputEvent>

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() = Unit

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() = Unit
}