package com.origins.predictor.domain.game

import com.origins.predictor.domain.game.models.GameEntity

interface GameRepository {

    @Throws(Exception::class)
    suspend fun getGames(): List<GameEntity>

    @Throws(Exception::class)
    suspend fun getGame(matchId: String?): GameEntity?

    @Throws(Exception::class)
    suspend fun checkExistGame(matchId: String?): Boolean
}