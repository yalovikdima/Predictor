package com.origins.predictor.data.game

import com.origins.predictor.base.PredictorLogger
import com.origins.predictor.data.game.models.GameResponse
import com.origins.predictor.domain.game.GameRepository
import com.origins.predictor.domain.game.models.GameEntity
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

internal class GameRepositoryImpl(
    private val httpClient: HttpClient,
    private val logger: PredictorLogger,
    private val gameMapper: GameMapper
) : GameRepository {
    override suspend fun getGames(): List<GameEntity> {
        return try {
            val response = httpClient.get<List<GameResponse>>("games/live")
            gameMapper.mapFrom(response)
        } catch (ignore: Exception) {
            logger.logD("getGames $ignore ${ignore.printStackTrace()}")
            throw ignore
        }
    }

    override suspend fun getGame(matchId: String?): GameEntity? {
        return try {
            val game = getGames().firstOrNull { it.optaId == matchId }
            if (game != null) {
                val response = httpClient.get<GameResponse>("games/${game.id}/live")
                logger.logD("getGame = game exists with matchId $matchId")
                gameMapper.mapFrom(response)
            } else {
                null
            }
        } catch (ignore: Exception) {
            logger.logD("getGame = no game with matchId $matchId")
            throw ignore
        }
    }

    override suspend fun checkExistGame(matchId: String?): Boolean {
        return try {
            val game = getGames().firstOrNull() { it.optaId == matchId }
            game != null
        } catch (ignore: Exception) {
            throw ignore
        }
    }
}