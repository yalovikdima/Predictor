package com.origins.predictor.features.prediction

import androidx.lifecycle.MutableLiveData
import com.origins.kmm.gaba.base.store.Store
import com.origins.predictor.base.coreui.viewmodel.PredictorGabaViewModel
import com.origins.predictor.domain.config.ContestantEntity
import com.origins.predictor.domain.game.models.ScoreEntity
import com.origins.predictor.features.prediction.ui.PredictionUiState

internal class PredictionViewModel internal constructor(
    store: Store<PredictionUiState, PredictionOutputEvent>
) : PredictorGabaViewModel<PredictionUiState, PredictionUiState, PredictionOutputEvent>(store) {

    val pendingScore = MutableLiveData<ScoreEntity?>()
    fun pendingScoreConsumed() {
        pendingScore.value = null
    }

    fun refresh() {
        store.state.value.refresh()
    }

    fun onValidateClick(isVisible: Boolean) {
        store.state.value.onValidateClick(isVisible)
    }

    fun onModifyClick() {
        store.state.value.onModifyClick()
    }

    fun onShareClick() {
        store.state.value.onShareClick()
    }

    fun loginAndValidate(contestant: ContestantEntity, isVisible: Boolean) {
        store.state.value.login(contestant, isVisible)
    }

    fun hideCongratsDialog() {
        store.state.value.hideCongratsDialog()
    }

    fun onScroll(score: ScoreEntity) {
        store.state.value.onScoreChange(score)
    }

    fun logout() {
        store.state.value.logout()
    }

    override fun mapUi(state: PredictionUiState) = state
}