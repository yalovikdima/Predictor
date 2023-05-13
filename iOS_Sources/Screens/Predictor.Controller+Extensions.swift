//
//  Predictor.Controller+Extensions.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 7.09.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit
import predictorShared

extension Predictor {
  enum OutputActions {
    case share(image: UIImage), termsAndConditions(url: URL?), gift(url: URL?)
  }
}

extension Predictor.Controller {
  var output: Observable<Predictor.OutputActions> {
    internalOutput.asObservable()
  }
}
