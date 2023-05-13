//
//  TooLateUIKMM.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 7.09.21.
//  Copyright © 2021 Origins-digital. All rights reserved.
//

import Foundation


protocol TooLateAndResultUI {
  var tooLateAndResulTitle: NSAttributedString { get }
  var tooLateAndResulDescription: NSAttributedString  { get }
  var baseUi: BaseUiKMM  { get }
}

extension PredictionUiResultUiKMM: TooLateAndResultUI {
  var tooLateAndResulTitle: NSAttributedString { baseUi.title.attributedString }
  var tooLateAndResulDescription: NSAttributedString { baseUi.subTitle.attributedString}
  var range: RangeUiKMM {
    .init(.init(minValue: 0,
                maxValue: 0,
                selectedStyle: TextStylesExportedPublic.provider.predictorNumberPickerSelected,
                unSelectedStyle: TextStylesExportedPublic.provider.predictorNumberPickerUnselected))
  }
}

extension PredictionUiTooLateUiKMM: TooLateAndResultUI {
  var tooLateAndResulTitle: NSAttributedString { baseUi.title.attributedString }
  var tooLateAndResulDescription: NSAttributedString { baseUi.subTitle.attributedString }
  var score: ScoreUiKMM { baseUi.score }
}
