package com.origins.predictor.base

import platform.Foundation.NSLog

actual class PredictorLogger actual constructor(private val isEnabled: Boolean) {

    actual fun logD(message: String) {
        if (isEnabled) {
            NSLog(message)
        }
    }

    actual fun logD(tag: Any, message: String) {
        if (isEnabled) {
            NSLog("$tag $message")
        }
    }

    actual fun logD(tag: Any, message: String, exception: Exception) {
        if (isEnabled) {
            NSLog("$tag $message $exception")
        }
    }

}