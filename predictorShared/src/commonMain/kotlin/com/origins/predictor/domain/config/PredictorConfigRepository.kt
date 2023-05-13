package com.origins.predictor.domain.config

import kotlinx.coroutines.flow.Flow

interface PredictorConfigRepository {

    fun observeContestant(): Flow<ContestantEntity?>
    fun getContestant(): ContestantEntity?
    fun storeContestant(contestant: ContestantEntity?)
}