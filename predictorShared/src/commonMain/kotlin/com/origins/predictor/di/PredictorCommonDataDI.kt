package com.origins.predictor.di

import com.netcosports.kmm.curl.CurlLogger
import com.netcosports.kmm.curl.NetcoCurlInterceptor
import com.origins.predictor.PredictorAnalytics
import com.origins.predictor.base.PredictorDataStorage
import com.origins.predictor.base.PredictorLogger
import com.origins.predictor.utils.PredictorConfig
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class PredictorCommonDataDI internal constructor(
    val config: PredictorConfig,
    val logger: PredictorLogger,
    val isLogsEnabled: Boolean,
    val predictorDataStorage: PredictorDataStorage,
    val analytics: PredictorAnalytics
) {
    private val json = Json {
        useAlternativeNames = false
        ignoreUnknownKeys = true
        isLenient = true
    }

    internal fun provideHttpClient(): HttpClient {
        return HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }
            if (isLogsEnabled) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            this@PredictorCommonDataDI.logger.logD(message)
                        }
                    }

                    level = LogLevel.ALL
                }
                install(NetcoCurlInterceptor) {
                    logger = CurlLogger { message ->
                        this@PredictorCommonDataDI.logger.logD("OriginsCurlInterceptor", message)
                    }
                }
            }
            defaultRequest {
                url.takeFrom(URLBuilder().takeFrom(config.api.baseUrl).apply {
                    encodedPath += url.encodedPath
                })
                header("x-account-key", config.api.accountKey)
            }
        }
    }
}