package com.origins.predictor.features.prediction.di

import com.origins.predictor.data.di.PredictionDI
import com.origins.predictor.di.PredictorDI
import com.origins.predictor.features.prediction.PredictionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val predictionUiModule = module {
    factory {
        get<PredictorDI>().providePredictionMainDI()
    }

    viewModel { (matchId: String) ->
        PredictionViewModel(
            store = get<PredictionDI>().providePredictionStore(matchId)
        )
    }
}