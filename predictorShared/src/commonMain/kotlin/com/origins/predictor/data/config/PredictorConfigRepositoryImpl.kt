package com.origins.predictor.data.config

import com.origins.predictor.base.PredictorDataStorage
import com.origins.predictor.domain.config.ContestantEntity
import com.origins.predictor.domain.config.PredictorConfigRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class PredictorConfigRepositoryImpl(
    private val predictorDataStorage: PredictorDataStorage
) : PredictorConfigRepository {

    private val observeContestant = MutableStateFlow(getContestant())

    override fun observeContestant(): StateFlow<ContestantEntity?> = observeContestant

    override fun getContestant(): ContestantEntity? {
        return predictorDataStorage.getContestant()
    }

    override fun storeContestant(contestant: ContestantEntity?) {
        predictorDataStorage.storeContestant(contestant)
        observeContestant.value = contestant
    }
}