package com.origins.predictor.di

import com.origins.predictor.PredictorAnalytics
import com.origins.predictor.base.PredictorDataStorage
import com.origins.predictor.base.PredictorLogger
import com.origins.predictor.data.di.GameDataDI
import com.origins.predictor.data.di.PredictionDI
import com.origins.predictor.data.di.PredictionDataDI
import com.origins.predictor.data.di.PredictorConfigDI
import com.origins.predictor.resources.ColorResourceProvider
import com.origins.predictor.resources.ImageResourceProvider
import com.origins.predictor.resources.StringResourceProvider
import com.origins.predictor.resources.TextStyleResourceProvider
import com.origins.predictor.utils.PredictorConfig

class PredictorDI(
    private val config: PredictorConfig,
    private val logger: PredictorLogger,
    private val isLogsEnabled: Boolean,
    private val contestantStorage: PredictorDataStorage,
    private val analytics: PredictorAnalytics,
    private val stringResourceProvider: StringResourceProvider,
    private val imageResourceProvider: ImageResourceProvider,
    private val textStyleResourceProvider: TextStyleResourceProvider,
    private val colorResourceProvider: ColorResourceProvider,
) {

    val commonDataDI: PredictorCommonDataDI = PredictorCommonDataDI(
        config = config,
        isLogsEnabled = isLogsEnabled,
        logger = logger,
        predictorDataStorage = contestantStorage,
        analytics = analytics,
    )

    val gameDataDI: GameDataDI = GameDataDI(
        commonDataDI
    )

    private val predictionDataDI: PredictionDataDI = PredictionDataDI(
        commonDataDI
    )

    val predictorConfigDI: PredictorConfigDI = PredictorConfigDI(
        commonDataDI
    )
    private val resourcesKMM = ResourcesKMM(
        stringResourceProvider,
        imageResourceProvider,
        textStyleResourceProvider,
        colorResourceProvider
    )


    fun providePredictionMainDI() = PredictionDI(
        gameRepository = gameDataDI.provideGameRepository(),
        predictionRepository = predictionDataDI.providePredictionRepository(),
        predictorConfigRepository = predictorConfigDI.providePredictorConfigRepository(),
        analytics = analytics,
        logger = commonDataDI.logger,
        resourcesKMM = resourcesKMM
    )

}