package com.origins.predictor.base

import com.origins.predictor.domain.config.ContestantEntity

expect class PredictorDataStorage {

    fun getContestant(): ContestantEntity?
    fun storeContestant(contestant: ContestantEntity?)
}