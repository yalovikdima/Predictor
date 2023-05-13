package com.origins.predictor.data.prediction.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PredictionRequestModel(
    @SerialName("GameId")
    val gameId: String,
    val contestant: ContestantRequestModel,
    val score: ScoreRequestModel
)

@Serializable
data class ScoreRequestModel(
    val homeTeam: String,
    val awayTeam: String
)

@Serializable
data class ContestantRequestModel(
    val externalId: String?,
    @SerialName("firstname")
    val firstName: String?,
    @SerialName("lastname")
    val lastName: String?
)

@Serializable
data class ScoreRequest(
    val score: ScoreRequestModel
)