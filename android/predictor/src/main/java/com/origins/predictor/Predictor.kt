package com.origins.predictor

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.origins.predictor.base.core.di.PredictorKoinComponent
import com.origins.predictor.domain.config.ContestantEntity
import com.origins.predictor.domain.config.PredictorConfigRepository
import com.origins.predictor.domain.game.GameRepository
import com.origins.predictor.domain.game.models.GameEntity
import com.origins.predictor.domain.game.models.ScoreEntity
import com.origins.predictor.features.PredictorFragment
import com.origins.predictor.features.di.predictorInitKoin
import com.origins.predictor.utils.PredictorApiConfig
import com.origins.predictor.utils.PredictorConfig
import org.koin.core.component.get
import org.koin.core.component.inject

object Predictor : PredictorKoinComponent {

    private var gameRepository: GameRepository? = null

    enum class Api {
        DEV, STAGING, PROD
    }

    private val _showLogin = MutableLiveData<Boolean>()
    val showLogin: LiveData<Boolean> get() = _showLogin

    internal fun setShowLogin(showLogin: Boolean) {
        _showLogin.value = showLogin
    }

    fun loginConsumed() {
        _showLogin.value = false
    }

    private var analytics: PredictorAnalytics = object : PredictorAnalytics {
        override fun onPredictorOpened(game: GameEntity) = Unit
        override fun onValidated(score: ScoreEntity, game: GameEntity) = Unit
        override fun onModified(score: ScoreEntity, game: GameEntity) = Unit
    }
    private val predictorConfigRepository: PredictorConfigRepository by inject()
    private var isInitialized = false
    var useNativeAppLogin = false
        private set

    fun init(
        accountKey: String,
        api: Api,
        isUseNativeAppLogin: Boolean,
        analytics: PredictorAnalytics = Predictor.analytics,
        isLogsEnabled: Boolean = false,
        qrCode: String?,
        context: Context
    ) {
        useNativeAppLogin = isUseNativeAppLogin
        Predictor.analytics = analytics

        predictorInitKoin(
            application = context.applicationContext as Application,
            config = PredictorConfig(
                api = when (api) {
                    Api.DEV -> PredictorApiConfig.dev(accountKey)
                    Api.STAGING -> PredictorApiConfig.staging(accountKey)
                    Api.PROD -> PredictorApiConfig.prod(accountKey)
                }
            ),
            analytics = analytics,
            isLogsEnabled = isLogsEnabled,
            qrCode = qrCode
        )
        gameRepository = get()
        isInitialized = true
    }

    fun setUserData(externalUserId: String?, firstName: String?, lastName: String?) {
        checkIsInitialized()
        val user = if (externalUserId != null) {
            ContestantEntity(
                firstName = firstName,
                lastName = lastName,
                externalId = externalUserId
            )
        } else {
            null
        }
        predictorConfigRepository.storeContestant(user)
    }

    suspend fun checkExistGame(matchId: String): Boolean {
        checkIsInitialized()
        return gameRepository?.checkExistGame(matchId) ?: false
    }

    fun getFragment(matchId: String): Fragment {
        checkIsInitialized()
        return PredictorFragment.newInstance(
            matchId = matchId,
        )
    }

    private fun checkIsInitialized() {
        if (!isInitialized) {
            throw Exception("You should call init() then setDataUser() if need , after this can be called getFragment()")
        }
    }
}