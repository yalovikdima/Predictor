package com.origins.predictor.base

import android.util.Log

actual class PredictorLogger actual constructor(private val isEnabled: Boolean) {

    actual fun logD(message: String) {
        if (isEnabled) {
            println(message)
        }
    }

    actual fun logD(tag: Any, message: String) {
        if (isEnabled) {
            Log.d(tag.toString(), message)
        }
    }

    actual fun logD(tag: Any, message: String, exception: Exception) {
        if (isEnabled) {
            Log.d(tag.toString(), "$message $exception")
        }
    }

    fun log(tag: Any, prefix: Any?, text: Any?, postfix: Any?, error: Throwable?) {
        if (isEnabled) {
            val tagValue = if (tag is String) {
                tag
            } else {
                "${tag::class.java.simpleName}(${tag.hashCode()})"
            }
            println(
                "PredictorLogger = $tagValue = ${
                    listOfNotNull(
                        prefix,
                        text,
                        postfix,
                        error?.toString()
                    ).joinToString(" = ")
                }"
            )
            error?.printStackTrace()
        }
    }
}