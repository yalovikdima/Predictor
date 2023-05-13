package com.origins.predictor.features.prediction

import Scene
import com.netcosports.rooibos.Swift
import com.origins.kmm.gaba.base.cmd.cancel
import com.origins.kmm.gaba.base.feature.dsl.DslFeature
import com.origins.kmm.gaba.base.feature.dsl.NextBuilder
import com.origins.predictor.PredictorAnalytics
import com.origins.predictor.base.PredictorLogger
import com.origins.predictor.di.ResourcesKMM
import com.origins.predictor.domain.config.ContestantEntity
import com.origins.predictor.domain.config.PredictorConfigRepository
import com.origins.predictor.domain.game.GameRepository
import com.origins.predictor.domain.game.models.CombineDataInfo
import com.origins.predictor.domain.game.models.GameEntity
import com.origins.predictor.domain.game.models.ScoreEntity
import com.origins.predictor.domain.prediction.PredictionEntity
import com.origins.predictor.domain.prediction.PredictionRepository
import com.origins.predictor.features.prediction.ui.RangeUi
import com.origins.predictor.features.prediction.ui.ScoreUi
import com.origins.predictor.features.prediction.ui.ShareUi
import com.origins.predictor.features.prediction.ui.TimerEntity
import com.origins.resources.entity.StringKMM
import com.origins.resources.entity.ui.Label
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import toLoad

internal typealias State = PredictionState
internal typealias Input = PredictionInput
internal typealias OutputEvent = PredictionOutputEvent

data class PredictionState(
    val scene: Scene,
    val matchId: String?,
    val game: GameEntity?,
    val prediction: PredictionEntity?,
    val data: PredictionDataState?,
    val timer: TimerEntity?,
    val isModifiedButtonVisible: Boolean,
    val isCongratulationDialogVisible: Boolean,
    val currentScore: ScoreEntity?
)

enum class PredictionDataState {
    GAME, TOO_LATE, RESULT, NOT_AVAILABLE
}


sealed class PredictionInput {
    sealed class Internal : Input() {
        data class LoadingFailed(val t: Throwable?) : Internal()

        data class Validated(val prediction: PredictionEntity) : Internal()
        data class Modified(val prediction: PredictionEntity) : Internal()
        object ShowModifiedButton : Internal()
        data class ModifiedButtonVisibilityResult(val isVisible: Boolean) : Internal()

        data class Game(val data: CombineDataInfo) : Internal()
        data class Result(val data: CombineDataInfo) : Internal()
        data class TooLate(val game: GameEntity) : Internal()
        object NotAvailable : Internal()
        data class ShowLogin(val score: ScoreEntity) : Internal()

        data class OnTimerTicked(val timer: TimerEntity) : Internal()
        object OnStopTimer : Internal()
        object HideLoginDialog : Internal()
        object LoggedOut : Internal()
    }

    sealed class Ui : Input() {
        object LoadData : Ui()
        data class ValidateClick(val isVisible: Boolean) : Ui()
        object ModifyClick : Ui()
        object ShareClick : Ui()
        data class Login(val contestant: ContestantEntity, val isVisible: Boolean) : Ui()
        object Logout : Ui()
        data class ValidateAfterLogin(val score: ScoreEntity, val isVisible: Boolean) : Ui()
        object HideCongratsDialog : Ui()
        data class OnScoreChange(val score: ScoreEntity) : Ui()

        object OnStart : Ui()
        object OnStop : Ui()
    }
}

@Swift
sealed class PredictionOutputEvent {
    data class SharePrediction(val share: ShareUi) : PredictionOutputEvent()
    data class ShowLoginDialog(val score: ScoreEntity) : PredictionOutputEvent()
    object HideLoginDialog : PredictionOutputEvent()
}


internal class PredictionFeature(
    private val gameRepository: GameRepository,
    private val predictionRepository: PredictionRepository,
    private val predictorConfigRepository: PredictorConfigRepository,
    private val analytics: PredictorAnalytics,
    private val resources: ResourcesKMM,
    private val logger: PredictorLogger
) : DslFeature<State, OutputEvent, Input>() {

    override fun initialInputs(state: State): List<Input> {
        return listOf<Input>(PredictionInput.Ui.LoadData)
    }

    override fun produceEvent(state: State, input: Input): OutputEvent? {
        return when (input) {
            is PredictionInput.Ui.ShareClick -> {
                PredictionOutputEvent.SharePrediction(createShareUI(state))
            }

            is PredictionInput.Internal.ShowLogin -> {
                PredictionOutputEvent.ShowLoginDialog(input.score)
            }
            is PredictionInput.Internal.HideLoginDialog -> {
                PredictionOutputEvent.HideLoginDialog
            }
            else -> null
        }
    }

    override fun NextBuilder<State, Input>.reduce(input: Input) {
        return when (input) {
            is PredictionInput.Internal.Game -> {
                state = state.copy(
                    scene = Scene.DATA,
                    data = PredictionDataState.GAME,
                    game = input.data.game,
                    prediction = input.data.prediction,
                    currentScore = if (input.data.prediction?.score != null) input.data.prediction.score else state.currentScore
                )
                if (state.timer == null) {
                    input.data.game?.let { analytics.onPredictorOpened(it) }
                    startTimer()
                } else Unit
            }

            is PredictionInput.Ui.OnScoreChange -> {
                state = state.copy(currentScore = input.score)
            }

            is PredictionInput.Ui.ValidateClick -> {
                if (input.isVisible) {
                    state.currentScore?.let { validate(it) } ?: Unit
                } else {

                }
            }
            is PredictionInput.Ui.ModifyClick -> {
                state.prediction?.predictionId?.let {
                    state.currentScore?.let { score ->
                        modify(it, score)
                    }
                } ?: Unit
            }

            is PredictionInput.Internal.Modified -> {
                state.game?.let {
                    analytics.onModified(input.prediction.score, it)
                }
                state = state.copy(
                    prediction = input.prediction
                )
            }

            is PredictionInput.Internal.ShowModifiedButton -> {
                cmdList += ModifiedButtonVisibilityTimer()
            }

            is PredictionInput.Internal.ModifiedButtonVisibilityResult -> {
                state = state.copy(
                    isModifiedButtonVisible = input.isVisible
                )
            }

            is PredictionInput.Internal.Validated -> {
                analytics.onValidated(input.prediction.score, state.game!!)
                state = state.copy(
                    data = PredictionDataState.GAME,
                    prediction = input.prediction,
                    isCongratulationDialogVisible = true
                )
            }

            is PredictionInput.Ui.HideCongratsDialog -> {
                state = state.copy(
                    isCongratulationDialogVisible = false
                )
            }

            is PredictionInput.Ui.LoadData -> {
                state = state.copy(scene = state.scene.toLoad())
                loadData()
            }

            is PredictionInput.Internal.TooLate -> {
                state = state.copy(
                    scene = Scene.DATA,
                    data = PredictionDataState.TOO_LATE,
                    game = input.game
                )
            }

            is PredictionInput.Internal.OnTimerTicked -> {
                state = state.copy(
                    timer = input.timer
                )
            }

            is PredictionInput.Internal.OnStopTimer -> {
                cmdList += CountDownTimer::class.cancel()
            }

            is PredictionInput.Ui.Login -> {
                cmdList += Login(
                    contestant = input.contestant,
                    score = state.currentScore,
                    predictorConfigRepository = predictorConfigRepository,
                    logger = logger,
                    isVisible = input.isVisible
                )
            }

            is PredictionInput.Ui.Logout -> {
                cmdList += Logout(
                    predictorConfigRepository = predictorConfigRepository,
                    logger = logger
                )
            }

            is PredictionInput.Ui.ValidateAfterLogin -> {
                if (input.isVisible) {
                    validate(input.score)
                } else {
                }
            }
            is PredictionInput.Internal.NotAvailable -> {
                state = state.copy(
                    scene = Scene.DATA,
                    data = PredictionDataState.NOT_AVAILABLE
                )
            }

            is PredictionInput.Internal.Result -> {
                state = state.copy(
                    scene = Scene.DATA,
                    data = PredictionDataState.RESULT,
                    prediction = input.data.prediction,
                    game = input.data.game,
                    currentScore = input.data.prediction?.score
                )
            }

            is PredictionInput.Internal.LoadingFailed -> {
                state = state.copy(scene = Scene.EMPTY_ERROR)
            }

            is PredictionInput.Internal.LoggedOut -> {
                loadData()
            }

            is PredictionInput.Ui.OnStart -> Unit
            is PredictionInput.Ui.OnStop -> Unit
            else -> {}
        }
    }

    private fun NextBuilder<State, Input>.loadData() {
        cmdList += LoadData(
            matchId = state.matchId,
            gameRepository,
            predictionRepository,
            predictorConfigRepository,
            logger
        )
    }

    private fun NextBuilder<State, Input>.modify(id: String, score: ScoreEntity) {
        cmdList += ModifyPrediction(
            score = score,
            predictionId = id,
            predictionRepository = predictionRepository,
            logger = logger
        )
    }

    private fun NextBuilder<State, Input>.startTimer() {
        state.game?.let {
            cmdList += CountDownTimer(it.endDate - DateTime.now().unixMillisLong, logger)
        } ?: Unit
    }

    private fun NextBuilder<State, Input>.validate(score: ScoreEntity) {
        cmdList +=
            SendPrediction(
                gameId = state.game?.id.orEmpty(),
                score = score,
                predictionRepository = predictionRepository,
                predictorConfigRepository = predictorConfigRepository,
                logger = logger
            )
    }


    private fun createShareUI(state: State): ShareUi {
        val (sp, ip, tp, cp) = resources
        val contestant = predictorConfigRepository.getContestant()
        val game = state.game
        val score = state.currentScore ?: ScoreEntity(0, 0)

        val name = contestant?.firstName
        val eventStartDate = game?.eventStartDate ?: 0
        val eventEndDate = game?.eventEndDate ?: 0
        val endTime = eventEndDate - DateTime.nowUnixLong()
        var date: StringKMM? = null
        val time: StringKMM

        if (endTime > 0 && eventStartDate != 0L) {
            date = StringKMM.fromText(dateFormat.format(DateTime.invoke(eventStartDate).local))
            time = StringKMM.fromText(timeFormat.format(DateTime.invoke(eventStartDate).local))
        } else {
            time = sp.getPredictorShareStatusFinal()
        }

        return ShareUi(
            chooserTitle = Label(
                text = sp.getPredictorShareTitle(),
                style = tp.predictorShareTitle
            ),
            appLogo = ip.predictorShareAppLogo,
            title = if (name.isNullOrEmpty()) {
                Label(sp.getPredictorShareMyPrognosis(), tp.predictorShareTitle)
            } else {
                Label(
                    sp.getPredictorSharePrognosisOf(StringKMM.fromText(name)),
                    tp.predictorShareTitle
                )
            },
            stadium = Label(StringKMM.fromText(game?.stadium.orEmpty()), tp.predictorShareStadium),
            league = Label(StringKMM.fromText(game?.league.orEmpty()), tp.predictorShareCompetition),
            homeTeamName = Label(
                StringKMM.fromText(game?.homeTeamCode.orEmpty()),
                tp.predictorShareTeam
            ),
            awayTeamName = Label(
                StringKMM.fromText(game?.awayTeamCode.orEmpty()),
                tp.predictorShareTeam
            ),
            homeTeamLogo = game?.homeTeamLogo.orEmpty(),
            awayTeamLogo = game?.awayTeamLogo.orEmpty(),
            score = ScoreUi(
                homeTeamScore = Label(StringKMM.fromText(score.homeScore.toString()), tp.predictorScoreShare),
                awayTeamScore = Label(StringKMM.fromText(score.awayScore.toString()), tp.predictorScoreShare),
                separator = Label(StringKMM.fromText("-"), tp.predictorScoreSeparatorShare)
            ),
            date = date?.let { Label(it, tp.predictorShareDate) },
            time = Label(time, tp.predictorShareDate),
            background = ip.predictorShareBkg,
            range = RangeUi(
                minValue = game?.range?.minValue ?: 0,
                maxValue = game?.range?.maxValue ?: 10,
                selectedStyle = tp.predictorNumberPickerShareSelected,
                unSelectedStyle = tp.predictorNumberPickerShareUnselected
            ),
            selectNumberBackground = cp.predictorShareNumberPickerBkg,
            qrCodeCorner = ip.predictorQrCodeStroke
        )
    }

    companion object {
        private val dateFormat = DateFormat("dd/MM/yyyy")
        private val timeFormat = DateFormat("HH:mm")

        fun initialState(matchId: String?): PredictionState {
            return PredictionState(
                scene = Scene.EMPTY_LOADING,
                matchId = matchId,
                game = null,
                prediction = null,
                data = null,
                timer = null,
                isModifiedButtonVisible = false,
                isCongratulationDialogVisible = false,
                currentScore = ScoreEntity(0, 0)
            )
        }
    }
}



