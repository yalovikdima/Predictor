package com.origins.predictor.features.prediction.ui

import com.origins.kmm.gaba.base.store.InputConsumer
import com.origins.predictor.LabelWithHintKMM
import com.origins.predictor.LabelWithIconKMM
import com.origins.predictor.domain.game.models.ScoreEntity
import com.origins.predictor.domain.prediction.PredictorState
import com.origins.predictor.features.prediction.PredictionDataState
import com.origins.predictor.features.prediction.PredictionInput
import com.origins.predictor.features.prediction.PredictionState
import com.origins.predictor.resources.ColorResourceProvider
import com.origins.predictor.resources.ImageResourceProvider
import com.origins.predictor.resources.StringResourceProvider
import com.origins.predictor.resources.TextStyleResourceProvider
import com.origins.resources.entity.ColorKMM
import com.origins.resources.entity.ImageKMM
import com.origins.resources.entity.StringKMM
import com.origins.resources.entity.ui.Label
import com.origins.resources.entity.uppercase

internal class PredictionUiMapper(
    private val ip: ImageResourceProvider,
    private val sp: StringResourceProvider,
    private val tp: TextStyleResourceProvider,
    private val cp: ColorResourceProvider,
) {

    fun mapFrom(consumer: InputConsumer<PredictionInput>, state: PredictionState): PredictionUiState {
        return PredictionUiState(
            scene = state.scene,
            consumer = consumer,
            data = mapFrom(state)
        )
    }

    private fun mapFrom(state: PredictionState): PredictionUi? {
        return when (state.data) {
            PredictionDataState.GAME -> createGameUi(state)
            PredictionDataState.TOO_LATE -> createTooLateUi(state)
            PredictionDataState.NOT_AVAILABLE -> createNotAvailableUi(state)
            PredictionDataState.RESULT -> createResultUi(state)
            else -> null
        }
    }

    private fun createResultUi(state: PredictionState): PredictionUi.ResultUi {
        return PredictionUi.ResultUi(
            baseUi = createBaseUi(state),
            image = when (state.prediction?.state) {
                PredictorState.WIN -> ip.predictorResultIcCorrect
                PredictorState.LOST -> ip.predictorResultIcIncorrect
                else -> null
            }
        )
    }

    private fun createNotAvailableUi(state: PredictionState): PredictionUi.NotAvailableUi {
        return PredictionUi.NotAvailableUi(
            title = getTitle(state),
            subTitle = getSubtitle(state),
            image = ip.predictorNotAvailableImage,
            numberPikerSelectBackground = cp.predictorNumberPickerSelectedItemDisableBkg,
            range = createRangeUi(state)
        )
    }

    private fun createTooLateUi(state: PredictionState): PredictionUi.TooLateUi {
        return PredictionUi.TooLateUi(
            baseUi = createBaseUi(state),
            range = createRangeUi(state),
            validateButton = createTooLateValidateButtonUi(),
            timer = createTimerUi(state),
            numberPikerSelectBackground = cp.predictorNumberPickerSelectedBkg
        )
    }

    private fun createGameUi(state: PredictionState): PredictionUi.GameUi {
        return PredictionUi.GameUi(
            baseUi = createBaseUi(state),
            congratulationDialog = createCongratulationUi(state),
            validateDialog = createValidateDialogUi(),
            predictionId = state.prediction?.predictionId,
            range = createRangeUi(state),
            validateButton = createGameValidateButtonUi(state),
            timer = createTimerUi(state),
            numberPikerSelectBackground = cp.predictorNumberPickerSelectedBkg
        )
    }

    private fun createValidateDialogUi(): ValidateDialogUi {
        return ValidateDialogUi(
            title = Label(
                text = sp.getPredictorValidateTitle(),
                style = tp.predictorValidateTitle
            ),
            subTitle = Label(
                text = sp.getPredictorValidateDescription(),
                style = tp.predictorValidateSubtitle
            ),
            firstName = LabelWithHintKMM(
                Label(
                    text = sp.getPredictorValidateFirstName(),
                    style = tp.predictorValidateHintEditText
                ),
                labelTextStyle = tp.predictorValidateEditText
            ),
            lastName = LabelWithHintKMM(
                Label(
                    text = sp.getPredictorValidateLastName(),
                    style = tp.predictorValidateHintEditText
                ),
                labelTextStyle = tp.predictorValidateEditText
            ),
            buttonLabel = Label(
                text = sp.getPredictorValidateSend(),
                style = tp.predictorValidateButton
            ),
            buttonBackground = cp.predictorValidateButtonBkg,
            background = cp.predictorCongratsBkg,
            errorText = sp.getPredictorValidateError()
        )
    }

    private fun createCongratulationUi(state: PredictionState): CongratulationDialogUi? {
        return if (state.game?.isPrizeDraw == true && state.isCongratulationDialogVisible) CongratulationDialogUi(
            title = Label(
                text = StringKMM.fromText(state.game.congratsPopupTitle),
                style = tp.predictorCongratsTitle
            ),
            subTitle = Label(
                text = StringKMM.fromText(state.game.congratsPopupSubtitle),
                style = tp.predictorCongratsSubtitle
            ),
            closeIcon = ip.predictorCongratsIcClose,
            image = ip.predictorCongratsImage,
            background = cp.predictorCongratsBkg,
            confirm = Label(
                text = sp.getPredictorPopupConfirm(),
                style = tp.predictorCongratsConfirmButton
            ),
            confirmBackground = cp.predictorCongratsConfirmButtonBkg,
            homeTeamLogo = state.game.homeTeamLogo,
            awayTeamLogo = state.game.awayTeamLogo,
            score = ScoreUi(
                homeTeamScore = Label(
                    text = StringKMM.fromText(state.prediction?.score?.homeScore.toString()),
                    style = tp.predictorScore
                ),
                awayTeamScore = Label(
                    text = StringKMM.fromText(state.prediction?.score?.awayScore.toString()),
                    style = tp.predictorScore
                ),
                separator = Label(
                    text = StringKMM.fromText("-"),
                    style = tp.predictorScoreSeparator
                )
            )
        ) else null
    }

    private fun createBaseUi(state: PredictionState): BaseUi {
        return BaseUi(
            title = getTitle(state),
            subTitle = getSubtitle(state),
            score = createScoreUi(state),
            sponsor = createSponsorUi(state),
            terms = createTermsUi(state),
            gift = createGiftUi(state),
            shareButton = createShareButtonUi(state),
        )
    }

    private fun createTimerUi(state: PredictionState): TimerUi {
        return TimerUi(
            days = createTimeItemUi(state.timer?.days ?: 0, sp.getPredictorTimerDays()),
            hours = createTimeItemUi(state.timer?.hours ?: 0, sp.getPredictorTimerHours()),
            minutes = createTimeItemUi(state.timer?.minutes ?: 0, sp.getPredictorTimerMinutes())
        )
    }

    private fun createTimeItemUi(value: Int, label: StringKMM): TimeItemUi {
        return TimeItemUi(
            value = Label(
                text = StringKMM.fromText(value.toString().padStart(2, '0')),
                style = tp.predictorCountdownNumber
            ),
            label = Label(
                text = label,
                style = tp.predictorCountdownText
            )
        )
    }

    private fun createShareButtonUi(state: PredictionState): ButtonUi? {
        return when (state.data) {
            PredictionDataState.GAME, PredictionDataState.RESULT -> {
                createButtonUi(
                    Label = Label(sp.getPredictorButtonShare().uppercase(), tp.predictorShareButton),
                    icon = null,
                    border = cp.predictorShareButtonBkgBorder,
                    background = cp.predictorSecondaryColor

                )
            }
            PredictionDataState.TOO_LATE -> {
                createButtonUi(
                    Label = Label(sp.getPredictorButtonShare().uppercase(), tp.predictorShareButtonDisable),
                    icon = null,
                    border = cp.predictorShareButtonDisableBkgBorder,
                    background = cp.predictorSecondaryColor

                )
            }
            else -> null
        }
    }

    private fun createButtonUi(
        Label: Label,
        icon: ImageKMM?,
        border: ColorKMM?,
        background: ColorKMM?,
        isModifiedButton: Boolean = false
    ): ButtonUi {
        return ButtonUi(
            label = LabelWithIconKMM(
                Label, icon
            ),
            border = border,
            background = background,
            isModifiedButton = isModifiedButton
        )
    }

    private fun createGameValidateButtonUi(state: PredictionState): ButtonUi {
        return if (state.prediction != null) {
            val label = if (state.isModifiedButtonVisible) Label(
                sp.getPredictorButtonModified().uppercase(),
                tp.predictorModifiedButton
            ) else Label(sp.getPredictorButtonModify().uppercase(), tp.predictorModifyButton)

            val border = if (state.isModifiedButtonVisible) cp.predictorModifyButtonBkgBorder else cp.predictorShareButtonBkgBorder
            val background = if (!state.isModifiedButtonVisible) cp.predictorValidateButtonBkg else null
            createButtonUi(
                Label = label,
                icon = if (state.isModifiedButtonVisible) ip.predictorIcModified else null,
                border = border,
                background = background,
                isModifiedButton = state.isModifiedButtonVisible
            )
        } else {
            createButtonUi(
                Label = Label(sp.getPredictorButtonValidate().uppercase(), tp.predictorValidateButton),
                icon = null,
                border = cp.predictorValidateButtonBkg,
                background = cp.predictorValidateButtonBkg
            )
        }
    }

    private fun createTooLateValidateButtonUi(): ButtonUi {
        return createButtonUi(
            Label = Label(sp.getPredictorButtonValidate().uppercase(), tp.predictorValidateButtonDisable),
            icon = null,
            border = cp.predictorValidateButtonDisableBkg,
            background = cp.predictorValidateButtonDisableBkg
        )
    }

    private fun getTitle(state: PredictionState): Label {
        val text = when (state.data) {
            PredictionDataState.GAME -> if (state.prediction == null) sp.getPredictorTitleQuestion() else sp.getPredictorTitlePrognosis()
            PredictionDataState.TOO_LATE -> sp.getPredictorTitleToLate()
            PredictionDataState.NOT_AVAILABLE -> sp.getPredictorNotAvailableTitle()
            PredictionDataState.RESULT -> {
                when (state.prediction?.state) {
                    PredictorState.WIN -> sp.getPredictorTitleWin()
                    PredictorState.LOST -> sp.getPredictorTitleLost()
                    else -> sp.getPredictorTitlePrognosis()
                }
            }
            else -> null
        }
        return Label(
            text = text ?: StringKMM.fromText(""),
            style = tp.predictorTitle
        )
    }

    private fun getSubtitle(state: PredictionState): Label {
        val text = when (state.data) {
            PredictionDataState.GAME -> StringKMM.fromText(if (state.prediction == null) state.game?.introText.orEmpty() else state.game?.activePredictionText.orEmpty())
            PredictionDataState.TOO_LATE -> StringKMM.fromText(state.game?.inactivePredictionText.orEmpty())
            PredictionDataState.NOT_AVAILABLE -> sp.getPredictorNotAvailableDescription()
            PredictionDataState.RESULT -> {
                StringKMM.fromText(
                    when (state.prediction?.state) {
                        PredictorState.WIN -> state.game?.resultWinText.orEmpty()
                        PredictorState.LOST -> state.game?.resultLostText.orEmpty()
                        else -> state.game?.inactivePredictionText.orEmpty()
                    }
                )
            }
            else -> null
        }
        return Label(
            text = text ?: StringKMM.fromText(""),
            style = tp.predictorSubtitle
        )
    }

    private fun createScoreUi(state: PredictionState): ScoreUi {
        val styleScore = when (state.data) {
            PredictionDataState.GAME, PredictionDataState.RESULT -> tp.predictorScore
            else -> tp.predictorScoreDisabled
        }

        val styleSeparator = when (state.data) {
            PredictionDataState.GAME, PredictionDataState.RESULT -> tp.predictorScoreSeparator
            else -> tp.predictorScoreSeparatorDisabled
        }
        val score = state.currentScore ?: ScoreEntity(0, 0)
        return ScoreUi(
            homeTeamScore = Label(
                text = StringKMM.fromText(score.homeScore.toString()),
                style = styleScore
            ),
            awayTeamScore = Label(
                text = StringKMM.fromText(score.awayScore.toString()),
                style = styleScore
            ),
            separator = Label(
                text = StringKMM.fromText("-"),
                style = styleSeparator
            )
        )
    }

    private fun createGiftUi(state: PredictionState): GiftUi? {
        val gift = state.game?.gift
        return if (gift == null) null else GiftUi(
            title = Label(StringKMM.fromText(gift.title), tp.predictorGift),
            imageUrl = gift.imageUrl,
            redirectUrl = gift.redirectUrl
        )
    }

    private fun createTermsUi(state: PredictionState): TermsUi? {
        val game = state.game
        return if (game == null) null else TermsUi(
            url = game.termsUrl,
            text = Label(
                text = sp.getPredictorTextTerms(),
                style = tp.predictorTerms
            )
        )
    }

    private fun createRangeUi(state: PredictionState): RangeUi {
        val selectedStyle = when (state.data) {
            PredictionDataState.GAME -> tp.predictorNumberPickerSelected
            else -> tp.predictorNumberPickerDisableSelected
        }

        val unSelectedStyle = when (state.data) {
            PredictionDataState.GAME -> tp.predictorNumberPickerUnselected
            else -> tp.predictorNumberPickerDisableUnselected
        }
        val range = state.game?.range
        return RangeUi(
            minValue = range?.minValue ?: 0,
            maxValue = range?.maxValue ?: 10,
            selectedStyle = selectedStyle,
            unSelectedStyle = unSelectedStyle,
        )
    }

    private fun createSponsorUi(state: PredictionState): SponsorUi? {
        val sponsor = state.game?.sponsor
        return if (sponsor != null && sponsor.bannerUrl?.isNotEmpty() == true) {
            SponsorUi(
                url = sponsor.url,
                imageUrl = sponsor.bannerUrl,
                presentedBy = Label(
                    text = sp.getPredictorSponsorPresentedBy(),
                    style = tp.predictorSponsorPresentedBy
                )
            )
        } else null
    }
}