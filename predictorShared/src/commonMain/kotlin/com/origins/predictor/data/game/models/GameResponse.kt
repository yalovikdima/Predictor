package com.origins.predictor.data.game.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GameResponse {
    @SerialName("id")
    val gameId: String? = null

    @SerialName("startDate")
    val startDate: String? = null

    @SerialName("endDate")
    val endDate: String? = null

    @SerialName("state")
    val state: String? = null

    @SerialName("prizeDraw")
    val prizeDraw: Boolean? = null

    @SerialName("introText")
    val introText: String? = null

    @SerialName("activePrediction")
    val activePrediction: String? = null

    @SerialName("inactivePrediction")
    val inactivePrediction: String? = null

    @SerialName("resultWinText")
    val resultWinText: String? = null

    @SerialName("resultLostText")
    val resultLostText: String? = null

    @SerialName("minRange")
    val minRange: Int? = null

    @SerialName("maxRange")
    val maxRange: Int? = null

    @SerialName("Sponsor")
    val sponsor: SponsorModel? = null

    @SerialName("stadium")
    val stadium: String? = null

    @SerialName("league")
    val league: String? = null

    @SerialName("homeTeamName")
    val homeTeamName: String? = null

    @SerialName("homeTeamCode")
    val homeTeamCode: String? = null

    @SerialName("homeTeamLogo")
    val homeTeamLogo: String? = null

    @SerialName("awayTeamName")
    val awayTeamName: String? = null

    @SerialName("awayTeamCode")
    val homeAwayCode: String? = null

    @SerialName("awayTeamLogo")
    val awayTeamLogo: String? = null

    @SerialName("eventStartDate")
    val eventStartDate: String? = null

    @SerialName("eventEndDate")
    val eventEndDate: String? = null

    @SerialName("optaId")
    val optaId: String? = null

    @SerialName("termsUrl")
    val termsUrl: String? = null

    @SerialName("Gifts")
    val gifts: List<GiftModel>? = null

    @SerialName("congratsPopupTitle")
    val congratsPopupTitle: String? = null

    @SerialName("congratsPopupSubtitle")
    val congratsPopupSubtitle: String? = null

}
