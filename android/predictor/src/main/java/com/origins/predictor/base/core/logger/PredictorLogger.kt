package com.origins.predictor.base.core.logger

import com.origins.predictor.base.PredictorLogger
import com.origins.predictor.base.core.di.PredictorKoin


fun predictorLog(tag: Any, text: Any?, error: Throwable?) {
    predictorLog(tag = tag, prefix = null, text = text, postfix = null, error = error)
}

internal fun predictorLog(
    tag: Any,
    prefix: Any? = null,
    text: Any? = null,
    postfix: Any? = null,
    error: Throwable? = null
) {
    PredictorKoin.koin.get<PredictorLogger>().log(tag, prefix, text, postfix, error)
}