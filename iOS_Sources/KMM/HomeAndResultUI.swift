//
//  HomeAndResultUI.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 14.09.21.
//  Copyright © 2021 Origins-digital. All rights reserved.
//

import Foundation

protocol HomeAndResultUI {
  var baseUi: BaseUiKMM { get }
}

extension PredictionUiResultUiKMM: HomeAndResultUI {}

extension PredictionUiGameUiKMM: HomeAndResultUI {}

extension PredictionUiTooLateUiKMM: HomeAndResultUI {}
