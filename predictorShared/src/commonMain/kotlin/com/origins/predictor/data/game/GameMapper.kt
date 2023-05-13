package com.origins.predictor.data.game

import com.origins.predictor.data.game.models.GameResponse
import com.origins.predictor.domain.game.models.*
import com.soywiz.klock.DateFormat

internal class GameMapper {
    private val dateFormat = DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")

    internal fun mapFrom(response: List<GameResponse>): List<GameEntity> {
        return response.map { mapFrom(it) }
    }

   internal fun mapFrom(response: GameResponse): GameEntity {
        return GameEntity(
            id = response.gameId.orEmpty(),
            startDate = dateFormat.tryParse(response.startDate ?: "")?.utc?.unixMillisLong ?: 0L,
            endDate = dateFormat.tryParse(response.endDate ?: "")?.utc?.unixMillisLong ?: 0L,
            state = when (response.state) {
                ACTIVATED -> GameState.ACTIVATED
                DRAFT -> GameState.DRAFT
                else -> GameState.ARCHIVED
            },
            isPrizeDraw = response.prizeDraw ?: false,
            introText = response.introText.orEmpty(),
            activePredictionText = response.activePrediction.orEmpty(),
            inactivePredictionText = response.inactivePrediction.orEmpty(),
            resultWinText = response.resultWinText.orEmpty(),
            resultLostText = response.resultLostText.orEmpty(),
            congratsPopupTitle = response.congratsPopupTitle.orEmpty(),
            congratsPopupSubtitle = response.congratsPopupSubtitle.orEmpty(),
            range = RangeEntity(
                minValue = response.minRange ?: 0,
                maxValue = response.maxRange ?: 100,
            ),
            sponsor = if (response.sponsor == null) null else SponsorEntity(
                id = response.sponsor.id.orEmpty(),
                text = response.sponsor.text.orEmpty(),
                url = response.sponsor.url,
                bannerUrl = response.sponsor.bannerUrl
            ),
            stadium = response.stadium.orEmpty(),
            league = response.league.orEmpty(),
            homeTeamName = response.homeTeamName.orEmpty(),
            awayTeamName = response.awayTeamName.orEmpty(),
            homeTeamCode = response.homeTeamCode.orEmpty(),
            awayTeamCode = response.homeAwayCode.orEmpty(),
            homeTeamLogo = response.homeTeamLogo.orEmpty(),
            awayTeamLogo = response.awayTeamLogo.orEmpty(),
            eventStartDate = dateFormat.tryParse(response.eventStartDate ?: "")?.utc?.unixMillisLong ?: 0L,
            eventEndDate = dateFormat.tryParse(response.eventEndDate ?: "")?.utc?.unixMillisLong ?: 0L,
            optaId = response.optaId,
            termsUrl = response.termsUrl,
            gift = response.gifts?.let {
                if (it.isEmpty()) null else GiftEntity(
                    title = response.gifts[0].title.orEmpty(),
                    imageUrl = response.gifts[0].imageUrl.orEmpty(),
                    redirectUrl = response.gifts[0].redirectUrl.orEmpty()
                )
            }
        )
    }

    companion object {
        const val ACTIVATED = "ACTIVATED"
        const val DRAFT = "DRAFT"
    }
}