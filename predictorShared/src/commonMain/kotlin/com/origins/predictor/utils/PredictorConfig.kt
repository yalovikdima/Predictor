package com.origins.predictor.utils

import com.origins.predictor.base.PredictorSerializable

data class PredictorConfig(
    val api: PredictorApiConfig
) : PredictorSerializable