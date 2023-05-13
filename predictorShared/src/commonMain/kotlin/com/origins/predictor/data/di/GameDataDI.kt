package com.origins.predictor.data.di

import com.origins.predictor.data.game.GameMapper
import com.origins.predictor.data.game.GameRepositoryImpl
import com.origins.predictor.di.PredictorCommonDataDI
import com.origins.predictor.domain.game.GameRepository

class GameDataDI internal constructor(
    private val commonDataDI: PredictorCommonDataDI
) {

    private fun provideGameDataMapper(): GameMapper = GameMapper()

    fun provideGameRepository(): GameRepository {
        return GameRepositoryImpl(
            httpClient = commonDataDI.provideHttpClient(),
            logger = commonDataDI.logger,
            gameMapper = provideGameDataMapper()
        )
    }
}