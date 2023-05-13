package com.origins.predictor.data.di

import com.origins.kmm.gaba.base.store.Store
import com.origins.predictor.PredictorAnalytics
import com.origins.predictor.base.PredictorLogger
import com.origins.predictor.di.ResourcesKMM
import com.origins.predictor.domain.config.PredictorConfigRepository
import com.origins.predictor.domain.game.GameRepository
import com.origins.predictor.domain.prediction.PredictionRepository
import com.origins.predictor.features.prediction.PredictionFeature
import com.origins.predictor.features.prediction.PredictionOutputEvent
import com.origins.predictor.features.prediction.ui.PredictionUiMapper
import com.origins.predictor.features.prediction.ui.PredictionUiState

class PredictionDI internal constructor(
    private val gameRepository: GameRepository,
    private val predictionRepository: PredictionRepository,
    private val predictorConfigRepository: PredictorConfigRepository,
    private val analytics: PredictorAnalytics,
    private val logger: PredictorLogger,
    private val resourcesKMM: ResourcesKMM
) {
    fun providePredictionStore(matchId: String): Store<PredictionUiState, PredictionOutputEvent> {

        val uiMapper = PredictionUiMapper(
            resourcesKMM.ip,
            resourcesKMM.sp,
            resourcesKMM.tp,
            resourcesKMM.cp,
        )

        return PredictionFeature(
            gameRepository = gameRepository,
            predictionRepository = predictionRepository,
            predictorConfigRepository = predictorConfigRepository,
            analytics = analytics,
            resources = resourcesKMM,
            logger = logger
        ).start(PredictionFeature.initialState(matchId)).toStore(uiMapper::mapFrom)
    }
}
