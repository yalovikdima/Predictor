//
//  Services.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 3/17/20.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import predictorShared

protocol KMMServicing {
  var mainDI: PredictionDI { get }

  func isGameAvailable(id: String, completition: @escaping (Bool, Error?) -> Void)
}

protocol InitializationServicing {
  var settings: PredictorSettings { get }
  var kmmService: KMMServicing { get }
}
