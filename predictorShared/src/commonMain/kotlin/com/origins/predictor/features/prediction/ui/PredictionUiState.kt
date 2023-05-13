package com.origins.predictor.features.prediction.ui

import Scene
import com.netcosports.rooibos.Swift
import com.origins.kmm.gaba.base.store.InputConsumer
import com.origins.predictor.LabelWithHintKMM
import com.origins.predictor.LabelWithIconKMM
import com.origins.predictor.base.PredictorSerializable
import com.origins.predictor.domain.config.ContestantEntity
import com.origins.predictor.domain.game.models.ScoreEntity
import com.origins.predictor.features.base.LifecycleObserver
import com.origins.predictor.features.base.RefreshObserver
import com.origins.predictor.features.prediction.PredictionInput
import com.origins.resources.entity.ColorKMM
import com.origins.resources.entity.ImageKMM
import com.origins.resources.entity.StringKMM
import com.origins.resources.entity.TextStyleKMM
import com.origins.resources.entity.ui.Label

@Swift
data class PredictionUiState internal constructor(
    val scene: Scene,
    val data: PredictionUi?,
    private val consumer: InputConsumer<PredictionInput>
) : RefreshObserver, LifecycleObserver {

    @Swift
    override fun refresh() {
        consumer.proceed(PredictionInput.Ui.LoadData)
    }

    @Swift
    fun onValidateClick(isVisible: Boolean) {
        consumer.proceed(PredictionInput.Ui.ValidateClick(isVisible))
    }

    @Swift
    fun onModifyClick() {
        consumer.proceed(PredictionInput.Ui.ModifyClick)
    }

    @Swift
    fun onShareClick() {
        consumer.proceed(PredictionInput.Ui.ShareClick)
    }

    @Swift
    fun login(contestant: ContestantEntity, isVisible: Boolean) {
        consumer.proceed(PredictionInput.Ui.Login(contestant, isVisible))
    }

    @Swift
    fun logout() {
        consumer.proceed(PredictionInput.Ui.Logout)
    }

    @Swift
    fun hideCongratsDialog() {
        consumer.proceed(PredictionInput.Ui.HideCongratsDialog)
    }

    @Swift
    fun onScoreChange(score: ScoreEntity) {
        consumer.proceed(PredictionInput.Ui.OnScoreChange(score))
    }

    @Swift
    override fun onStart() {
        consumer.proceed((PredictionInput.Ui.OnStart))
    }

    @Swift
    override fun onStop() {
        consumer.proceed((PredictionInput.Ui.OnStop))
    }
}

@Swift
sealed class PredictionUi {
    data class GameUi(
        val predictionId: String?,
        val baseUi: BaseUi,
        val timer: TimerUi,
        val range: RangeUi,
        val validateButton: ButtonUi,
        val numberPikerSelectBackground: ColorKMM,
        val congratulationDialog: CongratulationDialogUi?,
        val validateDialog: ValidateDialogUi
    ) : PredictionUi()

    data class TooLateUi(
        val baseUi: BaseUi,
        val timer: TimerUi,
        val range: RangeUi,
        val validateButton: ButtonUi,
        val numberPikerSelectBackground: ColorKMM,
    ) : PredictionUi()

    data class NotAvailableUi(
        val title: Label,
        val subTitle: Label,
        val image: ImageKMM,
        val numberPikerSelectBackground: ColorKMM,
        val range: RangeUi
    ) : PredictionUi()

    data class ResultUi(
        val baseUi: BaseUi,
        val image: ImageKMM?
    ) : PredictionUi()
}

@Swift
data class BaseUi(
    val title: Label,
    val subTitle: Label,
    val score: ScoreUi,
    val sponsor: SponsorUi?,
    val terms: TermsUi?,
    val gift: GiftUi?,
    val shareButton: ButtonUi?,
)

@Swift
data class TimerUi(
    val days: TimeItemUi,
    val hours: TimeItemUi,
    val minutes: TimeItemUi
)

@Swift
data class TimeItemUi(
    val value: Label,
    val label: Label
)

@Swift
data class ButtonUi(
    val label: LabelWithIconKMM,
    val border: ColorKMM?,
    val background: ColorKMM?,
    val isModifiedButton: Boolean = false
)

@Swift
data class ValidateDialogUi(
    val title: Label,
    val subTitle: Label,
    val firstName: LabelWithHintKMM,
    val lastName: LabelWithHintKMM,
    val buttonLabel: Label,
    val background: ColorKMM,
    val buttonBackground: ColorKMM,
    val errorText: StringKMM
)

@Swift
data class CongratulationDialogUi(
    val title: Label,
    val subTitle: Label,
    val score: ScoreUi,
    val image: ImageKMM,
    val homeTeamLogo: String,
    val awayTeamLogo: String,
    val confirm: Label,
    val confirmBackground: ColorKMM,
    val closeIcon: ImageKMM,
    val background: ColorKMM
)

@Swift
data class TermsUi(
    val url: String?,
    val text: Label
)

@Swift
data class SponsorUi(
    val url: String?,
    val imageUrl: String?,
    val presentedBy: Label
)

@Swift
data class RangeUi(
    val minValue: Int,
    val maxValue: Int,
    val selectedStyle: TextStyleKMM,
    val unSelectedStyle: TextStyleKMM
)

@Swift
data class GiftUi(
    val title: Label?,
    val imageUrl: String?,
    val redirectUrl: String?
)

@Swift
data class ScoreUi(
    val homeTeamScore: Label,
    val awayTeamScore: Label,
    val separator: Label
) : PredictorSerializable

@Swift
data class ShareUi(
    val chooserTitle: Label,
    val appLogo: ImageKMM,
    val title: Label,
    val stadium: Label,
    val league: Label,
    val homeTeamName: Label,
    val homeTeamLogo: String,
    val awayTeamName: Label,
    val awayTeamLogo: String,
    val score: ScoreUi,
    val date: Label?,
    val time: Label,
    val background: ImageKMM,
    val range: RangeUi,
    val selectNumberBackground: ColorKMM,
    val qrCodeCorner: ImageKMM
)
