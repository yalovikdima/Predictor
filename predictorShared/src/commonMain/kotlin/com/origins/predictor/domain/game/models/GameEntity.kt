package com.origins.predictor.domain.game.models

data class GameEntity(
    val id: String,
    val startDate: Long,
    val endDate: Long,
    val state: GameState,
    val isPrizeDraw: Boolean,
    val introText: String,
    val activePredictionText: String,
    val inactivePredictionText: String,
    val resultWinText: String,
    val resultLostText: String,
    val congratsPopupTitle: String,
    val congratsPopupSubtitle: String,
    val range: RangeEntity,
    val sponsor: SponsorEntity?,
    val stadium: String,
    val league: String,
    val homeTeamName: String,
    val homeTeamCode: String,
    val homeTeamLogo: String,
    val awayTeamName: String,
    val awayTeamCode: String,
    val awayTeamLogo: String,
    val eventStartDate: Long,
    val eventEndDate: Long,
    val optaId: String?,
    val termsUrl: String?,
    val gift: GiftEntity?
)

enum class GameState {
    ACTIVATED,
    DRAFT,
    ARCHIVED
}
