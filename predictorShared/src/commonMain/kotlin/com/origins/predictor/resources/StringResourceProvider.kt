// Generated using Origins Resources Plugin
package com.origins.predictor.resources

import com.origins.resources.entity.StringKMM
import com.origins.resources.entity.toStringKMM
import kotlin.String

public interface StringResourceProvider {
  public fun getPredictorTitleQuestion(): StringKMM

  public fun getPredictorTitlePrognosis(): StringKMM

  public fun getPredictorTimerDays(): StringKMM

  public fun getPredictorTimerHours(): StringKMM

  public fun getPredictorTimerMinutes(): StringKMM

  public fun getPredictorSponsorPresentedBy(): StringKMM

  public fun getPredictorButtonValidate(): StringKMM

  public fun getPredictorButtonModify(): StringKMM

  public fun getPredictorButtonShare(): StringKMM

  public fun getPredictorValidateTitle(): StringKMM

  public fun getPredictorValidateDescription(): StringKMM

  public fun getPredictorValidateFirstName(): StringKMM

  public fun getPredictorValidateLastName(): StringKMM

  public fun getPredictorValidateEmail(): StringKMM

  public fun getPredictorValidateError(): StringKMM

  public fun getPredictorValidateSend(): StringKMM

  public fun getPredictorTitleToLate(): StringKMM

  public fun getPredictorTitleLost(): StringKMM

  public fun getPredictorTitleWin(): StringKMM

  public fun getPredictorError(): StringKMM

  public fun getPredictorShareTitle(): StringKMM

  public fun getPredictorShareStatusFinal(): StringKMM

  public fun getPredictorErrorNotAvailable(): StringKMM

  public fun getPredictorErrorApi(): StringKMM

  public fun getPredictorErrorEmailIncorrect(): StringKMM

  public fun getPredictorButtonModified(): StringKMM

  public fun getPredictorShareMyPrognosis(): StringKMM

  public fun getPredictorCommonOk(): StringKMM

  public fun getPredictorPopupTitle(): StringKMM

  public fun getPredictorPopupDescription(): StringKMM

  public fun getPredictorPopupConfirm(): StringKMM

  public fun getPredictorNotAvailableTitle(): StringKMM

  public fun getPredictorNotAvailableDescription(): StringKMM

  public fun getPredictorSharePrognosisOf(p0: StringKMM): StringKMM

  public fun getPredictorNotificationTitleWin(): StringKMM

  public fun getPredictorNotificationTitleLost(): StringKMM

  public fun getPredictorTextTerms(): StringKMM
}

public fun StringResourceProvider.getPredictorSharePrognosisOf(p0: String): StringKMM =
    this.getPredictorSharePrognosisOf(p0.toStringKMM())
