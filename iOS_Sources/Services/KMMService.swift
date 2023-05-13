//
//  KMMService.swift
//  PredictorSDK
//
//  Created by Eugene Filipkov on 18.08.21.
//  Copyright © 2021 Origins-Digital. All rights reserved.
//

import UIKit
import predictorShared

final class KMMService: KMMServicing {
  private let storage = PredictorDataStorage()
  private let di: PredictorDI
  
  public init() { fatalError("This should not be used") }
  
  public init(config: PredictorApiConfig,
              logLevel: PredictorSettings.LogLevel,
              analytics: PredictorAnalytics?) {
    guard let analytics = analytics else { fatalError("Missing PredictorAnalytics") }
    let stringResourceProvider = StringResourceProviderImpl(provider: NativeStringProviderImpl())
    let imageResourceProvider = ImageResourceProviderImpl(provider: NativeImageProviderImpl())
    let textStyleResourceProvider = TextStyleResourceProviderImpl()
    let colorResourceProvider = ColorResourceProviderImpl()

    let isLogsEnabled = logLevel == .none ? false : true
    
    di = PredictorDI(config: .init(api: config),
                     logger: .init(isEnabled: isLogsEnabled),
                     isLogsEnabled: isLogsEnabled,
                     contestantStorage: storage,
                     analytics: analytics,
                     stringResourceProvider: stringResourceProvider,
                     imageResourceProvider: imageResourceProvider,
                     textStyleResourceProvider: textStyleResourceProvider,
                     colorResourceProvider: colorResourceProvider)
  }

  public var mainDI: PredictionDI {
    di.providePredictionMainDI()
  }

  public func isGameAvailable(id: String, completition: @escaping (Bool, Error?) -> Void) {
    di.gameDataDI.provideGameRepository().checkExistGame(matchId: id) { result, error in
      completition(result == true ? true : false, error)
    }
  }
}
