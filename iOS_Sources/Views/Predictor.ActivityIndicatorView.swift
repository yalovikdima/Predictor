//
//  Predictor.IndicatorView.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 7.09.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit

extension Predictor {
  class ActivityIndicatorView: ActivityIndicator, ActivityIndicatorOverriding {
    public override func setup() {
      super.setup()
      
      color = ColorsExportedPublic.provider.predictorPrimaryColor.color
    }
  }
}
