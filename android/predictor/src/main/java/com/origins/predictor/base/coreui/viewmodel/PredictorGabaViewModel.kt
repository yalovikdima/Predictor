package com.origins.predictor.base.coreui.viewmodel

import androidx.lifecycle.viewModelScope
import com.origins.kmm.gaba.base.store.Store
import com.origins.predictor.features.base.LifecycleObserver
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


abstract class PredictorGabaViewModel<UiState : Any, State : Any, OutputEvent : Any>(
    protected val store: Store<State, OutputEvent>
) : PredictorBaseViewModel<UiState, OutputEvent>() {

    private val outputEventsChannel = Channel<OutputEvent>(64)

    override val outputEvent = outputEventsChannel.receiveAsFlow()

    override val state: StateFlow<UiState> by lazy {
        store.state.asStateFlow().map {
            mapUi(it)
        }.flowOn(bgDispatcher)
            .stateIn(viewModelScope, SharingStarted.Lazily, mapUi(store.state.value))
    }

    init {
        viewModelScope.launch {
            store.outputEvents.asFlow().collect { event ->
                if (!handleOutputEvent(event)) {
                    outputEventsChannel.send(event)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        store.cancel()
    }


    override fun onResume() {
        (store.state.value as? LifecycleObserver)?.onStart()
    }

    override fun onStop() {
        (store.state.value as? LifecycleObserver)?.onStop()
    }

    abstract fun mapUi(state: State): UiState

    open fun handleOutputEvent(event: OutputEvent): Boolean {
        return false
    }
}

