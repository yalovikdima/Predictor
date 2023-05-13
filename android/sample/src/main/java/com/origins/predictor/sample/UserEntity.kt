package com.origins.predictor.sample

import java.io.Serializable

data class UserEntity(
    val deviceId: String?,
    val firstName: String?,
    val lastName: String?
) : Serializable