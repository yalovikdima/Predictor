package com.origins.predictor.base

expect class PredictorLogger(isEnabled: Boolean) {

    fun logD(message: String)

    fun logD(tag: Any, message: String)

    fun logD(tag: Any, message: String, exception: Exception)
}