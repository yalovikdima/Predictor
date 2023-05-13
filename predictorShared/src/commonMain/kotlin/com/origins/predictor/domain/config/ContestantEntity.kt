package com.origins.predictor.domain.config

import com.netcosports.rooibos.Swift

@Swift
data class ContestantEntity(
    val firstName: String?,
    val lastName: String?,
    val externalId: String?
)