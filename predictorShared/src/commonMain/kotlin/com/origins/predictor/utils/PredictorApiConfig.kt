package com.origins.predictor.utils

import com.origins.predictor.base.PredictorSerializable

data class PredictorApiConfig private constructor(
    val baseUrl: String,
    val accountKey: String
) : PredictorSerializable {

    companion object {

        private val DEV_BASE_URL: String
            get() = "https://dev-api-gateway.onrewind.tv/predictor-service-api"

        private val STAGING_BASE_URL: String
            get() = "https://staging-api-gateway.onrewind.tv/predictor-service-api"

        private val PROD_BASE_URL: String
            get() = "https://api-gateway.onrewind.tv/predictor-service-api"

        fun dev(accountKey: String): PredictorApiConfig {
            return PredictorApiConfig(
                baseUrl = DEV_BASE_URL,
                accountKey = accountKey
            )
        }

        fun staging(accountKey: String): PredictorApiConfig {
            return PredictorApiConfig(
                baseUrl = STAGING_BASE_URL,
                accountKey = accountKey
            )
        }

        fun prod(accountKey: String): PredictorApiConfig {
            return PredictorApiConfig(
                baseUrl = PROD_BASE_URL,
                accountKey = accountKey
            )
        }
    }
}