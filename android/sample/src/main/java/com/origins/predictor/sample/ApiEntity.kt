package com.origins.predictor.sample

data class ApiEntity(
    val type: Type
) {

    enum class Type(
        val accountKey: String
    ) {
        STAGING(
            accountKey = "HkzLQ4pzu"
        ),
        PROD(
            accountKey = "HkzLQ4pzu"
        ),
        DEV(
            accountKey = "HkzLQ4pzu"
        )
    }
}