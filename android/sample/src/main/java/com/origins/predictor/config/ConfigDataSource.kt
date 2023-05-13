package com.origins.predictor.config

import kotlinx.serialization.json.Json
import java.net.URL

class ConfigDataSource {
    private fun createJson() = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun getConfig(): ConfigModel? {
        return try {
            createJson().decodeFromString(
                ConfigModel.serializer(),
                (URL("https://www.dl.dropboxusercontent.com/s/b5agc0q1fg4fe2b/event_id.json?dl=0").readText())
            )
        } catch (e: Throwable) {
            null
        }
    }
}