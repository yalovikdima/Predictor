package com.origins.predictor.data.prediction.models

import com.origins.predictor.data.game.models.GameResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PredictionResponse {
    @SerialName("id")
    val predictionId: String? = null

    @SerialName("GameId")
    val gameId: String? = null

    @SerialName("state")
    val state: String? = null

    @SerialName("score")
    val score: ScoreModel? = null

    @SerialName("Contestant")
    val contestant: ContestantModel? = null

    @SerialName("Game")
    val game: GameResponse? = null

}

@Serializable
class ScoreModel {
    @SerialName("homeTeam")
    val homeTeamScore: Int? = null

    @SerialName("awayTeam")
    val awayTeamScore: Int? = null
}

@Serializable
class ContestantModel {
    @SerialName("id")
    val id: String? = null

    @SerialName("email")
    val email: String? = null

    @SerialName("name")
    val name: String? = null

    @SerialName("phone")
    val phone: String? = null

    @SerialName("externalId")
    val externalId: String? = null
}