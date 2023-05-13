package com.origins.predictor.data.prediction

import com.origins.predictor.data.prediction.models.PredictionRequestModel
import com.origins.predictor.data.prediction.models.PredictionResponse
import com.origins.predictor.data.prediction.models.ScoreRequest
import com.origins.predictor.domain.prediction.PredictionEntity
import com.origins.predictor.domain.prediction.PredictionRepository
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

internal class PredictionRepositoryImpl(
    private val httpClient: HttpClient,
    private val predictionMapper: PredictionMapper
) : PredictionRepository {
    override suspend fun sendPrediction(prediction: PredictionRequestModel): PredictionEntity {

        val response = httpClient.post<PredictionResponse>("predictions") {
            contentType(ContentType.Application.Json)
            body = prediction
        }
        return predictionMapper.mapFrom(response)
    }

    override suspend fun getPrediction(externalId: String, gameId: String?): PredictionEntity? {
        return try {
            predictionMapper.mapFrom(httpClient.get("games/${gameId}/predictions/${externalId}"))
        } catch (ignore: Exception) {
            if ((ignore as? ResponseException)?.response?.status == HttpStatusCode.NotFound) {
                null //no prediction
            } else {
                throw ignore
            }
        }
    }

    override suspend fun updatePrediction(predictionId: String?, score: ScoreRequest): PredictionEntity {
        val response = httpClient.put<PredictionResponse>("predictions/${predictionId}") {
            contentType(ContentType.Application.Json)
            body = score
        }
        return predictionMapper.mapFrom(response)
    }

}